package net.aucutt.lewinesnob.data

data class Wine(
    val id: String,
    val brand: String,
    val type: String,
    val varietal: String,
    val region: String,
    val rating: Int,
    val imageUri: String? = null,
)

object WineOptions {
    val types = listOf("Red", "White", "Rosé", "Sparkling", "Dessert", "Fortified")

    val varietals = listOf(
        "Cabernet Sauvignon",
        "Pinot Noir",
        "Merlot",
        "Syrah",
        "Zinfandel",
        "Malbec",
        "Chardonnay",
        "Sauvignon Blanc",
        "Pinot Grigio",
        "Riesling",
        "Champagne",
        "Blend",
        "Other",
    )
}
