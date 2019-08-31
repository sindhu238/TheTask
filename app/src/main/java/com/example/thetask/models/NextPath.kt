package com.example.thetask.models

import kotlinx.serialization.Serializable

@Serializable
data class NextPath(
    val next_path: String
)

@Serializable
data class TaskInfo(
    val path: String,
    val response_code: String
)