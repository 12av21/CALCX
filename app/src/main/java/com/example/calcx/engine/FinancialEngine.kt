package com.example.calcx.engine

import kotlin.math.pow
import kotlin.math.roundToLong

object FinancialEngine {

    data class SimpleInterestResult(
        val interest: Double,
        val totalAmount: Double
    )

    fun calculateSimpleInterest(principal: Double, ratePercent: Double, timeYears: Double): SimpleInterestResult {
        val interest = (principal * ratePercent * timeYears) / 100.0
        val total = principal + interest
        return SimpleInterestResult(interest = interest, totalAmount = total)
    }

    data class CompoundInterestResult(
        val interest: Double,
        val totalAmount: Double
    )

    fun calculateCompoundInterest(
        principal: Double,
        ratePercent: Double,
        timeYears: Double,
        compoundingPerYear: Int = 12
    ): CompoundInterestResult {
        val r = (ratePercent / 100.0) / compoundingPerYear
        val nt = compoundingPerYear * timeYears
        val total = principal * (1.0 + r).pow(nt)
        val interest = total - principal
        return CompoundInterestResult(interest = interest, totalAmount = total)
    }

    data class EmiResult(
        val monthlyEmi: Double,
        val totalInterest: Double,
        val totalPayment: Double
    )

    fun calculateEmi(principal: Double, annualRatePercent: Double, tenureMonths: Int): EmiResult {
        if (principal <= 0 || tenureMonths <= 0) return EmiResult(0.0, 0.0, 0.0)
        if (annualRatePercent <= 0) {
            val emi = principal / tenureMonths
            return EmiResult(monthlyEmi = emi, totalInterest = 0.0, totalPayment = principal)
        }
        val monthlyRate = (annualRatePercent / 12.0) / 100.0
        val factor = (1.0 + monthlyRate).pow(tenureMonths.toDouble())
        val emi = (principal * monthlyRate * factor) / (factor - 1.0)
        val totalPayment = emi * tenureMonths
        val totalInterest = totalPayment - principal
        return EmiResult(monthlyEmi = emi, totalInterest = totalInterest, totalPayment = totalPayment)
    }

    data class PercentageResult(
        val resultValue: Double,
        val description: String
    )

    fun calculatePercentOf(percentage: Double, total: Double): PercentageResult {
        val res = (percentage / 100.0) * total
        return PercentageResult(res, "$percentage% of $total is $res")
    }

    fun calculatePercentageChange(fromValue: Double, toValue: Double): PercentageResult {
        if (fromValue == 0.0) return PercentageResult(0.0, "From value cannot be zero")
        val diff = toValue - fromValue
        val changePct = (diff / fromValue) * 100.0
        val desc = if (changePct >= 0) "+${"%.2f".format(changePct)}% increase" else "${"%.2f".format(changePct)}% decrease"
        return PercentageResult(changePct, desc)
    }

    data class DiscountResult(
        val discountAmount: Double,
        val priceAfterDiscount: Double,
        val taxAmount: Double,
        val finalPrice: Double
    )

    fun calculateDiscount(originalPrice: Double, discountPercent: Double, taxPercent: Double = 0.0): DiscountResult {
        val discount = originalPrice * (discountPercent / 100.0)
        val afterDiscount = (originalPrice - discount).coerceAtLeast(0.0)
        val tax = afterDiscount * (taxPercent / 100.0)
        val finalPrice = afterDiscount + tax
        return DiscountResult(
            discountAmount = discount,
            priceAfterDiscount = afterDiscount,
            taxAmount = tax,
            finalPrice = finalPrice
        )
    }

    data class ProfitLossResult(
        val difference: Double,
        val percentage: Double,
        val isProfit: Boolean
    )

    fun calculateProfitLoss(costPrice: Double, sellingPrice: Double): ProfitLossResult {
        val diff = sellingPrice - costPrice
        val pct = if (costPrice != 0.0) (diff / costPrice) * 100.0 else 0.0
        return ProfitLossResult(
            difference = diff,
            percentage = pct,
            isProfit = diff >= 0
        )
    }

    data class GstResult(
        val netAmount: Double,
        val cgst: Double,
        val sgst: Double,
        val totalGst: Double,
        val grossAmount: Double
    )

    fun calculateGst(amount: Double, gstRatePercent: Double, isAddGst: Boolean): GstResult {
        return if (isAddGst) {
            val totalGst = amount * (gstRatePercent / 100.0)
            val halfGst = totalGst / 2.0
            val gross = amount + totalGst
            GstResult(amount, halfGst, halfGst, totalGst, gross)
        } else {
            val net = amount / (1.0 + (gstRatePercent / 100.0))
            val totalGst = amount - net
            val halfGst = totalGst / 2.0
            GstResult(net, halfGst, halfGst, totalGst, amount)
        }
    }

    data class TipResult(
        val totalTip: Double,
        val totalBill: Double,
        val tipPerPerson: Double,
        val totalPerPerson: Double
    )

    fun calculateTip(billAmount: Double, tipPercent: Double, splitCount: Int): TipResult {
        val validSplit = splitCount.coerceAtLeast(1)
        val tip = billAmount * (tipPercent / 100.0)
        val total = billAmount + tip
        return TipResult(
            totalTip = tip,
            totalBill = total,
            tipPerPerson = tip / validSplit,
            totalPerPerson = total / validSplit
        )
    }
}
