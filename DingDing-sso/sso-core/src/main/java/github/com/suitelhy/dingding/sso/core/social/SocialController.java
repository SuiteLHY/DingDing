package github.com.suitelhy.dingding.sso.core.social;

import github.com.suitelhy.dingding.sso.core.social.support.SocialUserInfo;
import org.springframework.social.connect.Connection;

/**
 * @author zhailiang
 */
public abstract class SocialController {

    /**
     * 根据Connection信息构建SocialUserInfo
     *
     * @param connection
     * @return
     */
    protected SocialUserInfo buildSocialUserInfo(Connection<?> connection) {
        SocialUserInfo userInfo = new SocialUserInfo();
        userInfo.setProviderId(connection.getKey().getProviderId());
        userInfo.setProviderUserId(connection.getKey().getProviderUserId());
        userInfo.setNickname(connection.getDisplayName());
        userInfo.setHeadImg(connection.getImageUrl());
        return userInfo;
    }

}
