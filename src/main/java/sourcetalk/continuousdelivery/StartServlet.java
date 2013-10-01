package sourcetalk.continuousdelivery;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String user = request.getParameter("user");
        System.out.println("User: " + user);

        if (user != null && !user.trim().isEmpty()) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("/hello");
            return;
        }

        response.sendRedirect("/");
    }
}