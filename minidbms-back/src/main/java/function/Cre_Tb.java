package function;
//zjb
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cre_Tb {
    static public int entry_num=10;
    //create table 表名(列名称1 数据类型1，列名称2 数据类型2)
    public static List<Map<String, String>> createTb(String dbName, String tbName, List<String> tmp) throws IOException {
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
        if(!tableFile.exists()){
            tableFile.mkdir();
        }
        else{
            Map<String, String> returnMap = new HashMap<String, String>();
            returnMap.put("result", "表已经存在");
            returnList.add(returnMap);
            return returnList;
        }
        //创建配置文件并设置根节点
        File table=new File("./minidata/"+dbName+"/"+tbName+"/"+tbName+"-config.xml");
        Document document = DocumentHelper.createDocument();
        Element rootElem = document.addElement(tbName+"s");
        //根节点的属性值为列名称=数据类型
        for (int i = 0; i < tmp.size(); i++) {
                String[] list=tmp.get(i).split(" ");
                rootElem.addAttribute(list[0],list[1]);
        }
        //物理层第一张子表的下标为0
        rootElem.addElement(tbName).setText("0");
        //存储物理层可插入子表的下标
        rootElem.addElement("insertables");
        //记录是否建有索引
        rootElem.addElement("index");
        //记录主键名称
        rootElem.addElement("index_name");
        //写入操作
        writeIO(table,document);

        //创建表的物理层第一张子表
        File first_file=new File("./minidata/"+dbName+"/"+tbName+"/"+tbName+"0.xml");
        Document first_document=DocumentHelper.createDocument();
        first_document.addElement(tbName+"s");
        //写入操作
        writeIO(first_file,first_document);

        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("result", "表创建成功");
        returnList.add(returnMap);
        return returnList;
    }
    //更新document树，写入外存
    public static void writeIO(File write_file,Document write_document) throws IOException {
        //确定文件输出位置
        FileOutputStream outputStream=new FileOutputStream(write_file);
        OutputFormat outputFormat=OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        //创建写入对象，写入document对象
        XMLWriter xmlWriter=new XMLWriter(outputStream,outputFormat);
        xmlWriter.write(write_document);
        //关闭流
        xmlWriter.close();
    }
}
