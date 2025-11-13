package com.example.patas_y_colas.repository

import android.content.Context
import com.example.patas_y_colas.data.network.LoginRequest
import com.example.patas_y_colas.data.network.RegisterRequest // <--- Importante: no olvides este import
import com.example.patas_y_colas.data.network.RetrofitClient
import com.example.patas_y_colas.data.network.TokenManager
import com.example.patas_y_colas.model.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PetRepository(private val context: Context) {

    private val api = RetrofitClient.getClient(context)

    // --- Login ---
    suspend fun login(email: String, pass: String): Boolean {
        return try {
            val response = api.login(LoginRequest(email, pass))
            if (response.isSuccessful && response.body() != null) {
                TokenManager.saveToken(context, response.body()!!.token)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // --- Registro (Nueva función) ---
    suspend fun register(nombre: String, apellido: String, email: String, pass: String): Boolean {
        return try {
            val request = RegisterRequest(
                firstname = nombre,
                lastname = apellido,
                email = email,
                password = pass
            )
            val response = api.register(request)
            if (response.isSuccessful && response.body() != null) {
                // Guardamos el token automáticamente para que el usuario entre directo
                TokenManager.saveToken(context, response.body()!!.token)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // --- Obtener mascotas (Viene de la API) ---
    val allPets: Flow<List<Pet>> = flow {
        try {
            val petsFromApi = api.getAllPets()
            emit(petsFromApi)
        } catch (e: Exception) {
            e.printStackTrace()
            // En caso de error (sin internet), emitimos lista vacía.
            // Aquí podrías implementar lógica para cargar de Room si quisieras modo offline.
            emit(emptyList())
        }
    }

    // --- Crear mascota ---
    suspend fun insert(pet: Pet) {
        try {
            api.createPet(pet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Actualizar mascota ---
    suspend fun update(pet: Pet) {
        try {
            api.updatePet(pet.id, pet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Eliminar mascota ---
    suspend fun delete(pet: Pet) {
        try {
            api.deletePet(pet.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}