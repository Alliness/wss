package alliness.wss.game.managers;

import alliness.wss.game.GameException;
import alliness.wss.game.interfaces.GameRoomManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LobbyManager extends Thread implements GameRoomManager {

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

    public void addAvatar(Avatar avatar) {

        for (Avatar av : lobbyList) {
            if (av.getConnection().getUUID().equals(avatar.getConnection().getUUID())) {
                GameException exception = new GameException("duplicate connection");
                av.disconnect(exception);
                avatar.disconnect(exception);
                return;
            }
        }
        avatar.moveTo(this);

        sendRoomInfoMessage(avatar);
        sendConnectedMessage(avatar);
        lobbyList.add(avatar);

    }

    @Override
    public boolean removeFromRoom(Avatar avatar) {
        boolean result = lobbyList.removeIf(avatar1 -> avatar1.getConnection().getUUID().equals(avatar.getConnection().getUUID()));
        if(result){
            sendLeaveMessage(avatar);
        }
        return result;
    }

    private void sendLobbyInfo(Avatar avatar) {
        JSONObject lobbyInfo = new JSONObject();
        JSONArray  players   = new JSONArray();

        lobbyList.forEach(avatar1 -> players.put(avatar1.getPlayer().serialize()));
        lobbyInfo.put("rooms", getBattleRooms().size());
        lobbyInfo.put("players", players);

        avatar.getConnection().sendMessage("lobby/info", lobbyInfo);
    }

    private void battleBegin(Avatar avatar, Avatar avatar1) {

        BattleManager battle = new BattleManager(avatar, avatar1);
        //add managers to battleRooms
        String roomId = battle.getRoomId();
        battleRooms.put(roomId, battle);
        //remove avatars from lobbyList
        lobbyList.removeIf(av -> av == avatar);
        lobbyList.removeIf(av -> av == avatar1);
    }

    public HashMap<String, BattleManager> getBattleRooms() {
        return battleRooms;
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

    @Override
    public void sendConnectedMessage(Avatar avatar) {
        lobbyList.forEach(avatar1 -> {
            avatar1.getConnection().sendMessage("lobby/connected", avatar.getPlayer().serialize());
        });
    }

    @Override
    public void sendLeaveMessage(Avatar avatar) {
        lobbyList.forEach(avatar1 -> {
            avatar1.getConnection().sendMessage("lobby/leave", avatar.getPlayer().serialize());
        });
    }

    @Override
    public void sendRoomInfoMessage(Avatar avatar) {
        JSONObject json = new JSONObject();
        json.put("roomList", new JSONArray().put(lobbyList));
        json.put("inBattle", battleRooms.size() * 2);
        json.put("queue", false);
        avatar.getConnection().sendMessage("lobby/info", json);
    }
}
