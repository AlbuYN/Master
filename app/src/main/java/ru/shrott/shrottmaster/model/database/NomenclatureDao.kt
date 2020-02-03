package ru.shrott.shrottmaster.model.database

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import ru.shrott.shrottmaster.model.dbo.NomenclatureDBO

@Dao
interface NomenclatureDao {

    @Query("SELECT * FROM nomenclaturedbo")
    fun getAll() : Flowable<List<NomenclatureDBO>>

    @Query("SELECT * FROM nomenclaturedbo WHERE id = :id")
    fun getById(id: Long) : Single<NomenclatureDBO>

    @Query("SELECT * FROM nomenclaturedbo WHERE scrap_group_id = :idScrapGroup")
    fun getByScrapGroupId(idScrapGroup: Long) : Flowable<List<NomenclatureDBO>>

    @Insert
    fun insert(nomenclatures: List<NomenclatureDBO>)

    @Update
    fun update(nomenclatures: List<NomenclatureDBO>)

    @Delete
    fun delete(nomenclatureDBO: NomenclatureDBO)

}