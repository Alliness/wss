package alliness.wss.game.player;

import alliness.wss.game.player.dto.ClassDTO;
import alliness.wss.game.player.dto.PlayerDTO;
import alliness.wss.game.player.dto.RaceDTO;

public class PlayerFactory {

    private PlayerDTO player;
    private RaceDTO playerRace;
    private ClassDTO playerClass;

    public PlayerFactory(PlayerDTO dto) {
        this.player = dto;
    }

    public PlayerFactory setName(String name){
        player.setName(name);
        return this;
    }

    public PlayerFactory setPlayerRace(RaceDTO playerRace) {
        this.playerRace = playerRace;
        return this;
    }

    public PlayerFactory setPlayerClass(ClassDTO playerClass) {
        this.playerClass = playerClass;
        return this;
    }

    public PlayerEntity build(){
        return new PlayerEntity(player, playerClass, playerRace);
    }

    public void save(){

    }
}
