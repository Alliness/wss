package alliness.wss.game.battle;

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
        String roomId = data.getString("roomId");
        BattleManager.getInstance().getRoom(roomId).getAvatars().forEach(avatar -> {
            if (avatar.getConnection() != connection)
                avatar.getConnection().sendMessage("chat/message", data.put("time", LocalTime.now()));
        });
    }
}
