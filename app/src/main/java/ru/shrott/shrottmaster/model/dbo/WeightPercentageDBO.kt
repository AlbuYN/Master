package ru.shrott.shrottmaster.model.dbo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index(value = ["idNomenclature", "idPass"], unique = true)],
    primaryKeys = ["idNomenclature", "idPass", "idScrapGroup"])
class WeightPercentageDBO (
                           var idNomenclature: String,
                           var idPass: String,
                           var idScrapGroup: String,
                           var name: String,
                           var percentage: Int)