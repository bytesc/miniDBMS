package function;

import factory.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lg {
    public static void main(String[] args) throws IOException, DocumentException {


//        System.out.println("欢迎进入My-Simple-Dbms,请先登录");
//        //声明一个变量a，记录用户输入错误次数，超过3此则退出系统
//        int a=3;
//        while(a>0) {
//            //获得用户输入ID和PASSWORD
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.println("请输入用户名：");
//            String name = bufferedReader.readLine();
//            System.out.println("请输入密码：");
//            String password = bufferedReader.readLine();
//
//            //验证请求登陆的用户信息
//            Document document = (Document) new SAXReader().read("./minidata/user/user.xml");
//            org.dom4j.Element node = (org.dom4j.Element) document.selectSingleNode("users/user[@name='" + name + "'and @password='" + password + "']");
//
//            if (node != null) {
//                System.out.println("登录成功");
//                a=0;
//            }
//            else {
//                System.out.println("用户名或密码错误，请重试");
//                a--;
//                if(a==0) {
//                System.out.println("输入用户名/密码错误3次，系统即将退出");
//                return;
//                }
//            }
//        }

        //加载索引文件到内存
        if (Is_Lg.need_loadIndex()) {
            Cre_Id.loadIndex();
        }

        Use_Db.dbName = "test";
        while (true) {
            System.out.println("请输入SQL语句：（您可以输入help以查询SQL语句帮助）");
            @SuppressWarnings("resource")
            Scanner input = new Scanner(System.in);
            String sql = input.nextLine();
//            /*
//             * 预处理:获得语句;
//             * 处理前后空格;
//             * 变小写;
//             * 处理中间多余的空格回车和特殊字符;
//             * 在末尾加特殊符号;
//             * 处理掉最后的;
//             */
//            //处理分行输入的问题，就是读;号才停止;
            //sql parse
            if (sql.equals("help")) {
                read_help();
                continue;
            }

            while (sql.lastIndexOf(";") != sql.length() - 1) {
                sql = sql + " " + input.nextLine();
            }

            sql = sql.trim(); // 去除字符串两侧的空白字符
            sql = sql.toLowerCase();//全部转为小写
            sql = sql.replaceAll("\\s+", " ");// 将sql中的连续空白字符替换为一个空格
            sql = sql.substring(0, sql.lastIndexOf(";"));// 去除SQL语句末尾的分号
            sql = "" + sql + " ENDOFSQL";
            System.out.println("1)SQL预处理结果: " + sql);

            List<List<String>> parameter_list = new ArrayList<List<String>>();//缓存sql body信息的list

            if (sql.equals("quit ENDOFSQL")) {
                return;
            } else {
                //将预处理后的SQL语句匹配SQL正则表达式，返回含有SQL的body信息的List
                try {
                    parameter_list = SingleSqlParserFactory.generateParser(sql);
                } catch (Exception e) {
                    e.printStackTrace();//异常处理，不用管

                }
                //根据SQL的body部分，调用相应的功能模块
                try {
                    PassingParametersFactory.dealParameters(parameter_list);
                } catch (Exception e) {
                    e.printStackTrace();//异常处理，不用管
                }
            }


        }
    }

    public static void read_help() throws DocumentException {//读取帮助文件
        File file = new File("./help.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        List<Node> nodes = document.getRootElement().selectNodes("help");
        for (Node node : nodes) {
            Element element = (Element) node;
            System.out.println(element.getText());
        }
    }
}
