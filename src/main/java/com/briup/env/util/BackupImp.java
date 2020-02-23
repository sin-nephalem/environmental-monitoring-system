package com.briup.env.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BackupImp implements Backup{
	private String filePath = "src/main/resources/bak/";
	@Override
	public Object load(String fileName, boolean del) throws Exception {
		File file = new File(filePath+fileName);
		if(!file.exists())
		{
			file.createNewFile();
			System.out.println("备份文件不存在");
			return null;
		}
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object obj = ois.readObject();
		if(del)
			file.delete();
		
		if(ois != null)ois.close();
		if(fis != null)fis.close();
		return obj;
	}

	@Override
	public void store(String fileName, Object obj, boolean append) throws Exception {
		FileOutputStream fos = new FileOutputStream(filePath+fileName,append);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);
		oos.flush();
		
		if(oos != null)oos.close();
		if(fos != null)fos.close();
	}

}
