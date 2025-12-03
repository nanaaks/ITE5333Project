package com.project.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.app.model.Ride
import com.project.app.model.User
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [User::class, Ride::class],
    version = 2
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao() : UserDao

    abstract fun rideDao() : RideDao

    companion object {
        @Volatile
        var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Project_DB"
                    )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(AppDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

private class AppDatabaseCallback(
    private val scope : CoroutineScope
) : RoomDatabase.Callback() {
    //TODO
}