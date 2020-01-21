package github.com.suitelhy.dingding.infrastructure.web.util;

import java.util.HashMap;

/**
 * ...
 */
public class Result extends HashMap {

    public static final int SUCCESS = 0;

    public static final int ERROR = -1;

    public Result(int result, String msg) {
        this.put("result", result);
        this.put("msg", msg);
    }

    public static Result success(String msg) {
        return new Result(SUCCESS, msg);
    }

    public static Result error(String msg) {
        return new Result(ERROR, msg);
    }

}
