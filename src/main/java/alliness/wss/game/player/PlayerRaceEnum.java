package alliness.wss.game.player;

import alliness.core.Dir;
import alliness.core.helpers.FReader;
import org.json.JSONObject;

import java.io.FileNotFoundException;

public enum PlayerRaceEnum {

    HUMAN("human.json"), DWARF("dwarf.json"), ELF("elf.json"), ORC("orc.json");

    public JSONObject json;

    PlayerRaceEnum(String fileName) {
        try {
            json = FReader.readJSON(Dir.RESOURCES + "/players/default/race/" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
