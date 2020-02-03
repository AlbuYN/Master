package ru.shrott.shrottmaster.model.database

import android.arch.persistence.room.*
import ru.shrott.shrottmaster.model.dbo.NomenclatureDBO
import ru.shrott.shrottmaster.model.dbo.ScrapGroupDBO
import ru.shrott.shrottmaster.model.dto.NomenclatureDTO
import ru.shrott.shrottmaster.model.dto.ScrapGroupDTO

@Dao
abstract class ScrapGroupNomenclature {

    @Insert
    abstract fun insert(scrapGroupDBO: ScrapGroupDBO)

    @Insert
    abstract fun insert(nomenclature: NomenclatureDBO)

    @Update
    abstract fun update(scrapGroupDBO: ScrapGroupDBO)

    @Update
    abstract fun update(nomenclatureDBO: NomenclatureDBO)

    @Query("DELETE FROM nomenclaturedbo")
    abstract fun deleteAllNomenclature()

    @Query("DELETE FROM scrapgroupdbo")
    abstract fun deleteAllScrapGroup()


    @Transaction
    open fun insertScrapGroupNomenclature(scrapGroups: List<ScrapGroupDTO>) {

        deleteAllNomenclature()
        deleteAllScrapGroup()

        for (scrapGroupDTO: ScrapGroupDTO in scrapGroups) {
            insert(ScrapGroupDBO(scrapGroupDTO.id, scrapGroupDTO.label))
            for (nomenclatureDTO: NomenclatureDTO in scrapGroupDTO.nomenclature) {
                insert(NomenclatureDBO(nomenclatureDTO.id, nomenclatureDTO.label, scrapGroupDTO.id))
            }
        }
    }

    /*
    @Transaction
    open fun updateScrapGroupNomenclature(scrapGroups: List<ScrapGroupDTO>) {
        for (scrapGroupDTO: ScrapGroupDTO in scrapGroups) {
            update(ScrapGroupDBO(scrapGroupDTO.id, scrapGroupDTO.label))
            for (nomenclatureDTO: NomenclatureDTO in scrapGroupDTO.nomenclature) {
                update(NomenclatureDBO(nomenclatureDTO.id, nomenclatureDTO.label, scrapGroupDTO.id))
            }
        }
    }
    */
}