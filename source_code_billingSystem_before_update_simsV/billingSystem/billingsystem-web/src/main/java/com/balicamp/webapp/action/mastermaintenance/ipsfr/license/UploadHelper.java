package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tapestry.request.IUploadFile;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;

public class UploadHelper{
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";
	
	public static void saveFile(String fileName, IUploadFile file){
		FileOutputStream fos = null;
		
		File dir = new File("webapps");
		if(!dir.exists()){
			dir.mkdir();
		}
		
		File dir2 = new File("webapps/SKM");
		if(!dir2.exists()){
			dir2.mkdir();
		}
		
		try{
			InputStream fileInputStream = file.getStream();
			
			fos = new FileOutputStream(new  File(DIRECTORY_DOCUMENT + fileName));
			byte[] buffer = new byte[1024];  
            while (true) {            
                int length = fileInputStream.read(buffer);  
                if (length <  0) {
                    break;
                }
                fos.write(buffer, 0, length);               
            }
            
            fos.close();
        	fileInputStream.close();
		}catch(Exception e){
			System.out.println("____ERROR SAVING_____");
			e.printStackTrace();
		}
	}
	
	public static void deleteFile(String fileName, DocumentUpload docToDelete){

		try{
			File fileToDelete = new File(DIRECTORY_DOCUMENT + fileName);
			if(fileToDelete.exists()){
				fileToDelete.delete();
			}else{
				System.out.println("====================FIle not exists==================================");
			}
		}catch(Exception e){
			System.out.println("_______ERROR DELETING_______");
			e.printStackTrace();
		}
	}
	
	public static void copyFile(String fileName, IUploadFile file) throws IOException{
		InputStream input = null;
		OutputStream output = null;
		
		try {
			input = new FileInputStream(new  File("temporary" + File.separator + fileName));
			output = new FileOutputStream(new  File(DIRECTORY_DOCUMENT + fileName));
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		}catch(FileNotFoundException e){ 
			System.out.println("_____Error Copying File_____");
			e.printStackTrace();
		}finally {
			if(input != null){
				input.close();
			}
			
			if(output != null){
				output.close();
			}
			
			//after copying or if error occurs, delete file in temporary folder
			File fileToDelete = new File("temporary" + File.separator + file.getFileName());
			if(fileToDelete.exists()){
				fileToDelete.delete();
			}
			
		}
	}
	
	public static void saveFileToServerTemp(String fileName, IUploadFile file){
		
		File dir = new File("temporary");
		if(!dir.exists()){
			dir.mkdir();
		}

		FileOutputStream fos = null;

		try{
			InputStream fileInputStream = file.getStream();
			
			fos = new FileOutputStream(new  File("temporary" + File.separator + fileName));
			byte[] buffer = new byte[1024];  
            while (true) {            
                int length = fileInputStream.read(buffer);  
                if (length <  0) {
                    break;
                }
                fos.write(buffer, 0, length);               
            }
            
            fos.close();
        	fileInputStream.close();
		}catch(Exception e){
			System.out.println("Ada yg salah saat proses upload");
			e.printStackTrace();
		}
		
	}
	
	public static boolean checkDuplicateFileName(String fileName, IUploadFile file){
		boolean error = false;

		File filename = new File(DIRECTORY_DOCUMENT + fileName);
		
		if(filename.exists()){
			error = true;
		}
		
		return error;
	}
	
}
