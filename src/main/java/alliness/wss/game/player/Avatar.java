package alliness.wss.game.player;

import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

public class Avatar {

    private final WebSocketConnection.Connection connection;
    private       PlayerDTO                      player;
    private       boolean                        info;

    public Avatar(PlayerDTO player, WebSocketConnection.Connection connection) {
        this.player = player;
        this.connection = connection;
    }

    public WebSocketConnection.Connection getConnection() {
        return connection;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setDefence(BodyPartEnum part) {
    }

    public void setAttack(BodyPartEnum part) {
    }

    private void set() {
    }

    public JSONObject getInfo() {
        return new JSONObject().put("player", player.serialize())
                               .put("connection", connection.getUUID());
    }
}
