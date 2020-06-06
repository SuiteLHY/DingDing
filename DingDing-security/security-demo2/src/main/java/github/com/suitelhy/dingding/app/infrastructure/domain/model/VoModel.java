package github.com.suitelhy.dingding.app.infrastructure.domain.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 值对象 (Value Object) 设计模板
 * @param <VO> <interface>VoModel</interface> 的实现类 (TIPS: 使用了"自限定类型").
 * @param <V>
 * @param <_DESCRIPTION>
 */
///**
// * 备注: 用 VO 作为字段时, 用 Mybatis 处理不当会出现:
// *-> <exception>org.apache.ibatis.executor.result.ResultMapException: Error attempting to get column 'status' from result set.
// *-> Cause: java.lang.IllegalArgumentException: No enum constant com.example.demo.domain.vo.AccountVo.Status.1</exception>
// *
// */
public interface VoModel<VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends Serializable {

    /**
     * VO 的值对应的业务名称
     *
     * @Description Unique attribute. 用于对 <tt>value()</tt> 进行补充说明.
     * @return 不为 null
     */
    @NotNull
    String name();

    /**
     * VO 的值
     *
     * @Description Unique attribute.
     * @return 可为 <code>null</code>
     */
    @Nullable
    V value();

    /**
     * VO 的详细信息
     *
     * @return
     */
    @Nullable
    default _DESCRIPTION description() {
        return null;
    }

    /**
     * VO 的 (展示)名称
     *
     * @return
     */
    @NotNull
    default String showName() {
        return name();
    }

    default boolean equalsValue(V value) {
        return Objects.equals(value(), value);
    }

    default int hashCodeValue() {
        return Objects.hashCode(value());
    }

    @NotNull
    @Override
    String toString();

    /**
     * VO 对象转 String 对象 <- <interface>VoModel</interface>默认实现
     *
     * @param vo VO 对象
     * @param <VO>
     * @param <V>
     * @param <_DESCRIPTION>
     * @return
     */
    static <VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION> String toString(@NotNull VO vo) {
        return vo.name()
                + "{value=" + vo.value()
                + ", description=" + vo.description()
                + ", showName=" + vo.showName()
                + "}";
    }

}
