package com.example

import com.example.calcx.engine.*
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    private val evaluator = MathEvaluator(
        angleUnit = AngleUnit.DEGREE,
        precision = 6,
        useThousandsSeparator = false
    )

    @Test
    fun testOperatorPrecedence() {
        val res = evaluator.evaluate("2 + 3 * 4")
        assertTrue(res is EvalResult.Success)
        assertEquals(14.0, (res as EvalResult.Success).value, 1e-6)
    }

    @Test
    fun testParentheses() {
        val res = evaluator.evaluate("(2 + 3) * 4")
        assertTrue(res is EvalResult.Success)
        assertEquals(20.0, (res as EvalResult.Success).value, 1e-6)
    }

    @Test
    fun testDivisionByZero() {
        val res = evaluator.evaluate("10 / 0")
        assertTrue(res is EvalResult.Error)
        assertEquals("Cannot divide by zero", (res as EvalResult.Error).message)
    }

    @Test
    fun testScientificFunctions() {
        // Degree mode sin(90) = 1
        val sinRes = evaluator.evaluate("sin(90)")
        assertTrue(sinRes is EvalResult.Success)
        assertEquals(1.0, (sinRes as EvalResult.Success).value, 1e-6)

        // Sqrt(25) = 5
        val sqrtRes = evaluator.evaluate("sqrt(25)")
        assertTrue(sqrtRes is EvalResult.Success)
        assertEquals(5.0, (sqrtRes as EvalResult.Success).value, 1e-6)

        // Power 2^5 = 32
        val powRes = evaluator.evaluate("2^5")
        assertTrue(powRes is EvalResult.Success)
        assertEquals(32.0, (powRes as EvalResult.Success).value, 1e-6)

        // Factorial 5! = 120
        val factRes = evaluator.evaluate("5!")
        assertTrue(factRes is EvalResult.Success)
        assertEquals(120.0, (factRes as EvalResult.Success).value, 1e-6)
    }

    @Test
    fun testProgrammerEngine() {
        // Bitwise AND
        val andVal = ProgrammerEngine.executeOp(0x0FuL, 0x03uL, BitwiseOp.AND, WordSize.BYTE)
        assertEquals(0x03uL, andVal)

        // Bitwise XOR
        val xorVal = ProgrammerEngine.executeOp(0xAAuL, 0x55uL, BitwiseOp.XOR, WordSize.BYTE)
        assertEquals(0xFFuL, xorVal)

        // Base conversion
        val hexStr = ProgrammerEngine.formatInBase(255uL, NumberBase.HEX, WordSize.BYTE)
        assertEquals("FF", hexStr)

        val binStr = ProgrammerEngine.formatInBase(10uL, NumberBase.BIN, WordSize.BYTE)
        assertEquals("0000 1010", binStr)
    }

    @Test
    fun testFinancialFormulas() {
        // Simple Interest: P=1000, R=10%, T=2 years -> I=200, Total=1200
        val si = FinancialEngine.calculateSimpleInterest(1000.0, 10.0, 2.0)
        assertEquals(200.0, si.interest, 1e-2)
        assertEquals(1200.0, si.totalAmount, 1e-2)

        // Tip calculation: $100 bill, 20% tip, 2 people -> $10 tip each, $60 total each
        val tip = FinancialEngine.calculateTip(100.0, 20.0, 2)
        assertEquals(20.0, tip.totalTip, 1e-2)
        assertEquals(10.0, tip.tipPerPerson, 1e-2)
        assertEquals(60.0, tip.totalPerPerson, 1e-2)

        // Discount: $100, 20% discount -> $80
        val disc = FinancialEngine.calculateDiscount(100.0, 20.0, 0.0)
        assertEquals(20.0, disc.discountAmount, 1e-2)
        assertEquals(80.0, disc.finalPrice, 1e-2)
    }

    @Test
    fun testUnitConverter() {
        // 1 Kilometer = 1000 Meters
        val km = UnitItem("Kilometer", "km", 1000.0)
        val m = UnitItem("Meter", "m", 1.0)
        val meters = UnitConverterEngine.convert(1.0, km, m, UnitCategory.LENGTH)
        assertEquals(1000.0, meters, 1e-6)

        // 100°C = 212°F
        val celsius = UnitItem("Celsius", "°C", 1.0)
        val fahr = UnitItem("Fahrenheit", "°F", 1.0)
        val fVal = UnitConverterEngine.convert(100.0, celsius, fahr, UnitCategory.TEMPERATURE)
        assertEquals(212.0, fVal, 1e-6)
    }
}
