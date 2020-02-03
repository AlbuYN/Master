package ru.shrott.shrottmaster.model.database

import android.arch.persistence.room.*
import io.reactivex.Flowable
import ru.shrott.shrottmaster.model.dbo.BadgeStatusDBO


@Dao
interface BadgeStatusDao {

    @Query("SELECT * FROM badgestatusdbo")
    fun getAll() : Flowable<List<BadgeStatusDBO>>


    @Query("DELETE FROM badgestatusdbo WHERE idPass = :idPass")
    fun deleteByIdPass(idPass: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(badgeStatusDBO: BadgeStatusDBO)

    @Delete
    fun delete(badgeStatusDBO: BadgeStatusDBO)
}