package function;
//rcy
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Up_Tb_Dt {
    public static List<Map<String, String>> updateTable(String dbName, String tbName, List<List<String>> tmp) throws DocumentException, IOException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        returnList.add(map);

        //数据库是否为空
        if (Is_Lg.isDatabaseEmpty()) {
            map.put("result", "数据库为空");
            return returnList;
        }
        //表存在则返回表的物理层最后一张子表的下标,得到配置文件
        File config_file = Is_Lg.isTable(dbName, tbName);
        //find标记是否找到记录
        Boolean find = false;
        //tmp2表示where的列名称和列值
        String[] tmp2 = tmp.get(1).get(0).split("=");
        //key为where的列名称
        String key=tmp2[0];


        //扫描所有文件,j记录文件下表,num用来遍历所有文件
        String this_file = Is_Lg.lastFileName(dbName, tbName);
        for (int j = Integer.parseInt(this_file); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            find=update(tbName,file,tmp,tmp2);
            if(find){
                map.put("result", "更新完成");
                return returnList;
            }
        }
        System.out.println("更新失败，未找到记录");
        map.put("result", "更新失败，未找到记录");
        return returnList;
    }

    public static boolean update(String tbName,File file,List<List<String>> tmp,String[] tmp2) throws DocumentException, IOException {
        boolean find=false;
        //创建解析器，document对象，获得根节点
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();

        List<Node> nodes = root.selectNodes(tbName);
        for (Node node : nodes) {
            Element currentNode = (Element) node;
            List<Attribute> list = currentNode.attributes();
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Attribute attribute = (Attribute) i.next();
                if (attribute.getName().equals(tmp2[0]) && attribute.getText().equals(tmp2[1])) {
                    find = true;
                    break;
                }
            }
            if (find) {
                for (int k = 0; k < tmp.get(0).size(); k++) {
                    String[] tmp1 = tmp.get(0).get(k).split("=");
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        if (attribute.getName().equals(tmp1[0])) {
                            attribute.setText(tmp1[1]);
                        }
                    }
                }
                //写入IO
                Cre_Tb.writeIO(file,document);
                System.out.println("更新成功");
                return true;
            }
        }
        return false;

    }
}
