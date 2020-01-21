//package github.com.suitelhy.dingding.infrastructure.config.mybatis.typehandler;
//
//import VoModel;
//import org.apache.ibatis.type.*;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * (弃用) Mybatis配置 -> 针对 VO 类型的 类型处理器(TypeHandler)
// *
// * @Reference
// *-> <a href="https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers">
// *->     mybatis – MyBatis 3 | 配置#类型处理器（typeHandlers）</a>
// *-> , <a href="https://blog.csdn.net/ned_mahone/article/details/82870173">
// *->     mybatis自定义类型处理器-TypehHandler_Ned_mahone的博客-CSDN博客</a>
// *-> , (jar包源码) <jar>mybatis-3.5.3.jar</jar>-<class>org.apache.ibatis.type.EnumTypeHandler</class>
// *-> &<class>org.apache.ibatis.type.EnumOrdinalTypeHandler</class>
// *-> , <a href="https://github.com/abel533/mybatis-enum">abel533/mybatis-enum: MyBatis 枚举全面使用指南</a>
// *
// */
//@MappedJdbcTypes(value = {JdbcType.TINYINT, JdbcType.SMALLINT, JdbcType.INTEGER}
//        , includeNullJdbcType = true)
//@MappedTypes({VoModel.class/*, Enum.class*/})
//public class VoTypeHandler<E extends Enum<E> & VoModel<Number>>
//        extends BaseTypeHandler<E>/*EnumTypeHandler<E>*/ {
//
//    private final Class<E> type;
//
//    private final E[] enums;
//
//    public VoTypeHandler(Class<E> type) {
//        /*super(type);*/
//        if (null == type) {
//            throw new IllegalArgumentException("Type argument cannot be null");
//        } else {
//            this.type = type;
//            this.enums = type.getEnumConstants();
//            if (this.enums == null) {
//                throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
//            }
//        }
//    }
//
//    /**
//     * 将 <param>vo</param> 转换为指定的 JdbcType 类型对象
//     * @Description 通过 预处理语句(PreparedStatement)对象 设置参数
//     *-> , 最终实现将 <param>vo</param> 存入数据库.
//     * @param ps
//     * @param i
//     * @param vo
//     * @param jdbcType
//     * @throws SQLException
//     */
//    @Override
//    public void setNonNullParameter(PreparedStatement ps
//            , int i
//            , E vo
//            , JdbcType jdbcType) throws SQLException {
//        if (null == jdbcType) {
//            ps.setByte(i, (Byte) vo.value());
//        } else {
//            ps.setObject(i, vo.value(), jdbcType.TYPE_CODE);
//        }
//    }
//
//    /**
//     * 从结果集 (ResultSet) 获取数据
//     * @Description 通过列名从结果集 (ResultSet) 获取数据
//     * @param resultSet
//     * @param columnName
//     * @return
//     * @throws SQLException
//     */
//    @Override
//    public E getNullableResult(ResultSet resultSet, String columnName)
//            throws SQLException {
//        Byte value = resultSet.getByte(columnName);
//        for (E each : enums) {
//            if (each.equalsValue(value)) {
//                return each;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 从结果集 (ResultSet) 获取数据
//     * @Description 从结果集 (ResultSet) 获取数据
//     * @param resultSet
//     * @param columnIndex
//     * @return
//     * @throws SQLException
//     */
//    @Override
//    public E getNullableResult(ResultSet resultSet, int columnIndex)
//            throws SQLException {
//        Byte value = resultSet.getByte(columnIndex);
//        for (E each : enums) {
//            if (each.equalsValue(value)) {
//                return each;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 从结果集 (ResultSet) 获取数据
//     * @Description 从结果集 (ResultSet) 获取数据
//     * @param cs
//     * @param columnIndex
//     * @return
//     * @throws SQLException
//     */
//    @Override
//    public E getNullableResult(CallableStatement cs, int columnIndex)
//            throws SQLException {
//        Byte value = cs.getByte(columnIndex);
//        for (E each : enums) {
//            if (each.equalsValue(value)) {
//                return each;
//            }
//        }
//        return null;
//    }
//
//}
