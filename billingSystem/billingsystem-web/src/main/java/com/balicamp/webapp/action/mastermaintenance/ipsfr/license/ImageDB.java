package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;



public abstract class ImageDB {
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";

	public static byte[] loadImage(String imageId) {
		FileInputStream fileInputStream = null;
		try{
			File file = new File(DIRECTORY_DOCUMENT + imageId);
			fileInputStream = new FileInputStream(file.getAbsolutePath());
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
//		check if fileInputStream null or not, if null do nothing
		if(fileInputStream != null){
			InputStream input = fileInputStream;
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			
			for (;;) {
				try {
					int noBytesRead = input.read(buf);
					if (noBytesRead == -1) {
						return output.toByteArray();
					}
						output.write(buf, 0, noBytesRead);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}else{
			return null;
		}
		
	}
	
}