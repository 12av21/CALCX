package com.example.calcx.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expression: String,
    val result: String,
    val mode: String, // STANDARD, SCIENTIFIC, PROGRAMMER, FINANCIAL, UNIT
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)
