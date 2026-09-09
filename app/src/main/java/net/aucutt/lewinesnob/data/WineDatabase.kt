package net.aucutt.lewinesnob.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Wine::class],
    version = 1,
    exportSchema = false,
)
abstract class WineDatabase : RoomDatabase() {
    abstract fun wineDao(): WineDao

    companion object {
        @Volatile
        private var instance: WineDatabase? = null

        fun getInstance(context: Context): WineDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WineDatabase::class.java,
                    "lewinesnob.db",
                ).build().also { instance = it }
            }
        }
    }
}
