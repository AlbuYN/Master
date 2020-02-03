package ru.shrott.shrottmaster.model.database

import android.arch.persistence.room.*
import io.reactivex.Flowable
import ru.shrott.shrottmaster.model.dbo.TotalPercentPollutionDBO

@Dao
interface TotalPercentPollutionDao {

    @Query("SELECT * FROM totalpercentpollutiondbo WHERE idPass = :idPass")
    fun getByIdPass(idPass: String) : Flowable<TotalPercentPollutionDBO>

    @Query("DELETE FROM totalpercentpollutiondbo WHERE idPass = :idPass")
    fun deleteByIdPass(idPass: String)


    @Insert
    fun insert(totalPercentPollutionDBO: TotalPercentPollutionDBO)

    @Update
    fun update(totalPercentPollutionDBO: TotalPercentPollutionDBO)

    @Delete
    fun delete(totalPercentPollutionDBO: TotalPercentPollutionDBO)
}