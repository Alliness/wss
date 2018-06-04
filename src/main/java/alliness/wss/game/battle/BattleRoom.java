package alliness.wss.game.battle;

import alliness.wss.game.GameException;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.BodyPartEnum;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleRoom {


    private final String       roomId;
    private       List<Avatar> avatars;
    private       int          playersReady;

    public BattleRoom(Avatar avatarOne, Avatar avatarTwo) {
        playersReady = 0;
        avatars = new ArrayList<>();
        roomId = UUID.randomUUID().toString();
        avatars.add(avatarOne);
        avatars.add(avatarTwo);
        avatars.forEach(avatar -> avatar.getConnection().onConnectionClosed(connection -> {
            disconnect(connection);
            avatar.setLocked(false);
            collapseBattle();
        }));

        avatarOne.getConnection().sendMessage("battle/start", new JSONObject().put("roomId", roomId).put("enemy", avatarTwo.getPlayer().serialize()));
        avatarTwo.getConnection().sendMessage("battle/start", new JSONObject().put("roomId", roomId).put("enemy", avatarOne.getPlayer().serialize()));
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

    public Avatar getAvatar(String uuid) throws GameException {
        for (Avatar avatar : avatars) {
            if (avatar.getConnection().getUUID().equals(uuid)) {
                return avatar;
            }
        }
        throw new GameException(String.format("unable to find avatar with uuid :%s", uuid));
    }

    /**
     * Handler for player attack politics (setter for player attack and defence)
     *
     * @param data       {@link JSONObject}
     * @param connection {@link alliness.wss.socket.WebSocketConnection.Connection}
     */
    public void collectAttackProcess(JSONObject data, WebSocketConnection.Connection connection) {
        try {
            BodyPartEnum attackPart  = BodyPartEnum.getPart(data.getInt("attack"));
            BodyPartEnum defencePart = BodyPartEnum.getPart(data.getInt("defence"));

            Avatar av = getAvatar(connection.getUUID());
            if (av.isLocked()) {
                connection.sendMessage("battle/error", new JSONObject().put("message", "player locked"));
                return;
            }
            av.setAttack(attackPart);
            av.setDefence(defencePart);
            playersReady++;
            av.setLocked(true);
            connection.sendMessage("battle/locked", new JSONObject());
            checkTurnIsReady();
        } catch (GameException e) {
            connection.sendMessage("battle/error", e.jsonMessage());
        }
    }

    private void checkTurnIsReady() {
        if (playersReady == 2 && avatars.size() == 2) {
            JSONObject av0 = attackProcess(avatars.get(0), avatars.get(1));
            JSONObject av1 = attackProcess(avatars.get(1), avatars.get(0));
            calculateAttack(av0, av1, avatars.get(0), avatars.get(1));
            calculateAttack(av1, av0, avatars.get(1), avatars.get(0));
            healthCheckPlayers();
            unlockPlayers();
        }
    }

    private void healthCheckPlayers() {
        avatars.forEach(avatar -> {
            if(avatar.getPlayer().getCurrentHealth() == 0)
            {
               avatars.forEach(target -> {
                   JSONObject data =new JSONObject();
                   data.put("defeated", avatar.getPlayer());
                   target.getConnection().sendMessage("battle/end", data);
               });
               collapseBattle();
            }
        });
    }

    private void calculateAttack(JSONObject deal, JSONObject taken, Avatar player, Avatar enemy) {

        JSONObject data = new JSONObject();
        data.put("deal", deal)
            .put("taken", taken)
            .put("player", player.getPlayer().serialize())
            .put("enemy", enemy.getPlayer().serialize());

        player.getConnection().sendMessage("battle/turn", data);

    }

    private JSONObject attackProcess(Avatar player, Avatar enemy) {
        return player.attack(enemy);
    }

    private void unlockPlayers() {
        playersReady = 0;
        avatars.forEach(avatar -> {
            avatar.setLocked(false);
            avatar.getConnection().sendMessage("battle/unlock", new JSONObject());
        });
    }

}
