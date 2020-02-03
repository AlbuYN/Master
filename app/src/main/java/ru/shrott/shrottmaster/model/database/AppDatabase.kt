package ru.shrott.shrottmaster.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ru.shrott.shrottmaster.model.dbo.*

@Database(entities = [ScrapGroupDBO::class, NomenclatureDBO::class, WeightPercentageDBO::class,
    MediaPassDBO::class, TotalPercentPollutionDBO::class, BadgeStatusDBO::class],
        version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scrapGroupDao(): ScrapGroupDao
    abstract fun nomenclatureDao(): NomenclatureDao
    abstract fun weightPercentageDao(): WeightPercentageDao
    abstract fun mediaPassDao(): MediaPassDao
    abstract fun badgeStatusDao(): BadgeStatusDao
    abstract fun totalPercentPollutionDao(): TotalPercentPollutionDao
    abstract fun scrapGroupNomenclature(): ScrapGroupNomenclature
}