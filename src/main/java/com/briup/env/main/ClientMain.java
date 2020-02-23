package com.briup.env.main;

import java.util.Collection;

import com.briup.env.bean.Environment;
import com.briup.env.client.Client;
import com.briup.env.client.ClientImp;
import com.briup.env.client.Gather;
import com.briup.env.client.GatherImp;

public class ClientMain {
	public static void main(String[] args) {
		Gather gather = new GatherImp();
		Client client = new ClientImp();
		
		try {
			Collection<Environment> c = gather.gather();
			client.send(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
