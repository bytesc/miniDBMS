package function;
//tjh
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rne_Tb {
    public static List<Map<String, String>> renametable(String oldtbName,String tbName,String dbName){
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        //判断数据库是否为空
        if(Is_Lg.isDatabaseEmpty()){
            Map<String, String> returnMap = new HashMap<String, String>();
            returnMap.put("result", "数据库为空");
            returnList.add(returnMap);
            return returnList;
        }
        //创建一张表的文件夹，逻辑上表示一张表
        File tableFile=new File("./minidata/"+dbName+"/"+tbName+"");
        System.out.println(tableFile);
        File oldtableFile=new File("./minidata/"+dbName+"/"+oldtbName+"");
        System.out.println(oldtableFile);

        if(!oldtableFile.exists()){
            Map<String, String> returnMap = new HashMap<String, String>();
            returnMap.put("result", "旧表不存在");
            returnList.add(returnMap);
            return returnList;
        }
        if(!tableFile.exists()){
            tableFile.mkdir();
        }
        else{
            Map<String, String> returnMap = new HashMap<String, String>();
            returnMap.put("result", "新表名称已经存在");
            returnList.add(returnMap);
            return returnList;
        }
        File[] files = oldtableFile.listFiles();
        for (File file : files) {
            String newFilePath = tableFile.getPath() + "/"+ file.getName();
            System.out.println(newFilePath);
            file.renameTo(new File(newFilePath));

        }
        //删除oldtableFile路径下的文件夹
        if(oldtableFile.exists() && oldtableFile.isDirectory()){
            File[] oldFiles = oldtableFile.listFiles();
            for (File file : oldFiles) {
                if (file.isDirectory()) {
                    File[] subFiles = file.listFiles();
                    for (File subFile : subFiles) {
                        subFile.delete();
                    }
                }
                file.delete();
            }
            oldtableFile.delete();
        }
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("result", "表名修改成功");
        returnList.add(returnMap);
        return returnList;

    }


}
