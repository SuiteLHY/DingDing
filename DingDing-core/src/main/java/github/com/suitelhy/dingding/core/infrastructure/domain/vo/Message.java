package github.com.suitelhy.dingding.core.infrastructure.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;

import javax.validation.constraints.NotNull;

///**
// * 消息 VO
// *
// * @Description 通过枚举和接口来组织枚举
// * @Update 弃用; 通过枚举和接口来组织(下一层)枚举发现实现过于复杂, 不符合开发要求(效率 &-> 质量)
// */
//public enum MessageVo implements VoModel<MessageVo, Integer, String> {
//    CHAT_MESSAGE(1, "聊天消息", "两个用户之间的聊天消息", Status.ChatMessageStatusVo.class)
//    , GROUP_CHAT_MESSAGE(2, "群聊消息", "多个用户之间，一对多的聊天消息", Status.GroupChatMessageStatusVo.class);
//
//    private final int code;
//
//    @NotNull
//    private final String name;
//
//    @Nullable
//    private final String description;
//
//    @NotNull
//    public final Status[] statusVoEnums;
//
//    <_STATUS extends Enum & Status> MessageVo(int code
//            , @NotNull String name
//            , @Nullable String description
//            , @NotNull Class<_STATUS> statusVoClazz) {
//        this.code = code;
//        this.name = name;
//        this.description = description;
//        this.statusVoEnums = statusVoClazz.getEnumConstants();
//    }
//
//    /**
//     * VO 的值
//     *
//     * @return 可为 <code>null</code>
//     * @Description Unique attribute.
//     */
//    @NotNull
//    @Override
//    public Integer value() {
//        return this.code;
//    }
//
//    /**
//     * VO 的详细信息
//     *
//     * @return
//     */
//    @Override
//    public String description() {
//        return this.description;
//    }
//
//    /**
//     * VO 的 (展示)名称
//     *
//     * @return
//     */
//    @Override
//    public @NotNull String showName() {
//        return this.name;
//    }
//
//    //===== (通过接口组织枚举) =====//
//    public interface Status<_VO extends Enum & VoModel<_VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
//            extends VoModel<_VO, V, _DESCRIPTION> {
//
//        /**
//         * 用户 - 聊天消息 <- 状态
//         * @Description 点对点聊天消息
//         */
//        enum ChatMessageStatusVo implements Status<ChatMessageStatusVo, Integer, String> {
//            UNSENT(0, "未发送", "消息未成功发送。")
//            , SENT(1, "已发送", "消息已成功发送，对方未阅读。")
//            , READ(2, "已阅读", "消息已成功发送，对方已阅读。");
//
//            private final int code;
//
//            @NotNull
//            private final String name;
//
//            @NotNull
//            private final String description;
//
//            ChatMessageStatusVo(int code, String name, String description) {
//                this.code = code;
//                this.name = name;
//                this.description = description;
//            }
//
//            /**
//             * VO 的值
//             *
//             * @return 可为 <code>null</code>
//             * @Description Unique attribute.
//             */
//            @Override
//            public Integer value() {
//                return this.code;
//            }
//
//            /**
//             * VO 的详细信息
//             *
//             * @return
//             */
//            @Override
//            public String description() {
//                return this.description;
//            }
//
//            /**
//             * VO 的 (展示)名称
//             *
//             * @return
//             */
//            @Override
//            public @NotNull String showName() {
//                return this.name;
//            }
//
//            /**
//             * Returns the name of this enum constant, as contained in the
//             * declaration.  This method may be overridden, though it typically
//             * isn't necessary or desirable.  An enum type should override this
//             * method when a more "programmer-friendly" string form exists.
//             *
//             * @return the name of this enum constant
//             */
//            @Override
//            public String toString() {
//                return VoModel.toString(this);
//            }
//
//        }
//
//        /**
//         * 用户 - 群聊消息 <- 状态
//         * @Description 点对线聊天消息
//         */
//        enum GroupChatMessageStatusVo implements Status<GroupChatMessageStatusVo, Integer, String> {
//            UNSENT(0, "未发送", "消息未成功发送，群聊中的所有用户都未接收到此消息。")
//            , PARTIAL_SEND(1, "部分发送", "消息已成功发送给群聊中的部分用户。")
//            , SENT(2, "已发送", "消息已成功发送，群聊中的所有用户都已接收到此消息。")
//            , PARTIAL_READ(3, "部分已读", "消息已成功发送，群聊中的部分用户已阅读此消息。")
//            , READ(4, "全部已读", "消息已成功发送，群聊中的所有用户都已阅读此消息。");
//
//            private final int code;
//
//            @NotNull
//            private final String name;
//
//            @NotNull
//            private final String description;
//
//            GroupChatMessageStatusVo(int code
//                    , @NotNull String name
//                    , @NotNull String description) {
//                this.code = code;
//                this.name = name;
//                this.description = description;
//            }
//
//            /**
//             * VO 的值
//             *
//             * @return 可为 <code>null</code>
//             * @Description Unique attribute.
//             */
//            @Override
//            public Integer value() {
//                return this.code;
//            }
//
//            /**
//             * VO 的详细信息
//             *
//             * @return
//             */
//            @Override
//            public String description() {
//                return this.description;
//            }
//
//            /**
//             * VO 的 (展示)名称
//             *
//             * @return
//             */
//            @Override
//            public @NotNull String showName() {
//                return this.name;
//            }
//
//            /**
//             * Returns the name of this enum constant, as contained in the
//             * declaration.  This method may be overridden, though it typically
//             * isn't necessary or desirable.  An enum type should override this
//             * method when a more "programmer-friendly" string form exists.
//             *
//             * @return the name of this enum constant
//             */
//            @Override
//            public String toString() {
//                return VoModel.toString(this);
//            }
//
//        }
//
//    }
//    //==========//
//
//}

/**
 * 消息 VO <- 接口
 *
 * @param <VO>
 * @param <V>
 * @param <_DESCRIPTION>
 */
public interface Message<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    enum ChatMessageVo
            implements VoModel<ChatMessageVo, Integer, String> {
        CHAT_MESSAGE(1
                , "聊天消息"
                , "两个用户之间的聊天消息");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<ChatMessageVo, Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(ChatMessageVo.class);
            }

            public static @NotNull Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        private final int code;

        private final @NotNull String name;

        private final @NotNull String description;

        ChatMessageVo(int code, @NotNull String name, @NotNull String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        /**
         * VO 的值
         *
         * @Description Unique attribute.
         *
         * @return 可为 <code>null</code>
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * VO 的详细信息
         *
         * @return
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * VO 的 (展示)名称
         *
         * @return
         */
        @Override
        public @NotNull String displayName() {
            return this.name;
        }

        /**
         * Returns the name of this enum constant, as contained in the
         * declaration.  This method may be overridden, though it typically
         * isn't necessary or desirable.  An enum type should override this
         * method when a more "programmer-friendly" string form exists.
         *
         * @return the name of this enum constant
         */
        @Override
        public String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         */
        @Override
        @SuppressWarnings("unchecked")
        public @NotNull Converter voAttributeConverter() {
            return Converter.getInstance();
        }

        //===== 一对多关联 (使用接口管理枚举) =====//
        public interface Status<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
                extends VoModel<VO, V, _DESCRIPTION> {

            enum AcceptStatusVo
                    implements Status<AcceptStatusVo, Integer, String> {
                UNSENT(0
                        , "未发送"
                        , "消息未成功发送。"), SENT(1
                        , "已发送"
                        , "消息已成功发送，对方未阅读。"), READ(2
                        , "已阅读"
                        , "消息已成功发送，对方已阅读。");

                /**
                 * 为持久化类型转换器提供支持
                 */
                @javax.persistence.Converter(autoApply = true)
                public static class Converter
                        extends VoAttributeConverter<AcceptStatusVo, Integer, String> {

                    // (单例模式 - 登记式)
                    private static class Factory {
                        private static final Converter SINGLETON = new Converter();
                    }

                    public Converter() {
                        super(AcceptStatusVo.class);
                    }

                    @NotNull
                    public static Converter getInstance() {
                        return Converter.Factory.SINGLETON;
                    }

                }

                private final int code;

                @NotNull
                private final String name;

                @NotNull
                private final String description;

                AcceptStatusVo(int code
                        , @NotNull String name
                        , @NotNull String description) {
                    this.code = code;
                    this.name = name;
                    this.description = description;
                }

                /**
                 * VO 的值
                 *
                 * @return 可为 <code>null</code>
                 * @Description Unique attribute.
                 */
                @Override
                public Integer value() {
                    return this.code;
                }

                /**
                 * VO 的详细信息
                 *
                 * @return
                 */
                @Override
                public String description() {
                    return this.description;
                }

                /**
                 * VO 的 (展示)名称
                 *
                 * @return
                 */
                @Override
                public @NotNull
                String displayName() {
                    return this.name;
                }

                /**
                 * Returns the name of this enum constant, as contained in the
                 * declaration.  This method may be overridden, though it typically
                 * isn't necessary or desirable.  An enum type should override this
                 * method when a more "programmer-friendly" string form exists.
                 *
                 * @return the name of this enum constant
                 */
                @Override
                public String toString() {
                    return VoModel.toString(this);
                }

                /**
                 * 提供类型转换器
                 *
                 * @Design 为持久化类型转换功能提供支持.
                 */
                @Override
                @NotNull
                public Converter voAttributeConverter() {
                    return Converter.getInstance();
                }

            }

        }
        //==========//

    }

    enum GroupChatMessageVo
            implements VoModel<GroupChatMessageVo, Integer, String> {
        GROUP_CHAT_MESSAGE(2
                , "群聊消息"
                , "多个用户之间，一对多的聊天消息");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<GroupChatMessageVo, Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final @NotNull Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(GroupChatMessageVo.class);
            }

            public static @NotNull Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        private final int code;

        private final @NotNull String name;

        private final @NotNull String description;

        GroupChatMessageVo(int code, @NotNull String name, @NotNull String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        /**
         * VO 的值
         *
         * @Description Unique attribute.
         *
         * @return 可为 <code>null</code>
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * VO 的详细信息
         *
         * @return
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * VO 的 (展示)名称
         *
         * @return
         */
        @Override
        public @NotNull String displayName() {
            return this.name;
        }

        /**
         * Returns the name of this enum constant, as contained in the
         * declaration.  This method may be overridden, though it typically
         * isn't necessary or desirable.  An enum type should override this
         * method when a more "programmer-friendly" string form exists.
         *
         * @return the name of this enum constant
         */
        @Override
        public String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         */
        @Override
        @SuppressWarnings("unchecked")
        public @NotNull Converter voAttributeConverter() {
            return Converter.getInstance();
        }

        public interface Status<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
                extends VoModel<VO, V, _DESCRIPTION> {

            enum AcceptStatusVo
                    implements VoModel<AcceptStatusVo, Integer, String> {
                UNSENT(0
                        , "未发送"
                        , "消息未成功发送，群聊中的所有用户都未接收到此消息。"), PARTIAL_SEND(1
                        , "部分发送"
                        , "消息已成功发送给群聊中的部分用户。"), SENT(2
                        , "已发送"
                        , "消息已成功发送，群聊中的所有用户都已接收到此消息。"), PARTIAL_READ(3
                        , "部分已读"
                        , "消息已成功发送，群聊中的部分用户已阅读此消息。"), READ(4
                        , "全部已读"
                        , "消息已成功发送，群聊中的所有用户都已阅读此消息。");

                /**
                 * 为持久化类型转换器提供支持
                 */
                @javax.persistence.Converter(autoApply = true)
                public static class Converter
                        extends VoAttributeConverter<AcceptStatusVo, Integer, String> {

                    // (单例模式 - 登记式)
                    private static class Factory {
                        private static final Converter SINGLETON = new Converter();
                    }

                    public Converter() {
                        super(AcceptStatusVo.class);
                    }

                    @NotNull
                    public static Converter getInstance() {
                        return Factory.SINGLETON;
                    }

                }

                public final int code;

                public final @NotNull String name;

                public final @NotNull String description;

                AcceptStatusVo(int code, @NotNull String name, @NotNull String description) {
                    this.code = code;
                    this.name = name;
                    this.description = description;
                }

                /**
                 * VO 的值
                 *
                 * @Description Unique attribute.
                 *
                 * @return 可为 <code>null</code>
                 */
                @Override
                public Integer value() {
                    return this.code;
                }

                /**
                 * VO 的详细信息
                 *
                 * @return
                 */
                @Override
                public String description() {
                    return this.description;
                }

                /**
                 * VO 的 (展示)名称
                 *
                 * @return
                 */
                @Override
                public @NotNull String displayName() {
                    return this.name;
                }

                /**
                 * Returns the name of this enum constant, as contained in the
                 * declaration.  This method may be overridden, though it typically
                 * isn't necessary or desirable.  An enum type should override this
                 * method when a more "programmer-friendly" string form exists.
                 *
                 * @return the name of this enum constant
                 */
                @Override
                public @NotNull String toString() {
                    return VoModel.toString(this);
                }

                /**
                 * 提供类型转换器
                 *
                 * @Design 为持久化类型转换功能提供支持.
                 */
                @Override
                public @NotNull Converter voAttributeConverter() {
                    return Converter.getInstance();
                }

            }

        }

    }

}
