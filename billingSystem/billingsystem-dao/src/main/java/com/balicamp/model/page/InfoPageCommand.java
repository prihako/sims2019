package com.balicamp.model.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.balicamp.model.ui.KeyValue;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class InfoPageCommand implements Serializable {
	private static final long serialVersionUID = 1166446566477932767L;
	
	private String title;
	private List<String> messageList;
	private List<KeyValue> messageTableList;
	
	private String prevPage;
	private List<InfoPageButton> infoPageButtonList;
	
	public InfoPageCommand(){
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(String prevPage) {
		this.prevPage = prevPage;
	}
	
	public void addInfoPageButton(InfoPageButton infoPageButton) {
		if ( infoPageButtonList == null )
			infoPageButtonList = new ArrayList<InfoPageButton>();
		infoPageButtonList.add(infoPageButton);
	}
	
	public List<InfoPageButton> getInfoPageButtonList() {
		return infoPageButtonList;
	}
	public void setInfoPageButtonList(List<InfoPageButton> infoPageButtonList) {
		this.infoPageButtonList = infoPageButtonList;
	}
	
	public static class InfoPageButton implements Serializable {
		private static final long serialVersionUID = 8567042799440414925L;
		
		private String value;
		private String htmlPage;
		
		public InfoPageButton(){
		}

		public InfoPageButton(String value, String htmlPage){
			setValue(value);
			setHtmlPage(htmlPage);
		}
		
		public String getJsHtmlLink(){
			if ( htmlPage == null )
				return "";
			return "location.href='" + htmlPage + "'";
		}

		public String getValue() {
			return value;
		}
		public String getHtmlPage() {
			return htmlPage;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public void setHtmlPage(String htmlPage) {
			this.htmlPage = htmlPage;
		}
	}

	//messageList
	public void addMessage(String message) {
		if ( messageList == null )
			messageList = new ArrayList<String>();
		messageList.add(message);
	}
	public List<String> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

	//messageTableList
	public List<KeyValue> getMessageTableList() {
		return messageTableList;
	}
	public void setMessageTableList(List<KeyValue> messageTableList) {
		this.messageTableList = messageTableList;
	}
	public void addMessageTable(KeyValue keyValue) {
		if ( messageTableList == null ) 
			messageTableList =  new ArrayList<KeyValue>();
		
		messageTableList.add(keyValue);
	}


}
