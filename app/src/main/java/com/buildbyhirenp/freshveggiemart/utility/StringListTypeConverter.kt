package com.buildbyhirenp.freshveggiemart.utility

import androidx.room.TypeConverter

class StringListTypeConverter {

    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        return value?.split(",")?.map { it }?.toCollection(ArrayList())
    }

    @TypeConverter
    fun toString(list: ArrayList<String?>?): String? {
        return list?.joinToString(",")
    }

}