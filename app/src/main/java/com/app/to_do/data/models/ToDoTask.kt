package com.app.to_do.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.to_do.utils.Constant.DATABASE_TABLE_NAME


@Entity(tableName = DATABASE_TABLE_NAME)
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
)
