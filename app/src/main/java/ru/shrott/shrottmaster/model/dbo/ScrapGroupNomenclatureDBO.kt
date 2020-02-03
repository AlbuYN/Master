package ru.shrott.shrottmaster.model.dbo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class ScrapGroupNomenclatureDBO {
    @Embedded
    lateinit var scrapGroupDBO: ScrapGroupDBO
    @Relation(parentColumn = "id", entity = NomenclatureDBO::class, entityColumn = "scrap_group_id")
    lateinit var nomenclatureDBOList: List<NomenclatureDBO>
}