package com.example.colega.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.colega.UserProto
import com.example.colega.models.user.UserResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val SORT_ORDER_KEY = "sort_order"

class UserPreferencesRepository(private val context: Context) {
    companion object{
        // create data store proto
        private val Context.userPreferencesStore: DataStore<UserProto> by dataStore(
            fileName = DATA_STORE_FILE_NAME,
            serializer = UserPreferencesSerializer
        )
    }

    // save data to data store proto
    suspend fun saveData(user: UserResponseItem) {
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setUsername(user.username).build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setPassword(user.password).build()
        }
        context.userPreferencesStore.updateData {
            it.toBuilder().setEmail(user.email).build()
        }
        context.userPreferencesStore.updateData {
            it.toBuilder().setUserId(user.id.toInt()).build()
        }
    }


    // delete datastore
    suspend fun clearData(){
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearUsername().build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearPassword().build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearEmail().build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearUserId().build()
        }
    }

    // read data store proto
    private val TAG: String = "UserPreferencesRepo"

    val readData: Flow<UserProto> = context.userPreferencesStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(UserProto.getDefaultInstance())
            } else {
                throw exception
            }
        }
}