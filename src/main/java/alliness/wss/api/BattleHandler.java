package alliness.wss.api;

import alliness.wss.game.battle.BattleManager;
import spark.Request;
import spark.Response;

public class BattleHandler extends AbstractApiHandler{


    public static Object getInfo(Request request, Response response) {
        return BattleManager.getInstance().getInfo();
    }
}
