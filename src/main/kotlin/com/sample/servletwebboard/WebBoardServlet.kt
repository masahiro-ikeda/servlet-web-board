package com.sample.servletwebboard

import com.sample.servletwebboard.entity.MessageEntity
import com.sample.servletwebboard.service.MessageService
import java.io.PrintWriter
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "WebBoard", value = ["/"])
class WebBoardServlet: HttpServlet() {

    protected override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {

        request.characterEncoding = "UTF-8"

        val service: MessageService = MessageService()
        val list: List<MessageEntity> = service.getMessage()

        response.contentType = "text/html;charset=UTF-8"
        val out: PrintWriter = response.writer

        out.println("<html>")
        out.println("<head>")
        out.println("  <style type=\"text/css\">")
        out.println("    <!-- body {font-family: meiryo, arial; max-width: 80%; margin: 0 auto; } -->")
        out.println("    <!-- h1 {text-align: center; background: lime; } -->")
        out.println("  </style>")
        out.println("  <meta charset=\"UTF-8\">")
        out.println("  <title>Servletで作った掲示板</title>")
        out.println("</heda>")
        out.println("<body>")
        out.println("  <h1>Servletで作った掲示板</h1>")
        out.println("  <form action=\"/\" method=\"post\">")
        out.println("    <input type=\"text\" name=\"name\" placeholder=\"name\" required></br>")
        out.println("    <textarea name=\"text\" rows=\"4\" cols=\"50\" maxlength=\"250\" placeholder=\"message\"></textarea></br>")
        out.println("    </br>")
        out.println("    <input type=\"submit\" value=\"投稿\">")
        out.println("  </form>")

        for (message: MessageEntity in list){
            out.println("<hr>")
            out.println("<p>No: ${message.id} / Name: ${escape(message.name)} / Time: ${message.created_at}</p>")
            out.println("<p>${escape(message.text)}</p>")
        }

        out.println("</body>")
        out.println("</html>")
    }

    protected override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {

        request.characterEncoding = "UTF-8"

        val name: String = request.getParameter("name")
        val text: String = request.getParameter("text")

        val service: MessageService = MessageService()
        service.postMessage(name, text)

        response.sendRedirect("/")
    }

    // 改行コードの変換とXSS対策
	private fun escape(input: String): String {

        var target: String = input

		// 「&」を変換
        target = target.replace("&", "&amp;");
		// 「<」を変換
        target = target.replace("<", "&lt;");
		// 「>」を変換
        target = target.replace(">", "&gt;");
		// 「"」を変換
        target = target.replace("\"", "&quot;");
		// 「'」を変換
        target = target.replace("'", "&#039;");

        // 改行コードを変換
        target = target.replace("\n", "<br>");

		return target;
	}
}