package alliness.wss.game;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class GameWorld extends Thread {

    private static final Logger log = Logger.getLogger(GameWorld.class);
    private static GameWorld instance;

    public static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
        }
        return instance;
    }

    private GameWorld() {
        run();
    }

    public void handleMessage(String message) {
        try {
            JSONObject obj = new JSONObject(message);
            String action = obj.getString("action");
            JSONObject body = obj.getJSONObject("body");
            switch (action) {
                case "/player/new":
                    createNewPlayer(body);
                    break;
                case "/battle/connect": //todo
                    break;
            }

        } catch (JSONException e) {
            log.error("unable to handle message", e);
        }
    }

    /**
     * Creates new Player
     *
     * @param body
     */
    private void createNewPlayer(JSONObject body) {
        // create new JSONObject
        // store in dir
        // create avatar
        // link player dto
        // link session
        // set avatar to battlemanger
    }
}
