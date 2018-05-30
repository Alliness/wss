package alliness.wss.game.player;

import alliness.wss.game.battle.BattleManager;
import alliness.wss.game.player.dto.PlayerDTO;
import alliness.wss.socket.WebSocketConnection;
import org.json.JSONObject;

public class Avatar {

    private final WebSocketConnection.Connection connection;
    private       PlayerDTO                      player;
    private       JSONObject                     info;

    private BodyPartEnum defence;
    private BodyPartEnum attack;

    public Avatar(PlayerDTO player, WebSocketConnection.Connection connection) {
        this.player = player;
        this.connection = connection;

        this.connection.onConnectionClosed(conn -> disconnect());
    }

    public WebSocketConnection.Connection getConnection() {
        return connection;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    private void set() {
    }

    public JSONObject getInfo() {
        JSONObject connectionData = connection.getInfo();
        connectionData.remove("fromClient");
        connectionData.remove("toClient");
        return new JSONObject().put("player", player.serialize())
                               .put("connection", connectionData);
    }

    public void disconnect() {
        BattleManager.getInstance().disconnect(this);
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

}
