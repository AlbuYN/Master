package ru.shrott.shrottmaster.model.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import ru.shrott.shrottmaster.model.dbo.MediaPassDBO

@Dao
interface MediaPassDao {

    @Query("SELECT * FROM mediapassdbo WHERE idPass = :idPass")
    fun getByIdPass(idPass: String) : Flowable<List<MediaPassDBO>>

    @Query("DELETE FROM mediapassdbo WHERE idPass = :idPass")
    fun deleteByIdPass(idPass: String)

    @Insert
    fun insert(mediaPassDBO: MediaPassDBO)

    @Delete
    fun delete(mediaPassDBO: MediaPassDBO)
}