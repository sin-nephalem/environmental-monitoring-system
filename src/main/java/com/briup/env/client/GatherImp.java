package com.briup.env.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.briup.env.Configuration;
import com.briup.env.bean.Environment;
import com.briup.env.support.ConfigurationAware;
import com.briup.env.support.PropertiesAware;
import com.briup.env.util.Backup;
import com.briup.env.util.BackupImp;
import com.briup.env.util.Log;
import com.briup.env.util.LogImp;

public class GatherImp  implements Gather,PropertiesAware,ConfigurationAware{
	
	private Backup backup;
	private Log logger;
	
	private	static int g=0;
	private static int er=0;
	private static int w=0;
	private String dateFilePath;
	
//	  public static void main(String[] args) throws Exception { 
//		  String path = "src/main/resources/data-file"; 
//		  List<Environment> list = getData(path);
//		  int i; 
//		  for(i = 0; i < list.size() ; i++ ) 
//		  {
//			  System.out.println(list.get(i));
//			}
//		  	System.out.println(i); 
//		 }
	 
	
	@Override
	  public Collection<Environment> gather() throws Exception 
	  {
		List<Environment> list = getData(dateFilePath);
		return list; 
	  }
	 
	
	public /* static */ List<Environment> getData(String path) throws Exception
	{
		List<Environment> list = new ArrayList<>();
		
		InputStream is = getClass().getClassLoader().getResourceAsStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		int filelen =is.available();
		
		Integer len = (Integer)(backup.load("length.bak",Backup.LOAD_UNREMOVE ));
		if(len != null)
		{
			br.skip(len);
		}
		backup.store("length.bak", filelen+2,Backup.STORE_APPEND);
		
		String str = null;
		String[] strs = null ;

		while((str = br.readLine()) != null )
		{
			//System.out.println(str);
			strs = str.split("[|]");
			if(strs.length != 9)
			{
				logger.warn("异常数据："+str);
				logger.warn("跳过当前循环，进行下次循环");
				continue;
			}
			if(strs[3].equals("16"))
			{
				list.add(wenshidu(strs)[0]);
				list.add(wenshidu(strs)[1]);
				w++;
			}
			else if(strs[3].equals("256"))
			{
				list.add(guangzhao(strs));
				g++;
			}
			else if(strs[3].equals("1280"))
			{
				list.add(eryanghuatan(strs));
				er++;
			}
			else
				System.out.println("未知类型数据(16|206|1280):"+strs[3]);
		} 
		if(is != null) is.close();
		if(br != null) br.close();
		return list;
	}
	
	private /* static */ Environment eryanghuatan(String[] strs) {
		// TODO Auto-generated method stub
		Environment e = new Environment();
		
		e.setName("二氧化碳浓度");
		setEvery(e, strs);
		String str1 = strs[6].substring(0, 4);
		float f = (float)Integer.parseInt(str1, 16);
		e.setData(f);
		return e;
	}

	private /*static */ Environment guangzhao(String[] strs) {
		// TODO Auto-generated method stub
		
		Environment e = new Environment();
		
		e.setName("光照强度");
		setEvery(e, strs);
		String str1 = strs[6].substring(0, 4);
		float f = (float)Integer.parseInt(str1, 16);
		e.setData(f);
		
		return e;
	}

	public /* static */Environment[] wenshidu(String[] str)
	{
		Environment[] es = new Environment[2];
		Environment e1 = new Environment();
		Environment e2 = new Environment();
		
		e1.setName("温度");
		setEvery(e1, str);
		String str1 = str[6].substring(0, 4);
		float f = (((float)Integer.parseInt(str1, 16)*0.00268127f)-46.85F);
		e1.setData(f);
		
		e2.setName("湿度");
		setEvery(e2, str);
		String str2 = str[6].substring(4, 8);
		float f2 = (float)(((float)Integer.parseInt(str2, 16)*0.00190735)-6);
		e2.setData(f2);
		
		es[0]=e1;
		es[1]=e2;
		
		return es;
	}

	public int getG() {
		return g;
	}

	public int getEr() {
		return er;
	}

	public int getW() {
		return w;
	}

	public /* static */ void setEvery(Environment e,String[] strs)
	{
		e.setSrcId(strs[0]);
		e.setDesId(strs[1]);
		e.setDevId(strs[2]);
		e.setSersorAddress(strs[3]);
		e.setCount(Integer.parseInt(strs[4]));
		e.setCmd(strs[5]);
		e.setStatus(Integer.parseInt(strs[7]));
		e.setGather_date(new Timestamp(Long.parseLong(strs[8])));
	}


	@Override
	public void init(Properties p) throws Exception {
		// TODO Auto-generated method stub
		dateFilePath = p.getProperty("date-file-path");
	}


	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		// TODO Auto-generated method stub
		backup = configuration.getBackup();
		logger = configuration.getLogger();
	}
	
}
