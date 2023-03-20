package com.wei.taipeitour.vo

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object CategoryConverter {

    @JvmStatic
    @TypeConverter
    fun objectToString(list: List<Category>): String {
        return Gson().toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun stringToObject(json: String?): List<Category> {
        val listType: Type = object : TypeToken<List<Category>>() {}.type
        return Gson().fromJson(json, listType)
    }
}

object FileConverter {

    @JvmStatic
    @TypeConverter
    fun objectToString(list: List<File>): String {
        return Gson().toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun stringToObject(json: String?): List<File> {
        val listType: Type = object : TypeToken<List<File>>() {}.type
        return Gson().fromJson(json, listType)
    }
}

object ImageConverter {

    @JvmStatic
    @TypeConverter
    fun objectToString(list: List<Image>): String {
        return Gson().toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun stringToObject(json: String?): List<Image> {
        val listType: Type = object : TypeToken<List<Image>>() {}.type
        return Gson().fromJson(json, listType)
    }
}

object LinkConverter {

    @JvmStatic
    @TypeConverter
    fun objectToString(list: List<Link>): String {
        return Gson().toJson(list)
    }

    @JvmStatic
    @TypeConverter
    fun stringToObject(json: String?): List<Link> {
        val listType: Type = object : TypeToken<List<Link>>() {}.type
        return Gson().fromJson(json, listType)
    }
}
