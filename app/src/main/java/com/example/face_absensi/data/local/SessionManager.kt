package com.example.face_absensi.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    // Tambahan Data Profil
    private val NAME_KEY = stringPreferencesKey("user_name")
    private val EMAIL_KEY = stringPreferencesKey("user_email")
    private val ROLE_KEY = stringPreferencesKey("user_role")

    suspend fun saveSession(token: String, name: String, email: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[NAME_KEY] = name
            prefs[EMAIL_KEY] = email
            prefs[ROLE_KEY] = role
        }
    }

    val getToken = context.dataStore.data.map { it[TOKEN_KEY] ?: "" }
    val getName = context.dataStore.data.map { it[NAME_KEY] ?: "User" }
    val getEmail = context.dataStore.data.map { it[EMAIL_KEY] ?: "email@domain.com" }
    val getRole = context.dataStore.data.map { it[ROLE_KEY] ?: "Staff" }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}