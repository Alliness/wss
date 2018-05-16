package alliness.websocketserver.socket;

import alliness.core.ConfigLoader;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebSocketServer extends Thread {

    private static final Logger log = Logger.getLogger(WebSocketServer.class);
    private static WebSocketServer instance;
    private final  Server          server;

    public static WebSocketServer getInstance() {
        if (instance == null) {
            try {
                instance = new WebSocketServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private WebSocketServer() {
        server = new Server(Integer.parseInt(ConfigLoader.getInstance().getProperty("websocket.port")));
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(ConnectionHandler.class);
            }
        };
        server.setHandler(wsHandler);
    }

    public void run() {
        try {
            server.start();
            log.info(String.format("Socket Server port: %s", ConfigLoader.getInstance().getProperty("websocket.port")));
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
