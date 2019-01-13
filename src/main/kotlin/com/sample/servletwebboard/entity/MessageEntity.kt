package com.sample.servletwebboard.entity

import java.sql.Date

data class MessageEntity(
        val id: Int,
        val name: String,
        val text: String,
        val dateTime: Date
)