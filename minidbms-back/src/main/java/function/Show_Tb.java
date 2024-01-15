package function;

import java.io.File;

public class Show_Tb {
    //show tables
    public static void showTable(String dbname){
        //数据库是否为空
        if(Is_Lg.isDatabaseEmpty()){
            return;
        }
        File dir=new File("./minidata/"+dbname+"");
        for(File file:dir.listFiles()){
            if(file.exists()){
                System.out.println(file.getName());
            }
        }
    }
}
