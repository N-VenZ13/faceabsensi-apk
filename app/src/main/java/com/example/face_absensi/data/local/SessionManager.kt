package com.example.face_absensi.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Ekstensi harus di luar class
private val Context.dataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context // Gunakan @ApplicationContext
) {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    val getToken = context.dataStore.data.map { it[TOKEN_KEY] ?: "" }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}