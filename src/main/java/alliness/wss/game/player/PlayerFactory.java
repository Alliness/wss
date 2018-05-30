package alliness.wss.game.player;

import alliness.core.Serializable;
import alliness.core.helpers.FWriter;
import alliness.wss.game.player.dto.PlayerDTO;
import org.json.JSONObject;

import java.io.File;

public class PlayerFactory {

    private Player player;
    private PlayerDTO dto;
    private PlayerRaceEnum playerRace;
    private PlayerClassEnum playerClass;
    private String name;

    public PlayerFactory(PlayerDTO dto) {
        this.dto = dto;
    }

    public PlayerFactory setName(String name) {
        this.name = name;
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

    public Player build() {
        JSONObject object = dto.serialize();
        for (String s : playerRace.json.keySet()) {
            object.put(s, playerRace.json.get(s));
        }

        for (String s : playerClass.json.keySet()) {
            object.put(s, playerClass.json.get(s));
        }

        dto = Serializable.deserialize(object, PlayerDTO.class);

        player = new Player(dto);
        player.setName(name);
        return player;
    }

    public void save(File file) {
        FWriter.writeToFile(player.serialize(), file);
    }
}
