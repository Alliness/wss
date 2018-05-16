package alliness.websocketserver.Api;

import alliness.core.ConfigLoader;
import alliness.websocketserver.App;
import alliness.websocketserver.socket.WebSocketConnection;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import static spark.Spark.*;

public class Api extends Thread {

    private static final Logger log = Logger.getLogger(Api.class);
    private static Api instance;

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    public void run() {

        port(Integer.parseInt(ConfigLoader.getInstance().getProperty("api.port")));
        before((request, response) -> {
            response.type("application/json;charset=UTF-8");
        });

        path("/api", () -> {
            path("/socket", () -> {
                get("/info", (request, response) -> WebSocketConnection.getInstance().getInfo());
            });
        });

        notFound((request, response) -> {
            response.status(404);
            response.body(new JSONObject().put("code", 404).put("message", "not found").toString());
            return response.body();
        });
        log.info(String.format("Api Web Server port: %s",port()));

    }
}
