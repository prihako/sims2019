package id.co.sigma.mx.project.ftpreconcile.junit;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.FileTransactionReceive;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class FTPTest extends PropertyPlaceholderConfigurer {

	private String ftpSettingFile;

	Properties mergedProperties;

	public Properties getMergedProperties() throws IOException {
		if (mergedProperties == null) {

			mergedProperties = mergeProperties();

		}
		return mergedProperties;

	}

	String packagerFile;

	/**
	 * @param packagerFile the packagerFile to set
	 */
	public void setPackagerFile(String packagerFile) {
		this.packagerFile = packagerFile;
	}

	public String getPackagerFile(){
		return packagerFile;
	}

	public static void main(String[] args){
		try {
			System.out.println("coba");
			System.out.println(new FTPTest().ftpSettingFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private static void propTest() throws IOException{
//				Properties p = PropertiesUtil.fromFile("/classes/ftp.properties");
//				System.out.println(p.get("ftp.local.address"));
//		ApplicationContext context =
//			new ClassPathXmlApplicationContext("/META-INF/spring/logics.xml");
//		FTPTest propertyConfigurer = (FTPTest) context.getBean("prop");
//		String b = propertyConfigurer.getMergedProperties().getProperty("ftp.server.address");
//				String a = (String) context.getBean("configuration");
//		System.out.println(b);
//				Configuration conf = admin.getConfiguration("my-persistent-id", null);
//	}

	//	public static void parsing(String file) throws SQLException, ClassNotFoundException {
	//
	//		try {
	//			java.io.FileInputStream fis = new java.io.FileInputStream(
	//					new java.io.File(file));
	//			byte[] buf = new byte[fis.available()];
	//			fis.read(buf);
	//			fis.close();
	//
	//			MT940 mt940 = new MT940();
	//			mt940.setContentFile(new String(buf));
	//
	//			if (MT940Parser.isValidFileTransac(mt940)) {
	//				System.out.println("valid file.");
	//				List<Transaction> listTRecordFile = mt940.getRecordTransaction();
	//				Object[] objectList = listTRecordFile.toArray();
	//				Transaction[] trans = Arrays.copyOf(objectList,objectList.length,Transaction[].class);
	//
	//
	//				for (Iterator i = listTRecordFile.iterator(); i.hasNext();) {
	//					Transaction t = (Transaction) i.next();
	//					JdbcUtil.saveTransactions(t);
	//					System.out.println("rec=" + i.next());
	//				}
	//
	//				// System.out.println("listTRecordFile.size() = " +
	//				// listTRecordFile.size());
	//				// System.out.println("list bean = \n" + listTRecordFile);
	//
	//			} else {
	//				System.out.println("invalid file.");
	//
	//			}
	//
	//		} catch (java.io.IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public static void ftpProceed() throws Exception{
//		FTPManager ftp = new FTPManager();

		String localDir = "C:/Documents and Settings/sigma/Desktop/";

//		String serverDir = "localhost";

//		String[] filesNames = ftp.listFilesNames(serverDir);

		String name = null;

//		for(int i=0 ; i<filesNames.length ; i++){
//			name = filesNames[i];
//			if(name.contains("1250011068953MT94020130430")){
//				break;
//			}
//		}

		File directoryFile = new File(localDir);
		if(!directoryFile.exists())
			directoryFile.mkdir();

//		ftp.retrieve(localDir+name, name, serverDir);

		java.io.FileInputStream fis = new java.io.FileInputStream(
				new java.io.File(localDir+name));
		byte[] buf = new byte[fis.available()];
		fis.read(buf);
		fis.close();

		FileTransactionReceive ftr = new FileTransactionReceive();
		ftr.setContentFileTransaction(buf);
		ftr.setFileNameTransaction(name);
		ftr.setUserName("sangbas");
		ftr.setKoreksiFlag("N");
		ftr.setTypeListener(PostelConstant.FTP);

		//		ProcessorMessage.processTransaction(ftr,false);

		//		saveFile(localDir,name);

		//		parsing(localDir+name);
	}

	/**
	 * @param ftpSettingFile the ftpSettingFile to set
	 */
	public void setFtpSettingFile(String ftpSettingFile) {
		this.ftpSettingFile = ftpSettingFile;
	}

	//	private static void saveFile(String localDir, String fileName) throws Exception {
	//		java.io.FileInputStream fis = new java.io.FileInputStream(
	//				new java.io.File(localDir+fileName));
	//		byte[] buf = new byte[fis.available()];
	//		fis.read(buf);
	//		fis.close();
	//
	//		TransactionFile tf = new TransactionFile();
	//		tf.setFileId(JdbcUtil.getSequenceValue("SEQ_TRANSACTION_FILE"));
	//		tf.setData(buf);
	//		tf.setFileName(fileName);
	//		tf.setFileSize(buf.length);
	//		tf.setListenerLogId(JdbcUtil.getSequenceValue("SEQ_TRANSACTION_FILE"));
	//		tf.setUserName("sangbas");
	//		tf.setKoreksiFlag("N");
	//
	//		JdbcUtil.saveTransactionFile(tf);
	//	}
}
