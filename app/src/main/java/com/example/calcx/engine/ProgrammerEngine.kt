package com.example.calcx.engine

enum class NumberBase(val radix: Int, val prefix: String) {
    HEX(16, "0x"),
    DEC(10, ""),
    OCT(8, "0o"),
    BIN(2, "0b")
}

enum class WordSize(val bits: Int, val mask: ULong) {
    BYTE(8, 0xFFuL),
    WORD(16, 0xFFFFuL),
    DWORD(32, 0xFFFFFFFFuL),
    QWORD(64, 0xFFFFFFFFFFFFFFFFuL)
}

enum class BitwiseOp {
    AND, OR, XOR, NAND, NOR, XNOR, LSH, RSH
}

data class ProgrammerState(
    val currentValue: ULong = 0uL,
    val pendingValue: ULong? = null,
    val pendingOp: BitwiseOp? = null,
    val activeBase: NumberBase = NumberBase.DEC,
    val wordSize: WordSize = WordSize.QWORD,
    val inputBuffer: String = "0"
)

class ProgrammerEngine {

    companion object {
        fun formatInBase(value: ULong, base: NumberBase, wordSize: WordSize): String {
            val masked = value and wordSize.mask
            return when (base) {
                NumberBase.HEX -> masked.toString(16).uppercase()
                NumberBase.DEC -> {
                    // Show as signed or unsigned depending on MSB of word size
                    masked.toLong().toString(10)
                }
                NumberBase.OCT -> masked.toString(8)
                NumberBase.BIN -> {
                    val raw = masked.toString(2)
                    // Format with spaces every 4 bits
                    val padded = raw.padStart(wordSize.bits, '0')
                    padded.chunked(4).joinToString(" ")
                }
            }
        }

        fun parseInput(text: String, base: NumberBase, wordSize: WordSize): ULong {
            val clean = text.replace(" ", "").replace("_", "")
            if (clean.isEmpty()) return 0uL
            return try {
                val parsed = clean.toULong(base.radix)
                parsed and wordSize.mask
            } catch (e: Exception) {
                0uL
            }
        }

        fun executeOp(a: ULong, b: ULong, op: BitwiseOp, wordSize: WordSize): ULong {
            val mask = wordSize.mask
            val result = when (op) {
                BitwiseOp.AND -> a and b
                BitwiseOp.OR -> a or b
                BitwiseOp.XOR -> a xor b
                BitwiseOp.NAND -> (a and b).inv()
                BitwiseOp.NOR -> (a or b).inv()
                BitwiseOp.XNOR -> (a xor b).inv()
                BitwiseOp.LSH -> {
                    val shift = (b and 63uL).toInt()
                    a shl shift
                }
                BitwiseOp.RSH -> {
                    val shift = (b and 63uL).toInt()
                    a shr shift
                }
            }
            return result and mask
        }

        fun not(value: ULong, wordSize: WordSize): ULong {
            return value.inv() and wordSize.mask
        }

        fun rotateLeft(value: ULong, wordSize: WordSize): ULong {
            val bits = wordSize.bits
            val masked = value and wordSize.mask
            val msb = (masked shr (bits - 1)) and 1uL
            val shifted = (masked shl 1) and wordSize.mask
            return (shifted or msb) and wordSize.mask
        }

        fun rotateRight(value: ULong, wordSize: WordSize): ULong {
            val bits = wordSize.bits
            val masked = value and wordSize.mask
            val lsb = masked and 1uL
            val shifted = masked shr 1
            val rotated = shifted or (lsb shl (bits - 1))
            return rotated and wordSize.mask
        }

        fun toggleBit(value: ULong, bitIndex: Int, wordSize: WordSize): ULong {
            if (bitIndex >= wordSize.bits) return value
            val bitMask = 1uL shl bitIndex
            return (value xor bitMask) and wordSize.mask
        }

        fun isCharAllowed(char: Char, base: NumberBase): Boolean {
            val c = char.uppercaseChar()
            return when (base) {
                NumberBase.BIN -> c in '0'..'1'
                NumberBase.OCT -> c in '0'..'7'
                NumberBase.DEC -> c in '0'..'9'
                NumberBase.HEX -> (c in '0'..'9') || (c in 'A'..'F')
            }
        }
    }
}
