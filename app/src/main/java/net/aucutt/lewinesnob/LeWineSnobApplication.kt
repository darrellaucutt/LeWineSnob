package net.aucutt.lewinesnob

import android.app.Application
import net.aucutt.lewinesnob.data.TastingNoteRepository
import net.aucutt.lewinesnob.data.WineDatabase
import net.aucutt.lewinesnob.data.WineImageStore
import net.aucutt.lewinesnob.data.WineRepository

class LeWineSnobApplication : Application() {
    lateinit var wineRepository: WineRepository
        private set
    lateinit var tastingNoteRepository: TastingNoteRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = WineDatabase.getInstance(this)
        wineRepository = WineRepository(
            wineDao = database.wineDao(),
            imageStore = WineImageStore(this),
        )
        tastingNoteRepository = TastingNoteRepository(
            tastingNoteDao = database.tastingNoteDao(),
        )
    }
}
