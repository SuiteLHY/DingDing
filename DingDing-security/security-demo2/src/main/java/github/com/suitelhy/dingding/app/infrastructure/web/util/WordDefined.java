package github.com.suitelhy.dingding.app.infrastructure.web.util;

/**
 * 词典国际化
 */
public enum WordDefined {
    SINGLETON;

    public final String LOGIN_USERID_ERROR = "用户名不存在!";
    public final String LOGIN_PASSWORD_ERROR = "密码错误!";
    public final String LOGIN_USERID_DISABLED = "账号已被禁用!";
    public final String LOGIN_NO_LOGED = "未登录!";
    public final String LOGIN_SUCCESS = "登录成功!";
    public final String LOGOUT_SUCCESS = "注销成功!";

    public final /*String*/Integer LOG_TYPE_LOGIN = /*"登陆"*/11;
    public final /*String*/Integer LOG_TYPE_LOGOUT = /*"注销"*/4;
    public final /*String*/Integer LOG_TYPE_ADD = /*"新增"*/1;
    public final /*String*/Integer LOG_TYPE_UPDATE = /*"更新"*/2;
    public final /*String*/Integer LOG_TYPE_DELETE = /*"删除"*/5;
    public final String LOG_TYPE_COMPLETE = "完成";
    public final String LOG_TYPE_IMPORT = "导入";
    public final String LOG_TYPE_EXPORT = "导出";
    public final String LOG_TYPE_DEPLOY = "部署";
    public final String LOG_TYPE_START = "启动";

    public final String LOG_DETAIL_USER_LOGIN = "用户登陆";
    public final String LOG_DETAIL_USER_LOGOUT = "用户退出";
    public final String LOG_DETAIL_UPDATE_PROFILE = "更新用户资料";
    public final String LOG_DETAIL_UPDATE_PROFILEHEAD = "更新用户头像";
    public final String LOG_DETAIL_SYSCONFIG = "系统设置";
    public final String LOG_DETAIL_UPDATE_PASSWORD = "更新密码";

}
