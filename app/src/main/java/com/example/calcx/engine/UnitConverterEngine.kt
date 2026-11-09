package com.example.calcx.engine

data class UnitItem(
    val name: String,
    val symbol: String,
    val factorToBase: Double // Multiply by this to get base unit
)

enum class UnitCategory(val displayName: String, val baseUnit: String) {
    LENGTH("Length", "Meters"),
    WEIGHT("Weight", "Kilograms"),
    TEMPERATURE("Temperature", "Celsius"),
    AREA("Area", "Sq Meters"),
    VOLUME("Volume", "Liters"),
    SPEED("Speed", "m/s"),
    TIME("Time", "Seconds"),
    DATA("Data Storage", "Bytes"),
    ENERGY("Energy", "Joules"),
    PRESSURE("Pressure", "Pascals"),
    POWER("Power", "Watts"),
    CURRENCY("Currency", "USD")
}

object UnitConverterEngine {

    val unitsByCategory: Map<UnitCategory, List<UnitItem>> = mapOf(
        UnitCategory.LENGTH to listOf(
            UnitItem("Meter", "m", 1.0),
            UnitItem("Kilometer", "km", 1000.0),
            UnitItem("Centimeter", "cm", 0.01),
            UnitItem("Millimeter", "mm", 0.001),
            UnitItem("Micrometer", "µm", 1e-6),
            UnitItem("Nanometer", "nm", 1e-9),
            UnitItem("Mile", "mi", 1609.344),
            UnitItem("Yard", "yd", 0.9144),
            UnitItem("Foot", "ft", 0.3048),
            UnitItem("Inch", "in", 0.0254),
            UnitItem("Nautical Mile", "nmi", 1852.0)
        ),
        UnitCategory.WEIGHT to listOf(
            UnitItem("Kilogram", "kg", 1.0),
            UnitItem("Gram", "g", 0.001),
            UnitItem("Milligram", "mg", 1e-6),
            UnitItem("Metric Ton", "t", 1000.0),
            UnitItem("Pound", "lb", 0.45359237),
            UnitItem("Ounce", "oz", 0.028349523125),
            UnitItem("Stone", "st", 6.35029318),
            UnitItem("Carat", "ct", 0.0002)
        ),
        UnitCategory.TEMPERATURE to listOf(
            UnitItem("Celsius", "°C", 1.0),
            UnitItem("Fahrenheit", "°F", 1.0),
            UnitItem("Kelvin", "K", 1.0)
        ),
        UnitCategory.AREA to listOf(
            UnitItem("Square Meter", "m²", 1.0),
            UnitItem("Square Kilometer", "km²", 1e6),
            UnitItem("Square Centimeter", "cm²", 1e-4),
            UnitItem("Square Foot", "ft²", 0.092903),
            UnitItem("Square Yard", "yd²", 0.836127),
            UnitItem("Square Mile", "mi²", 2.58999e6),
            UnitItem("Acre", "ac", 4046.856),
            UnitItem("Hectare", "ha", 10000.0)
        ),
        UnitCategory.VOLUME to listOf(
            UnitItem("Liter", "L", 1.0),
            UnitItem("Milliliter", "mL", 0.001),
            UnitItem("Cubic Meter", "m³", 1000.0),
            UnitItem("US Gallon", "gal", 3.78541),
            UnitItem("US Quart", "qt", 0.946353),
            UnitItem("US Pint", "pt", 0.473176),
            UnitItem("US Cup", "cup", 0.236588),
            UnitItem("Fluid Ounce", "fl oz", 0.0295735)
        ),
        UnitCategory.SPEED to listOf(
            UnitItem("Meters/sec", "m/s", 1.0),
            UnitItem("Kilometers/hour", "km/h", 0.277778),
            UnitItem("Miles/hour", "mph", 0.44704),
            UnitItem("Knot", "kn", 0.514444),
            UnitItem("Feet/sec", "ft/s", 0.3048)
        ),
        UnitCategory.TIME to listOf(
            UnitItem("Second", "s", 1.0),
            UnitItem("Millisecond", "ms", 0.001),
            UnitItem("Minute", "min", 60.0),
            UnitItem("Hour", "h", 3600.0),
            UnitItem("Day", "d", 86400.0),
            UnitItem("Week", "wk", 604800.0),
            UnitItem("Month (30d)", "mo", 2592000.0),
            UnitItem("Year (365d)", "yr", 31536000.0)
        ),
        UnitCategory.DATA to listOf(
            UnitItem("Byte", "B", 1.0),
            UnitItem("Kilobyte", "KB", 1000.0),
            UnitItem("Megabyte", "MB", 1e6),
            UnitItem("Gigabyte", "GB", 1e9),
            UnitItem("Terabyte", "TB", 1e12),
            UnitItem("Petabyte", "PB", 1e15),
            UnitItem("Kibibyte", "KiB", 1024.0),
            UnitItem("Mebibyte", "MiB", 1048576.0),
            UnitItem("Gibibyte", "GiB", 1073741824.0),
            UnitItem("Tebibyte", "TiB", 1099511627776.0),
            UnitItem("Bit", "b", 0.125)
        ),
        UnitCategory.ENERGY to listOf(
            UnitItem("Joule", "J", 1.0),
            UnitItem("Kilojoule", "kJ", 1000.0),
            UnitItem("Calorie", "cal", 4.184),
            UnitItem("Kilocalorie", "kcal", 4184.0),
            UnitItem("Watt-hour", "Wh", 3600.0),
            UnitItem("Kilowatt-hour", "kWh", 3.6e6),
            UnitItem("BTU", "BTU", 1055.06),
            UnitItem("Electronvolt", "eV", 1.60218e-19)
        ),
        UnitCategory.PRESSURE to listOf(
            UnitItem("Pascal", "Pa", 1.0),
            UnitItem("Kilopascal", "kPa", 1000.0),
            UnitItem("Bar", "bar", 100000.0),
            UnitItem("PSI", "psi", 6894.76),
            UnitItem("Atmosphere", "atm", 101325.0),
            UnitItem("Torr (mmHg)", "Torr", 133.322)
        ),
        UnitCategory.POWER to listOf(
            UnitItem("Watt", "W", 1.0),
            UnitItem("Kilowatt", "kW", 1000.0),
            UnitItem("Megawatt", "MW", 1e6),
            UnitItem("Mechanical Horsepower", "hp", 745.69987),
            UnitItem("Metric Horsepower", "PS", 735.49875)
        ),
        UnitCategory.CURRENCY to listOf(
            UnitItem("US Dollar", "USD", 1.0),
            UnitItem("Euro", "EUR", 1.08),
            UnitItem("British Pound", "GBP", 1.29),
            UnitItem("Japanese Yen", "JPY", 0.0066),
            UnitItem("Canadian Dollar", "CAD", 0.73),
            UnitItem("Australian Dollar", "AUD", 0.66),
            UnitItem("Swiss Franc", "CHF", 1.13),
            UnitItem("Chinese Yuan", "CNY", 0.14),
            UnitItem("Indian Rupee", "INR", 0.012),
            UnitItem("Singapore Dollar", "SGD", 0.76),
            UnitItem("UAE Dirham", "AED", 0.27),
            UnitItem("Brazilian Real", "BRL", 0.18),
            UnitItem("South Korean Won", "KRW", 0.00073)
        )
    )

    fun convert(value: Double, from: UnitItem, to: UnitItem, category: UnitCategory): Double {
        if (category == UnitCategory.TEMPERATURE) {
            return convertTemperature(value, from.symbol, to.symbol)
        }
        val inBase = value * from.factorToBase
        return inBase / to.factorToBase
    }

    private fun convertTemperature(value: Double, fromSymbol: String, toSymbol: String): Double {
        if (fromSymbol == toSymbol) return value
        // Convert to Celsius first
        val celsius = when (fromSymbol) {
            "°C" -> value
            "°F" -> (value - 32.0) * (5.0 / 9.0)
            "K" -> value - 273.15
            else -> value
        }
        // Convert from Celsius to target
        return when (toSymbol) {
            "°C" -> celsius
            "°F" -> (celsius * (9.0 / 5.0)) + 32.0
            "K" -> celsius + 273.15
            else -> celsius
        }
    }
}
