package alliness.wss.game.battle;

import alliness.core.utils.RandomUtils;
import alliness.wss.game.GameException;
import alliness.wss.game.player.Avatar;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleRoom {


    private final String roomId;
    private       Avatar firstPlayer;
    private       Avatar secondPlayer;

    private List<Avatar> avatars;

    public BattleRoom(Avatar avatarOne, Avatar avatarTwo) {
        int firstTurn = RandomUtils.getRandomInt(1, 2);
        avatars = new ArrayList<>();
        roomId = UUID.randomUUID().toString();

        switch (firstTurn) {
            case 1:
                firstPlayer = avatarOne;
                secondPlayer = avatarTwo;
                break;
            case 2:
                firstPlayer = avatarTwo;
                secondPlayer = avatarOne;
                break;
        }
        avatars.add(firstPlayer);
        avatars.add(secondPlayer);

        JSONObject turn = new JSONObject().put("uuid", firstPlayer.getConnection().getUUID())
                                          .put("player", firstPlayer.getPlayer().serialize());

        JSONObject data = new JSONObject().put("turn", turn)
                                          .put("room", roomId);

        avatars.forEach(avatar -> avatar.getConnection().sendMessage("battle/start", data));

        avatars.forEach(avatar -> avatar.getConnection().onConnectionClosed(connection -> {
            disconnect(connection);
            collapseBattle();
        }));
    }

    private void disconnect(WebSocketConnection.Connection connection) {
        avatars.removeIf(avatar -> avatar.getConnection().getUUID().equals(connection.getUUID()));
    }

    private void collapseBattle() {
        avatars.forEach(avatar -> {
            try {
                BattleManager.getInstance().addAvatar(avatar);
            } catch (GameException e) {
                disconnect(avatar.getConnection());
            }
        });
        BattleManager.getInstance().closeRoom(this);
    }

    public String getRoomId() {
        return roomId;
    }

    public List<Avatar> getAvatars() {
        return avatars;
    }
}
