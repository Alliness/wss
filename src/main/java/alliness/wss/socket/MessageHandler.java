package alliness.wss.socket;

import alliness.wss.game.GameWorld;
import alliness.wss.game.managers.LobbyManager;
import alliness.wss.socket.handlers.PlayerMessageHandler;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler {

    private static final Logger log = Logger.getLogger(GameWorld.class);

    public static void handleMessage(String message, WebSocketConnection.Connection connection) {
        try {
            JSONObject obj    = new JSONObject(message);
            String     action = obj.getString("action");
            JSONObject data   = obj.getJSONObject("data");

            switch (action) {
                case "lobby/invite":
                    LobbyManager.getInstance().inviteToBattle(data, connection);
                    break;
                case "battle/attack":
                    LobbyManager.getInstance().setAttack(data, connection);
                    break;
                case "player/action":
                    PlayerMessageHandler.handleAction(data, connection);
            }

        } catch (JSONException e) {
            log.error("unable to handle message", e);
            connection.sendMessage("game/exception", new JSONObject().put("message", e.getMessage()));
        }
    }
}
