package net.aucutt.lewinesnob.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasting_notes",
    foreignKeys = [
        ForeignKey(
            entity = Wine::class,
            parentColumns = ["id"],
            childColumns = ["wineId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("wineId")],
)
data class TastingNote(
    @PrimaryKey val id: String,
    val wineId: String,
    val date: Long,
    val notes: String,
)
