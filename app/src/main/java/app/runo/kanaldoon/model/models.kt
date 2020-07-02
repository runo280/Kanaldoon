package app.runo.kanaldoon.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.RESTRICT
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class, parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId"),
        onDelete = RESTRICT
    )],
    indices = [Index(
        value = ["username"],
        unique = true
    )]
)
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull
    val username: String,
    var title: String = "",
    val desc: String = "",
    var avatarUrl: String = "",
    val viewCount: Int = 0,
    val lastUpdate: Long = -1,
    val lastView: Long = -1,
    val categoryId: Int,
    val target: String,
    val pinned: Boolean = false,
    val pinnedOrder: Int = -1
)

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull
    val title: String?,
    val order: Int = -1
)