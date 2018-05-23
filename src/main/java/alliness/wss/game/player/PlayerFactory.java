package alliness.wss.game.player;

import alliness.core.Serializable;
import alliness.core.helpers.FWriter;
import alliness.wss.game.player.dto.PlayerDTO;
import org.json.JSONObject;

import java.io.File;

public class PlayerFactory {

    private PlayerDTO       player;
    private PlayerRaceEnum  playerRace;
    private PlayerClassEnum playerClass;

    public PlayerFactory(PlayerDTO dto) {
        this.player = dto;
    }

    public PlayerFactory setName(String name) {
        player.setName(name);
        return this;
    }

    public PlayerFactory setPlayerRace(PlayerRaceEnum playerRace) {
        this.playerRace = playerRace;
        return this;
    }

    public PlayerFactory setPlayerClass(PlayerClassEnum playerClass) {
        this.playerClass = playerClass;
        return this;
    }

    public PlayerDTO build() {
        JSONObject object = player.serialize();
        for (String s : playerRace.json.keySet()) {
            object.put(s, playerRace.json.get(s));
        }

        for (String s : playerClass.json.keySet()) {
            object.put(s, playerClass.json.get(s));
        }

        player = Serializable.deserialize(object, PlayerDTO.class);
        return player;
    }

    public void save(File file) {
        FWriter.writeToFile(player.serialize(), file);
    }
}
