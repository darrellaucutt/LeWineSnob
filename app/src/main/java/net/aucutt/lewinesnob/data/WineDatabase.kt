package net.aucutt.lewinesnob.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Wine::class, TastingNote::class],
    version = 2,
    exportSchema = false,
)
abstract class WineDatabase : RoomDatabase() {
    abstract fun wineDao(): WineDao
    abstract fun tastingNoteDao(): TastingNoteDao

    companion object {
        @Volatile
        private var instance: WineDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS tasting_notes (
                        id TEXT NOT NULL PRIMARY KEY,
                        wineId TEXT NOT NULL,
                        date INTEGER NOT NULL,
                        notes TEXT NOT NULL,
                        FOREIGN KEY(wineId) REFERENCES wines(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_tasting_notes_wineId ON tasting_notes(wineId)"
                )
            }
        }

        fun getInstance(context: Context): WineDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WineDatabase::class.java,
                    "lewinesnob.db",
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { instance = it }
            }
        }
    }
}
