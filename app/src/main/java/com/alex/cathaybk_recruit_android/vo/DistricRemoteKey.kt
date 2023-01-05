package com.alex.cathaybk_recruit_android.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class DistricRemoteKey(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val distric: String, // technically mutable but fine for a demo
    val nextPageKey: String?
)
