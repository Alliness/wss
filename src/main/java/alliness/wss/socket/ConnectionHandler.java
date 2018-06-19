package alliness.wss.socket;

import alliness.wss.game.managers.GameManager;
import alliness.wss.game.managers.LobbyManager;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class ConnectionHandler {

    private static final Logger log = Logger.getLogger(ConnectionHandler.class);
    private WebSocketConnection.Connection connection;

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        log.info(String.format("[WS]Client(%s)leave: statusCode=%s, reason=%s", connection.getSession().getRemoteAddress(), statusCode, reason));
        connection.disconnect();
        GameManager.getInstance().getAvatar(null, connection.getUUID()).exitFromRoom();
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        log.info(String.format("[WS]Client(%s) connection error: %s", connection.getSession().getRemoteAddress(), t));
        WebSocketConnection.getInstance().remove(connection);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        log.info(String.format("[WS] new Client: %s", session.getRemoteAddress()));
        connection = WebSocketConnection.getInstance().add(session);
        connection.start();
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        connection.handleMessage(message);
    }
}
