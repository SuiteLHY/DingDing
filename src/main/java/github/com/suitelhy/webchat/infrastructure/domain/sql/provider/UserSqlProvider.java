package github.com.suitelhy.webchat.infrastructure.domain.sql.provider;

import github.com.suitelhy.webchat.domain.entity.User;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Set;

/**
 * User实体对应的 SQL 生成
 *
 * 修改数据的操作非常敏感。
 *
 * 在系统吞吐量足够大且实际条件允许的情况下，应该做读写分离等架构优化，将修改
 *-> 持久层数据的操作业务分离到专门负责的集群上，同时也可以将生成 SQL 时的安全
 *-> 校验部分转移至 API 校验中，更方便管理。
 *
 */
/**
 * Mybatis 通过SQL预编译实现 防止SQL注入攻击 的效果.
 *
 * 详细说明: <a href="https://blog.csdn.net/fmwind/article/details/59110918">MyBatis如何防止SQL注入</a>
 *
 */
public class UserSqlProvider {

    public String selectAll(Map<String, Object> params) {
        return new SQL()
                .SELECT(User.MAPPER.getColumns())
                .FROM(User.MAPPER.getTableName())
                .ORDER_BY(User.MAPPER.getColumn("firsttime"))
                .OFFSET(User.MAPPER.toValueForSQL("offset"))
                .LIMIT(User.MAPPER.toValueForSQL("limit"))
                .toString();
    }

    public String selectCount() {
        return new SQL()
                .SELECT("COUNT(1) "
                        + User.MAPPER.getColumn("userid"))
                .FROM(User.MAPPER.getTableName())
                .toString();
    }

    public String selectUserByUserid(Map<String, Object> params) {
        String userid = (String) params.get("userid");
        SQL resultSQL = new SQL()
                .SELECT(User.MAPPER.getBusinessFieldNames())
                .FROM(User.MAPPER.getTableName());
        if (null != userid && !"".equals(userid.trim())) {
            resultSQL.WHERE(User.MAPPER.getColumn("userid")
                    + "="
                    + User.MAPPER.getColumnValueForSQL("userid"));
        } else {
            resultSQL.WHERE(User.MAPPER.getColumn("userid")
                    + " IS NULL ");
        }
        return resultSQL.toString();
    }

    public String insert(Map<String, Object> params) {
        User user = (User) params.get("user");
        if (user.isEmpty()) {
            throw new RuntimeException("Entity类型参数 user 非法", new IllegalArgumentException());
        }
        Map<String, String> columnsMap = user.MAPPER.getColumnsMapForSQL(user);
        String[] columnNames = columnsMap.keySet().toArray(new String[0]);
        String[] columnValues = columnsMap.values().toArray(new String[0]);
        SQL resultSQL = new SQL()
                .INSERT_INTO(user.MAPPER.getTableName())
                .INTO_COLUMNS(columnNames)
                .INTO_VALUES(columnValues);
        return resultSQL.toString();
    }

    public String update(Map<String, Object> params) {
        User user = (User) params.get("user");
        if (user.isEmpty()) {
            throw new RuntimeException("Entity类型参数 user 非法", new IllegalArgumentException());
        }
//        Map<String, Object> columnsMap = User.MAPPER.getColumnsMap(user);
        Map<String, String> columnsMap = user.MAPPER.getColumnsMapForSQL();
        Set<Map.Entry<String, String>> entrySet = columnsMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String entryKey = entry.getKey();
            String entryValue = entry.getValue();
            if (null == entryValue) {
                entrySet.remove(entryKey);
                continue;
            }
            if (User.MAPPER.getColumn("userid").equals(entryKey)) {
                entrySet.remove(entryKey);
            }
        }
        String[] columnsMapArr = new String[columnsMap.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : entrySet) {
            columnsMapArr[index++] = entry.getKey()
                    + "="
                    + entry.getValue();
        }
        String whereCondition = user.MAPPER.getColumn("userid")
                + "="
                + user.MAPPER.getColumnValueForSQL("userid");
        SQL resultSQL = new SQL()
                .UPDATE(user.MAPPER.getTableName())
                .SET(columnsMapArr)
                .WHERE(whereCondition);
        return resultSQL.toString();
    }

    public String delete(/*Map<String, Object> params*/User user) {
        /*User user = (User) params.get("user");*/
        if (user.isEmpty()) {
            throw new RuntimeException("Entity类型参数 user 非法", new IllegalArgumentException());
        }
        String whereCondition = user.MAPPER.getColumn("userid")
                + "="
                + /*user.getUserid()*/user.MAPPER.getColumnValueForSQL("userid");
        return new SQL()
                .DELETE_FROM(user.MAPPER.getTableName())
                .WHERE(whereCondition)
                .toString();
    }

}
