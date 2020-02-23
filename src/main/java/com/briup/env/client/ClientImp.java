package com.briup.env.client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

import com.briup.env.bean.Environment;

public class ClientImp implements Client {

	private String host = "127.0.0.1";
	private int port = 1221;
	
	@Override
	public void send(Collection<Environment> c) throws Exception {
		// TODO Auto-generated method stub
		Socket s = new Socket(host, port);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(c);
	}

}
