package github.com.suitelhy.webchat.infrastructure.config.springdata.attribute.converter;

import github.com.suitelhy.webchat.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 领域对象 (Domain Object) - VO 的属性持久化转换控制器
 *
 * @Reference
 *-> {@link github.com.suitelhy.webchat.infrastructure.config.mybatis.typehandler.VoTypeHandler}
 *-> ; 不确定是否和 Mybatis 的 <function>TypeHandler</function> 是否一致
 *-> (通过 Constructor 注入具体类型), <Google>查找</Google> 了一下:
 *-> <a href="https://blog.csdn.net/wanping321/article/details/90269057">
 *->     Springboot JPA  枚举Enum类型存入到数据库_Enum存库,JPA_miskss的博客-CSDN博客</a>
 *
 * @param <V>
 */
@Converter
public abstract class VoAttributeConverter<VO extends Enum<VO> & /*VoModel<C>*/VoModel<VO, V>, V extends Number>
        implements AttributeConverter<VO, V> {

    private final Class<VO> voClazz;

    /*private final VO[] enums;*/

    public VoAttributeConverter(Class<VO> voClazz) {
        if (null == voClazz) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.voClazz = voClazz;
            /*this.enums = voClazz.getEnumConstants();
            if (null == this.enums) {
                throw new IllegalArgumentException(voClazz.getSimpleName()
                        + " does not represent an enum type.");
            }*/
        }
    }

    /**
     * Vo 转持久化类型, 存入数据库
     * @Description <param>voModel</param> 转换为 <class>Number</class>
     * @param vo
     * @return value
     */
    @Override
    public V convertToDatabaseColumn(@Nullable VO vo) {
        return null == vo ? null : vo.value();
    }

    /**
     * 持久化类型 转 Vo, 从数据库取出
     * @Description  <param>number</param> 转换为 <class>VoModel</class>
     * @param value
     * @return Vo
     */
    @Override
    public VO convertToEntityAttribute(@Nullable V value) {
        for (VO each : /*enums*/voClazz.getEnumConstants()) {
            if (each.equalsValue(value)) {
                return each;
            }
        }
        return null;
    }

}
