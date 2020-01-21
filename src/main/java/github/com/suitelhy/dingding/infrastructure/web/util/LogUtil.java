package github.com.suitelhy.dingding.infrastructure.web.util;

import github.com.suitelhy.dingding.domain.entity.Log;
import github.com.suitelhy.dingding.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.infrastructure.domain.vo.HandleTypeVo;

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
