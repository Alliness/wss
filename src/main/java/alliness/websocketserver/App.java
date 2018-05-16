package alliness.websocketserver;

import alliness.websocketserver.Api.Api;
import alliness.websocketserver.socket.WebSocketServer;

public class App {

    private static App instnace;
    private Object info;

    public static App getInstnace() {
        return instnace;
    }

    private App() {
        Api             api    = Api.getInstance();
        WebSocketServer socket = WebSocketServer.getInstance();
        api.run();
        socket.run();
        instnace = this;
    }

    static App run() {
        return new App();
    }
}
