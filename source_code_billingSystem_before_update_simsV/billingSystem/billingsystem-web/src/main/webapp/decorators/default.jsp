<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <title><decorator:title/></title>
        <decorator:head/>
		<style type="text/css">
		<!--
		@import url("styles/mayora/layout.css");
		html,body { height:100%;}
		-->
		</style>
		
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout.css'/>" />
        <script type="text/javascript" src="<c:url value='/scripts/mayora/tools.js'/>"></script>		
		<link rel="icon" href="<c:url value="/images/favicon.ico"/>"/>
		
		<script language="javascript">
			// function onRefresh with javascript status check
			function formRefresh(form) {
				if(renderIsOk){
					form.events.refresh();
				}
			}

			var timer;
			var counter;
			function timer(){counter = 600;	timer = self.setInterval("setTime()",1000);}
			function setTime() {
				if(counter-- < 0)	{	stopTimer();	}
			}
			function stopTimer()	{
				alert('SESSION TIMEOUT');
				timer = window.clearInterval(timer);
				parent.location.replace("login.jsp");
				<!--window.location="<c:url value='/afterLogout.html'/>";-->
							
			}
			timer();
        </script>
        
        <script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
		
		<!-- HTTP 1.1 -->
        <meta http-equiv="Cache-Control" content="no-store"/>
        <!-- HTTP 1.0 -->
        <meta http-equiv="Pragma" content="no-cache"/>
        <!-- Prevents caching at the Proxy Server -->
        <meta http-equiv="Expires" content="0"/>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/> <decorator:getProperty property="body.onunload" writeEntireProperty="true"/> >
	<decorator:body/>
</body>
</html>