package github.com.suitelhy.dingding.app.infrastructure.web.util;

import github.com.suitelhy.dingding.app.domain.entity.Log;
import github.com.suitelhy.dingding.app.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.app.infrastructure.domain.vo.HandleType;

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
                , VoUtil.getVoByValue(HandleType.LogVo.class, type)
                , userid);
    }

}
