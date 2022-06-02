package com.example.art_run_android.running

data class RecommendedRoute(
    val distance: Int,
    val id: Int,
    val title: String,
    val wktRoute: String
)

data class RouteId(
    val routeId: Int
)

data class CompleteRoute(
    val color: String,
    val createdAt: String,
    val distance: Int,
    val isPublic: Boolean,
    val kcal: Int,
    val speed: Int,
    val time: Int,
    val title: String,
    val wktRunRoute: String
)