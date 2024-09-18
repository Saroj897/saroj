package com.example.nit3213finalproject

import java.io.Serializable

data class Entity(
    val assetType: String,
    val ticker: String,
    val currentPrice: String,
    val dividendYield: String,
    val description: String
) : Serializable