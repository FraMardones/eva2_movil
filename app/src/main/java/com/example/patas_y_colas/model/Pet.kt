package com.example.patas_y_colas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pets")
data class Pet(
    // Room necesita esto para compilar, aunque los datos vengan del backend
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val species: String,
    val breed: String,
    val age: String,
    val weight: String,
    val imageUri: String? = null,

    // Esta lista se llena automáticamente desde el JSON del backend ("vaccineRecords")
    // Y Room la guarda usando tu archivo Converters.kt existente
    @SerializedName("vaccineRecords")
    val vaccines: List<VaccineRecord> = emptyList()
)