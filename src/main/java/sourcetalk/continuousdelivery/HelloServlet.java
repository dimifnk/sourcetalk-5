package sourcetalk.continuousdelivery;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Scanner;

public class HelloServlet extends HttpServlet {
    private final FriendRepository friendRepository = new DatabaseFriendRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("/");
            return;
        }

        String version = new Scanner(getClass().getClassLoader().getResourceAsStream("version.txt"), "UTF-8").next();
        String titleColor = Environment.getConfiguration().getTitleColor();
        String hostName = InetAddress.getLocalHost().getHostName();

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>" +
                "<head><meta charset=\"utf-8\"></head>" +
                "<body>" +
                "<h1 style=\"color:" + titleColor + "\">" +
                "Hello " + htmlEscape(user) + "!" +
                "</h1>" +
                "<p>I am a test application and I run on server '" + hostName + "' in the '" +Environment.getName() +"' environment.</p>" +
                "<p>My version is: " + version + "</p>" +
                "<p>These are my friends:</p>" +
                "<table>" +
                "<thead>" +
                "<tr><th>First Name</th><th>Last Name</th></tr>" +
                "</thead>" +
                "<tbody>");

        for (Friend friend : friendRepository.getFriends()) {
            out.println("<tr><td>" + htmlEscape(friend.getFirstName()) + "</td>" +
                    "<td>" + htmlEscape(friend.getLastName()) + "</td></tr>");
        }

        out.println("</tbody>" +
                "</table>" +
                "<form action=\"/hello\" method=\"post\">" +
                "<p>New friend:</p>" +
                "<input name=\"firstName\" type=\"text\" placeholder=\"First Name\" size=\"20\"> " +
                "<input name=\"lastName\" type=\"text\" placeholder=\"Last Name\" size=\"20\"> " +
                "<input name=\"submit\" type=\"submit\" value=\"Add\">" +
                "</form>" +
                "</html>");

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        System.out.println("First Name: " + firstName + " / Last Name: " + lastName);

        if ((firstName != null && !firstName.trim().isEmpty()) && (lastName != null && !lastName.trim().isEmpty())) {
            friendRepository.addFriend(new Friend(firstName.trim(), lastName.trim()));
        }

        response.sendRedirect("/hello");
    }

    private String htmlEscape(String text) {
        return text.replace("<", "&lt;").replace(">", "&gt;");
    }
}