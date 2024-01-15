package function;

import java.io.File;

public class Del_Tb {
    //delete table 表名
    public static void deleteTable(String dbName, String tbName) {
        //判断数据库是否为空
        if (Is_Lg.isDatabaseEmpty()) {
            return;
        }
        //表存在则返回一个对象
        File file = Is_Lg.hasDir(dbName, tbName);
        System.out.println(file.toString());
        //删除整张表
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        file.delete();
        System.out.println(tbName+ "表删除成功");

    }
}
