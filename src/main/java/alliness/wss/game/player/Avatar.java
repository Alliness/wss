package alliness.wss.game.player;

import alliness.wss.game.battle.BattleManager;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

public class Avatar {

    private final WebSocketConnection.Connection connection;
    private       PlayerDTO                      player;
    private       JSONObject                     info;

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
        JSONObject connectionData = connection.getInfo();
        connectionData.remove("fromClient");
        connectionData.remove("toClient");
        return new JSONObject().put("player", player.serialize())
                               .put("connection", connectionData);
    }

    public void disconnect() {
        connection.disconnect();
        BattleManager.getInstance().getAvatars().removeIf(avatar -> avatar.equals(this));
    }
}
