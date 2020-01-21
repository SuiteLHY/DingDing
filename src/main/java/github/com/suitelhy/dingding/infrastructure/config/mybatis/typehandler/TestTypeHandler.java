//package github.com.suitelhy.dingding.infrastructure.config.mybatis.typehandler;
//
//import VoModel;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedTypes;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * Mybatis配置 -> 针对 VO 类型的 类型处理器(TypeHandler)
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
///*@MappedJdbcTypes({JdbcType.TINYINT})*/
//@MappedTypes({String.class})
//public class TestTypeHandler
//        extends BaseTypeHandler<String> {
//
//    @Override
//    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
//        System.err.println("111111111111111111111");
//    }
//
//    @Override
//    public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
//        System.err.println("2222222222222222222222222");
//        return null;
//    }
//
//    @Override
//    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
//        System.err.println("2222222222222222222222222");
//        return null;
//    }
//
//    @Override
//    public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
//        System.err.println("2222222222222222222222222");
//        return null;
//    }
//
//}
