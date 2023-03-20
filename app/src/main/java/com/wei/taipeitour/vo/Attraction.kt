package com.wei.taipeitour.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(CategoryConverter::class, FileConverter::class, ImageConverter::class, LinkConverter::class)
@Entity(tableName = "attractions")
data class Attraction(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_zh")
    val nameZh: String?,
    @SerializedName("open_status")
    val openStatus: Int,
    @SerializedName("introduction")
    val introduction: String,
    @SerializedName("open_time")
    val openTime: String,
    @SerializedName("zipcode")
    val zipcode: String,
    @SerializedName("distric")
    val distric: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("fax")
    val fax: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("months")
    val months: String,
    @SerializedName("nlat")
    val nlat: Double,
    @SerializedName("elong")
    val elong: Double,
    @SerializedName("official_site")
    val officialSite: String,
    @SerializedName("facebook")
    val facebook: String,
    @SerializedName("ticket")
    val ticket: String,
    @SerializedName("remind")
    val remind: String,
    @SerializedName("staytime")
    val staytime: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("category")
    val category: List<Category>,
    @SerializedName("target")
    val target: List<Category>,
    @SerializedName("service")
    val service: List<Category>,
    @SerializedName("friendly")
    val friendly: List<Category>,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("files")
    val files: List<File>,
    @SerializedName("links")
    val links: List<Link>,
    @ColumnInfo(name = "page")
    val page: Int,
)

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)

data class File(
    @SerializedName("src")
    val src: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("ext")
    val ext: String,
)

data class Image(
    @SerializedName("src")
    val src: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("ext")
    val ext: String,
)

data class Link(
    @SerializedName("src")
    val src: String,
    @SerializedName("subject")
    val subject: String,
)
