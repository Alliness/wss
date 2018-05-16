package alliness.websocketserver.socket;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("all")
public class WebSocketConnection {

    private static final Logger           log         = Logger.getLogger(WebSocketConnection.class);
    private              List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    private final long startTime;

    private static WebSocketConnection instance;

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

    public Connection add(Session session) {
        return new Connection(session);
    }

    public void remove(Connection connection) {
        connection.stopAlltasks();
        connections.removeIf(client -> client == connection);
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public JSONArray getConnectionsInfo() {
        JSONArray arr = new JSONArray();
        connections.forEach(connection -> {
            JSONObject jcon = new JSONObject();
            jcon.put("connectTime", connection.getConnectTime());
            jcon.put("remoteAddress", connection.getSession().getRemoteAddress());
            jcon.put("send", connection.getSendedMessages());
            jcon.put("recevied", connection.getReceivedMesages());
            arr.put(jcon);
        });
        return arr;
    }

    /**
     *
     */
    public class Connection extends Thread {

        private Session session;
        private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        private       List<ScheduledFuture<?>> tasksLists               = Collections.synchronizedList(new ArrayList<>());
        private       List<Runnable>           commands                 = Collections.synchronizedList(new ArrayList<>());
        private long             connectTime;
        private List<JSONObject> sendedMessages;
        private List<String>     receivedMesages;

        public Connection(Session session) {
            if (!connections.contains(this)) {
                this.session = session;
                connectTime = System.currentTimeMillis();

                sendedMessages = new ArrayList<>();
                receivedMesages = new ArrayList<>();

                connections.add(this);
            } else {
                interrupt();
            }
        }


        public void handleMessage(String message) {
            try {
                System.out.println(message);
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
    }

}
