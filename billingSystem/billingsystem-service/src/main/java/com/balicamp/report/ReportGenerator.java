package com.balicamp.report;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;


public class ReportGenerator {

	private boolean init = false;
	private static final String REPORT_TIPE_TXT ="TXT";
	private static final String REPORT_TIPE_CSV ="CSV";
	
	private  void init(){
		if(!init){
			try {
				Velocity.setProperty(Velocity.RESOURCE_LOADER, "class");			
				Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
				Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, ClasspathResourceLoader.class.getName());        
		        
				Velocity.init();
				init = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private StringWriter generate(ReportContent content,ReportHeader header, String tipe) {				
		try {
			if(!init){init();}

			VelocityContext context = new VelocityContext();
			
			/* sementara baru report TXT yang berisi header */
			if(null != header){
				context.put("header", header);
				content.setHeader(true);				
			}
			
			context.put("content", content);			
			
			String templateName = (tipe.equals(REPORT_TIPE_CSV)?"reportCSV.vm":"reportTXT.vm");
			Template template = Velocity.getTemplate(templateName);
			
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			return writer;
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * fungsi generate report  format txt tanpa header
	 * @param content content dari report yang akan di generate
	 * @return
	 */
	@Deprecated
	public StringWriter generateTXT(ReportContent content) {
		return generate(content,null, REPORT_TIPE_TXT);
	}
	
	/**
	 * fungsi generate report  format CSV tanpa header
	 * @param content content dari report yang akan di generate
	 * @return
	 */
	@Deprecated
	public StringWriter generateCSV(ReportContent content) {
		return generate(content, null,REPORT_TIPE_CSV);
	}
	
	
	/**
	 * fungsi generate report  format txt dilengkapi header
	 * @param content content dari report yang akan di generate
	 * @return
	 */
	public StringWriter generateTXT(ReportContent content, ReportHeader header) {
		return generate(content,header, REPORT_TIPE_TXT);
	}
	
	/**
	 * fungsi generate report  format CSV tanpa header
	 * @param content content dari report yang akan di generate
	 * @return
	 */
	public StringWriter generateCSV(ReportContent content, ReportHeader header) {
		return generate(content, header,REPORT_TIPE_CSV);
	}

	public static void main(String[] args) {
		ReportGenerator rg = new ReportGenerator();
		
		ReportContent content = new ReportContent();
		ReportHeader header = new ReportHeader();
		header.setJudul("JUUUDDDDDDUUULLLLLL");
		content.setLine("-----------------------------------------------");
		StringWriter writer = rg.generateTXT(content, null);
		System.out.println(writer.toString());
		
	}

}

	
