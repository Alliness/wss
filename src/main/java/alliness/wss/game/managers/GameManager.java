package alliness.wss.game.managers;

import alliness.wss.game.GameException;
import alliness.wss.game.interfaces.GameRoomManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            avatar.exitFromRoom();
            removeFromRoom(avatar);
        });

        avatars.add(avatar);
        sendConnectedMessage(avatar);
        sendRoomInfoMessage(avatar);
        sendPlayerInfo(avatar);
    }

    public boolean removeFromRoom(Avatar avatar) {
        boolean result = avatars.removeIf(avatar::equals);
        if (result) {
            sendLeaveMessage(avatar);
        }
        return result;
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

    @Override
    public void sendConnectedMessage(Avatar avatar) {
        avatars.forEach(avatar1 -> avatar1.getConnection().sendMessage("room/connected", avatar.getPlayer().serialize()));
    }

    @Override
    public void sendLeaveMessage(Avatar avatar) {
        avatars.forEach(avatar1 -> avatar1.getConnection().sendMessage("room/leave", avatar.getPlayer().serialize()));
    }

    @Override
    public void sendRoomInfoMessage(Avatar avatar) {
        JSONObject json = new JSONObject();
        json.put("online", new JSONArray(getOnlinePlayers()));
        avatar.getConnection().sendMessage("room/info", json);
    }

    public Avatar getAvatar(String name, String uuid) {
        for (Avatar avatar : avatars) {
            if (avatar.getPlayer().getName().equals(name) || avatar.getConnection().getUUID().equals(uuid)) {
                return avatar;
            }
        }
        return null;
    }

    private void sendPlayerInfo(Avatar avatar) {
        avatar.getConnection().sendMessage("player/info", avatar.getPlayer().serialize());
    }
}
