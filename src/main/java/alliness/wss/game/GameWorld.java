package alliness.wss.game;

import alliness.wss.game.managers.GameManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.Player;
import alliness.wss.socket.WebSocketConnection;
import org.apache.log4j.Logger;

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

    public void createAvatar(Player player, WebSocketConnection.Connection connection) throws GameException {

        Avatar avatar = new Avatar(player, connection);
        GameManager.getInstance().addAvatar(avatar);

    }
}
