package github.com.suitelhy.webchat.infrastructure.domain.vo;

import github.com.suitelhy.webchat.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.webchat.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

/**
 * 操作类型
 *
 */
public interface HandleTypeVo<VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 日志记录 - 操作类型
     */
    enum Log implements HandleTypeVo<Log, Integer, String> {
        USER_REGISTRATION(1
                , "用户注册"
                , "正常业务流程, 注册用户")
        , USER_UPDATE(2
                , "用户更新"
                , "正常业务流程, 更新用户信息")
        , USER_ABNORMALITY(3
                , "用户异常"
                , "非正常业务流程, 或者脏数据等情况; 该账户异常, 很可能出现了严重的问题, 应该被禁用")
        , USER_DESTRUCTION(4
                , "用户销毁"
                , "正常业务流程, 用户变为\"已销毁\"状态; \"已销毁\"状态的用户应该被禁用且不应该出现在用户列表之中")
        , DATA_USER_DELETION(5
                , "用户数据删除"
                , "用户的数据被删除, 或确认数据丢失")
        , USER_LOGIN(11
                , "用户登入"
                , "正常业务流程, 用户登入系统")
        , USER_LOGGED_OUT(12
                , "用户登出"
                , "正常业务流程, 用户登出系统");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<Log, Integer, String> {

            public Converter() {
                super(Log.class);
            }

        }

        public final Integer code;

        public final String name;

        public final String description;

        Log(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        /**
         * VO 的值
         *
         * @return
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * 备注: <method>equals(Object)</method>
         * @param value
         * @return
         */
        public boolean equals(@Nullable Integer value) {
            return equalsValue(value);
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

    }

}
