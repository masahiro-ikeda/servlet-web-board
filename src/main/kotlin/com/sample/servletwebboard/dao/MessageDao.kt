package com.sample.servletwebboard.dao

import com.sample.servletwebboard.entity.MessageEntity
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class MessageDao(private val con: Connection) {

    fun getNextId(): Int {

        val sqlBuilder: StringBuilder = StringBuilder()
        sqlBuilder.append("SELECT              ")
        sqlBuilder.append("  MAX(id) as max_id ")
        sqlBuilder.append("FROM                ")
        sqlBuilder.append("  message           ")

        val ps: PreparedStatement = con.prepareStatement(sqlBuilder.toString())
        val rs: ResultSet = ps.executeQuery()

        val nextId: Int =
                if (rs.next()) rs.getInt("max_id") + 1
                else 1

        ps.close()
        rs.close()

        return nextId
    }

    fun selectMessage(): MutableList<MessageEntity> {

        val list: MutableList<MessageEntity> = mutableListOf()

        val sqlBuilder: StringBuilder = StringBuilder()
        sqlBuilder.append("SELECT              ")
        sqlBuilder.append("  id, ")
        sqlBuilder.append("  name ")
        sqlBuilder.append("  text, ")
        sqlBuilder.append("  created_at ")
        sqlBuilder.append("FROM                ")
        sqlBuilder.append("  message           ")
        sqlBuilder.append("ORDER BY           ")
        sqlBuilder.append("  id           ")

        val ps: PreparedStatement = con.prepareStatement(sqlBuilder.toString())
        val rs: ResultSet = ps.executeQuery()

        while (rs.next()){
            val entity: MessageEntity = MessageEntity(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("text"),
                    rs.getDate("created_at")
            )
            list.add(entity)
        }

        ps.close()
        rs.close()

        return list
    }

    fun insertMessage(entity: MessageEntity): Boolean {

        val sqlBuilder: StringBuilder = StringBuilder()
        sqlBuilder.append("INSERT INTO message ( ")
        sqlBuilder.append("  id, ")
        sqlBuilder.append("  name, ")
        sqlBuilder.append("  text, ")
        sqlBuilder.append("  created_at ")
        sqlBuilder.append(") VALUES (               ")
        sqlBuilder.append("  ?,           ")
        sqlBuilder.append("  ?,           ")
        sqlBuilder.append("  ?,           ")
        sqlBuilder.append("  ?           ")
        sqlBuilder.append(")           ")

        val ps: PreparedStatement = con.prepareStatement(sqlBuilder.toString())
        ps.setInt(0, entity.id)
        ps.setString(1, entity.name)
        ps.setString(2, entity.text)
        ps.setDate(0, entity.dateTime)

        val result: Boolean = ps.execute()

        ps.close()

        return result
    }
}