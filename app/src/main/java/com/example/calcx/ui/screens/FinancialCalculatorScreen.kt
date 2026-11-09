package com.example.calcx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcx.engine.FinancialEngine
import com.example.calcx.viewmodel.CalculatorViewModel

enum class FinancialSubTool(val label: String) {
    EMI("Loan / EMI"),
    COMPOUND_INTEREST("Compound"),
    SIMPLE_INTEREST("Simple"),
    DISCOUNT("Discount"),
    TIP("Tip"),
    TAX_GST("Tax & GST"),
    PERCENTAGE("Percentage"),
    PROFIT_LOSS("Profit & Loss")
}

@Composable
fun FinancialCalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = viewModel.themeManager.currentTheme
    var selectedTool by remember { mutableStateOf(FinancialSubTool.EMI) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        // Top SubTool Horizontal Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.surface)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FinancialSubTool.entries.forEach { tool ->
                val isSelected = selectedTool == tool
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedTool = tool },
                    label = {
                        Text(
                            text = tool.label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) theme.buttonEqualsText else theme.textSecondary
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = theme.accent,
                        containerColor = theme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) theme.accent else Color.Transparent
                    )
                )
            }
        }

        // Selected Tool Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTool) {
                FinancialSubTool.EMI -> EmiCalculatorView(theme)
                FinancialSubTool.COMPOUND_INTEREST -> CompoundInterestView(theme)
                FinancialSubTool.SIMPLE_INTEREST -> SimpleInterestView(theme)
                FinancialSubTool.DISCOUNT -> DiscountCalculatorView(theme)
                FinancialSubTool.TIP -> TipCalculatorView(theme)
                FinancialSubTool.TAX_GST -> TaxGstCalculatorView(theme)
                FinancialSubTool.PERCENTAGE -> PercentageCalculatorView(theme)
                FinancialSubTool.PROFIT_LOSS -> ProfitLossCalculatorView(theme)
            }
        }
    }
}

@Composable
fun EmiCalculatorView(theme: com.example.calcx.theme.ThemeColors) {
    var principalStr by remember { mutableStateOf("100000") }
    var rateStr by remember { mutableStateOf("8.5") }
    var tenureStr by remember { mutableStateOf("24") }
    var isTenureYears by remember { mutableStateOf(false) }

    val principal = principalStr.toDoubleOrNull() ?: 0.0
    val rate = rateStr.toDoubleOrNull() ?: 0.0
    val tenureMonths = (tenureStr.toIntOrNull() ?: 0) * (if (isTenureYears) 12 else 1)

    val result = FinancialEngine.calculateEmi(principal, rate, tenureMonths)

    Card(
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Loan / EMI Calculator", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)

            OutlinedTextField(
                value = principalStr,
                onValueChange = { principalStr = it },
                label = { Text("Loan Amount / Principal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rateStr,
                onValueChange = { rateStr = it },
                label = { Text("Annual Interest Rate (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = tenureStr,
                    onValueChange = { tenureStr = it },
                    label = { Text(if (isTenureYears) "Tenure (Years)" else "Tenure (Months)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { isTenureYears = !isTenureYears },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.surfaceVariant)
                ) {
                    Text(if (isTenureYears) "Years" else "Months", color = theme.accent)
                }
            }
        }
    }

    // Results Summary Card
    Card(
        colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Results Summary", color = theme.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            FinanceSummaryRow("Monthly EMI", "$ ${"%.2f".format(result.monthlyEmi)}", theme.accent, isHighlight = true)
            FinanceSummaryRow("Total Interest", "$ ${"%.2f".format(result.totalInterest)}", theme.accentSecondary)
            FinanceSummaryRow("Total Payment", "$ ${"%.2f".format(result.totalPayment)}", theme.textPrimary)
        }
    }
}

@Composable
fun CompoundInterestView(theme: com.example.calcx.theme.ThemeColors) {
    var principalStr by remember { mutableStateOf("10000") }
    var rateStr by remember { mutableStateOf("7.0") }
    var timeStr by remember { mutableStateOf("5") }
    var frequency by remember { mutableStateOf(12) } // monthly

    val principal = principalStr.toDoubleOrNull() ?: 0.0
    val rate = rateStr.toDoubleOrNull() ?: 0.0
    val time = timeStr.toDoubleOrNull() ?: 0.0

    val result = FinancialEngine.calculateCompoundInterest(principal, rate, time, frequency)

    Card(colors = CardDefaults.cardColors(containerColor = theme.surface), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Compound Interest Calculator", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = principalStr, onValueChange = { principalStr = it }, label = { Text("Initial Deposit") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rateStr, onValueChange = { rateStr = it }, label = { Text("Annual Rate (%)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = timeStr, onValueChange = { timeStr = it }, label = { Text("Time Period (Years)") }, modifier = Modifier.fillMaxWidth())

            Text("Compounding Frequency", color = theme.textSecondary, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(1 to "Yearly", 4 to "Quarterly", 12 to "Monthly", 365 to "Daily").forEach { (freq, lbl) ->
                    val isSel = frequency == freq
                    FilterChip(
                        selected = isSel,
                        onClick = { frequency = freq },
                        label = { Text(lbl, fontSize = 12.sp) }
                    )
                }
            }
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinanceSummaryRow("Maturity Amount", "$ ${"%.2f".format(result.totalAmount)}", theme.accent, isHighlight = true)
            FinanceSummaryRow("Total Interest Earned", "$ ${"%.2f".format(result.interest)}", theme.accentSecondary)
        }
    }
}

@Composable
fun SimpleInterestView(theme: com.example.calcx.theme.ThemeColors) {
    var pStr by remember { mutableStateOf("5000") }
    var rStr by remember { mutableStateOf("5.0") }
    var tStr by remember { mutableStateOf("3") }

    val p = pStr.toDoubleOrNull() ?: 0.0
    val r = rStr.toDoubleOrNull() ?: 0.0
    val t = tStr.toDoubleOrNull() ?: 0.0
    val res = FinancialEngine.calculateSimpleInterest(p, r, t)

    Card(colors = CardDefaults.cardColors(containerColor = theme.surface), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Simple Interest Calculator", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = pStr, onValueChange = { pStr = it }, label = { Text("Principal Amount") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rStr, onValueChange = { rStr = it }, label = { Text("Interest Rate (% per year)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = tStr, onValueChange = { tStr = it }, label = { Text("Time (Years)") }, modifier = Modifier.fillMaxWidth())
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinanceSummaryRow("Total Amount", "$ ${"%.2f".format(res.totalAmount)}", theme.accent, isHighlight = true)
            FinanceSummaryRow("Total Interest", "$ ${"%.2f".format(res.interest)}", theme.accentSecondary)
        }
    }
}

@Composable
fun DiscountCalculatorView(theme: com.example.calcx.theme.ThemeColors) {
    var priceStr by remember { mutableStateOf("120") }
    var discountStr by remember { mutableStateOf("25") }
    var taxStr by remember { mutableStateOf("8.5") }

    val price = priceStr.toDoubleOrNull() ?: 0.0
    val discount = discountStr.toDoubleOrNull() ?: 0.0
    val tax = taxStr.toDoubleOrNull() ?: 0.0
    val res = FinancialEngine.calculateDiscount(price, discount, tax)

    Card(colors = CardDefaults.cardColors(containerColor = theme.surface), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Discount & Sale Calculator", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Original Price ($)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = discountStr, onValueChange = { discountStr = it }, label = { Text("Discount (%)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = taxStr, onValueChange = { taxStr = it }, label = { Text("Sales Tax (%)") }, modifier = Modifier.fillMaxWidth())
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinanceSummaryRow("Final Price", "$ ${"%.2f".format(res.finalPrice)}", theme.accent, isHighlight = true)
            FinanceSummaryRow("You Save", "$ ${"%.2f".format(res.discountAmount)}", theme.accentSecondary)
            FinanceSummaryRow("Tax Added", "$ ${"%.2f".format(res.taxAmount)}", theme.textSecondary)
        }
    }
}

@Composable
fun TipCalculatorView(theme: com.example.calcx.theme.ThemeColors) {
    var billStr by remember { mutableStateOf("85.00") }
    var tipPercent by remember { mutableStateOf(18.0) }
    var splitCount by remember { mutableStateOf(2) }

    val bill = billStr.toDoubleOrNull() ?: 0.0
    val res = FinancialEngine.calculateTip(bill, tipPercent, splitCount)

    Card(colors = CardDefaults.cardColors(containerColor = theme.surface), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Tip & Bill Splitter", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = billStr, onValueChange = { billStr = it }, label = { Text("Bill Total ($)") }, modifier = Modifier.fillMaxWidth())

            Text("Tip Percentage: ${tipPercent.toInt()}%", color = theme.textSecondary, fontSize = 14.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(10.0, 15.0, 18.0, 20.0, 25.0).forEach { p ->
                    FilterChip(
                        selected = tipPercent == p,
                        onClick = { tipPercent = p },
                        label = { Text("${p.toInt()}%") }
                    )
                }
            }

            Text("Split Between: $splitCount people", color = theme.textSecondary, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { if (splitCount > 1) splitCount-- },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.surfaceVariant)
                ) { Text("-", fontSize = 18.sp, color = theme.accent) }
                Text("$splitCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary)
                Button(
                    onClick = { splitCount++ },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.surfaceVariant)
                ) { Text("+", fontSize = 18.sp, color = theme.accent) }
            }
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinanceSummaryRow("Total per Person", "$ ${"%.2f".format(res.totalPerPerson)}", theme.accent, isHighlight = true)
            FinanceSummaryRow("Tip per Person", "$ ${"%.2f".format(res.tipPerPerson)}", theme.accentSecondary)
            FinanceSummaryRow("Total Bill + Tip", "$ ${"%.2f".format(res.totalBill)}", theme.textPrimary)
        }
    }
}

@Composable
fun TaxGstCalculatorView(theme: com.example.calcx.theme.ThemeColors) {
    var amountStr by remember { mutableStateOf("1000") }
    var rateStr by remember { mutableStateOf("18.0") }
    var isAddGst by remember { mutableStateOf(true) }

    val amount = amountStr.toDoubleOrNull() ?: 0.0
    val rate = rateStr.toDoubleOrNull() ?: 0.0
    val res = FinancialEngine.calculateGst(amount, rate, isAddGst)

    Card(colors = CardDefaults.cardColors(containerColor = theme.surface), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Tax & GST Calculator", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = amountStr, onValueChange = { amountStr = it }, label = { Text("Amount ($)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rateStr, onValueChange = { rateStr = it }, label = { Text("Tax Rate (%)") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = isAddGst, onClick = { isAddGst = true }, label = { Text("Add GST (+)") })
                FilterChip(selected = !isAddGst, onClick = { isAddGst = false }, label = { Text("Remove GST (-)") })
            }
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinanceSummaryRow(if (isAddGst) "Gross Amount" else "Net Amount", "$ ${"%.2f".format(if (isAddGst) res.grossAmount else res.netAmount)}", theme.accent, isHighlight = true)
            FinanceSummaryRow("Total Tax / GST", "$ ${"%.2f".format(res.totalGst)}", theme.accentSecondary)
            FinanceSummaryRow("CGST / SGST (each)", "$ ${"%.2f".format(res.cgst)}", theme.textSecondary)
        }
    }
}

@Composable
fun PercentageCalculatorView(theme: com.example.calcx.theme.ThemeColors) {
    var pctStr by remember { mutableStateOf("25") }
    var totalStr by remember { mutableStateOf("400") }

    val pct = pctStr.toDoubleOrNull() ?: 0.0
    val total = totalStr.toDoubleOrNull() ?: 0.0
    val res = FinancialEngine.calculatePercentOf(pct, total)

    Card(colors = CardDefaults.cardColors(containerColor = theme.surface), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Percentage Calculator", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = pctStr, onValueChange = { pctStr = it }, label = { Text("What is (%)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = totalStr, onValueChange = { totalStr = it }, label = { Text("Of (Total)") }, modifier = Modifier.fillMaxWidth())
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinanceSummaryRow("Result", "${"%.2f".format(res.resultValue)}", theme.accent, isHighlight = true)
            Text(res.description, color = theme.textSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
fun ProfitLossView(theme: com.example.calcx.theme.ThemeColors) {
    ProfitLossCalculatorView(theme)
}

@Composable
fun ProfitLossCalculatorView(theme: com.example.calcx.theme.ThemeColors) {
    var cpStr by remember { mutableStateOf("500") }
    var spStr by remember { mutableStateOf("650") }

    val cp = cpStr.toDoubleOrNull() ?: 0.0
    val sp = spStr.toDoubleOrNull() ?: 0.0
    val res = FinancialEngine.calculateProfitLoss(cp, sp)

    Card(colors = CardDefaults.cardColors(containerColor = theme.surface), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Profit & Loss Calculator", color = theme.accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            OutlinedTextField(value = cpStr, onValueChange = { cpStr = it }, label = { Text("Cost Price ($)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = spStr, onValueChange = { spStr = it }, label = { Text("Selling Price ($)") }, modifier = Modifier.fillMaxWidth())
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val status = if (res.isProfit) "PROFIT" else "LOSS"
            val statusColor = if (res.isProfit) Color(0xFF00E676) else Color(0xFFFF5252)
            FinanceSummaryRow(status, "$ ${"%.2f".format(kotlin.math.abs(res.difference))}", statusColor, isHighlight = true)
            FinanceSummaryRow("Margin Percentage", "${"%.2f".format(res.percentage)}%", theme.accent)
        }
    }
}

@Composable
fun FinanceSummaryRow(label: String, value: String, color: Color, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.8f), fontSize = if (isHighlight) 16.sp else 14.sp)
        Text(
            text = value,
            color = color,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = if (isHighlight) 20.sp else 16.sp
        )
    }
}
