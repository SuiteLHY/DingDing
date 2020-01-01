package github.com.suitelhy.webchat.infrastructure.web.util;

import github.com.suitelhy.webchat.domain.entity.Log;

public class LogUtil {

    /**
     *
     * @param userid
     * @param time
     * @param type
     * @param detail
     * @param ip
     * @return
     */
    public Log setLog(String userid
            , String time
            , Integer type
            , String detail
            , String ip) {
        Log log = Log.Factory.SINGLETON.create();
        log.setUserid(userid);
        log.setTime(time);
        log.setType(type);
        log.setDetail(detail);
        log.setIp(ip);
        return log;
    }

}
