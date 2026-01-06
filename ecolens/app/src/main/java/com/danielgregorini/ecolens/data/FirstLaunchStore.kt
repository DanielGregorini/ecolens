package com.danielgregorini.ecolens.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

object FirstLaunchStore {

    private val FIRST_LAUNCH = booleanPreferencesKey("first_launch")

    suspend fun isFirstLaunch(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[FIRST_LAUNCH] ?: true
    }

    suspend fun setLaunched(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[FIRST_LAUNCH] = false
        }
    }
}
