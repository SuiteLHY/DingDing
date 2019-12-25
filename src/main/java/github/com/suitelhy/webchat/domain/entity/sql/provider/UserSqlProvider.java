package github.com.suitelhy.webchat.domain.entity.sql.provider;

import github.com.suitelhy.webchat.domain.entity.User;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Set;

/**
 * User 实体对应的 SQL 生成
 *
 * 修改数据的操作非常敏感。
 *
 * 在系统吞吐量足够大且实际条件允许的情况下，应该做读写分离等架构优化，将修改
 *-> 持久层数据的操作业务分离到专门负责的集群上，同时也可以将生成 SQL 时的安全
 *-> 校验转移的 API 校验中更方便管理。
 */
public class UserSqlProvider {

    public String selectAll(Map<String, Object> params) {
        String offset = Integer.toString((Integer) params.get("offset"));
        String limit = Integer.toString((Integer) params.get("limit"));
        return new SQL()
                .SELECT(User.ENTITY_MAPPER.getColumns())
                .FROM(User.ENTITY_MAPPER.getTableName())
                .ORDER_BY(User.ENTITY_MAPPER.getColumn("firsttime"))
                .OFFSET(offset).LIMIT(limit)
                .toString();
    }

    public String selectCount() {
        return new SQL()
                .SELECT("COUNT(1) "
                        + User.ENTITY_MAPPER.getColumn("userid"))
                .FROM(User.ENTITY_MAPPER.getTableName())
                .toString();
    }

    public String selectUserByUserid(Map<String, Object> params) {
        String userid = (String) params.get("userid");
        SQL resultSQL = new SQL()
                .SELECT(User.ENTITY_MAPPER.getBusinessFields())
                .FROM(User.ENTITY_MAPPER.getTableName());
        if (null != userid && !"".equals(userid.trim())) {
            resultSQL.WHERE(User.ENTITY_MAPPER.getColumn("userid")
                    + "=" + userid);
        } else {
            resultSQL.WHERE(User.ENTITY_MAPPER.getColumn("userid")
                    + " IS NULL ");
        }
        return resultSQL.toString();
    }

    public String insert(Map<String, Object> params) {
        User user = (User) params.get("user");
        Map<String, String> columnsMap = User.ENTITY_MAPPER.getColumnsMapForSQL(user);
        String[] columnNames = columnsMap.keySet().toArray(new String[0]);
        String[] columnValues = columnsMap.values().toArray(new String[0]);
        SQL resultSQL = new SQL()
                .INSERT_INTO(User.ENTITY_MAPPER.getTableName())
                .INTO_COLUMNS(columnNames)
                .INTO_VALUES(columnValues);
        return resultSQL.toString();
    }

    public String update(Map<String, Object> params) {
        User user = (User) params.get("user");
        if (null == user.getUserid()
                || "".equals(user.getUserid())
                || null == User.ENTITY_MAPPER.getColumn("userid")) {
            throw new RuntimeException("参数 user 缺少有效信息", new IllegalArgumentException());
        }
        StringBuilder whereCondition = new StringBuilder()
                .append(User.ENTITY_MAPPER.getColumn("userid"))
                .append("=")
                .append(user.getUserid());
        Map<String, Object> columnsMap = User.ENTITY_MAPPER.getColumnsMap(user);
        Set<Map.Entry<String, Object>> entrySet = columnsMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            if (null == entryValue) {
                entrySet.remove(entryKey);
                continue;
            }
            if (User.ENTITY_MAPPER.getColumn("userid").equals(entryKey)) {
                entrySet.remove(entryKey);
            }
        }
        String[] columnsMapArr = new String[columnsMap.size()];
        int index = 0;
        for (Map.Entry<String, Object> entry : entrySet) {
            columnsMapArr[index] = entry.getKey()
                    + "="
                    + entry.getValue();
        }
        SQL resultSQL = new SQL()
                .UPDATE(User.ENTITY_MAPPER.getTableName())
                .SET(columnsMapArr)
                .WHERE(whereCondition.toString());
        return resultSQL.toString();
    }

    public String delete(Map<String, Object> params) {
        User user = (User) params.get("user");
        if (null == user.getUserid()
                || "".equals(user.getUserid())
                || null == User.ENTITY_MAPPER.getColumn("userid")) {
            throw new RuntimeException("参数 user 缺少有效信息", new IllegalArgumentException());
        }
        StringBuilder whereCondition = new StringBuilder()
                .append(User.ENTITY_MAPPER.getColumn("userid"))
                .append("=")
                .append(user.getUserid());
        return new SQL()
                .DELETE_FROM(User.ENTITY_MAPPER.getTableName())
                .WHERE(whereCondition.toString())
                .toString();
    }

}
