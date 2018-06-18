package alliness.wss.game.managers;

import alliness.wss.game.GameException;
import alliness.wss.game.interfaces.GameRoomManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.Player;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameManager implements GameRoomManager {

    private        List<Avatar> avatars;
    private static GameManager  instance;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private GameManager() {
        avatars = Collections.synchronizedList(new ArrayList<Avatar>());
    }

    public void addAvatar(Avatar avatar) {

        for (Avatar av : avatars) {
            if (av.getConnection().getUUID().equals(avatar.getConnection().getUUID())) {
                GameException exception = new GameException("duplicate connection");
                av.disconnect(exception);
                avatar.disconnect(exception);
                return;
            }
        }
        avatar.moveTo(this);
        avatar.getConnection().onConnectionClosed(connection -> {
            avatar.getRoom().removeFromRoom(avatar);
            removeFromRoom(avatar);
        });

        avatars.add(avatar);
    }

    public boolean removeFromRoom(Avatar avatar) {
        return avatars.removeIf(avatar::equals);
    }

    public void kick(Avatar avatar, String reason) {
        avatar.getConnection().sendMessage("player/kick", new JSONObject().put("reason", reason));
        avatar.getConnection().disconnect();
    }

    public List<Avatar> getAvatars() {
        return avatars;
    }

    public List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Avatar avatar : avatars) {
            players.add(avatar.getPlayer());
        }
        return players;
    }

    public boolean isOnline(String name) {
        return avatars.stream().anyMatch(avatar -> avatar.getPlayer().getName().equals(name));
    }
}
