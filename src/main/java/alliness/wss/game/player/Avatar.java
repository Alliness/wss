package alliness.wss.game.player;

import alliness.wss.game.GameException;
import alliness.wss.game.interfaces.GameRoomManager;
import alliness.wss.game.managers.GameManager;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

public class Avatar {

    private final WebSocketConnection.Connection connection;
    private       Player                         player;

    private BodyPartEnum    defence;
    private BodyPartEnum    attack;
    private boolean         locked;
    private GameRoomManager room;

    public Avatar(Player player, WebSocketConnection.Connection connection) {
        this.player = player;
        this.connection = connection;
        this.connection.onConnectionClosed(conn -> disconnect());
    }

    public WebSocketConnection.Connection getConnection() {
        return connection;
    }

    public Player getPlayer() {
        return player;
    }

    public void disconnect() {
        connection.sendMessage("player/disconnect", new JSONObject());
        connection.disconnect();
    }

    public void disconnect(GameException exception) {
        connection.sendMessage("player/disconnect", exception.jsonMessage());
        connection.disconnect();
    }

    public BodyPartEnum getDefence() {
        return defence;
    }

    public void setDefence(BodyPartEnum defence) {
        this.defence = defence;
    }

    public BodyPartEnum getAttack() {
        return attack;
    }

    public void setAttack(BodyPartEnum attack) {
        this.attack = attack;
    }

    public JSONObject attack(Avatar enemy) {
        JSONObject obj    = new JSONObject();
        double     damage = 0;
        boolean    blocked;
        if (getAttack().equals(enemy.getDefence())) {
            blocked = true;
        } else {
            blocked = false;
            damage = getPlayer().getBaseAttack() - enemy.getPlayer().getBaseDefence();
            enemy.takeDamage(damage);
        }
        obj.put("blocked", blocked);
        obj.put("damage", damage);
        setAttack(null);
        return obj;
    }

    private void takeDamage(double damage) {
        player.setCurrentHealth(player.getCurrentHealth() - damage);
        setDefence(null);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void moveTo(GameRoomManager room) {
        this.room = room;
    }

    public GameRoomManager getRoom() {
        return room;
    }
}
