package ru.shrott.shrottmaster.model.database

import android.arch.persistence.room.*
import io.reactivex.Flowable
import ru.shrott.shrottmaster.model.dbo.WeightPercentageDBO

@Dao
interface WeightPercentageDao {

    @Query("SELECT * FROM weightpercentagedbo WHERE idPass = :idPass")
    fun getByIdPass(idPass: String) : Flowable<List<WeightPercentageDBO>>

    @Query("DELETE FROM weightpercentagedbo WHERE idPass = :idPass")
    fun deleteByIdPass(idPass: String)


    @Insert
    fun insert(weightPercentageDBO: WeightPercentageDBO)

    @Update
    fun update(weightPercentageDBO: WeightPercentageDBO)

    @Delete
    fun delete(weightPercentageDBO: WeightPercentageDBO)



}