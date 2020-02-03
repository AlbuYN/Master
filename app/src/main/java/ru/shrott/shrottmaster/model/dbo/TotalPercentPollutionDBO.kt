package ru.shrott.shrottmaster.model.dbo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index(value = ["idPass"], unique = true)])
class TotalPercentPollutionDBO (
    @PrimaryKey
    var idPass: String,
    var totalPercentPollution: Int
)