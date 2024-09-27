package com.buildbyhirenp.freshveggiemart.models

data class CheckStatus(
    val code: String,
    val message: String,
    val success: Boolean
)