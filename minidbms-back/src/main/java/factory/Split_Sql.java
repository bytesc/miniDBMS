package factory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import sqlparser.*;

/*
 * 工厂类:用于创建不同的BaseSingleSqlParser的实例，并对每句sql进行解析；
 */
public class Split_Sql {
	public static List<List<String>> generateParser(String sql)
	{
		Base_P tmp = null;

		if(contains(sql,"(create database)([^\\n]+)")) {// 判断字符串sql是否包含"create database"以及之后的任意字符
		System.out.println("进入sqlparser：create database");
		tmp = new Cre_Db_P(sql);
		}
		else if(contains(sql,"(drop database)(.+)"))
		{
			System.out.println("进入sqlparser：drop database");
			tmp = new Drp_Db_P(sql);

		}
		else if(contains(sql,"(show databases)"))
		{
			System.out.println("进入sqlparser：show databases");
			tmp = new Show_Db_P(sql);
		}
		else if(contains(sql,"(show tables)"))
		{
			System.out.println("进入sqlparser：show tables");
			tmp = new Show_Tb_P(sql);
		}
		else if(contains(sql,"(use database)(.+)"))
		{
			System.out.println("进入sqlparser：use database");
			tmp = new Use_Db_P(sql);
		}
		else if(contains(sql,"(create table)(.+)"))
		{
			System.out.println("进入sqlparser：create table");
			tmp = new Cre_Tb_P(sql);

		}
		else if(contains(sql,"(insert into)(.+)(values)(.+)"))
		{
			System.out.println("进入sqlparser：insert into");
			tmp = new Ist_Sql_P(sql);

		}
		else if(contains(sql,"(insert into)(.+)(valuess)(.+)()"))
		{
			System.out.println("进入sqlparser：insert into where");
			tmp = new Ist_Sct_Sql_P(sql);

		}
		else if(contains(sql,"(select \\* from)(.+)"))
		{
			if(contains(sql,"(select \\* from)(.+)(where)(.+)")){
				System.out.println("进入sqlparser：select * from where");
				tmp=new Sct_All_Where_P(sql);
			}
			else {
				System.out.println("进入sqlparser：select * from");
				tmp = new Sct_All_P(sql);
			}

		}
		else if(contains(sql,"(select)(.+)(from)(.+)"))
		{

			System.out.println("进入sqlparser：select from");
			tmp = new Sct_P(sql);

		}

		else if(contains(sql,"(delete from)(.+)"))
		{
			System.out.println("进入sqlparser：delete from");
			tmp = new Del_Sql_P(sql);

		}

        else if(contains(sql,"(drop table)(.+)"))
        {
            System.out.println("进入sqlparser：drop table");
            tmp = new Drp_Tb_Sql_P(sql);

        }
		else if(contains(sql,"(update)(.+)(set)(.+)"))
		{
			System.out.println("进入sqlparser：update set");
			tmp = new Up_P(sql);

		}
		else if(contains(sql,"(create index on)(.+)")){
			System.out.println("进入sqlparser：create index on");
			tmp=new Cre_Id_P(sql);
		}
		else if(contains(sql,"(drop index on)(.+)")){
			System.out.println("进入sqlparser：drop index on");
			tmp=new Drp_Id_P(sql);
		}
		else if(contains(sql,"(create user)")){
			System.out.println("进入sqlparser：create user");
			tmp=new Cre_User_P(sql);
		}
		else if(contains(sql,"(alter table)(.+)(add)")){
			System.out.println("进入sqlparser：add");
			tmp=new Add_Tb_P(sql);

		}
		//[[alter table, tablename], [drop, conlums]]
		else if(contains(sql,"(alter table)(.+)(drop)")){
			System.out.println("进入sqlparser：delete table");
			tmp=new Drp_Tb_P(sql);

		}//
		else if(contains(sql,"(alter table)(.+)(modify)")){
			System.out.println("进入sqlparser：modify table");
			tmp=new Mod_Tb_P(sql);

		}

		else if(contains(sql,"(rename table)")){
			System.out.println("进入sqlparser：rename table");
			tmp=new Re_Tb_P(sql);
		}
		else
		{
			System.out.println("SQL语句有误，请重新输入");
			return null;
		}
		return tmp.splitSql2Segment();//注意此处是个方法，返回的是一个List<List<String>>

	}
	/** //**
	 　* 看word是否在lineText中存在，支持正则表达式
	 　* @param sql:要解析的sql语句
	 　* @param regExp:正则表达式
	 　* @return
	 　*/
	private static boolean contains(String sql,String regExp)
	{
		Pattern pattern=Pattern.compile(regExp,Pattern.CASE_INSENSITIVE);
		Matcher matcher=pattern.matcher(sql);
		return matcher.find();
	}
}
