package alliness.wss.game.battle;

import alliness.wss.game.GameException;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

import java.time.LocalTime;

public class ChatManager {

    private static ChatManager instance;

//    private List<JSONObject>

    private ChatManager() {

    }

    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    public void handle(JSONObject data, WebSocketConnection.Connection connection) {
        try {

            String     roomId  = data.getString("roomId");
            JSONObject message = new JSONObject();
            BattleRoom room    = BattleManager.getInstance().getRoom(roomId);

            message.put("text", data.getString("text"))
                   .put("from", room.getAvatar(connection.getUUID()).getPlayer().getName())
                   .put("time", LocalTime.now());

            room.getAvatars().forEach(avatar -> {
                if (avatar.getConnection() != connection)
                    avatar.getConnection().sendMessage("chat/message", message);
            });
        } catch (GameException e) {
            connection.sendMessage("chat/error", e.jsonMessage());
        }
    }
}
