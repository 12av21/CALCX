package com.example.calcx.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calcx.data.db.AppDatabase
import com.example.calcx.data.model.CalculationEntity
import com.example.calcx.engine.*
import com.example.calcx.theme.ThemeManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val calculationDao = db.calculationDao()
    val themeManager = ThemeManager(application)

    // Current app screen / mode
    var currentScreen by mutableStateOf(themeManager.currentSettings.defaultMode)
    val history = calculationDao.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Standard & Scientific State
    var expression by mutableStateOf("")
        private set
    var result by mutableStateOf("0")
        private set
    var previewResult by mutableStateOf<String?>(null)
        private set
    var memoryValue by mutableStateOf<Double?>(null)
        private set

    // Undo / Redo Stacks
    private val undoStack = mutableListOf<String>()
    private val redoStack = mutableListOf<String>()
    var canUndo by mutableStateOf(false)
        private set
    var canRedo by mutableStateOf(false)
        private set

    private val mathEvaluator = MathEvaluator(
        angleUnit = themeManager.currentSettings.angleUnit,
        precision = themeManager.currentSettings.precision,
        useThousandsSeparator = themeManager.currentSettings.thousandsSeparator
    )

    // Scientific Secondary Function Mode (2nd key)
    var isSecondActive by mutableStateOf(false)

    // Programmer Mode State
    var programmerBase by mutableStateOf(NumberBase.DEC)
    var programmerWordSize by mutableStateOf(WordSize.QWORD)
    var programmerValue by mutableStateOf(0uL)
    var programmerPendingValue by mutableStateOf<ULong?>(null)
    var programmerPendingOp by mutableStateOf<BitwiseOp?>(null)
    var programmerInputBuffer by mutableStateOf("0")

    init {
        updateEvaluatorSettings()
    }

    private fun updateEvaluatorSettings() {
        mathEvaluator.setAngleUnit(themeManager.currentSettings.angleUnit)
        mathEvaluator.setPrecision(themeManager.currentSettings.precision)
        mathEvaluator.setThousandsSeparator(themeManager.currentSettings.thousandsSeparator)
    }

    // --- Expression & Keypad Handling ---

    fun onKeyInput(key: String) {
        saveStateForUndo()
        when (key) {
            "AC" -> clearAll()
            "C" -> clearEntry()
            "DEL" -> backspace()
            "=" -> calculateFinalResult()
            "+/-" -> toggleSign()
            "%" -> appendPercentage()
            "MC" -> memoryClear()
            "MR" -> memoryRecall()
            "M+" -> memoryAdd()
            "M-" -> memorySubtract()
            "DEG", "RAD" -> toggleAngleUnit()
            "2nd" -> isSecondActive = !isSecondActive
            else -> appendKey(key)
        }
        updatePreview()
    }

    private fun saveStateForUndo() {
        undoStack.add(expression)
        if (undoStack.size > 50) undoStack.removeAt(0)
        redoStack.clear()
        canUndo = undoStack.isNotEmpty()
        canRedo = false
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.add(expression)
            expression = undoStack.removeAt(undoStack.lastIndex)
            canUndo = undoStack.isNotEmpty()
            canRedo = true
            updatePreview()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.add(expression)
            expression = redoStack.removeAt(redoStack.lastIndex)
            canUndo = true
            canRedo = redoStack.isNotEmpty()
            updatePreview()
        }
    }

    private fun appendKey(key: String) {
        // If previous evaluation produced a result and user starts with a digit/function, start fresh
        if (result != "0" && expression.isEmpty() && isDigitOrNumber(key)) {
            expression = key
            return
        }
        // If user enters an operator right after a calculation, chain from previous result
        if (expression.isEmpty() && result != "0" && isOperator(key)) {
            expression = result + key
            return
        }
        expression += key
    }

    private fun isDigitOrNumber(key: String): Boolean {
        return key.all { it in '0'..'9' || it == '.' || it == 'π' || it == 'e' }
    }

    private fun isOperator(key: String): Boolean {
        return key in listOf("+", "-", "×", "÷", "^", "%", "mod")
    }

    private fun appendPercentage() {
        if (expression.isNotEmpty() && !isOperator(expression.takeLast(1))) {
            expression += "%"
        }
    }

    private fun toggleSign() {
        if (expression.isEmpty()) {
            if (result != "0") {
                expression = if (result.startsWith("-")) result.drop(1) else "-$result"
            }
            return
        }
        // If expression is a single number, toggle sign
        val clean = expression.replace(",", "")
        val num = clean.toDoubleOrNull()
        if (num != null) {
            expression = if (clean.startsWith("-")) clean.drop(1) else "-$clean"
        } else {
            // Append +/- as negation
            expression = "-($expression)"
        }
    }

    private fun backspace() {
        if (expression.isNotEmpty()) {
            // Check for multi-char tokens like 'sin(', 'cos(', 'sqrt(', 'log('
            val funcs = listOf("asin(", "acos(", "atan(", "sinh(", "cosh(", "tanh(", "sqrt(", "cbrt(", "log(", "ln(", "mod ")
            var matchedFunc: String? = null
            for (f in funcs) {
                if (expression.endsWith(f)) {
                    matchedFunc = f
                    break
                }
            }
            expression = if (matchedFunc != null) {
                expression.dropLast(matchedFunc.length)
            } else {
                expression.dropLast(1)
            }
        }
    }

    private fun clearAll() {
        expression = ""
        result = "0"
        previewResult = null
    }

    private fun clearEntry() {
        expression = ""
        previewResult = null
    }

    fun calculateFinalResult(mode: String = "STANDARD") {
        if (expression.isBlank()) return
        val eval = mathEvaluator.evaluate(expression)
        when (eval) {
            is EvalResult.Success -> {
                result = eval.formatted
                previewResult = null
                val exprToSave = expression
                val resToSave = eval.formatted
                expression = ""

                // Save to Room SQLite Database
                viewModelScope.launch {
                    calculationDao.insertCalculation(
                        CalculationEntity(
                            expression = exprToSave,
                            result = resToSave,
                            mode = mode,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
            is EvalResult.Error -> {
                result = eval.message
                previewResult = null
            }
        }
    }

    private fun updatePreview() {
        if (expression.isNotBlank()) {
            previewResult = mathEvaluator.evaluatePreview(expression)
        } else {
            previewResult = null
        }
    }

    fun restoreCalculation(item: CalculationEntity) {
        expression = item.expression
        result = item.result
        updatePreview()
    }

    fun deleteHistoryItem(item: CalculationEntity) {
        viewModelScope.launch {
            calculationDao.deleteCalculation(item)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            calculationDao.clearAllHistory()
        }
    }

    // Memory operations
    private fun memoryClear() {
        memoryValue = null
    }

    private fun memoryRecall() {
        memoryValue?.let {
            val formatted = mathEvaluator.formatResult(it)
            appendKey(formatted)
        }
    }

    private fun memoryAdd() {
        val current = result.replace(",", "").toDoubleOrNull() ?: return
        memoryValue = (memoryValue ?: 0.0) + current
    }

    private fun memorySubtract() {
        val current = result.replace(",", "").toDoubleOrNull() ?: return
        memoryValue = (memoryValue ?: 0.0) - current
    }

    fun toggleAngleUnit() {
        val newUnit = if (themeManager.currentSettings.angleUnit == AngleUnit.DEGREE) {
            AngleUnit.RADIAN
        } else {
            AngleUnit.DEGREE
        }
        themeManager.setAngleUnit(newUnit)
        mathEvaluator.setAngleUnit(newUnit)
        updatePreview()
    }

    // --- Programmer Mode Logic ---

    fun onProgrammerKey(key: String) {
        when (key) {
            "AC" -> {
                programmerValue = 0uL
                programmerPendingValue = null
                programmerPendingOp = null
                programmerInputBuffer = "0"
            }
            "DEL" -> {
                if (programmerInputBuffer.length > 1) {
                    programmerInputBuffer = programmerInputBuffer.dropLast(1)
                    programmerValue = ProgrammerEngine.parseInput(programmerInputBuffer, programmerBase, programmerWordSize)
                } else {
                    programmerInputBuffer = "0"
                    programmerValue = 0uL
                }
            }
            "NOT" -> {
                programmerValue = ProgrammerEngine.not(programmerValue, programmerWordSize)
                programmerInputBuffer = ProgrammerEngine.formatInBase(programmerValue, programmerBase, programmerWordSize)
            }
            "ROL" -> {
                programmerValue = ProgrammerEngine.rotateLeft(programmerValue, programmerWordSize)
                programmerInputBuffer = ProgrammerEngine.formatInBase(programmerValue, programmerBase, programmerWordSize)
            }
            "ROR" -> {
                programmerValue = ProgrammerEngine.rotateRight(programmerValue, programmerWordSize)
                programmerInputBuffer = ProgrammerEngine.formatInBase(programmerValue, programmerBase, programmerWordSize)
            }
            "AND", "OR", "XOR", "NAND", "NOR", "XNOR", "LSH", "RSH" -> {
                programmerPendingOp = BitwiseOp.valueOf(key)
                programmerPendingValue = programmerValue
                programmerInputBuffer = "0"
            }
            "=" -> {
                val op = programmerPendingOp
                val pending = programmerPendingValue
                if (op != null && pending != null) {
                    val computed = ProgrammerEngine.executeOp(pending, programmerValue, op, programmerWordSize)
                    programmerValue = computed
                    programmerPendingValue = null
                    programmerPendingOp = null
                    programmerInputBuffer = ProgrammerEngine.formatInBase(computed, programmerBase, programmerWordSize)

                    viewModelScope.launch {
                        calculationDao.insertCalculation(
                            CalculationEntity(
                                expression = "${ProgrammerEngine.formatInBase(pending, programmerBase, programmerWordSize)} $op ${ProgrammerEngine.formatInBase(programmerValue, programmerBase, programmerWordSize)}",
                                result = programmerInputBuffer,
                                mode = "PROGRAMMER"
                            )
                        )
                    }
                }
            }
            else -> {
                // Input character (0-9, A-F)
                if (key.length == 1 && ProgrammerEngine.isCharAllowed(key[0], programmerBase)) {
                    programmerInputBuffer = if (programmerInputBuffer == "0") key else programmerInputBuffer + key
                    programmerValue = ProgrammerEngine.parseInput(programmerInputBuffer, programmerBase, programmerWordSize)
                }
            }
        }
    }

    fun setProgrammerActiveBase(base: NumberBase) {
        programmerBase = base
        programmerInputBuffer = ProgrammerEngine.formatInBase(programmerValue, base, programmerWordSize)
    }

    fun setProgrammerWord(size: WordSize) {
        programmerWordSize = size
        programmerValue = programmerValue and size.mask
        programmerInputBuffer = ProgrammerEngine.formatInBase(programmerValue, programmerBase, size)
    }

    fun toggleProgrammerBit(bitIndex: Int) {
        programmerValue = ProgrammerEngine.toggleBit(programmerValue, bitIndex, programmerWordSize)
        programmerInputBuffer = ProgrammerEngine.formatInBase(programmerValue, programmerBase, programmerWordSize)
    }
}
