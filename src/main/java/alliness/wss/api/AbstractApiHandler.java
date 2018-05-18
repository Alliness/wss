package alliness.wss.api;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public abstract class AbstractApiHandler {

    protected static final Logger log = Logger.getLogger(AbstractApiHandler.class);


    static JSONObject jsonFailMessage(Object message) {
        return new JSONObject().put("success", false).put("message", message);
    }

    static JSONObject jsonFailMessage(JSONObject json) {
        return json.put("success", false);
    }

    static JSONObject jsonSuccessMessage(String key, Object message) {
        return new JSONObject().put("success", true).put(key, message);
    }

    static JSONObject jsonSuccessMessage(JSONObject json) {
        return json.put("success", true);
    }


}
