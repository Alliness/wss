package alliness.wss.api;

import alliness.core.Dir;
import alliness.core.Serializable;
import alliness.core.helpers.FReader;
import alliness.core.helpers.FWriter;
import alliness.wss.game.GameException;
import alliness.wss.game.GameWorld;
import alliness.wss.game.battle.BattleManager;
import alliness.wss.game.player.Avatar;
import alliness.wss.game.player.PlayerDTO;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class PlayerHandler extends AbstractApiHandler {

    static Object newPlayer(Request request, Response response) {

        try {
            JSONObject body = new JSONObject(request.body());
            String     name = body.getString("name");
            if (name == null || Objects.equals(name, "")) {
                return jsonFailMessage("player name  is invalid");
            }
            try {
                JSONObject defPlayerJson = FReader.readJSON(new File(Dir.RESOURCES + "/players/default.json"));
                PlayerDTO  player        = Serializable.deserialize(defPlayerJson, PlayerDTO.class);

                player.setName(name);
                File file = new File(Dir.RESOURCES + "/players/created/" + player.getName() + ".json");

                if (file.exists()) {
                    return jsonFailMessage("player already exist");
                }

                FWriter.writeToFile(player.serialize(), file);
                return jsonSuccessMessage("player", player.serialize());
            } catch (FileNotFoundException e) {
                return jsonFailMessage(e.getMessage());
            }

        } catch (JSONException e) {
            log.error("unable to parse request", e);
            return jsonFailMessage(e.getMessage());
        }
    }

    static Object connect(Request request, Response response) {

        JSONObject body = new JSONObject(request.body());
        String     uuid = body.getString("uuid");
        String     name = body.getString("name");


        WebSocketConnection.Connection connection = WebSocketConnection.getInstance().getConnection(uuid);

        if (connection == null) {
            return jsonFailMessage(String.format("not found connection with uuid %s", uuid));
        }

        File file = new File(Dir.RESOURCES + "/players/created/" + name + ".json");

        if (!file.exists()) {
            return jsonFailMessage(String.format("not found player with name %s", name));
        }

        try {
            PlayerDTO player = Serializable.deserialize(FReader.readJSON(file), PlayerDTO.class);

            for (Avatar avatar : BattleManager.getInstance().getAvatars()) {
                if (avatar.getPlayer().getName().equals(name)) {
                    return jsonFailMessage(String.format("player %s already selected", name));
                }
            }
            GameWorld.getInstance().createAvatar(player, connection);
            return new JSONObject().put("success", true);
        } catch (FileNotFoundException | GameException e) {
            return jsonFailMessage(e.getMessage());
        }

    }

    public static Object availablePlayers(Request request, Response response) {
        JSONArray players = new JSONArray();

        File   dir   = new File(Dir.RESOURCES + "/players/created");
        File[] files = dir.listFiles();

        if (files == null) {
            return jsonFailMessage("files not found");
        }
        for (File file : files) {
            try {
                JSONObject obj    = FReader.readJSON(file);
                PlayerDTO  player = Serializable.deserialize(obj, PlayerDTO.class);
                players.put(player.serialize());
            } catch (JSONException | FileNotFoundException e) {
                return jsonFailMessage(e.getMessage());
            }
        }
        return players;
    }

    public static Object getInfo(Request request, Response response) {
        String[] parsed = request.uri().split("/");
        String   name   = parsed[parsed.length - 1];
        try {
            JSONObject player = FReader.readJSON(Dir.RESOURCES + "/players/created/" + name + ".json");
            return jsonSuccessMessage("player", player);
        } catch (Exception e) {
            return jsonFailMessage(e.getMessage());
        }
    }
}
