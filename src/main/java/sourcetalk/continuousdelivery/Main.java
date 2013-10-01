package sourcetalk.continuousdelivery;

import com.googlecode.flyway.core.Flyway;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Main {
    private static Server httpServer;
    private static Server shutdownServer;

    public static void main(String[] args) {
        start();
        try {
            httpServer.join();
        } catch (Exception e) {
            System.out.println("Unable to start server !");
            e.printStackTrace();
        }
    }

    public static void start() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(Environment.getConfiguration().getDbUrl(), "SA", "");
        if (Environment.getConfiguration().isDbLoadTestData()) {
            flyway.setLocations("db/migration", "db/testdata");
        } else {
            flyway.setLocations("db/migration");
        }
        flyway.migrate();

        createHttpServer();
        createShutdownServer();

        try {
            shutdownServer.start();
            httpServer.start();
        } catch (Exception e) {
            System.out.println("Unable to start server !");
            e.printStackTrace();
        }
    }

    public static void stop() {
        new Thread() {
            @Override
            public void run() {
                try {
                    shutdownServer.stop();
                    httpServer.stop();
                } catch (Exception e) {
                    System.out.println("Unable to shutdown server !");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static void createHttpServer() {
        httpServer = new Server(Environment.getConfiguration().getPortHttp());

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setWelcomeFiles(new String[]{"index.html"});
        webAppContext.setBaseResource(Resource.newClassPathResource("web"));
        webAppContext.addServlet(StartServlet.class.getName(), "/start");
        webAppContext.addServlet(HelloServlet.class.getName(), "/hello");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{webAppContext, new DefaultHandler()});
        httpServer.setHandler(handlers);
    }

    private static void createShutdownServer() {
        shutdownServer = new Server(Environment.getConfiguration().getPortShutdown());
        shutdownServer.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                if ("GET".equals(request.getMethod()) && "/shutdown".equals(target)) {
                    try {
                        Main.stop();
                    } catch (Exception e) {
                        System.out.println("Unable to shutdown server !");
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}