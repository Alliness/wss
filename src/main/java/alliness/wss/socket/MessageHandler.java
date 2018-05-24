package alliness.wss.socket;

import alliness.wss.game.GameWorld;
import alliness.wss.game.battle.ChatManager;
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

            switch (action){
                case "chat/send":
                    ChatManager.getInstance().handle(data, connection);
                    break;
            }

        } catch (JSONException e) {
            log.error("unable to handle message", e);
        }
    }
}
