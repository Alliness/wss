package alliness.wss.socket.handlers;

import alliness.wss.game.managers.GameManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

public class PlayerMessageHandler {

    public static void handleAction(JSONObject data, WebSocketConnection.Connection connection) {
        switch (data.getString("event")) {
            case "info":
                getPlayerInfo(GameManager.getInstance().getAvatar(null, connection.getUUID()));
                break;
            case "skills":
                getPlayerSkills(GameManager.getInstance().getAvatar(null, connection.getUUID()));
                break;
            case "inventory":
                getPlayerInventory(GameManager.getInstance().getAvatar(null, connection.getUUID()));
                break;
        }
    }

    private static void getPlayerSkills(Avatar avatar) {

    }

    private static void getPlayerInventory(Avatar avatar) {

    }

    private static void getPlayerInfo(Avatar avatar) {
        avatar.getConnection().sendMessage("player/info", avatar.getPlayer().serialize());
    }
}
