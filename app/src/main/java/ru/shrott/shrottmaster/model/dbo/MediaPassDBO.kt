package ru.shrott.shrottmaster.model.dbo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index(value = ["idPass", "uri"], unique = true)],
    primaryKeys = ["idPass", "uri"])
class MediaPassDBO (
    var uri: String,
    var idPass: String,
    var imgBase64: String
)