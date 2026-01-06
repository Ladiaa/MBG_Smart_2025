package com.example.mbgsmart.data.model

import com.google.firebase.Timestamp

data class Menu(

    val id: String = "",

    val schoolId: String = "",
    val schoolName: String = "",
    val catering: String = "",
    val date: String = "",

    val imageUri: String = "",

    val karbo: Any? = null,
    val protein: Any? = null,
    val sayur: Any? = null,
    val buah: Any? = null,
    val minuman: Any? = null,


    val statusKelengkapan: String = "Lengkap",

    val timestamp: Timestamp = Timestamp.now()
)

fun Any?.asText(): String {
    return when (this) {
        is String -> this
        is Boolean -> if (this) "Ada" else "Tidak Ada"
        else -> ""
    }
}

