package com.uvg.example.lab06api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    // Función para obtener los datos del usuario
    suspend fun obtenerDatosUsuario(): Map<String, Any>? {
        return try {
            user?.let {
                val document = db.collection("usuarios").document(it.uid).get().await()
                if (document.exists()) document.data else null
            }
        } catch (e: Exception) {
            null // Manejo de errores
        }
    }

    // Función para enviar los datos del usuario
    suspend fun enviarDatosUsuario(usuarioData: Map<String, Any>) {
        try {
            user?.let {
                db.collection("usuarios").document(it.uid).set(usuarioData).await()
            }
        } catch (e: Exception) {
            throw e // Re-lanzar el error para manejarlo en el nivel superior
        }
    }
}

