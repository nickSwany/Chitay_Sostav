package com.example.chitaysostav.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromAdditives(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toAdditives(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromNutriments(map: Map<String, Any>): String = gson.toJson(map)

    @TypeConverter
    fun toNutriments(value: String): Map<String, Any> {
        return gson.fromJson(value, object : TypeToken<Map<String, Any>>() {}.type) ?: emptyMap()
    }
}