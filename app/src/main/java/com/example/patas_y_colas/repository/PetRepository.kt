package com.example.patas_y_colas.repository

import android.content.Context
import com.example.patas_y_colas.data.network.LoginRequest
import com.example.patas_y_colas.data.network.RetrofitClient
import com.example.patas_y_colas.data.network.TokenManager
import com.example.patas_y_colas.model.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PetRepository(private val context: Context) {

    private val api = RetrofitClient.getClient(context)

    // Login
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

    // Obtener mascotas (Ahora viene de la API)
    val allPets: Flow<List<Pet>> = flow {
        try {
            val petsFromApi = api.getAllPets()
            emit(petsFromApi)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList()) // En caso de error, lista vacía o manejar caché local
        }
    }

    suspend fun insert(pet: Pet) {
        try {
            api.createPet(pet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun update(pet: Pet) {
        try {
            api.updatePet(pet.id, pet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun delete(pet: Pet) {
        try {
            api.deletePet(pet.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}