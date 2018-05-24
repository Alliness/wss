package alliness.wss.game;

import alliness.wss.game.battle.BattleManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.dto.PlayerDTO;
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


    public void createAvatar(PlayerDTO player, WebSocketConnection.Connection connection) throws GameException {

        Avatar avatar = new Avatar(player, connection);
        BattleManager.getInstance().addAvatar(avatar);
    }
}
