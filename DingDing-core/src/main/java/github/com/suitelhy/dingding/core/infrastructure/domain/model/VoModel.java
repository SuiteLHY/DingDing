package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 值对象 (Value Object) 设计模板
 *
 * @param <VO>           <interface>VoModel</interface> 的实现类 (TIPS: 使用了"自限定类型").
 * @param <V>
 * @param <_DESCRIPTION>
 */
public interface VoModel<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends Serializable {

    /**
     * VO 的值对应的业务名称
     *
     * @return 不为 null
     * @Description Unique attribute. 用于对 <tt>value()</tt> 进行补充说明.
     */
    @NotNull
    String name();

    /**
     * VO 的值
     *
     * @return 可为 {@code null}, 此时[VO的值]为 {@code null} (而不是缺失设置).
     * @Description Unique attribute.
     */
    @Nullable
    V value();

    /**
     * VO 的详细信息
     *
     * @return
     */
    default @Nullable
    _DESCRIPTION description() {
        return null;
    }

    /**
     * VO 的 (展示)名称
     *
     * @return
     */
    default @NotNull
    String displayName() {
        return name();
    }

    /**
     * 等效比较
     *
     * @param value {@link V}
     * @return 判断结果
     * @see this#value()
     */
    default boolean equalsValue(V value) {
        return Objects.equals(value(), value);
    }

    default int hashCodeValue() {
        return Objects.hashCode(value());
    }

    /**
     * 转换为字符串
     *
     * @return 转换结果
     */
    @Override
    @NotNull
    String toString();

    /**
     * VO 对象转 String 对象 <- <interface>VoModel</interface>默认实现
     *
     * @param vo             [VO 对象]
     * @param <VO>
     * @param <V>
     * @param <_DESCRIPTION>
     * @return 当前 [VO 对象] 的字符串表示
     * @throws IllegalArgumentException {@param vo}非法 (值为{@code null}) 时抛出该异常
     */
    static <VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
    @NotNull String toString(@NotNull VO vo)
            throws IllegalArgumentException {
        if (null == vo) {
            throw new IllegalArgumentException("非法参数: [VO 对象]");
        }

        return String.format("%s{value=%s, description=\"%s\", displayName=\"%s\"}"
                , vo.name()
                , vo.value()
                , vo.description()
                , vo.displayName());
    }

    //===== 关联设计 =====//

    /**
     * 提供类型转换器
     *
     * @return {@link _CONVERTER}
     * @Design 为持久化类型转换功能提供支持.
     */
    <_CONVERTER extends VoAttributeConverter<VO, V, _DESCRIPTION>>
    @NotNull _CONVERTER voAttributeConverter();

    //==========//

}
