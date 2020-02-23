package com.briup.env.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.briup.env.Configuration;
import com.briup.env.bean.Environment;
import com.briup.env.support.ConfigurationAware;
import com.briup.env.support.PropertiesAware;
import com.briup.env.util.Log;

@SuppressWarnings("all")
public class ServerImp  implements Server,PropertiesAware,ConfigurationAware {

	public static ArrayList<Socket> clients = new ArrayList<>();
	ServerSocket ss;
	private static boolean f = true;
//	private final ExecutorService pool;
	
	private int serverPort;
	
	private DBStoreImp dbi = new DBStoreImp();
	private Log logger;
	
	@Override
	public void reciver() throws Exception { 
		ss = new ServerSocket(serverPort);
		System.out.println("服务器已启动，端口号1220");
		System.out.println("等待客户端连接");
		while (f) {
			Socket client = ss.accept();
//			pool.execute(new ClientThread(ss.accept(),clients));
			clients.add(client);
			System.out.println("客户端已连接"); 
			// 创建线程并且启动 
			new ClientThread(client, clients).start();
		}
	}

	@Override
	  public void shutdown() throws Exception { 
		// TODO Auto-generated method stub
		for(Socket s:clients)
		{
			s.close();
		}
		ss.close();
	  }
	@SuppressWarnings("all")
	private class ClientThread extends Thread{
		// 线程任务：无线接收客户端文件
		private Socket client;
		private ArrayList<Socket> clients;

		public ClientThread(Socket client, ArrayList<Socket> clients) {
			super();
			this.client = client;
			this.clients = clients;
		}

		Object o = new Object();

		public void run() {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(client.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (o) {
				try {
					Collection<Environment> c = (Collection<Environment>) (ois.readObject());
					int i = 0;
					dbi.saveDB(c);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

