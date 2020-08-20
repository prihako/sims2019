package id.co.sigma.mx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class Generator {
	public BasicDataSource ds;
	public JdbcTemplate jdbc;
	private final static String PATH = "D:/queryBtpns.sql";
	
	public Generator()
	{
		ds = new BasicDataSource();
		ds.setDriverClassName("org.postgresql.Driver");
		ds.setUrl("jdbc:postgresql://localhost/esb_btpns_uat?user=mx_usr&password=mx_pwd");
		jdbc = new JdbcTemplate(ds);
	}
	
	public String execute()
	{
		StringBuffer strBuff = new StringBuffer();
		//print out endpoint
		String sqlEndpoint = "select * from endpoints order by id";
		String sqlEndpointDetails = "select * from endpoint_details where endpoint_id = ?";
		String sqlEndpointRcs = "select * from endpoint_rcs where endpoint_id = ?";
		String sqlMappings = "select * from mappings where endpoint_id=?  order by id,parent_id nulls first";
		String sqlMappingDetails = "select * from mapping_details where mapping_id= ? order by going_out,id";
		SqlRowSet rowSetEndpoint = jdbc.queryForRowSet(sqlEndpoint);
		while(rowSetEndpoint.next())
		{
			strBuff.append("----------------------------------------------\n");
			strBuff.append("----Endpoint : " + rowSetEndpoint.getString("code") + "\n");
			strBuff.append("----------------------------------------------\n");
			
			String insertEndpoint = "insert into endpoints(code,name,state,node_id) select '"+rowSetEndpoint.getString("code")+"','"+rowSetEndpoint.getString("name")+"','"+rowSetEndpoint.getString("state")+"',"+rowSetEndpoint.getInt("node_id")+""
					+ "	WHERE NOT EXISTS (SELECT 1 FROM endpoints WHERE code='"+rowSetEndpoint.getString("code")+"');";
			strBuff.append(insertEndpoint+ "\n");
			strBuff.append("\n");
			
			SqlRowSet rowSetEndpointDetails = jdbc.queryForRowSet(sqlEndpointDetails,new Object[]{rowSetEndpoint.getObject("id")});
			if(rowSetEndpointDetails.next()){
				String insertEndpoint_details1 = "insert into endpoint_details(host, port, reconnect_delay, packager_file, signon_timeout,"
						+ "signon_sent,signon_required,echo_sent,echo_interval,echo_timeout,header_class,"
						+ "is_server,type,endpoint_id,signon_property,signoff_property,echo_property,custom_filter,handle_late_response) ";
				String insertEndpoint_details2 = "values (" 
						 + nullCheckString(rowSetEndpointDetails.getString("host")) + ","  + nullCheckString(rowSetEndpointDetails.getString("port")) 
						 + "," + nullCheckString(rowSetEndpointDetails.getString("reconnect_delay")) + ","  + nullCheckString(rowSetEndpointDetails.getString("packager_file"))
						 + ","  + nullCheckString(rowSetEndpointDetails.getString("signon_timeout") )+ ","  + rowSetEndpointDetails.getBoolean("signon_sent")
						 + ","  + rowSetEndpointDetails.getBoolean("signon_required") + ","  + rowSetEndpointDetails.getBoolean("echo_sent")
						 + ","  + nullCheckString(rowSetEndpointDetails.getString("echo_interval")) + ","  + nullCheckString(rowSetEndpointDetails.getString("echo_timeout"))
						 + ","  + nullCheckString(rowSetEndpointDetails.getString("header_class")) + ","  + rowSetEndpointDetails.getBoolean("is_server")
						 + ","  + nullCheckString(rowSetEndpointDetails.getString("type")) + ","  + "(select id from endpoints where code='"+rowSetEndpoint.getString("code") + "')"
						 + ","  + nullCheckString(rowSetEndpointDetails.getString("signon_property")) + ","  + nullCheckString(rowSetEndpointDetails.getString("signoff_property"))
						 + ","  + nullCheckString(rowSetEndpointDetails.getString("echo_property")) + ","  + nullCheckString(rowSetEndpointDetails.getString("custom_filter"))
						 + ","  + rowSetEndpointDetails.getBoolean("handle_late_response") 
						 + ");";
				strBuff.append(insertEndpoint_details1+insertEndpoint_details2+ "\n");
				
			}
			strBuff.append("\n");
			
			SqlRowSet rowSetEndpointRcs = jdbc.queryForRowSet(sqlEndpointRcs,new Object[]{rowSetEndpoint.getObject("id")});
			while(rowSetEndpointRcs.next()){
				String rcCheck = rowSetEndpointRcs.getString("rc")== null ? "rc is null" : "rc='"+rowSetEndpointRcs.getString("rc")+"'" ;
				String insertEndpointRcs = "insert into endpoint_rcs(endpoint_id, rc, description) select "
						+  "(select id from endpoints where node_id=0 and code='"+rowSetEndpoint.getString("code") + "'), " 
						+ nullCheckString(rowSetEndpointRcs.getString("rc")) + "," +  nullCheckString(rowSetEndpointRcs.getString("description"))
						+ " WHERE NOT EXISTS (SELECT 1 FROM endpoint_rcs where endpoint_id=(select id from endpoints where node_id=0 and code='"+rowSetEndpoint.getString("code") + "') and "+rcCheck+" );";
				strBuff.append(insertEndpointRcs+ "\n");
			}
			strBuff.append("\n");
			
			SqlRowSet rowSetMappings = jdbc.queryForRowSet(sqlMappings,new Object[]{rowSetEndpoint.getObject("id")});
			while(rowSetMappings.next())
			{
				String parentId= "null";
				if(rowSetMappings.getObject("parent_id")!=null)
				{
					String sqlMetMappingCode = "select code from mappings where id="+rowSetMappings.getInt("parent_id");					
					String mappingCode = jdbc.queryForObject(sqlMetMappingCode, String.class);
					parentId = "(select id from mappings where code='"+mappingCode+"')";
				}
				String insertMappings = "insert into mappings(endpoint_id,code,description,packager_id,parent_id) select "
						+ "(select id from endpoints where node_id=0 and code='"+rowSetEndpoint.getString("code")+"'),'"+rowSetMappings.getString("code")+"','"+rowSetMappings.getString("description")+"',"+rowSetMappings.getObject("packager_id")+","+parentId
						+ " WHERE NOT EXISTS (SELECT 1 FROM mappings where endpoint_id=(select id from endpoints where node_id=0 and code='"+rowSetEndpoint.getString("code")+"') and code='"+rowSetMappings.getString("code")+"');";
				strBuff.append(insertMappings+ "\n");
				SqlRowSet rowSetMappingDetails = jdbc.queryForRowSet(sqlMappingDetails,new Object[]{rowSetMappings.getObject("id")});
				while(rowSetMappingDetails.next())
				{
					String destination = rowSetMappingDetails.getObject("destination") == null ? "null" : "'"+rowSetMappingDetails.getString("destination")+"'" ;
					String destinationCheck = rowSetMappingDetails.getObject("destination") == null ? "destination is null" : "destination='"+rowSetMappingDetails.getString("destination")+"'" ;
					String conditions = rowSetMappingDetails.getObject("condition") == null ? "null" : "'"+rowSetMappingDetails.getString("condition")+"'" ;
					String conditionsCheck = rowSetMappingDetails.getObject("condition") == null ? "condition is null" : " condition ='"+rowSetMappingDetails.getString("condition")+"'" ;
					String insertMappingDetails = "insert into mapping_details(mapping_id,going_out,source,destination,condition) select "
							+ "(select m.id from mappings m,endpoints e where m.endpoint_id=e.id and e.node_id=0 and e.code='"+rowSetEndpoint.getString("code")+"' and m.code='"+rowSetMappings.getString("code")+"'),"+rowSetMappingDetails.getString("going_out")+",'"+rowSetMappingDetails.getString("source")+"',"+destination+","+conditions+""
							+ " WHERE NOT EXISTS (SELECT 1 FROM mapping_details where mapping_id=(select m.id from mappings m,endpoints e where m.endpoint_id=e.id and e.node_id=0 and e.code='"+rowSetEndpoint.getString("code")+"' and m.code='"+rowSetMappings.getString("code")+"') and going_out="+rowSetMappingDetails.getString("going_out")+" and source='"+rowSetMappingDetails.getString("source")+"' and "+destinationCheck+" and "+conditionsCheck+");";
					strBuff.append(insertMappingDetails+ "\n");
				}
				strBuff.append("\n");
			}
			
			strBuff.append("\n\n\n\n");
			
		}
		
		String sqlTransaction = "select * from transactions order by id";
		SqlRowSet rowSetTransaction = jdbc.queryForRowSet(sqlTransaction);
		while(rowSetTransaction.next())
		{
			strBuff.append("----------------------------------------------\n");
			strBuff.append("----Transaction : " + rowSetTransaction.getString("code")+ "\n");
			strBuff.append("----------------------------------------------\n");
			
			String insertTransaction = "insert into transactions(code,name) select '"+rowSetTransaction.getString("code")+"','"+rowSetTransaction.getString("name")+"' where NOT EXISTS (SELECT 1 FROM transactions where code='"+rowSetTransaction.getString("code")+"');";
			strBuff.append(insertTransaction);
			String sqlRoutes = "select * from routes where transaction_id =  ?  order by last_invocation_id nulls first,id";
			SqlRowSet rowSetRoutes = jdbc.queryForRowSet(sqlRoutes,new Object[]{rowSetTransaction.getObject("id")});
			while(rowSetRoutes.next())
			{
				strBuff.append("\n----------------------------------------------\n");
				strBuff.append("-----Routes : " + rowSetTransaction.getString("code")+ "\n");
				strBuff.append("----------------------------------------------\n");
				
				String conditions = rowSetRoutes.getObject("condition") == null ? null : rowSetRoutes.getString("condition");
				String lastInvocationId = null;
				String nextInvocationId = null;
				if(rowSetRoutes.getObject("last_invocation_id")!=null)
				{
					String sqlMetMappingCode = "select m.code,e.code as endpointCode from mappings m,endpoints e where e.node_id=0 and m.endpoint_id=e.id and m.id="+rowSetRoutes.getInt("last_invocation_id");
					Map ret = jdbc.queryForMap(sqlMetMappingCode);
					String mappingCode = ""+ret.get("code");
					String endpointCode = ""+ret.get("endpointCode");
					lastInvocationId = "(select m.id from mappings m,endpoints e where e.node_id=0 and m.endpoint_id=e.id and e.code='"+endpointCode+"' and m.code='"+mappingCode+"')";
				}
				if(rowSetRoutes.getObject("next_invocation_id")!=null)
				{
					String sqlMetMappingCode = "select m.code,e.code as endpointCode from mappings m,endpoints e where e.node_id=0 and m.endpoint_id=e.id and m.id="+rowSetRoutes.getInt("next_invocation_id");
					Map ret = jdbc.queryForMap(sqlMetMappingCode);
					String mappingCode = ""+ret.get("code");
					String endpointCode = ""+ret.get("endpointCode");
					nextInvocationId = "(select m.id from mappings m,endpoints e where e.node_id=0 and m.endpoint_id=e.id and e.code='"+endpointCode+"' and m.code='"+mappingCode+"')";
				}
				
				String lastInvocationIdCheck = lastInvocationId == null ? "last_invocation_id is null" : "last_invocation_id="+lastInvocationId;
				String conditionsCheck = conditions == null ? "condition is null" : "condition='"+conditions+"'";
				
				String insertRoutes = "insert into routes(transaction_id,last_invocation_id,condition,weight,next_invocation_id,synchronized,nexts_in_background) select "
						+ "(select id from transactions where code='"+rowSetTransaction.getString("code")+"'),"+lastInvocationId+","+nullCheckString(conditions)+","+rowSetRoutes.getInt("weight")+","+nextInvocationId+","+rowSetRoutes.getObject("synchronized")+","+rowSetRoutes.getObject("nexts_in_background")
						+ " WHERE NOT EXISTS (SELECT 1 FROM routes where transaction_id=(select id from transactions where code='"+rowSetTransaction.getString("code")+"') and "+lastInvocationIdCheck+" and "+conditionsCheck+" and weight="+rowSetRoutes.getInt("weight")+" and next_invocation_id="+nextInvocationId+");";
				strBuff.append(insertRoutes+ "\n");
				
				String sqlConditionings = "select * from conditionings where route_id = ? ";
				SqlRowSet rowSetConditionings = jdbc.queryForRowSet(sqlConditionings,new Object[]{rowSetRoutes.getObject("id")});
				while(rowSetConditionings.next())
				{
					String condition2 = rowSetConditionings.getObject("condition") == null ? "null" : "'"+rowSetConditionings.getString("condition")+"'" ;
					String condition2Check = rowSetConditionings.getObject("condition") == null ? "condition is null" : "condition='"+rowSetConditionings.getString("condition")+"'" ;
					String insertConditionings = "insert into conditionings(route_id,path,data,condition,is_final) select "
							+ "(select id from routes where transaction_id = (select id from transactions where code='"+rowSetTransaction.getString("code")+"') and last_invocation_id "+checkIfNull(lastInvocationId)+" and next_invocation_id = "+nextInvocationId+" and condition "+checkIfNull(conditions)+"),"
									+ "'"+rowSetConditionings.getString("path")+"','"+rowSetConditionings.getString("data")+"',"+condition2+","+rowSetConditionings.getObject("is_final")
							+ " WHERE NOT EXISTS (SELECT 1 FROM conditionings where route_id=(select id from routes where transaction_id = (select id from transactions where code='"+rowSetTransaction.getString("code")+"') and last_invocation_id "+checkIfNull(lastInvocationId)+" and next_invocation_id = "+nextInvocationId+" and condition "+checkIfNull(conditions)+") and path='"+rowSetConditionings.getString("path")+"' and data='"+rowSetConditionings.getString("data")+"' and is_final="+rowSetConditionings.getObject("is_final")+" and "+condition2Check+");";
					strBuff.append(insertConditionings+ "\n");
				}
			}
			
			strBuff.append("\n\n\n\n");
		}	
		
		return strBuff.toString();
	}
	
	public String nullCheckString(String str){
		if(str == null){
			return str;
		}else{
			return "'" + str + "'";
		}
	}
	
//	public String executeEndpointRcs(){
//		StringBuffer strBuff = new StringBuffer();
//		String sqlEndpoint = "select * from endpoints order by id";
//		String sqlEndpointRcs = "select * from endpoint_rcs where endpoint_id = ?";
//		SqlRowSet rowSetEndpoint = jdbc.queryForRowSet(sqlEndpoint);
//		while(rowSetEndpoint.next())
//		{
//			strBuff.append("-----endpoint_rcs : " + rowSetEndpoint.getObject("code") +  "--------------\n");
//			
//			SqlRowSet rowSetEndpointRcs = jdbc.queryForRowSet(sqlEndpointRcs,new Object[]{rowSetEndpoint.getObject("id")});
//			while(rowSetEndpointRcs.next()){
//				String rcCheck = rowSetEndpointRcs.getString("rc") == null ? "rc is null" : "rc='"+rowSetEndpointRcs.getString("rc")+"'" ;
//				
//				String insertEndpointRcs = "insert into endpoint_rcs(endpoint_id, rc, description) select "
//						+  "(select id from endpoints where node_id=0 and code='"+rowSetEndpoint.getString("code") + "'), " 
//						+ nullCheckString(rowSetEndpointRcs.getString("rc")) + "," +  nullCheckString(rowSetEndpointRcs.getString("description"))
//						+ " WHERE NOT EXISTS (SELECT 1 FROM endpoint_rcs where endpoint_id=(select id from endpoints where node_id=0 and code='"+rowSetEndpoint.getString("code") + "') and "+rcCheck+");";
//				strBuff.append(insertEndpointRcs+ "\n");
//			}
//			
//			strBuff.append("\n");
//			
//		}
//		return strBuff.toString();
//	}
	
	public String executeEndpointRcsInternalMX(){
		StringBuffer strBuff = new StringBuffer();
		String sqlEndpointRcs = "select * from endpoint_rcs where endpoint_id is null";
		SqlRowSet rowSetEndpointRcs = jdbc.queryForRowSet(sqlEndpointRcs);
		
		strBuff.append("-----endpoint rcs Internal ESB--------------\n");
		while(rowSetEndpointRcs.next())
		{
			String rcCheck = rowSetEndpointRcs.getString("rc") == null ? "rc is null" : "rc='"+rowSetEndpointRcs.getString("rc")+"'" ;
			
			String insertEndpointRcs = "insert into endpoint_rcs(endpoint_id, rc, description) select "
					+  null + ", " 
					+ nullCheckString(rowSetEndpointRcs.getString("rc")) + "," +  nullCheckString(rowSetEndpointRcs.getString("description"))
					+ " WHERE NOT EXISTS (SELECT 1 FROM endpoint_rcs where endpoint_id is null and "+rcCheck+");";
			strBuff.append(insertEndpointRcs+ "\n");
		}
		strBuff.append("\n");
		
		return strBuff.toString();
	}
	
	public String executeRcMappings(){
		StringBuffer strBuff = new StringBuffer();
		String sqlEndpoint = "select * from endpoints order by id";
//		String sqlEndpoint = "select * from endpoints where code = 'tmn01' order by id";
		String sqlEndpointRcs = "select * from endpoint_rcs where endpoint_id = ?";
		String sqlEndpointRcsId = "select * from endpoint_rcs where id = ?";
		String sqlRcMappingsSource = "select * from rc_mappings where source_rc_id = ?";
		String sqlRcMappingsTarget = "select * from rc_mappings where target_rc_id = ? ";
		SqlRowSet rowSetEndpoint = jdbc.queryForRowSet(sqlEndpoint);
		while(rowSetEndpoint.next())
		{
			strBuff.append("-----rc_mappings : " + rowSetEndpoint.getObject("code") +  "--------------\n");
			SqlRowSet rowSetEndpointRcs = jdbc.queryForRowSet(sqlEndpointRcs,new Object[]{rowSetEndpoint.getObject("id")});
			while(rowSetEndpointRcs.next()){
//				String insertEndpointRcs = "insert into endpoint_rcs(endpoint_id, rc, description) values ("
//						+  "(select id from endpoints where code='"+rowSetEndpoint.getString("code") + "'), " 
//						+ nullCheckString(rowSetEndpointRcs.getString("rc")) + "," +  nullCheckString(rowSetEndpointRcs.getString("description"))
//						+ ")";
//				strBuff.append(insertEndpointRcs);
				
				SqlRowSet rowSetRcMappings = jdbc.queryForRowSet(sqlRcMappingsTarget,new Object[]{rowSetEndpointRcs.getObject("id")});
				while(rowSetRcMappings.next()){
					SqlRowSet rowSetSourceRcId =  jdbc.queryForRowSet(sqlEndpointRcsId,new Object[]{rowSetRcMappings.getInt("source_rc_id")});
					rowSetSourceRcId.next();
					String insertRcMappings = "insert into rc_mappings(source_rc_id, target_rc_id) select "
							+  "(select id from endpoint_rcs where description " + checkIfNull(rowSetSourceRcId.getString("description")) + " and rc " + checkIfNull(rowSetSourceRcId.getString("rc")) + "), "
							+ "(select id from endpoint_rcs where endpoint_id = (select id from endpoints where node_id=0 and code = '" + rowSetEndpoint.getObject("code") + "') and rc = " +  nullCheckString(rowSetEndpointRcs.getString("rc")) + ")"
									+ " where NOT EXISTS (SELECT 1 FROM rc_mappings where source_rc_id=(select id from endpoint_rcs where description " + checkIfNull(rowSetSourceRcId.getString("description")) + " and rc " + checkIfNull(rowSetSourceRcId.getString("rc")) + ") and target_rc_id=(select id from endpoint_rcs where endpoint_id = (select id from endpoints where node_id=0 and code = '" + rowSetEndpoint.getObject("code") + "') and rc = " +  nullCheckString(rowSetEndpointRcs.getString("rc")) + "));";
					strBuff.append(insertRcMappings+ "\n");
				}
				
				SqlRowSet rowSetRcMappings2 = jdbc.queryForRowSet(sqlRcMappingsSource,new Object[]{rowSetEndpointRcs.getObject("id")});
				while(rowSetRcMappings2.next()){
					SqlRowSet rowSetTargetRcId =  jdbc.queryForRowSet(sqlEndpointRcsId,new Object[]{rowSetRcMappings2.getInt("target_rc_id")});
					rowSetTargetRcId.next();
					String insertRcMappings = "insert into rc_mappings(source_rc_id, target_rc_id) select "
							+ "(select id from endpoint_rcs where endpoint_id = (select id from endpoints where node_id=0 and code = '" + rowSetEndpoint.getObject("code") + "') and rc " + checkIfNull(rowSetEndpointRcs.getString("rc")) + "),"
							+  "(select id from endpoint_rcs where description " + checkIfNull(rowSetTargetRcId.getString("description")) + " and rc = " + nullCheckString(rowSetTargetRcId.getString("rc")) + ")"
									+ " where not exists (select 1 from rc_mappings where source_rc_id=(select id from endpoint_rcs where endpoint_id = (select id from endpoints where node_id=0 and code = '" + rowSetEndpoint.getObject("code") + "') and rc " + checkIfNull(rowSetEndpointRcs.getString("rc")) + ") and target_rc_id=(select id from endpoint_rcs where description " + checkIfNull(rowSetTargetRcId.getString("description")) + " and rc = " + nullCheckString(rowSetTargetRcId.getString("rc")) + "));";
					strBuff.append(insertRcMappings+ "\n");
				}
				
			}
			strBuff.append("\n");
			strBuff.append("\n");
		}
		
		return strBuff.toString();
	}
	
	public String checkIfNull(String str){
		if(str == null){
			return "is null";
		}else{
			return "= '" + str + "'"; 
		}
	}
	
	public static void main(String[] args) {
		Generator gen = new Generator();

		try {
			File file = new File(PATH);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(gen.execute() + gen.executeEndpointRcsInternalMX() + gen.executeRcMappings());
//			bw.write(gen.executeRcMappings());
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
