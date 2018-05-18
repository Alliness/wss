package alliness.wss.socket;

import alliness.wss.game.GameWorld;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("all")
public class WebSocketConnection {

    private static final Logger log = Logger.getLogger(WebSocketConnection.class);
    private List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    private final long startTime;

    private static WebSocketConnection instance;
    private boolean info;

    public synchronized static WebSocketConnection getInstance() {
        if (instance == null) {
            instance = new WebSocketConnection();
        }
        return instance;
    }

    private WebSocketConnection() {
        startTime = System.currentTimeMillis();
    }

    public JSONObject getInfo() {
        JSONObject info = new JSONObject();

        info.put("uptime", System.currentTimeMillis() - startTime);
        info.put("connections", getConnectionsInfo());
        info.put("count", connections.size());
        return info;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public Connection add(Session session) {
        return new Connection(session);
    }

    public void remove(Connection connection) {
        connection.stopAlltasks();
        for (Connection conn : connections) {
            if(conn.equals(connection)){
                conn.interrupt();
            }
        }
        connections.removeIf(client -> client == connection);
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public JSONArray getConnectionsInfo() {
        JSONArray arr = new JSONArray();
        connections.forEach(connection -> {
            arr.put(connection.getInfo());
        });
        return arr;
    }

    public Connection getConnection(String uuid) {

        Optional<Connection> res = connections.stream().filter(connection -> connection.getUUID().equals(uuid)).findFirst();
        if (res.isPresent()) {
            return res.get();
        } else {
            return null;
        }

    }

    public class Connection extends Thread {

        private String uuid;
        private Session session;
        private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        private List<ScheduledFuture<?>> tasksLists = Collections.synchronizedList(new ArrayList<>());
        private List<Runnable> commands = Collections.synchronizedList(new ArrayList<>());
        private long connectTime;
        private List<JSONObject> sendedMessages;

        private List<String> receivedMesages;

        public Connection(Session session) {
            if (!connections.contains(this)) {
                this.session = session;
                connectTime = System.currentTimeMillis();
                sendedMessages = new ArrayList<>();
                receivedMesages = new ArrayList<>();
                uuid = generateUUID();
                connections.add(this);

                sendMessage("connection/id", new JSONObject().put("uuid", uuid));
            } else {
                sendMessage("connection/error", new JSONObject().put("reason", "duplicate connection"));
                interrupt();
            }
        }

        public String getUUID() {
            return uuid;
        }

        public void handleMessage(String message) {
            if(message.equals("2"))
                return;
            try {
                GameWorld.getInstance().handleMessage(message);
                receivedMesages.add(message);
            } catch (JSONException ignored) {
            }
        }

        public void sendMessage(String action, JSONObject json) {
            try {
                JSONObject msg = new JSONObject();
                msg.put("data", json);
                msg.put("action", action);
                sendedMessages.add(msg);
                session.getRemote().sendString(msg.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            scheduledExecutorService.scheduleAtFixedRate(this::healthCheck, 0, 20, TimeUnit.SECONDS);
        }

        private void healthCheck() {
            try {
                session.getRemote().sendString("1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public ScheduledFuture<?> addTask(Runnable task, int delay) {

            log.info(String.format("[WSS-%s] added new Task for Client", session.getRemoteAddress()));
            ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(task, 0, delay, TimeUnit.MILLISECONDS);
            tasksLists.add(scheduledFuture);
            return scheduledFuture;
        }

        public void stopTask(ScheduledFuture<?> task) {
            if (tasksLists.contains(task)) {
                task.cancel(false);
                tasksLists.removeIf(scheduledFuture -> scheduledFuture.equals(task));
            }
        }

        public void stopAlltasks() {
            tasksLists.forEach(scheduledFuture -> scheduledFuture.cancel(true));
            tasksLists.clear();
            commands.clear();
        }

        public Session getSession() {
            return session;
        }

        public long getConnectTime() {
            return System.currentTimeMillis() - connectTime;
        }

        public List<JSONObject> getSendedMessages() {
            return sendedMessages;
        }

        public List<String> getReceivedMesages() {
            return receivedMesages;
        }

        public JSONObject getInfo() {
            JSONObject jcon = new JSONObject();
            jcon.put("uuid", getUUID())
                    .put("connectTime", getConnectTime())
                    .put("remoteAddress", getSession().getRemoteAddress())
                    .put("toClient", getSendedMessages())
                    .put("fromClient", getReceivedMesages());

            return jcon;
        }

        public void disconnect() {
            remove(this);
            session.close();
        }
    }

}
