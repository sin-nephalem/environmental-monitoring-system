package com.briup.env.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;

import com.briup.env.bean.Environment;
import com.briup.env.util.JDBCUtil;

public class DBStoreImp implements DBStore{

	@Override
	public void saveDB(Collection<Environment> c) throws Exception {
		Connection conn = JDBCUtil.getConnection(); 
		PreparedStatement p = null;
		//通过表来找对应的插入指令
		int i = 0;//sql指令表名
		int j = 0;//判断是否表内无数据和是否为第一次找到对应数据
		int k = 1;//基准表名
		String sql = null;
		for(;k<=31;k++)
		{
		  for(Environment e:c)
		  {
			String str = e.getGather_date().toString();
			String s = str.substring(8, 10);
			i = Integer.parseInt(s);			
			if(i==k)
			{
				j++;
				if(j==1)
				{
					sql = "insert into E_DETAIL_"
						+i
						+"(name,srcId,desId,devId,sersorAddress,count,cmd,status,data,gather_date ) "
						+ "values(?,?,?,?,?,?,?,?,?,?)";
					p = conn.prepareStatement(sql);
				}
				p.setString(1, e.getName());
				p.setString(2, e.getSrcId());
				p.setString(3, e.getDesId());
				p.setString(4, e.getDevId());
				p.setString(5, e.getSersorAddress());
				p.setInt(6, e.getCount());
				p.setString(7, e.getCmd());
				p.setInt(8, e.getStatus());
				p.setFloat(9, e.getData());
				p.setTimestamp(10, e.getGather_date());
				p.addBatch();
			}
		}
		  if(j!=0)
		  {
			p.executeBatch();
			p.clearBatch();
			j=0;
		  }
	  }
		System.out.println("提交完毕");
		p.close();
		conn.close();
	}
}
