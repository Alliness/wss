package alliness.wss.game.player;

import alliness.core.Serializable;
import alliness.wss.game.player.dto.ClassDTO;
import alliness.wss.game.player.dto.PlayerDTO;
import alliness.wss.game.player.dto.RaceDTO;
import org.json.JSONObject;

public class PlayerEntity extends Serializable {

    PlayerDTO player;
    ClassDTO playerClass;
    RaceDTO race;

    public PlayerEntity(PlayerDTO player, ClassDTO playerClass, RaceDTO playerRace) {
        this.player = player;
        this.playerClass = playerClass;
        this.race = playerRace;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public ClassDTO getPlayerClass() {
        return playerClass;
    }

    public RaceDTO getRace() {
        return race;
    }

    @Override
    public String toString() {
        return serialize().toString();
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = super.serialize();
        object.put("race", race.serialize())
                .put("class", playerClass.serialize());
        return object;
    }
}
