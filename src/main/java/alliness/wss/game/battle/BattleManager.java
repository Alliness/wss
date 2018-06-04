package alliness.wss.game.battle;

import alliness.wss.game.GameException;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.BodyPartEnum;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BattleManager {

    private        List<Avatar>  waitList;
    private        List<String>  onlineList;
    private static BattleManager instance;
    private        BattleState   state;

    private HashMap<String, BattleRoom> battleRooms;

    private BattleManager() {
        waitList = new LinkedList<>();
        onlineList = new ArrayList<>();
        battleRooms = new HashMap<>();
        setState(BattleState.PREPARE);
    }

    public static BattleManager getInstance() {
        if (instance == null) {
            instance = new BattleManager();
        }
        return instance;
    }

    /**
     * add new {@link Avatar} to battle lobby(waitList)
     *
     * @param avatar {@link Avatar}
     * @throws GameException {@link GameException}
     */
    public void addAvatar(Avatar avatar) throws GameException {

        for (Avatar av : waitList) {
            if (av.getConnection().getUUID().equals(avatar.getConnection().getUUID())) {
                avatar.getConnection().sendMessage("battle/disconnect",
                                                   new JSONObject().put("error", "duplicate connections"));
                av.disconnect();
                throw new GameException("duplicate connections");
            }
        }
        waitList.add(avatar);
        onlineList.add(avatar.getPlayer().getName());

        battleInitFor(avatar);
        waitList.forEach(avatar1 -> avatar1.getConnection().sendMessage("battle/connected", getInfo()));
        checkBattleState();
    }

    private void battleInitFor(Avatar avatar) {
        JSONObject data = new JSONObject();
        JSONArray  body = new JSONArray();

        for (BodyPartEnum bodyPartEnum : BodyPartEnum.list()) {
            body.put(bodyPartEnum);
        }

        data.put("body", body);
        data.put("player", avatar.getPlayer().serialize());
        avatar.getConnection().sendMessage("battle/init", data);
    }

    /**
     * check battle ready
     * if players in waitList 1 - battle state set as PREPARE
     * if players in waitList 2 - battle state set as READY battle room will be created
     */
    private void checkBattleState() {

        switch (waitList.size()) {
            case 1:
                setState(BattleState.PREPARE);
                break;
            case 2:
                setState(BattleState.READY);
                battleBegin(waitList.get(0), waitList.get(1));
                break;
        }
    }

    /**
     * Create battle room {@link BattleRoom} instance for two avatars
     *
     * @param avatar  {@link Avatar} player 1
     * @param avatar1 {@link Avatar} player 2
     *                Those players moved from waitList to battleRoom
     */
    private void battleBegin(Avatar avatar, Avatar avatar1) {

        BattleRoom battle = new BattleRoom(avatar, avatar1);
        //add battle to battleRooms
        String roomId = battle.getRoomId();
        battleRooms.put(roomId, battle);
        //remove avatars from waitList
        waitList.removeIf(av -> av == avatar);
        waitList.removeIf(av -> av == avatar1);
        checkBattleState();
    }

    public List<Avatar> getWaitList() {
        return waitList;
    }

    public BattleState getState() {
        return state;
    }

    public void setState(BattleState state) {
        this.state = state;
        waitList.forEach(avatar -> avatar.getConnection()
                                         .sendMessage("battle/state", new JSONObject().put("state", state)));
    }

    public JSONObject getInfo() {
        JSONArray arr = new JSONArray();

        waitList.forEach(avatar -> arr.put(avatar.getPlayer().serialize()));

        return new JSONObject().put("waitList", arr).put("rooms", battleRooms.size()).put("online", onlineList);
    }

    public List<String> getOnlineList() {
        return onlineList;
    }

    public HashMap<String, BattleRoom> getBattleRooms() {
        return battleRooms;
    }

    public boolean disconnect(String name) {

        Avatar target = null;
        for (Avatar avatar : waitList) {
            if (avatar.getPlayer().getName().equals(name)) {
                target = avatar;
                avatar.disconnect();
            }
        }
        disconnect(target);
        return target != null;
    }

    public void disconnect(WebSocketConnection.Connection connection) {
        for (Avatar avatar : waitList) {
            if (avatar.getConnection().getUUID().equals(connection.getUUID())) {
                disconnect(avatar);
            }
        }
    }

    public void disconnect(Avatar av) {
        waitList.removeIf(avatar -> avatar.equals(av));
        onlineList.removeIf(avatar -> avatar.equals(av.getPlayer().getName()));
        waitList.forEach(avatar -> avatar.getConnection()
                                         .sendMessage(
                                                 "battle/disconnect",
                                                 new JSONObject().put("name", av.getPlayer().getName())
                                                                 .put("uuid", avatar.getConnection().getUUID())
                                         ));
        checkBattleState();
    }

    public void closeRoom(BattleRoom battleInstance) {
        battleRooms.remove(battleInstance.getRoomId());
    }

    public BattleRoom getRoom(String roomId) throws GameException {
        try {
            return battleRooms.get(roomId);
        } catch (Exception e) {
            throw new GameException(String.format("unable to get room %s", roomId));
        }
    }


    public void setAttack(JSONObject data, WebSocketConnection.Connection connection) {
        try {
            BattleRoom room = getRoom(data.getString("roomId"));
            room.collectAttackProcess(data, connection);
        } catch (GameException e) {
            connection.sendMessage("battle/error", e.jsonMessage());
            e.printStackTrace();
        }
    }

    public boolean isOnline(String name) {
        return onlineList.contains(name);
    }
}
