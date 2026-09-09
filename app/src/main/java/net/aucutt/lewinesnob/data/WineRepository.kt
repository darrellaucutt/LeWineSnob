package net.aucutt.lewinesnob.data

class WineRepository(
    private val wineDao: WineDao,
    private val imageStore: WineImageStore,
) {
    fun observeWines() = wineDao.observeWines()

    suspend fun addWine(wine: Wine) {
        val storedUri = imageStore.persist(wine.id, wine.imageUri)
        wineDao.upsert(wine.copy(imageUri = storedUri))
    }

    suspend fun deleteWine(id: String) {
        wineDao.deleteById(id)
    }
}
