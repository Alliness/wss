package alliness.wss;

import alliness.wss.api.Api;
import alliness.wss.game.GameWorld;
import alliness.wss.socket.WebSocketServer;

public class App {

    private static App instnace;

    public static App getInstnace() {
        return instnace;
    }

    private App() {
        Api             api    = Api.getInstance();
        GameWorld world = GameWorld.getInstance();
        WebSocketServer socket = WebSocketServer.getInstance();
        api.run();
        socket.run();
        instnace = this;
    }

    static App run() {
        return new App();
    }
}
