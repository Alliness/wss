package alliness.wss.game.player;

import alliness.core.Dir;
import alliness.core.helpers.FReader;
import org.json.JSONObject;

import java.io.FileNotFoundException;

public enum PlayerClassEnum {

    WIZARD("Wizard.json"), KNIGHT("knight.json"), ARCHER("archer.json");


    public JSONObject json;

    PlayerClassEnum(String fileName) {
        try {
            json = FReader.readJSON(Dir.RESOURCES + "/players/default/class/" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
