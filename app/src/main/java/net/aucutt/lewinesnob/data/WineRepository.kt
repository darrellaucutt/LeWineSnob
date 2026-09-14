package net.aucutt.lewinesnob.data

class WineRepository(
    private val wineDao: WineDao,
    private val imageStore: WineImageStore,
) {
    fun observeWines() = wineDao.observeWines()

    suspend fun findCollision(brand: String, type: String, varietal: String, year: Int?): Wine? {
        return wineDao.findCollision(brand.trim(), type.trim(), varietal.trim(), year)
    }

    suspend fun addWine(wine: Wine) {
        val storedUri = imageStore.persist(wine.id, wine.imageUri)
        wineDao.upsert(wine.copy(imageUri = storedUri))
    }

    suspend fun updateRating(id: String, rating: Int) {
        wineDao.updateRating(id, rating)
    }

    suspend fun deleteWine(id: String) {
        wineDao.deleteById(id)
        imageStore.delete(id)
    }
}
