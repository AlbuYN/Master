package ru.shrott.shrottmaster.model.dbo

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(foreignKeys = [ForeignKey(
    entity = ScrapGroupDBO::class,
    parentColumns = ["id"],
    childColumns = ["scrap_group_id"],
    onDelete = CASCADE
)], indices = [Index(value = ["scrap_group_id"])])
data class NomenclatureDBO (
    @PrimaryKey
    var id: String,
    var label: String,
    @ColumnInfo(name = "scrap_group_id")
    var scrapGroupId: String
)