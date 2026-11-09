package com.example.calcx.engine

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.*

enum class AngleUnit {
    DEGREE, RADIAN
}

sealed class EvalResult {
    data class Success(val value: Double, val formatted: String) : EvalResult()
    data class Error(val message: String) : EvalResult()
}

class MathEvaluator(
    private var angleUnit: AngleUnit = AngleUnit.DEGREE,
    private var precision: Int = 8,
    private var useThousandsSeparator: Boolean = true
) {

    fun setAngleUnit(unit: AngleUnit) {
        this.angleUnit = unit
    }

    fun setPrecision(p: Int) {
        this.precision = p
    }

    fun setThousandsSeparator(enabled: Boolean) {
        this.useThousandsSeparator = enabled
    }

    /**
     * Evaluates an arithmetic / scientific expression string.
     */
    fun evaluate(expression: String): EvalResult {
        if (expression.isBlank()) {
            return EvalResult.Success(0.0, "0")
        }

        val sanitized = sanitize(expression)
        if (sanitized.isBlank()) {
            return EvalResult.Success(0.0, "0")
        }

        return try {
            val parser = Parser(sanitized, angleUnit)
            val result = parser.parse()
            if (result.isNaN()) {
                EvalResult.Error("Undefined")
            } else if (result.isInfinite()) {
                EvalResult.Error(if (result > 0) "Infinity" else "-Infinity")
            } else {
                EvalResult.Success(result, formatResult(result))
            }
        } catch (e: ArithmeticException) {
            EvalResult.Error(e.message ?: "Math error")
        } catch (e: Exception) {
            EvalResult.Error("Invalid expression")
        }
    }

    /**
     * Safely attempts to preview partial expressions while typing.
     */
    fun evaluatePreview(expression: String): String? {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return null

        // If ends with operator, strip trailing operator for preview
        var exprToTest = trimmed
        val trailingOperators = charArrayOf('+', '-', '×', '*', '÷', '/', '^', '%', '(')
        while (exprToTest.isNotEmpty() && trailingOperators.contains(exprToTest.last())) {
            exprToTest = exprToTest.dropLast(1).trim()
        }
        if (exprToTest.isEmpty()) return null

        // Balance open parentheses if any
        val openCount = exprToTest.count { it == '(' }
        val closeCount = exprToTest.count { it == ')' }
        if (openCount > closeCount) {
            exprToTest += ")".repeat(openCount - closeCount)
        }

        return when (val res = evaluate(exprToTest)) {
            is EvalResult.Success -> {
                // Only show preview if it differs from simple literal
                val stripped = exprToTest.replace(",", "").trim()
                if (stripped.toDoubleOrNull() != null) null else res.formatted
            }
            is EvalResult.Error -> null
        }
    }

    fun formatResult(value: Double): String {
        if (value.isNaN()) return "Undefined"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"

        // Handle exact integer representation within limits
        val absVal = abs(value)
        if (absVal != 0.0 && (absVal >= 1e12 || absVal < 1e-7)) {
            val df = DecimalFormat("0.######E0", DecimalFormatSymbols(Locale.US))
            return df.format(value)
        }

        try {
            val bd = BigDecimal.valueOf(value).setScale(precision, RoundingMode.HALF_UP).stripTrailingZeros()
            val plain = bd.toPlainString()

            if (!useThousandsSeparator) {
                return plain
            }

            val parts = plain.split(".")
            val intPart = parts[0]
            val isNegative = intPart.startsWith("-")
            val digitsOnly = if (isNegative) intPart.substring(1) else intPart

            val formattedInt = buildString {
                val len = digitsOnly.length
                for (i in 0 until len) {
                    append(digitsOnly[i])
                    val remaining = len - 1 - i
                    if (remaining > 0 && remaining % 3 == 0) {
                        append(',')
                    }
                }
            }

            val signedInt = if (isNegative) "-$formattedInt" else formattedInt
            return if (parts.size > 1 && parts[1].isNotEmpty()) {
                "$signedInt.${parts[1]}"
            } else {
                signedInt
            }
        } catch (e: Exception) {
            return value.toString()
        }
    }

    private fun sanitize(input: String): String {
        return input
            .replace("×", "*")
            .replace("÷", "/")
            .replace("π", "pi")
            .replace("√", "sqrt")
            .replace("∛", "cbrt")
            .replace("mod", "%")
            .replace(" ", "")
            .replace(",", "")
    }

    private class Parser(
        private val str: String,
        private val angleUnit: AngleUnit
    ) {
        private var pos = -1
        private var ch = ' '

        private fun nextChar() {
            pos++
            ch = if (pos < str.length) str[pos] else '\u0000'
        }

        private fun eat(charToEat: Char): Boolean {
            while (ch == ' ') nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw IllegalArgumentException("Unexpected: $ch")
            return x
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor | term `%` factor
        // factor = `+` factor | `-` factor | `(` expression `)` | number | functionName factor | factor `^` factor | factor `!`
        private fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                when {
                    eat('+') -> x += parseTerm()
                    eat('-') -> x -= parseTerm()
                    else -> return x
                }
            }
        }

        private fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                when {
                    eat('*') -> x *= parseFactor()
                    eat('/') -> {
                        val divisor = parseFactor()
                        if (divisor == 0.0) throw ArithmeticException("Cannot divide by zero")
                        x /= divisor
                    }
                    eat('%') -> {
                        val modVal = parseFactor()
                        if (modVal == 0.0) throw ArithmeticException("Cannot modulo by zero")
                        x %= modVal
                    }
                    else -> return x
                }
            }
        }

        private fun parseFactor(): Double {
            if (eat('+')) return +parseFactor()
            if (eat('-')) return -parseFactor()

            var x: Double
            val startPos = pos

            if (eat('(')) {
                x = parseExpression()
                if (!eat(')')) throw IllegalArgumentException("Missing closing parenthesis")
            } else if ((ch in '0'..'9') || ch == '.') {
                while ((ch in '0'..'9') || ch == '.') nextChar()
                // Handle scientific notation e.g. 1e5
                if (ch == 'e' || ch == 'E') {
                    nextChar()
                    if (ch == '+' || ch == '-') nextChar()
                    while (ch in '0'..'9') nextChar()
                }
                x = str.substring(startPos, pos).toDouble()
            } else if (ch in 'a'..'z' || ch in 'A'..'Z') {
                while (ch in 'a'..'z' || ch in 'A'..'Z' || ch in '0'..'9') nextChar()
                val func = str.substring(startPos, pos).lowercase()
                x = when (func) {
                    "pi" -> Math.PI
                    "e" -> Math.E
                    else -> {
                        // Function with argument
                        val arg = parseFactor()
                        evaluateFunction(func, arg)
                    }
                }
            } else {
                throw IllegalArgumentException("Unexpected char: '$ch' at position $pos in '$str'")
            }

            // Postfix operators: ^ (power) and ! (factorial)
            while (true) {
                when {
                    eat('^') -> {
                        val exponent = parseFactor()
                        x = x.pow(exponent)
                    }
                    eat('!') -> {
                        x = factorial(x)
                    }
                    else -> return x
                }
            }
        }

        private fun evaluateFunction(func: String, arg: Double): Double {
            return when (func) {
                "sqrt" -> {
                    if (arg < 0) throw ArithmeticException("Square root of negative number")
                    sqrt(arg)
                }
                "cbrt" -> cbrt(arg)
                "sin" -> sin(toRadians(arg))
                "cos" -> cos(toRadians(arg))
                "tan" -> {
                    val rad = toRadians(arg)
                    // Check for undefined tan at 90 + 180k
                    val cosVal = cos(rad)
                    if (abs(cosVal) < 1e-15) throw ArithmeticException("Tan undefined")
                    tan(rad)
                }
                "asin" -> {
                    if (arg < -1.0 || arg > 1.0) throw ArithmeticException("asin domain error [-1, 1]")
                    fromRadians(asin(arg))
                }
                "acos" -> {
                    if (arg < -1.0 || arg > 1.0) throw ArithmeticException("acos domain error [-1, 1]")
                    fromRadians(acos(arg))
                }
                "atan" -> fromRadians(atan(arg))
                "sinh" -> sinh(arg)
                "cosh" -> cosh(arg)
                "tanh" -> tanh(arg)
                "log" -> {
                    if (arg <= 0) throw ArithmeticException("log domain error (>0)")
                    log10(arg)
                }
                "ln" -> {
                    if (arg <= 0) throw ArithmeticException("ln domain error (>0)")
                    ln(arg)
                }
                "abs" -> abs(arg)
                "exp" -> exp(arg)
                else -> throw IllegalArgumentException("Unknown function: $func")
            }
        }

        private fun toRadians(angle: Double): Double {
            return if (angleUnit == AngleUnit.DEGREE) Math.toRadians(angle) else angle
        }

        private fun fromRadians(rad: Double): Double {
            return if (angleUnit == AngleUnit.DEGREE) Math.toDegrees(rad) else rad
        }

        private fun factorial(n: Double): Double {
            if (n < 0 || n != floor(n)) throw ArithmeticException("Factorial requires non-negative integer")
            if (n > 170) throw ArithmeticException("Factorial overflow (>170)")
            var res = 1.0
            val intVal = n.toLong()
            for (i in 2..intVal) {
                res *= i
            }
            return res
        }
    }
}
