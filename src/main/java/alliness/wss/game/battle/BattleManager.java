package alliness.wss.game.battle;

import alliness.wss.game.player.Avatar;

import java.util.LinkedList;
import java.util.List;

public class BattleManager {

    private List<Avatar> avatars;
    private Avatar activePlayer;

    public BattleManager(){
        avatars = new LinkedList<>();
    }

    public void addAvatar(Avatar avatar){
        if(avatars.contains(avatar)){
            return;
        }
        avatars.add(avatar);
    }

}
