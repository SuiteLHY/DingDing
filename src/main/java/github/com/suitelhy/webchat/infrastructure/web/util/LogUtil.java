package github.com.suitelhy.webchat.infrastructure.web.util;

import github.com.suitelhy.webchat.domain.entity.Log;
import github.com.suitelhy.webchat.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.webchat.infrastructure.domain.vo.HandleTypeVo;

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
        return Log.Factory.USER_LOG.create(detail
                , ip
                , time
                , VoUtil.getVoByValue(HandleTypeVo.Log.class, type)
                , userid);
    }

}
