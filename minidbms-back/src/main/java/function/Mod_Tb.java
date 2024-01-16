package function;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mod_Tb {////[[alter table, tablename], [drop, conlums]]

    public static List<Map<String, String>> modify(String dbName, String tbName, List<String> tmp1) throws DocumentException,IOException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        returnList.add(map);
        String[] key_value = tmp1.get(1).split("=");
        String oldconlum = key_value[0];
        String newconlum = key_value[1];
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
            Attribute idAttr;
            idAttr = rootElement.element(tbName).attribute(oldconlum);
            for (Node node : nodes) {
                String Name=((Element)node).attributeValue(oldconlum);
                ((Element)node).remove(idAttr);
                ((Element)node).addAttribute(newconlum,Name);
            }

            Cre_Tb.writeIO(file,document);
///
        }
        System.out.println("表" + tbName + "修改字段成功");
        map.put("result", "表" + tbName + "修改字段成功");
        return returnList;

    }
}

