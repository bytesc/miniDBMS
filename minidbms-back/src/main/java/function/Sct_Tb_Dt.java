package function;
//tjh
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

public class Sct_Tb_Dt {
    //根据参数不同调用不同的查询方法
    public static List<Map<String, String>> select(String dbName, String tbName, List<String> columns, List<String> condition) throws DocumentException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        //数据库是否为空
        if (Is_Lg.isDatabaseEmpty()) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", "数据库为空");
            returnList.add(map);
            return returnList;
        }
        //表是否存在
        File config_file = Is_Lg.isTable(dbName, tbName);
        if (config_file == null) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", "表不存在");
            returnList.add(map);
            return returnList;
        }

    //列名称为空，则查询语句为select * from 表名/select * from 表名 where 列名称=列值
        if (columns == null) {
            //条件为空，则查询语句为select * from 表名
            if (condition == null) {
                returnList = selectFromTb(dbName, tbName);
            }
            //条件不为空，则查询语句为select * from 表名 where 列名称=列值
            else {
                String[] key_value = condition.get(1).split("=");
                String key = key_value[0];
                //查询
               returnList = selectAllFromTb(dbName, tbName, condition);
            }
        }
        //列名称不为空，则查询语句为select 列名称1，列名称2 from 表名 where 列名称=列值/select 列名称1，列名称2 from 表名
        else {
            //条件为空，则查询语句为select 列名称1，列名称2 from 表名
            if (condition.size() == 0) {
                returnList = selectFromTb(dbName, tbName, columns);
            }
            //条件不为空，则查询语句为select 列名称1，列名称2 from 表名 where 列名称=列值
            else {
                String[] key_value = condition.get(1).split("=");
                String key = key_value[0];
                //如果表没有建立主键索引或者不是通过主键查询，调用未创建索引的方法
                returnList = selectFromTb(dbName, tbName, columns, condition);
            }
        }
        return returnList;
    }

    public static Object select2(String dbName, List<String> tbName, List<String> columns, List<String> condition) throws DocumentException {
        Object returnVal;

        //数据库是否为空
//        if (Is_Lg.isDatabaseEmpty()) {
//            return;
//        }
        //表是否存在
//        File config_file = Is_Lg.isTable(dbName, tbName[0]);
//        if (config_file == null) {
//            return;
//        }

        //列名称为空，则查询语句为select * from 表名/select * from 表名 where 列名称=列值
        if (columns == null) {
            //条件为空，则查询语句为select * from 表名(连接查询一般条件不为空)
            returnVal = selectAllFromTb(dbName, tbName, condition);
        }
        else {
            returnVal = selectFromTb(dbName, tbName, columns, condition);
        }

        return returnVal;
    }


    //select * from 表名
    public static List<Map<String, String>> selectFromTb(String dbName, String tbName) throws DocumentException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        //若表存在，则得到最后一张子表的下标
        String file_num = Is_Lg.lastFileName(dbName, tbName);

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            //解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            //获得根节点
            Element rootElement = document.getRootElement();
            //获得节点名为tbName的节点List
            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                // 只用于存储键值对并返回，与逻辑无关
                HashMap<String, String> map = new HashMap<String, String>();

                Element elementNode = (Element) node;
                List<Attribute> list = elementNode.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    System.out.print(attribute.getName() + "=" + attribute.getText() + " ");

                    map.put(attribute.getName(), attribute.getText());
                }
                returnList.add(map);
            }
        }
        return returnList;
    }

    //select * from 表名 where 列名称=列值
    public static List<Map<String, String>> selectAllFromTb(String dbName, String tbName, List<String> tmp1) throws DocumentException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        //若表存在，则得到表的最后一个文件下标
        String file_num = Is_Lg.lastFileName(dbName, tbName);
        //标记是否找到记录
        boolean condition_find = false;
        boolean find;
        //存where条件的condition数组
        String[] condition = tmp1.get(1).split("=");
        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");

            //解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                // 只用于存储键值对并返回，与逻辑无关
                HashMap<String, String> map = new HashMap<String, String>();

                find = false;
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        condition_find = true;
                        find = true;
                        break;
                    }
                }
                if (find) {
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        System.out.print(attribute.getName() + "=" + attribute.getText() + " ");

                        map.put(attribute.getName(), attribute.getText());
                    }
                    System.out.println();
                    returnList.add(map);
                }
            }
        }

        if (!condition_find) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", "未找到要求数据");
            returnList.add(map);
        }
        return returnList;
    }

    //select * from 表名1,表名2 where 列名称1=列名称2
    public static List<Map<String, String>> selectAllFromTb(String dbName, List<String> tbName, List<String> tmp1) throws DocumentException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        // 获取连接语句中的表名和连接条件
        String[] joinConditions = tmp1.get(0).split("=");

        // 存储每个表的最后一个文件下标
        Map<String, String> tableFileNums = new HashMap<String, String>();
        for (String tableName : tbName) {
            String fileNum = Is_Lg.lastFileName(dbName, tableName);
            tableFileNums.put(tableName, fileNum);
        }


        // 标记是否找到记录
        boolean conditionFind = false;

        for (int j = Integer.parseInt(tableFileNums.get(tbName.get(0))); j >= 0; j--) {
            String num = String.valueOf(j);
            File file = new File("./minidata/" + dbName + "/" + tbName.get(0) + "/" + tbName.get(0) + num + ".xml");

            // 解析表1的XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName.get(0));

            for (Node node : nodes) {
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();

                // 获取连接字段的值
                String joinValue = null;
                for (Attribute attribute : list) {
                    joinValue = attribute.getText();
                    break;

                }
                //System.out.println(joinValue);
                if (joinValue != null) {
                    // 在表2中查找匹配的记录
                    boolean matchFound = false;
                    for (int k = Integer.parseInt(tableFileNums.get(tbName.get(1))); k >= 0; k--) {
                        String num2 = String.valueOf(k);
                        File file2 = new File("./minidata/" + dbName + "/" + tbName.get(1) + "/" + tbName.get(1) + num2 + ".xml");

                        // 解析表2的XML
                        SAXReader reader2 = new SAXReader();
                        Document document2 = reader2.read(file2);
                        Element rootElement2 = document2.getRootElement();

                        List<Node> nodes2 = rootElement2.selectNodes(tbName.get(1));

                        for (Node node2 : nodes2) {
                            Element node3 = (Element) node2;
                            List<Attribute> list2 = node3.attributes();

                            HashMap<String, String> map = new HashMap<String, String>();

                            // 检查连接字段的值是否匹配
                            for (Attribute attribute2 : list2) {
                                if (attribute2.getText().equals(joinValue)) {
                                    conditionFind = true;
                                    //System.out.print(attribute2.getName() + "=" + attribute2.getText() + " ");

                                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                                        Attribute attribute = (Attribute) i.next();
                                        System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                                        map.put(attribute.getName(), attribute.getText());
                                    }

                                    for (Iterator i = list2.iterator(); i.hasNext(); ) {
                                        Attribute attribute = (Attribute) i.next();
                                        if (attribute.getText().equals(joinValue)) {
                                            continue;
                                        }
                                        System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                                        map.put(attribute.getName(), attribute.getText());
                                    }
                                    System.out.println();
                                    returnList.add(map);
                                }

                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!conditionFind) {
            System.out.println("未找到匹配的记录");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", "未找到匹配的记录");
            returnList.add(map);
        }

        return returnList;
    }

    //select 列名称1，列名称2 from 表名
    public static List<Map<String, String>> selectFromTb(String dbName, String tbName, List<String> tmp1) throws DocumentException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        //若表存在，则得到表的最后一个文件下标
        String file_num = Is_Lg.lastFileName(dbName, tbName);
        //标记是否找到列
        boolean columns_find = false;

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            //解析XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            //遍历所有节点
            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                HashMap<String, String> map = new HashMap<String, String>();

                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    for (int k = 0; k < tmp1.size(); k++) {
                        if (attribute.getName().equals(tmp1.get(k))) {
                            columns_find = true;
                            System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                            map.put(attribute.getName(), attribute.getText());
                            break;
                        }
                    }
                }
                System.out.println();
                returnList.add(map);
            }

        }
        if (!columns_find) {
            System.out.println("未找到列");

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", "未找到列");
            returnList.add(map);
        }

        return returnList;
    }

    //select 列名称1，列名称2 from 表名 where 列名称=列值
    public static List<Map<String, String>> selectFromTb(String dbName, String tbName, List<String> tmp1, List<String> tmp2) throws DocumentException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        //若表存在，则得到表的最后一个文件下标
        String file_num = Is_Lg.lastFileName(dbName, tbName);
        //存where条件的condition数组
        String[] condition = new String[0];
        condition = tmp2.get(1).split("=");
        boolean condition_find = false;
        boolean element_find = false;
        boolean find1;

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./minidata/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            //解析XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                HashMap<String, String> map = new HashMap<String, String>();

                find1 = false;
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        condition_find = true;
                        find1 = true;
                        break;
                    }
                }
                if (find1) {
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        for (int k = 0; k < tmp1.size(); k++) {
                            if (attribute.getName().equals(tmp1.get(k))) {
                                element_find = true;
                                System.out.print(attribute.getName() + "=" + attribute.getText() + " ");

                                map.put(attribute.getName(), attribute.getText());
                                break;
                            }
                        }
                    }
                    System.out.println();
                    returnList.add(map);
                }
            }
        }
        if (!condition_find) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result",  "未找到条件");
            returnList.add(map);
        }
        if (condition_find && !element_find) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result",  "未找到列");
            returnList.add(map);
        }

        return returnList;
    }

    public static List<Map<String, String>> selectFromTb(String dbName, List<String> tbName, List<String> tmp2, List<String> tmp1) throws DocumentException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        // 获取连接语句中的表名和连接条件
        String[] joinConditions = tmp1.get(0).split("=");

        // 存储每个表的最后一个文件下标
        Map<String, String> tableFileNums = new HashMap<String, String>();
        for (String tableName : tbName) {
            String fileNum = Is_Lg.lastFileName(dbName, tableName);
            tableFileNums.put(tableName, fileNum);
        }


        // 标记是否找到记录
        boolean conditionFind = false;

        for (int j = Integer.parseInt(tableFileNums.get(tbName.get(0))); j >= 0; j--) {
            String num = String.valueOf(j);
            File file = new File("./minidata/" + dbName + "/" + tbName.get(0) + "/" + tbName.get(0) + num + ".xml");

            // 解析表1的XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName.get(0));

            for (Node node : nodes) {
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();

                // 获取连接字段的值
                String joinValue = null;
                for (Attribute attribute : list) {
                    joinValue = attribute.getText();
                    break;

                }
                //System.out.println(joinValue);
                if (joinValue != null) {
                    // 在表2中查找匹配的记录
                    boolean matchFound = false;
                    for (int k = Integer.parseInt(tableFileNums.get(tbName.get(1))); k >= 0; k--) {
                        String num2 = String.valueOf(k);
                        File file2 = new File("./minidata/" + dbName + "/" + tbName.get(1) + "/" + tbName.get(1) + num2 + ".xml");

                        // 解析表2的XML
                        SAXReader reader2 = new SAXReader();
                        Document document2 = reader2.read(file2);
                        Element rootElement2 = document2.getRootElement();

                        List<Node> nodes2 = rootElement2.selectNodes(tbName.get(1));

                        HashMap<String, String> map = new HashMap<String, String>();

                        for (Node node2 : nodes2) {
                            Element node3 = (Element) node2;
                            List<Attribute> list2 = node3.attributes();

                            // 检查连接字段的值是否匹配
                            for (Attribute attribute2 : list2) {
                                if (attribute2.getText().equals(joinValue)) {
                                    conditionFind = true;
                                    //System.out.print(attribute2.getName() + "=" + attribute2.getText() + " ");

                                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                                        Attribute attribute = (Attribute) i.next();
                                        if (!tmp2.contains(attribute.getName())) {
                                            continue;
                                        }
                                        System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                                        map.put(attribute.getName(), attribute.getText());
                                    }

                                    for (Iterator i = list2.iterator(); i.hasNext(); ) {
                                        Attribute attribute = (Attribute) i.next();
                                        if (!tmp2.contains(attribute.getName())) {
                                            continue;
                                        }
                                        if (attribute.getText().equals(joinValue)) {
                                            continue;
                                        }
                                        System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                                        map.put(attribute.getName(), attribute.getText());
                                    }
                                    System.out.println();
                                    returnList.add(map);
                                }

                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!conditionFind) {
            System.out.println("未找到匹配的记录");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", "未找到匹配的记录");
            returnList.add(map);
        }

        return returnList;
    }
}
