package alliness.wss.game.player;

import alliness.wss.game.GameException;

public enum BodyPartEnum {

    HEAD(1),
    TORSO(2),
    LEGS(3);

    public int part;

    BodyPartEnum(int part) {
        this.part = part;
    }

    public static BodyPartEnum getPart(int part) throws GameException {
        for (BodyPartEnum bodyPartEnum : BodyPartEnum.values()) {
            if (bodyPartEnum.part == part) {
                return bodyPartEnum;
            }
        }
        throw new GameException(String.format("unable to get body part with number %s", part));
    }
}
