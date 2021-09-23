package com.multithread.screencapture.history.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_history")
data class ImageDataModel(

    @PrimaryKey(autoGenerate = true)
    var id: Long?=null,

    val imagePath: String,
    val title: String,
    val date: Long
) : Serializable
