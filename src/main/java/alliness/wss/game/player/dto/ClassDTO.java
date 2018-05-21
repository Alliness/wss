package alliness.wss.game.player.dto;

import alliness.core.Serializable;
import alliness.wss.game.player.PlayerClassEnum;
import com.google.gson.annotations.SerializedName;

public class ClassDTO extends Serializable {

    @SerializedName("class")
    private PlayerClassEnum playerClass;
}
