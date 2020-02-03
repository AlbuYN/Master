package ru.shrott.shrottmaster.model.database


import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import ru.shrott.shrottmaster.model.dbo.ScrapGroupDBO
import ru.shrott.shrottmaster.model.dbo.ScrapGroupNomenclatureDBO

@Dao
interface ScrapGroupDao {

    @Transaction @Query("SELECT * FROM scrapgroupdbo")
    fun getAll() : Flowable<List<ScrapGroupNomenclatureDBO>> // в клиентском коде - .observeOn(AndroidSchedulers.mainThread()) только это т.к. в фоновый переводит автоматически
    //.getAll()
    //       .observeOn(AndroidSchedulers.mainThread())
    //       .subscribe(new Consumer<List<Employee>>() {
    //           @Override
    //           public void accept(List<Employee> employees) throws Exception {
    //               // ...
    //           }
    //       });

    @Query("SELECT * FROM scrapgroupdbo WHERE 'id' = :id")
    fun getById(id: Int) : Single<ScrapGroupDBO>
    //.getById(1)
    //       .subscribeOn(Schedulers.io())
    //       .observeOn(AndroidSchedulers.mainThread())
    //       .subscribe(new DisposableSingleObserver<Employee>() {
    //           @Override
    //           public void onSuccess(Employee employee) {
    //               // ...
    //           }
    //
    //           @Override
    //           public void onError(Throwable e) {
    //               // ...
    //           }
    //       });

    @Insert
    fun insert(scrapGroupDBO: ScrapGroupDBO)

    @Update
    fun update(scrapGroupDBO: ScrapGroupDBO)

    @Delete
    fun delete(scrapGroupDBO: ScrapGroupDBO)
}