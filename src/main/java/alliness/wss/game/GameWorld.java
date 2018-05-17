package alliness.wss.game;

import alliness.wss.game.battle.BattleManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.PlayerDTO;
import alliness.wss.socket.WebSocketConnection;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class GameWorld extends Thread {

    private static final Logger log = Logger.getLogger(GameWorld.class);
    private static GameWorld instance;

    public static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
        }
        return instance;
    }

    private GameWorld() {
    }

    public void handleMessage(String message) {
        try {
            JSONObject obj    = new JSONObject(message);
            String     action = obj.getString("action");
            JSONObject body   = obj.getJSONObject("body");


        } catch (JSONException e) {
            log.error("unable to handle message", e);
        }
    }

    public void createAvatar(PlayerDTO player, WebSocketConnection.Connection connection) throws GameException {

        Avatar avatar = new Avatar(player, connection);
        BattleManager.getInstance().addAvatar(avatar);
    }
}
