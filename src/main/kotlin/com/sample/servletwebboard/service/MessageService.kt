package com.sample.servletwebboard.service

import com.sample.servletwebboard.dao.MessageDao
import com.sample.servletwebboard.entity.MessageEntity
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.SQLException

class MessageService {

    fun getMessage(): List<MessageEntity> {
        var con: Connection? = null
        var list: MutableList<MessageEntity> = mutableListOf()

        try {
            con = getConnection()
            val dao: MessageDao = MessageDao(con)

            list = dao.selectMessage()

            return list
        } catch (e: SQLException) {
            e.printStackTrace()
            throw Exception()
        } finally {
            close(con!!)
        }
    }

    fun postMessage(name: String, text: String) {

        var con: Connection? = null
        try {
            con = getConnection()
            con.autoCommit = false

            val dao: MessageDao = MessageDao(con)

            val message: MessageEntity = MessageEntity(
                    dao.getNextId(),
                    name,
                    text,
                    Date(java.util.Date().time)
            )

            if (dao.insertMessage(message)) {
                con.commit()
            } else {
                con.rollback()
            }
        } catch (e: SQLException) {
            con!!.rollback()
            e.printStackTrace()
        } finally {
            close(con!!);
        }
    }

    private fun getConnection(): Connection {

        val url: String = "jdbc:mysql://localhost/develop"
        val user: String = "root"
        val pass: String = "pass"

        return DriverManager.getConnection(url, user, pass)
    }

    private fun close(connection: Connection) {
        if (!connection.isClosed) {
            connection.close()
        }
    }
}