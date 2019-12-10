package httpapi.utils.dbutils;
/**
 * 根据名称获取id的方法封装
 */

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetIdByName {

    public static String getIdByName(String sql,String idOfName){
        ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sql);
        String id = null;
        try {
            if(resultSetByQuery.next()){
                id = resultSetByQuery.getString(idOfName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    //根据意图类别名称获取意图类别id
    public static String GetId(String categoryName){
        String sql = "select * from t_intention_category where intention_type_name = ?";
        ResultSet resultSet = JdbcUtil.getResultSetByTransPara(sql, categoryName);
        String id = null;
        try {
            if(resultSet.next()){
                id = resultSet.getString("id");


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    //根据意图名称获取意图id
    public static String GetIntentionId(String categoryId,String intentionName){
        String sql = "SELECT * FROM t_intention WHERE intention_category='"+categoryId+"' AND intention_name = '"+intentionName+"' and public_intention_flag=1 and is_delete=0";
        ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sql);
        String id = null;
        try {
            if(resultSetByQuery.next()){
                id = resultSetByQuery.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

}
