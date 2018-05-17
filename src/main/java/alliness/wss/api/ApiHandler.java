package alliness.wss.api;

import alliness.core.Dir;
import alliness.core.Serializable;
import alliness.core.helpers.FReader;
import alliness.core.helpers.FWriter;
import alliness.wss.game.GameException;
import alliness.wss.game.GameWorld;
import alliness.wss.game.player.PlayerDTO;
import alliness.wss.socket.WebSocketConnection;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.File;
import java.util.Objects;

class ApiHandler {

    private static final Logger log = Logger.getLogger(ApiHandler.class);

    static Object newPlayer(Request request, Response response) {

        try {
            JSONObject body = new JSONObject(request.body());
            String     name = body.getString("name");
            if (name == null || Objects.equals(name, "")) {
                return new JSONObject().put("success", false).put("error", "player name  is invalid");
            }

            JSONObject defPlayerJson = FReader.readJSON(new File(Dir.RESOURCES + "/players/default.json"));
            PlayerDTO  player        = Serializable.deserialize(defPlayerJson, PlayerDTO.class);


            player.setName(name);
            File file = new File(Dir.RESOURCES + "/players/created/" + player.getName() + ".json");

            if (file.exists()) {
                return new JSONObject().put("success", false).put("error", "player already exist");
            }

            FWriter.writeToFile(player.serialize(), file);
            return new JSONObject().put("success", true).put("player", player.serialize());

        } catch (JSONException e) {
            log.error("unable to parse request", e);
            return new JSONObject().put("success", false).put("error", e.getMessage());
        }
    }

    static Object connect(Request request, Response response) {

        JSONObject body = new JSONObject(request.body());
        String     uuid = body.getString("uuid");
        String     name = body.getString("name");


        WebSocketConnection.Connection connection = WebSocketConnection.getInstance().getConnection(uuid);

        if (connection == null) {
            return new JSONObject().put("success", false).put("error", String.format("not found connection with uuid %s", uuid));
        }

        File file = new File(Dir.RESOURCES + "/players/created/" + name + ".json");

        if (!file.exists()) {
            return new JSONObject().put("success", false).put("error", String.format("not found player with name %s", name));
        }

        PlayerDTO player = Serializable.deserialize(FReader.readJSON(file), PlayerDTO.class);

        try {
            GameWorld.getInstance().createAvatar(player, connection);
            return new JSONObject().put("success", true);
        } catch (GameException e) {
            return new JSONObject().put("success", false).put("error", "unable to connect to battle").put("message", e.getMessage());
        }

    }
}
