package net.aucutt.lewinesnob

import android.app.Application
import net.aucutt.lewinesnob.data.WineDatabase
import net.aucutt.lewinesnob.data.WineImageStore
import net.aucutt.lewinesnob.data.WineRepository

class LeWineSnobApplication : Application() {
    lateinit var wineRepository: WineRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = WineDatabase.getInstance(this)
        wineRepository = WineRepository(
            wineDao = database.wineDao(),
            imageStore = WineImageStore(this),
        )
    }
}
