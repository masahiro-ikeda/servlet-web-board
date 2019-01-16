package com.sample.servletwebboard.service

import com.sample.servletwebboard.dao.MessageDao
import com.sample.servletwebboard.entity.MessageEntity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Timestamp

class MessageService {

    fun getMessage(): List<MessageEntity> {

        var con: Connection? = null

        try {
            con = getConnection()
            val dao: MessageDao = MessageDao(con)
            return dao.selectMessage()

        } catch (e: SQLException) {
            println("log [error:getMessage]")
            e.printStackTrace()
            throw RuntimeException()
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
                    Timestamp(System.currentTimeMillis())
            )

            if (dao.insertMessage(message)) {
                con.commit()
            } else {
                con.rollback()
            }
        } catch (e: SQLException) {
            println("log [error:postMessage]")
            con!!.rollback()
            e.printStackTrace()
            throw RuntimeException()
        } finally {
            close(con!!);
        }
    }

    private fun getConnection(): Connection {

        val url:  String = "jdbc:mysql://db/develop?useSSL=false"
        val user: String = "dev_user"
        val pass: String = "dev_pass"

        Class.forName("com.mysql.jdbc.Driver")
        return DriverManager.getConnection(url, user, pass)
    }

    private fun close(connection: Connection) {
        if (!connection.isClosed) {
            connection.close()
        }
    }
}