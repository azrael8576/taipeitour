package com.alex.cathaybk_recruit_android.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class AttractionRemoteKey(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val id: Int,
    val prevPageKey: Int?,
    val nextPageKey: Int?
)
