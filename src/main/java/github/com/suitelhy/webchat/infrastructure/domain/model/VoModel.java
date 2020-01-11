package github.com.suitelhy.webchat.infrastructure.domain.model;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 值对象 (Value Object) 设计模板
 * @param <VO>
 * @param <V>
 */
/**
 * 备注: 用 VO 作为字段时, 用 Mybatis 处理不当会出现:
 *-> <exception>org.apache.ibatis.executor.result.ResultMapException: Error attempting to get column 'status' from result set.
 *-> Cause: java.lang.IllegalArgumentException: No enum constant com.example.demo.domain.vo.AccountVo.Status.1</exception>
 *
 */
public interface VoModel<VO extends Enum, V extends Number>
        extends Serializable {

    /**
     * VO 对应的值对应的业务名称
     * @Description 用于对 VO 的值进行补充说明.
     * @return 不为 null
     */
    @NotNull
    String name();

    /**
     * VO 的值
     * @return 可为 null
     */
    V value();

    default boolean equalsValue(V value) {
        return Objects.equals(value(), value);
    }

    default int hashCodeValue() {
        return Objects.hashCode(value());
    }

}
