package alliness.wss.api;

import alliness.core.Dir;
import alliness.wss.game.player.PlayerClassEnum;
import alliness.wss.game.player.PlayerRaceEnum;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.File;
import java.util.Arrays;

public class ResourcesHandler extends ApiHandler {


    public static Object getIcons(Request request, Response response) {
        String dirPath = Dir.WEB + "/assets/img/";
        File   folder  = new File(dirPath);
        File[] files   = folder.listFiles();
        if (files != null && folder.exists()) {
            JSONArray images = new JSONArray();
            for (File file : files) {
                images.put(file.getName());
            }
            return jsonSuccessMessage(new JSONObject().put("images", images));
        } else {
            return jsonFailMessage(String.format("unable to get directory %s", dirPath));
        }
    }

    public static Object getData(Request request, Response response) {
        JSONObject data    = new JSONObject();
        JSONArray  classes = new JSONArray();
        JSONArray  races   = new JSONArray();

        for (PlayerClassEnum playerClassEnum : PlayerClassEnum.values()) {
            classes.put(playerClassEnum.name());
        }

        for (PlayerRaceEnum playerRaceEnum : PlayerRaceEnum.values()) {
            races.put(playerRaceEnum.name());
        }
        data.put("classes", classes)
            .put("races", races);
        return data;
    }
}
