package github.com.suitelhy.webchat.infrastructure.web.util;

import github.com.suitelhy.webchat.domain.entity.Log;
import github.com.suitelhy.webchat.domain.vo.HandleTypeVo;

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
        HandleTypeVo.Log typeVo = null;
        for (HandleTypeVo.Log each : HandleTypeVo.Log.class.getEnumConstants()) {
            if (each.equalsValue(type)) {
                typeVo = each;
                break;
            }
        }
        return Log.Factory.USER_LOG.create(detail
                , ip
                , time
                , typeVo
                , userid);
    }

}
