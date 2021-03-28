package github.com.suitelhy.dingding.user.service.api.infrastructure.domain.util;

import github.com.suitelhy.dingding.core.infrastructure.domain.policy.DBPolicy;
import github.com.suitelhy.dingding.core.infrastructure.util.RegexUtil;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Entity 工具类
 *
 * @Description Entity 层 - 业务辅助工具.
 *
 * @author Suite
 */
public final class EntityUtil {

    public static final class Regex {

        /**
         * 通用规则
         *
         * @Description 正则表达式校验 - 通用规则分类.
         */
        public static final class GeneralRule {

            /**
             * 英文单词
             *
             * @Description 英文字母集合的简单校验.
             *
             * @param param       被校验对象
             * @param isUppercase 是否大写
             * @param maxLength   单词的最大长度
             *
             * @return 校验结果
             */
            public /*static*/ boolean englishPhrases(@NotNull String param
                    , @Nullable Boolean isUppercase
                    , @Nullable Integer maxLength)
            {
                if (null != param && (null == maxLength || maxLength > 0)) {
                    String part_lengthRange = (null == maxLength)
                            ? "+"
                            : String.format("{1,%d}", maxLength);
                    if (Boolean.TRUE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern(String.format("^[A-Z]%s$", part_lengthRange))
                                .matcher(param)
                                .matches();
                    } else if (Boolean.FALSE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern(String.format("^[a-z]%s$", part_lengthRange))
                                .matcher(param)
                                .matches();
                    } else {
                        return RegexUtil.getInstance()
                                .getPattern(String.format("^[A-Za-z]%s$", part_lengthRange))
                                .matcher(param)
                                .matches();
                    }
                }
                return false;
            }

            /**
             * 英文单词, 数字
             *
             * @Description 英文单词与数字的集合的简单校验; 交集/并集 取决于参数<param>param</param>.
             *
             * @param param 被校验对象, 不为 null, 非空字符串.
             * @param rule  参数集合
             *              {
             *               "isUppercase": true / false / null; 是否大写, Boolean类型; 缺省 -> null (兼容大写和小写字母).
             *               , "setPrinciple": "intersection" / "union" / null; 集合规则, String类型; 缺省或值为 null -> "union".
             *               , "maxLength": Integer类型, 大于 0; 缺省或值为 null -> 正无穷.
             *              }
             *
             * @return 校验结果
             */
            public /*static*/ boolean englishPhrases_Number(@NotNull String param, @NotNull Map<String, ?> rule)
                    throws IllegalArgumentException {
                //=== 参数合法性校验 ===//

                if (null == param || "".equals(param)) {
                    //-- 非法输入: 被校验对象
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "被校验对象"
                            , param
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == rule) {
                    //-- 非法输入: 参数集合
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "参数集合"
                            , rule
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                //=== <param>rule</param> 参数提取 ===//

                Boolean isUppercase = (null == rule.get("isUppercase"))
                        ? null
                        : Boolean.TRUE.equals(rule.get("isUppercase"));

                Integer maxLength = (rule.get("maxLength") instanceof Integer)
                        ? (Integer) rule.get("maxLength")
                        : null;
                if (null != maxLength && maxLength <= 0) {
                    maxLength = null;
                }

                String setPrinciple = "intersection".equals(rule.get("setPrinciple"))
                        ? "intersection"
                        : "union";

                //======//

                String part_lengthRange = (null == maxLength)
                        ? "+"
                        : "{1,".concat(maxLength.toString()).concat("}");

                if ("intersection".equals(setPrinciple)) {
                    //--- 集合规则: 交集
                    if (Boolean.TRUE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern(String.format("(^[A-Z]%s$)|(^[0-9]%s$)", part_lengthRange, part_lengthRange))
                                .matcher(param)
                                .matches();
                    } else if (Boolean.FALSE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("(^[a-z]".concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[0-9]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    } else {
                        return RegexUtil.getInstance()
                                .getPattern("(^[A-Za-z]".concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[0-9]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    }
                } else {
                    //--- 集合规则: 并集
                    if (Boolean.TRUE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("^[A-Z0-9]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    } else if (Boolean.FALSE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("^[a-z0-9]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    } else {
                        return RegexUtil.getInstance()
                                .getPattern("^[A-Za-z0-9]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    }
                }
            }

            /**
             * 英文单词, 数字, 下划线
             *
             * @Description 英文单词、数字、下划线的集合的简单校验; 交集/并集 取决于参数<param>param</param>.
             *
             * @param param 被校验对象, 不为 null, 非空字符串.
             * @param rule  参数集合; 说明:
             *              {
             *              "isUppercase": true / false / null; 是否大写, Boolean类型; 缺省 -> null (兼容大写和小写字母).
             *              , "setPrinciple": "intersection" / "union" / null; 集合规则, String类型; 缺省或值为 null -> "union".
             *              , "maxLength": Integer类型, 大于 0; 缺省或值为 null -> 正无穷.
             *              }
             *
             * @return 校验结果
             */
            public /*static*/ boolean englishPhrases_Number_Underscore(@NotNull String param
                    , @NotNull Map<String, ?> rule) {
                //=== 参数合法性校验 ===//

                if (null == param || "".equals(param)) {
                    //-- 非法输入: 被校验对象
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "被校验对象"
                            , param
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == rule) {
                    //-- 非法输入: 参数集合
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "参数集合"
                            , rule
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                //=== <param>rule</param> 参数提取 ===//

                Boolean isUppercase = (null == rule.get("isUppercase"))
                        ? null
                        : Boolean.TRUE.equals(rule.get("isUppercase"));

                Integer maxLength = rule.get("maxLength") instanceof Integer
                        ? (Integer) rule.get("maxLength")
                        : null;
                if (null != maxLength && maxLength <= 0) {
                    maxLength = null;
                }

                String setPrinciple = "intersection".equals(rule.get("setPrinciple"))
                        ? "intersection"
                        : "union";

                //======//

                String part_lengthRange = (null == maxLength)
                        ? "+"
                        : String.format("{1,%d}", maxLength);

                if ("intersection".equals(setPrinciple)) {
                    //--- 集合规则: 交集
                    if (Boolean.TRUE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("(^[A-Z_]".concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[0-9_]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    } else if (Boolean.FALSE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("(^[a-z_]".concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[0-9_]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    } else {
                        return RegexUtil.getInstance()
                                .getPattern("(^[A-Za-z_]".concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[0-9_]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    }
                } else {
                    //--- 集合规则: 并集
                    if (Boolean.TRUE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("^[A-Z0-9_]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    } else if (Boolean.FALSE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("^[a-z0-9_]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    } else {
                        return RegexUtil.getInstance()
                                .getPattern("^[A-Za-z0-9_]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    }
                }
            }

            /**
             * 基础的一维数组
             *
             * @param param 被校验对象; 非 {@code null}, 非空字符串.
             * @return 校验结果
             * @Description 一维数组, 且仅限于存放[值对象]的数组. 存值包括自然数、大小写英文、汉字.
             */
            public boolean oneDimensionalArray(@NotNull String param) {
                return RegexUtil.getInstance()
                        .getPattern("^\\[(([a-zA-z0-9\\u4e00-\\u9fa5]+)(,[\\s]?[a-zA-z0-9\\u4e00-\\u9fa5]+)*)?\\]$")
                        .matcher(Objects.requireNonNull(param))
                        .matches();
            }

            /**
             * 中文, 英文单词, 数字
             *
             * @param param 被校验对象, 不为 null, 非空字符串.
             * @param rule  参数集合; 说明:
             *              {
             *              "isUppercase": true / false / null; 是否大写, Boolean类型; 缺省 -> null.
             *              , "setPrinciple": "intersection" / "union" / null; 集合规则, String类型; 缺省或值为 null -> "union".
             *              , "maxLength": Integer类型, 大于 0; 缺省或值为 null -> 正无穷.
             *              }
             * @return 校验结果
             * @Description 中文、英文单词与数字的集合的简单校验; 交集/并集 取决于参数<param>param</param>.
             */
            public /*static*/ boolean chinese_EnglishPhrases_Number(@NotNull String param
                    , @NotNull Map<String, ?> rule) {
                //=== 参数合法性校验 ===//

                if (null == param || "".equals(param)) {
                    //-- 非法输入: 被校验对象
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "被校验对象"
                            , param
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == rule) {
                    //-- 非法输入: 参数集合
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "参数集合"
                            , rule
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                //=== <param>rule</param> 参数提取 ===//

                Boolean isUppercase = (null == rule.get("isUppercase"))
                        ? null
                        : Boolean.TRUE.equals(rule.get("isUppercase"));

                Integer maxLength = rule.get("maxLength") instanceof Integer
                        ? (Integer) rule.get("maxLength")
                        : null;
                if (null != maxLength && maxLength <= 0) {
                    maxLength = null;
                }

                String setPrinciple = "intersection".equals(rule.get("setPrinciple"))
                        ? "intersection"
                        : "union";
                //======//

                String part_lengthRange = (null == maxLength)
                        ? "+"
                        : "{1,".concat(maxLength.toString()).concat("}");

                if ("intersection".equals(setPrinciple)) {
                    //--- 集合规则: 交集
                    if (Boolean.TRUE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("(^[\\u4e00-\\u9fa5]".concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[A-Z]").concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[0-9]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    } else if (Boolean.FALSE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern(
                                        "(^[\\u4e00-\\u9fa5]".concat(part_lengthRange).concat("$)")
                                                .concat("|")
                                                .concat("(^[a-z]").concat(part_lengthRange).concat("$)")
                                                .concat("|")
                                                .concat("(^[0-9]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    } else {
                        return RegexUtil.getInstance()
                                .getPattern("(^[\\u4e00-\\u9fa5]".concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[A-Za-z]").concat(part_lengthRange).concat("$)")
                                        .concat("|")
                                        .concat("(^[0-9]").concat(part_lengthRange).concat("$)"))
                                .matcher(param)
                                .matches();
                    }
                } else {
                    //--- 集合规则: 并集
                    if (Boolean.TRUE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("^[\\u4e00-\\u9fa5A-Z0-9]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    } else if (Boolean.FALSE.equals(isUppercase)) {
                        return RegexUtil.getInstance()
                                .getPattern("^[\\u4e00-\\u9fa5a-z0-9]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    } else {
                        return RegexUtil.getInstance()
                                .getPattern("^[\\u4e00-\\u9fa5A-Za-z0-9]".concat(part_lengthRange).concat("$"))
                                .matcher(param)
                                .matches();
                    }
                }
            }

            /**
             * URL
             *
             * @param param 被校验对象
             * @return 校验结果
             * @Description 较为严格的校验.
             * @Reference <a href="https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url">
             * ->     javascript - What is a good regular expression to match a URL? - Stack Overflow</a>
             * ->     , <a href="https://blog.walterlv.com/post/match-web-url-using-regex.html">使用正则表达式尽可能准确匹配域名/网址</a>
             * ->     , (相关知识) <a href="https://www.cnblogs.com/panpanwelcome/p/7464511.html">正则匹配-URL-域名 - 晴天彩虹 - 博客园</a>
             */
            public /*static*/ boolean url(@NotNull String param) {
                return RegexUtil.getInstance()
                        .getPattern("[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
                        .matcher(Objects.requireNonNull(param))
                        .matches();
            }

            /**
             * URL 的 Path 部分
             *
             * @param param 被校验对象
             * @return 校验结果
             * @Description 较为宽松的校验.
             * @Reference {@link <a href="https://stackoverflow.com/questions/27745/getting-parts-of-a-url-regex">language agnostic - Getting parts of a URL (Regex) - Stack Overflow</a>}
             * {@link <a href="https://tools.ietf.org/html/rfc3986#appendix-B">RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax</a>}
             */
            public /*static*/ boolean urlPath(@NotNull String param) {
                return RegexUtil.getInstance()
                        .getPattern("\\/([^?#]*)(\\?([^#]*))?(#(.*))?")
                        .matcher(Objects.requireNonNull(param))
                        .matches();
            }

            // (单例模式 - 登记式)
            private static class Factory {
                private static final GeneralRule SINGLETON = new GeneralRule();
            }

            private GeneralRule() {
            }

            @NotNull
            public static GeneralRule getInstance() {
                return Factory.SINGLETON;
            }

        }

        /**
         * 校验 -> Entity 属性名称
         *
         * @param fieldName 被校验对象
         * @return 校验结果
         * @Description 使用正则表达式校验
         */
        public static boolean validateFieldName(@Nullable String fieldName) {
            return null != fieldName
                    && RegexUtil.getInstance().getPattern("^[a-z][A-Za-z0-9]+$").matcher(fieldName).matches();
        }

        /**
         * 校验 -> Entity 唯一标识 (ID)
         *
         * @Description Entity - ID 校验通用规范. 至多32位的16(或以下)进制数字
         *
         * @param id 被校验对象
         *
         * @return 校验结果
         */
        public static boolean validateId(@NotNull String id) {
            return null != id
                    && (RegexUtil.getInstance().getPattern("^[a-zA-Z0-9]{1,32}$").matcher(id).matches() || DBPolicy.MYSQL.validateUuid(id));
        }

        /**
         * 校验 -> 用户名称
         *
         * @Description 用户名称 <- 规则: <tt>大小写字母 | 数字(无符号) | 中文字符</tt>, 长度范围 [1,30].
         *
         * @param username 被校验对象
         *
         * @return 校验结果
         *
         * @see User.Validator#USER#validateUsername(String)
         */
        public static boolean validateUsername(@Nullable String username) {
            return null != username
                    && RegexUtil.getInstance()
                    .getPattern("^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,30}$")
                    .matcher(username)
                    .matches();
        }

        /**
         * 校验 -> 用户密码（明文）
         *
         * @Description 用户密码（明文）规则: 以字母开头, 长度在6~18之间, 只能包含字母、数字和下划线.
         *
         * @param passwordPlaintext 被校验对象
         *
         * @return 校验结果
         *
         * @see User.Validator#USER#passwordPlaintext(String)
         */
        public static boolean validateUserPasswordPlaintext(@Nullable String passwordPlaintext) {
            return null != passwordPlaintext
                    && RegexUtil.getInstance().getPattern("^[a-zA-Z]\\w{5,17}$").matcher(passwordPlaintext).matches();
        }

        /**
         * 校验 -> 用户密码（密文）
         *
         * @Description 用户密码（密文）.
         *
         * @param password 被校验对象
         *
         * @return 校验结果
         *
         * @see User.Validator#USER#password(String)
         */
        public static boolean validateUserPassword(@Nullable String password) {
            return null != password
                    && RegexUtil.getInstance()
                    .getPattern(User.SecurityStrategy.PasswordEncoder.INSTANCE.encoderVo.RegexPattern)
                    .matcher(password)
                    .matches();
        }

        private Regex() {
        }

    }

//    /**
//     * 通过反射执行指定方法
//     *
//     * @param entity            实体对象
//     * @param fieldName         字段名称
//     * @param parameterTypes    参数类型 (数组)
//     * @param args              参数对象 (数组)
//     * @param <T>               (泛型) {@link EntityModel}的派生类型
//     *
//     * @return 所执行的方法的返回值 - 可为 null, 此时成功执行方法且方法返回值为 null; 或者方法执行失败且由 <code>invokeMethod</code> 方法处理异常.
//     */
//    public static <T extends EntityModel> Object invokeMethod(@NotNull T entity
//            , @NotNull String fieldName
//            , @Nullable Class<?>[] parameterTypes
//            , @Nullable Object[] args)
//    {
//        Object result;
//        Method method;
//        try {
//            method = entity.getClass().getMethod(fieldName, parameterTypes);
//            result = method.invoke(entity, args);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException("反射调用工具 - 通过反射执行指定方法出错", e);
//        }
//        return result;
//    }

    private EntityUtil() {
    }

}
