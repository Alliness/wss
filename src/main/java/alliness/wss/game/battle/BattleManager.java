package alliness.wss.game.battle;

import alliness.core.utils.RandomUtils;
import alliness.wss.game.GameException;
import alliness.wss.game.player.Avatar;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class BattleManager {

    private        List<Avatar>  avatars;
    private        Avatar        active;
    private static BattleManager instance;
    private        BattleState   state;

    private BattleManager() {
        avatars = new LinkedList<>();
        setState(BattleState.PREPARE);
    }

    public static BattleManager getInstance() {
        if (instance == null) {
            instance = new BattleManager();
        }
        return instance;
    }

    public void addAvatar(Avatar avatar) throws GameException {

        for (Avatar av : avatars) {
            if (av.getConnection().getUUID().equals(avatar.getConnection().getUUID())) {
                avatars.forEach(avatar1 -> avatar1.getConnection()
                                                  .sendMessage("battle/disconnect",
                                                               new JSONObject().put("error", "duplicate connections")));
                avatars.clear();
                checkBattleState();
                throw new GameException("duplicate connections");
            }
        }
        avatars.add(avatar);
        avatars.forEach(avatar1 -> avatar1.getConnection().sendMessage("battle/connected", getInfo()));
        checkBattleState();

    }

    private void checkBattleState() {

        switch (avatars.size()) {
            case 0:
                setState(BattleState.NOTREADY);
                break;
            case 1:
                setState(BattleState.PREPARE);
                break;
            case 2:
                setState(BattleState.READY);
                battleBegin();
                break;
        }

        // how many players in manager
        //set state and send message

    }

    private void battleBegin() {
        setFirstTurn();
        avatars.forEach(avatar -> {
            avatar.getConnection()
                  .sendMessage("battle/start",
                               new JSONObject().put("turn",
                                                    new JSONObject().put("uuid", active.getConnection().getUUID())
                                                                    .put("player", active.getPlayer().serialize())));
        });
        setState(BattleState.INPROGRESS);
    }

    public List<Avatar> getAvatars() {
        return avatars;
    }

    public BattleState getState() {
        return state;
    }

    public void setState(BattleState state) {
        if (this.state != state) {
            this.state = state;
            avatars.forEach(avatar -> avatar.getConnection()
                                            .sendMessage("battle/state", new JSONObject().put("state", state)));
        }
    }

    private void setFirstTurn() {
        int firstTurn = RandomUtils.getRandomInt(1, avatars.size());
        active = avatars.get(firstTurn);
    }

    public JSONObject getInfo() {
        JSONArray arr = new JSONArray();

        avatars.forEach(avatar -> arr.put(avatar.getPlayer().serialize()));

        return new JSONObject().put("players", arr)
                               .put("state", state);
    }

    public boolean disconnect(String name) {

        Avatar target = null;
        for (Avatar avatar : avatars) {
            if (avatar.getPlayer().getName().equals(name)) {
                target = avatar;
                avatar.disconnect();
            }
        }
        disconnect(target);
        return target != null;
    }

    public boolean disconnect(WebSocketConnection.Connection connection) {
        Avatar target = null;
        for (Avatar avatar : avatars) {
            if (avatar.getConnection().getUUID().equals(connection.getUUID())) {
                target = avatar;
                avatar.disconnect();
            }
        }
        disconnect(target);
        return target != null;
    }

    public void disconnect(Avatar av) {
        avatars.removeIf(avatar -> avatar.equals(av));

        avatars.forEach(avatar -> {
            avatar.getConnection()
                  .sendMessage(
                          "battle/disconnect", new JSONObject()
                                  .put("name", av.getPlayer().getName())
                                  .put("uuid", avatar.getConnection().getUUID())
                  );
        });
        checkBattleState();
    }

}
