package com.briup.env.main;

import com.briup.env.server.ServerImp;

public class ServerMain {
	public static void main(String[] args) {
		ServerImp si = new ServerImp();
		try {
			si.reciver();
			si.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
