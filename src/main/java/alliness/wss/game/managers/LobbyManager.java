package alliness.wss.game.managers;

import alliness.wss.game.GameException;
import alliness.wss.game.player.Avatar;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LobbyManager extends Thread{

    private        List<Avatar> lobbyList;
    private static LobbyManager instance;

    private HashMap<String, BattleManager> battleRooms;

    private LobbyManager() {
        lobbyList = new LinkedList<>();
        battleRooms = new HashMap<>();
        start();
    }

    public static LobbyManager getInstance() {
        if (instance == null) {
            instance = new LobbyManager();
        }
        return instance;
    }

    /**
     * add new {@link Avatar} to managers lobby(lobbyList)
     *
     * @param avatar {@link Avatar}
     */
    public void addAvatar(Avatar avatar) throws GameException {

        for (Avatar av : lobbyList) {
            if (av.getConnection().getUUID().equals(avatar.getConnection().getUUID())) {
                av.disconnect();
                throw new GameException("duplicate connections");
            }
        }

        lobbyList.forEach(avatar1 -> avatar1.getConnection().sendMessage("managers/connected", avatar.getPlayer().serialize()));
        lobbyList.add(avatar);
        sendLobbyInfo(avatar);
    }

    private void sendLobbyInfo(Avatar avatar) {
        JSONObject lobbyInfo = new JSONObject();
        JSONArray  players   = new JSONArray();

        lobbyList.forEach(avatar1 -> {
            players.put(avatar1.getPlayer().serialize());
        });
        lobbyInfo.put("rooms", getBattleRooms().size());
        lobbyInfo.put("players", players);

        avatar.getConnection().sendMessage("lobby/info", lobbyInfo);
    }

    /**
     * Create managers room {@link BattleManager} instance for two avatars
     *
     * @param avatar  {@link Avatar} player 1
     * @param avatar1 {@link Avatar} player 2
     *                Those players moved from lobbyList to battleRoom
     */
    private void battleBegin(Avatar avatar, Avatar avatar1) {

        BattleManager battle = new BattleManager(avatar, avatar1);
        //add managers to battleRooms
        String roomId = battle.getRoomId();
        battleRooms.put(roomId, battle);
        //remove avatars from lobbyList
        lobbyList.removeIf(av -> av == avatar);
        lobbyList.removeIf(av -> av == avatar1);
    }

    public List<Avatar> getLobbyList() {
        return lobbyList;
    }


    public HashMap<String, BattleManager> getBattleRooms() {
        return battleRooms;
    }

    public boolean disconnect(String name) {

        boolean found = false;
        for (Avatar avatar : lobbyList) {
            if (avatar.getPlayer().getName().equals(name)) {
                found = true;
                avatar.disconnect();
                disconnect(avatar);
            }
        }
        return found;
    }

    public void disconnect(WebSocketConnection.Connection connection) {
        for (Avatar avatar : lobbyList) {
            if (avatar.getConnection().getUUID().equals(connection.getUUID())) {
                disconnect(avatar);
            }
        }
    }

    public void disconnect(Avatar av) {
        lobbyList.removeIf(avatar -> avatar.equals(av));
        lobbyList.forEach(avatar -> avatar.getConnection()
                                          .sendMessage(
                                                  "lobby/disconnect",
                                                  new JSONObject().put("name", av.getPlayer().serialize())
                                          ));
    }

    public void closeRoom(BattleManager battleInstance) {
        battleRooms.remove(battleInstance.getRoomId());
    }

    public BattleManager getRoom(String roomId) throws GameException {
        try {
            return battleRooms.get(roomId);
        } catch (Exception e) {
            throw new GameException(String.format("unable to get room %s", roomId));
        }
    }


    public void setAttack(JSONObject data, WebSocketConnection.Connection connection) {
        try {
            BattleManager room = getRoom(data.getString("roomId"));
            room.collectAttackProcess(data, connection);
        } catch (GameException e) {
            connection.sendMessage("managers/error", e.jsonMessage());
            e.printStackTrace();
        }
    }

    public void inviteToBattle(JSONObject data, WebSocketConnection.Connection connection) {

    }
}
