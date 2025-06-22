package com.example.pharmaadminpro.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class PreferenceDataStore(private val context: Context) {


    val Context.dataStore  by preferencesDataStore("Key Store")

    companion object{
        private val AdminLoginKey = stringPreferencesKey("Admin_key")
    }

    suspend fun saveAdminKeyInPreference( key: String){
        context.dataStore.edit { prefs ->
            prefs[AdminLoginKey]= key
        }
    }


    suspend fun getAdminKeyValue(): String?{
        return context.dataStore.data.map {
            it[AdminLoginKey]
        }.first()
    }



}
