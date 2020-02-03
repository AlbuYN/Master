package ru.shrott.shrottmaster.model.dbo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index(value = ["id"])])
data class ScrapGroupDBO (
    @PrimaryKey
    var id: String,
    var label: String
)