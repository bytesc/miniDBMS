package function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_Tb {////[[alter table, tablename], [add, conlums=type]]

    public static List<Map<String, String>> add(String dbName, String tbName, List<String> tmp1) throws DocumentException,IOException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        returnList.add(map);
        String[] key_value = tmp1.get(1).split("=");
        String key = key_value[0];
        String file_num = Is_Lg.lastFileName(dbName, tbName);
        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            if (!file.exists()) {
                System.out.println("table " + tbName + " is not exist");
                map.put("result", "表" + tbName + "不存在");
                return returnList;
            }
            //解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName);
            System.out.println("增加字段");

            for (Node node : nodes) {

                ((Element) node).addAttribute(key," ");
               // System.out.println((Element)node);
            }
            Cre_Tb.writeIO(file,document);


        }
        System.out.println("表" + tbName + "添加字段成功");
        map.put("result", "表" + tbName + "添加字段成功");
        return returnList;

        }

    }

