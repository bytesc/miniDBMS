package function;
//rcy
import bpulstree.BPlusTree;
import bpulstree.Main;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Del_Tb_Dt {
    //delete from 表名 where 列名称=列值
    public static List<Map<String, String>> deleteFromTable(String dbName, String tbName, List<String> tmp) throws DocumentException, IOException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        returnList.add(map);

        //数据库是否为空
        if (Is_Lg.isDatabaseEmpty()) {
            map.put("result", "数据库为空");
            return returnList;
        }
        //输出tmp列表
        for (int i = 0; i < tmp.size(); i++) {
            System.out.println(tmp.get(i));
        }
        //表存在则返回物理层最后一张子表的下标，并得到配置文件
        File config_file= Is_Lg.isTable(dbName,tbName);
        String write_file_last_num = Is_Lg.lastFileName(dbName, tbName);
        //获取where列名称
        String [] key_value=tmp.get(0).split("=");
        String key=key_value[0];
        //find标记是否找到记录
        Boolean find = false;
        //非主键查询删除
        if(!Is_Lg.isIndex(config_file,key)) {
            for (int j = Integer.parseInt(write_file_last_num); j >= 0; j--) {
                //设置变量traverse_file用来遍历表的所有文件
                String last_num = "" + j;
                //创建写入对象，创建sax解析器，document对象，获得root节点
                File file = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + last_num + ".xml");
                find=delete(file,dbName,tbName,key_value,last_num);
                if(find){
                    map.put("result", "删除完成");
                    return returnList;
                }
            }
            System.out.println("没有找到要删除的记录");
            map.put("result", "没有找到要删除的记录");
            return returnList;
        }
        //主键查询删除
        else {
            BPlusTree tree= Cre_Id.findTree(tbName);
            String file_name=tree.search(Integer.parseInt(key_value[1]));
            String num=file_name.substring(file_name.length()-1,file_name.length());
            File file=new File("./minidata/"+dbName+"/"+tbName+"/"+file_name+".xml");
            find=delete(file,dbName,tbName,key_value,num);
            //删除数据后更新索引
            if(find) {
                Cre_Id.updateIndex_delete(tbName, key_value[1]);
                map.put("result", "删除完成");
                return returnList;
            }
            System.out.println("没有找到要删除的记录");
            map.put("result", "没有找到要删除的记录");
            return returnList;
        }
    }
    //删除用delete方法
    public static boolean delete(File file,String dbName,String tbName,String[] key_value,String last_num) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        boolean find=false;

        //设置一个节点用来遍历
        Element element;
        List<Node> nodes = root.selectNodes(tbName);
        boolean isFull=nodes.size()==10;
        for (Node node : nodes) {
            Element currentNode=(Element)node;
            List<Attribute> lists=currentNode.attributes();
            for (Iterator i = lists.iterator(); i.hasNext(); ) {
                Attribute attribute = (Attribute) i.next();
                if (attribute.getName().equals(key_value[0]) && attribute.getText().equals(key_value[1])) {
                    find = true;
                    break;
                }
            }
            if (find) {

                root.remove(currentNode);
                //写入IO
                Cre_Tb.writeIO(file,document);

                //更新配置文件
                if(isFull) {
                    File file1 = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + "-config.xml");
                    SAXReader saxReader = new SAXReader();
                    Document document1 = saxReader.read(file1);
                    Element element1 = (Element) document1.getRootElement().selectSingleNode("insertables");
                    element1.addElement("insertable").setText(last_num);
                    //写入IO
                    Cre_Tb.writeIO(file1, document1);
                }
                System.out.println("删除记录成功");
                return true;
            }
        }
        return false;
    }
}

