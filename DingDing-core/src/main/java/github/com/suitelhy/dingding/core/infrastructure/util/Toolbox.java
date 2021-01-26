package github.com.suitelhy.dingding.core.infrastructure.util;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

//interface Tool<T> {
//
//    /**
//     * 获取工具类实例
//     *
//     * @Description (枚举类的抽象方法)
//     *
//     * @return {@link Toolbox} 的枚举值所声明的工具类实例对象
//     */
//    @NotNull
//    T getInstance();
//
//}

/**
 * 工具整合
 *
 * @Design 整合项目中需要使用的各种工具类.
 *
 * @author Suite
 */
public /*enum*/interface Toolbox {

//    /**
//     * 正则表达式 - 常规匹配
//     *
//     * @Description 业务中较为普遍使用的正则校验.
//     *
//     * @see EntityUtil.Regex.GeneralRule
//     */
//    REGEX__REGULAR_MATCH {
//        @Override
//        public @NotNull Class<EntityUtil.Regex.GeneralRule> getInstance() {
//            return EntityUtil.Regex.GeneralRule.class;
//        }
//    },
//    /**
//     * 正则表达式工具
//     *
//     * @see RegexUtil
//     */
//    REGEX_UTIL {
//        @Override
//        public @NotNull Class<RegexUtil> getInstance() {
//            return RegexUtil.class;
//        }
//    },
//    /**
//     * ValueObject 的业务辅助工具
//     *
//     * @see VoUtil
//     * @see github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel
//     */
//    VO_UTIL {
//        @Override
//        public @NotNull Class<VoUtil> getInstance() {
//            return VoUtil.class;
//        }
//    };
//
//    /**
//     * 获取工具类实例
//     *
//     * @Description (枚举类的抽象方法)
//     *
//     * @return {@link ToolboxEnum} 的枚举值所声明的工具类实例对象
//     */
//    @NotNull
//    public abstract <T> T getInstance();

    /**
     * 数组工具
     *
     * @Description 数组类型数据的工具类.
     *
     * @see github.com.suitelhy.dingding.core.infrastructure.util.ArrayUtil
     */
    final class ArrayUtil {

        public static class OneDimensionalArray {

            public static @NotNull github.com.suitelhy.dingding.core.infrastructure.util.ArrayUtil.OneDimensionalArrayUtil getInstance() {
                return github.com.suitelhy.dingding.core.infrastructure.util.ArrayUtil.OneDimensionalArrayUtil.getInstance();
            }

            private OneDimensionalArray() {}

        }

        public static @NotNull github.com.suitelhy.dingding.core.infrastructure.util.ArrayUtil getInstance() {
            return github.com.suitelhy.dingding.core.infrastructure.util.ArrayUtil.getInstance();
        }

        private ArrayUtil() {}

    }

    /**
     * 网络工具
     *
     * @Description 网络工具类.
     *
     * @see github.com.suitelhy.dingding.core.infrastructure.web.util.NetUtil
     */
    final class NetUtil {

        public static @NotNull github.com.suitelhy.dingding.core.infrastructure.web.util.NetUtil getInstance() {
            return github.com.suitelhy.dingding.core.infrastructure.web.util.NetUtil.getInstance();
        }

        private NetUtil() {}

    }

    /**
     * 正则表达式 - 常用校验模板
     *
     * @Description 业务中较为普遍使用的正则校验.
     *
     * @see github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil.Regex.GeneralRule
     */
    final class RegexGeneralRule {

        public static @NotNull github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil.Regex.GeneralRule getInstance() {
            return github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil.Regex.GeneralRule.getInstance();
        }

        private RegexGeneralRule() {}

    }

    /**
     * 正则表达式 - 基础工具类
     *
     * @Description [正则表达式 - 基础工具类]的整合, 适用于需要直接使用[符合 {@link Pattern} 格式的字符串]的场景.
     * · 【说明】{@link RegexUtil#getInstance()} 使用了[正则表达式模板的缓存池]以减少不必要的系统开销.
     *
     * @see github.com.suitelhy.dingding.core.infrastructure.util.RegexUtil
     */
    final class RegexUtil {

        public static @NotNull github.com.suitelhy.dingding.core.infrastructure.util.RegexUtil getInstance() {
            return github.com.suitelhy.dingding.core.infrastructure.util.RegexUtil.getInstance();
        }

        private RegexUtil() {}

    }

    /**
     * ValueObject 对象的业务辅助工具
     *
     * @Description 用于 {@link github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel} 的实例对象.
     *
     * @see github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil
     * @see github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel
     */
    final class VoUtil {

        public static @NotNull github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil getInstance() {
            return github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil.getInstance();
        }

        private VoUtil() {}

    }



}
