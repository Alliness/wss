package alliness.wss.api;

import alliness.core.ConfigLoader;
import alliness.core.Dir;
import alliness.wss.socket.WebSocketConnection;
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

        externalStaticFileLocation(Dir.PROJECT+ "/site");

        before((request, response) -> {
            response.type("application/json;charset=UTF-8");
        });

        path("/api", () -> {

            path("/socket", () -> {
                get("/info", (request, response) -> WebSocketConnection.getInstance().getInfo());
            });

        });


        path("/game", () -> {
            path("/player", () -> {
                post("/new", ApiHandler::newPlayer);
                post("/connect", ApiHandler::connect);
                get("/available", ApiHandler::availablePlayers);
            });

            path("/battle", () -> {

            });

            get("/info", ApiHandler::info);
        });

        notFound((request, response) -> {
            response.status(404);
            response.body(new JSONObject().put("success", false).put("error", 404).put("message", "page not found").toString());
            return response.body();
        });

        log.info(String.format("Api Web Server port: %s", port()));

    }
}
