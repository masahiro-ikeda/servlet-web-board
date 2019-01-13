package com.sample.servletwebboard

import com.sample.servletwebboard.entity.MessageEntity
import com.sample.servletwebboard.service.MessageService
import java.io.PrintWriter
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class WebBoardServlet: HttpServlet() {

    protected override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {

        request.characterEncoding = "UTF-8"

        val service: MessageService = MessageService()
        val list: List<MessageEntity> = service.getMessage()

        response.contentType = "text/html;charset=UTF-8"
        val out: PrintWriter = response.writer
        out.println(list)
    }

    protected override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {

    }


}