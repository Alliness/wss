package alliness.wss.game.interfaces;

import alliness.wss.game.GameException;
import alliness.wss.game.player.Avatar;

public interface GameRoomManager {

    void addAvatar(Avatar avatar);

    boolean removeFromRoom(Avatar avatar);
}
