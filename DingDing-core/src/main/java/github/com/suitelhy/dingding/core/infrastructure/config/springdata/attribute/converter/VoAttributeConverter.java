package github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 领域对象 (Domain Object) - VO 的属性持久化转换控制器
 *
 * @param <V>
 * @Reference 不确定是否和 Mybatis 的 <function>TypeHandler</function> 是否一致
 * -> (通过 Constructor 注入具体类型), <Google>查找</Google> 了一下:
 * -> <a href="https://stackoverflow.com/questions/23564506/is-it-possible-to-write-a-generic-enum-converter-for-jpa">
 * ->     java-是否可以为JPA编写通用枚举转换器？ - 堆栈溢出</a>
 * -> , <a href="https://blog.csdn.net/wanping321/article/details/90269057">
 * ->     Springboot JPA  枚举Enum类型存入到数据库_Enum存库,JPA_miskss的博客-CSDN博客</a>
 */
@Converter
public abstract class VoAttributeConverter<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        implements AttributeConverter<VO, V> {

    private final Class<VO> voClazz;

    public VoAttributeConverter(Class<VO> voClazz) {
        if (null == voClazz) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.voClazz = voClazz;
        }
    }

    /**
     * Vo 转持久化类型, 存入数据库
     *
     * @param vo {@link VO}
     * @return {@link V}
     * @Description <param>voModel</param> 转换为 <class>Number</class>
     */
    @Override
    public V convertToDatabaseColumn(@Nullable VO vo) {
        return (null == vo)
                ? null
                : vo.value();
    }

    /**
     * 持久化类型 转 Vo, 从数据库取出
     *
     * @param value {@link V}
     * @return {@link VO}
     * @Description <param>number</param> 转换为 <class>VoModel</class>
     */
    @Override
    public VO convertToEntityAttribute(@Nullable V value) {
        for (VO each : this.voClazz.getEnumConstants()) {
            if (each.equalsValue(value)) {
                return each;
            }
        }
        return null;
    }

}
