package factory;
//all
import function.*;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.*;
/*
 * 工厂类：对解析后的sql语句进行判断，调用相应功能的模块函数
 */
public class Exe_Fun {

    public static Object dealParameters(List<List<String>> list) throws IOException, DocumentException {//将语句预处理后，生成的结果
        /**
         *  若无任何匹配，
         *  默认返回为“操作有误”提示
         */
        Object returnVal = null;
        List<Map<String, String>> tempL = new ArrayList<Map<String, String>>();
        HashMap<String, String> tempM = new HashMap<String, String>();
        tempM.put("result", "操作有误");
        tempL.add(tempM);
        returnVal = tempL;

        List<String> ls = new ArrayList<String>();
        ls = list.get(0);//一开始的肯定是处理语句
//--------------------------------------------------------------------------------------------------------------------------------------
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println("list.get(" + i + "): " + list.get(i));
//        }
        String sql_key = ls.get(0);

        if (sql_key.equals("create table")) {
            System.out.println("进入function：创建表");

            List<String> bodyList = new ArrayList<String>();//传递参数
            List<String> body = list.get(1);
            for (int i = 1; i < body.size(); i++) {
                bodyList.add(body.get(i));
            }
            returnVal = Cre_Tb.createTb(Use_Db.dbName, ls.get(1), bodyList);

        }
        else if (sql_key.equals("show databases")) {
            System.out.println("进入function：列出所有数据库");
            returnVal = Show_Db.showDatabase();
        }
        else if (sql_key.equals("show tables")) {
            System.out.println("进入function：列出所有表");
            returnVal = Show_Tb.showTable(Use_Db.dbName);
        }
        else if (sql_key.equals("use database")) {
            System.out.println("进入function：进入数据库");
            Use_Db.dbName = ls.get(1);

            // 处理输出，与原本逻辑无耦合
            tempL = new ArrayList<Map<String, String>>();
            HashMap<String, String> map = new HashMap<String, String>();
            tempL.add(map);

            //if database illegal
            if (!Is_Lg.isDatabase()) {
                Use_Db.dbName = null;
                map.put("result", "数据库不合法");
            } else {
                map.put("result", "进入数据库" + Use_Db.dbName);
            }

            returnVal =  tempL;
        }
        else if (sql_key.equals("create database")) {
            System.out.println("进入function：创建数据库");
            returnVal=Cre_Db.createDB(ls.get(1));
        }
        else if (sql_key.equals("insert into")) {
            System.out.println("进入function：插入数据到表");

            List<String> tmp2 = list.get(2);
            List<String> tmp1 = list.get(1);
            //
            returnVal = Ist_Tb_Dt.insertIntoTable(Use_Db.dbName, ls.get(1), tmp1, tmp2);
        }
        else if (sql_key.equals("select * from")) {
            //包含where条件
            if (list.size() > 1) {
                System.out.println("进入function：查询指定记录");
                String tableName = list.get(0).get(1);
                List<String> condition = list.get(1);
                String tableName2 = list.get(0).size() > 2 ? list.get(0).get(2) : null;
                if (tableName2!=null) {
                    List<String> tableNames = Arrays.asList(tableName, tableName2);
                    returnVal = Sct_Tb_Dt.select2(Use_Db.dbName, tableNames, null, condition);
                } else {
                    returnVal = Sct_Tb_Dt.select(Use_Db.dbName, tableName, null, condition);
                }

            }
            else {
                System.out.println("进入function：查询所有记录");
                String tableName = list.get(0).get(1);
                returnVal = Sct_Tb_Dt.select(Use_Db.dbName, tableName,null,null);
            }

        }
        else if (sql_key.equals("select")) {
            System.out.println("进入function：查询记录中的某些列");
            List<String> columns=list.get(0);
            List<String> condition = list.get(2);
            String tableName = list.get(1).get(1);
            String tableName2 = list.get(1).size() > 2 ? list.get(1).get(2) : null;
            if (tableName2!=null) {
                List<String> tableNames = Arrays.asList(tableName, tableName2);
                returnVal = Sct_Tb_Dt.select2(Use_Db.dbName, tableNames, columns, condition);
            } else {
                returnVal = Sct_Tb_Dt.select(Use_Db.dbName, tableName, columns, condition);
            }
        }
        else if (sql_key.equals("update")) {
            System.out.println("进入function：更新指定记录");

            List<List<String>> tmp = getPararmeterList(list);
            returnVal = Up_Tb_Dt.updateTable(Use_Db.dbName, list.get(0).get(1), tmp);
        }
        else if (sql_key.equals("drop database")) {
            System.out.println("进入function：删除数据库");
            returnVal = Drp_Db.deleteDB((ls.get(1)));
        }
        else if (sql_key.equals("drop table")) {
            System.out.println("进入function：删除表");
            returnVal = Del_Tb.deleteTable(Use_Db.dbName, ls.get(1));
        }
        else if (sql_key.equals("rename table")) {
            System.out.println("进入function：修改表名");
            returnVal = Rne_Tb.renametable(ls.get(1),list.get(1).get(1),Use_Db.dbName);
        }
        else if (sql_key.equals("delete from")) {
            System.out.println("进入function：删除指定记录");
            //取出每个list中的start部分，只传递后面的参数部分；
            List<String> tmp1 = list.get(1);
            List<String> tmp2 = new ArrayList<String>();
            for (int i = 1; i < tmp1.size(); i++) {
                String r = tmp1.get(i);
                tmp2.add(r);
            }
            returnVal = Del_Tb_Dt.deleteFromTable(Use_Db.dbName, ls.get(1), tmp2);
        }
        else if(sql_key.equals("alter table"))//[[alter table, book], [add, price, string]]
        {   String cond = list.get(1).get(0);
            if(cond.equals("add"))
            {
                System.out.println("进入function：增加表字段");//[[alter table, tablename], [add, conlums=type]]
                returnVal=Add_Tb.add(Use_Db.dbName,list.get(0).get(1),list.get(1));

            }
            if(cond.equals("drop"))////[[alter table, tablename], [drop, conlums]]
            {
                System.out.println("进入function：删除表字段");
                returnVal=Drp_Tb.delete(Use_Db.dbName,list.get(0).get(1),list.get(1));

            }
            if(cond.equals("modify"))//[[alter table, book], [modify, price=price1]]
            {
                System.out.println("进入function：修改表字段");
                returnVal=Mod_Tb.modify(Use_Db.dbName,list.get(0).get(1),list.get(1));

            }

        }
//        else if (sql_key.equals("create index on")) {
//            System.out.println("进入function：创建索引");
//            Cre_Id.createIndex(Use_Db.dbName, list.get(0).get(1), list.get(1).get(1));
//        }
//        else if (sql_key.equals("drop index on")) {
//            System.out.println("进入function：删除索引");
//            Drp_Id.dropIndex(Use_Db.dbName, list.get(0).get(1));
//        }
//        else if (sql_key.equals("create user")) {
//            System.out.println("进入function：创建新用户");
//            Cre_User.createUser();
//        }

        return returnVal;
    }
    /*
     * 当list参数为多行时，用于提取多行有效的参数。
     */
    protected static List<List<String>> getPararmeterList(List<List<String>> list) {
        List<List<String>> tmp1 = new ArrayList<List<String>>(); //传递参数
        List<String> tmp2;
        List<String> tmp3;

        //System.out.println(list.size());

        for (int i = 1; i < list.size(); i++) {
            tmp2 = new ArrayList<String>();
            tmp3 = new ArrayList<String>();

            tmp2 = list.get(i);
            for (int j = 1; j < tmp2.size(); j++) {
                String r = tmp2.get(j);
                tmp3.add(r);
            }
            tmp1.add(tmp3);
        }

//		for(int i = 0 ; i < tmp1.size();i++)
//		{
//			System.out.println(tmp1.get(i));
//		}

        return tmp1;
    }
}