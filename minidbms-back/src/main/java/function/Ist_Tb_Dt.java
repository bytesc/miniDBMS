package function;
//rcy
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ist_Tb_Dt {

    //insert into 表名(列名称1，列名称2) values(列值1，列值2);
    public static List<Map<String, String>> insertIntoTable(String dbName, String tbname, List<String> tmp1, List<String> tmp2) throws DocumentException, IOException {
        //数据库是否合法
        if (!Is_Lg.isDatabase()) {
            // 只用于输出
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", "数据库不合法");
            list.add(map);

            return list;
        }
        //表存在则打开配置文件
        File config_file= Is_Lg.isTable(dbName,tbname);
        //解析配置文件
        SAXReader config_file_reader = new SAXReader();
        Document config_file_document = config_file_reader.read(config_file);
        Element write_file_element = (Element) config_file_document.getRootElement().selectSingleNode(tbname);
        Element insertable_element=(Element)config_file_document.getRootElement().selectSingleNode("insertables");

        //若有物理层可插入子表，则将数据插入到该物理层子表
        String write_file_name;
        if (insertable_element.selectNodes("insertable").size() > 0) {
            write_file_name = tbname + insertable_element.selectSingleNode("insertable").getText();
            //创建写入对象，获取记录数量
            File write_file = new File("./minidata/" + dbName + "/" + tbname + "/" + write_file_name + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(write_file);
            Element rootElement = document.getRootElement();
            Element element = rootElement.addElement(tbname);
            for (int i = 1; i < tmp1.size(); i++) {
                element.addAttribute(tmp1.get(i), tmp2.get(i));
            }
            //若可插入文件记录数>=10,更新配置信息
            List<Node> nodes = document.getRootElement().selectNodes(tbname);
            if (nodes.size() >= Cre_Tb.entry_num) {
                insertable_element.remove(insertable_element.selectSingleNode("insertable"));
                Cre_Tb.writeIO(config_file,config_file_document);
            }
            Cre_Tb.writeIO(write_file,document);

            System.out.println("插入成功");

        } else {
            //否则插入数据到物理层最后一张子表
            write_file_name = tbname + write_file_element.getText();
            //创建写入对象，获取记录数量
            File write_file = new File("./minidata/" + dbName + "/" + tbname + "/" + write_file_name + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(write_file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbname);

            //如果该文件中记录个数>=10，新建文件写入
            if (nodes.size() >= Cre_Tb.entry_num) {
                Document newDocument = DocumentHelper.createDocument();
                Element newRoot = newDocument.addElement(tbname + "s");
                Element newChild = newRoot.addElement(tbname);
                //更新文件数量
                int file_amount_int = (Integer.parseInt(write_file_element.getText()) + 1);
                String file_amount = ("" + file_amount_int);
                //设置新文件名
                String newfile = tbname + file_amount;
                for (int i = 1; i < tmp1.size(); i++) {
                    newChild.addAttribute(tmp1.get(i),tmp2.get(i));
                }
                Cre_Tb.writeIO(new File("./minidata/" + dbName + "/" + tbname + "/" + newfile + ".xml"),newDocument);

                System.out.println("插入成功");
                //更新配置文件中表的文件数量
                write_file_element.setText(file_amount);
                Cre_Tb.writeIO(config_file,config_file_document);
            }
            //否则在最后一条记录后插入
            else {
                //创建新节点
                Element childElement = rootElement.addElement(tbname);
                for (int i = 1; i < tmp1.size(); i++) {
                    childElement.addAttribute(tmp1.get(i),tmp2.get(i));
                }
                Cre_Tb.writeIO(write_file,document);

                System.out.println("插入成功");
            }
        }

        // 只用于输出
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("result", "插入成功");
        list.add(map);

        return list;
    }
}
