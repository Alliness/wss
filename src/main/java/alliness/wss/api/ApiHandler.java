package alliness.wss.api;

import spark.Request;
import spark.Response;

class ApiHandler extends AbstractApiHandler {


    public static Object info(Request request, Response response) {
        return jsonSuccessMessage("status", "GREEN");
    }
}
