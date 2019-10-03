<%@page import="java.util.Enumeration"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>KOMINFO-SDPPI [BILLING-SYSTEM]</title>
<link rel="icon" href="<c:url value="/images/favicon.ico" /> "/>
<link href="styles/mayora/login.css" rel="stylesheet" type="text/css" />
<script type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
</script>
</head>
<body>
	<%
	String loginMessage = request.getParameter("loginMessage");
	if ("loginStatus.fail.userBlock".equals(loginMessage)) {
		// do nothing
	} else if ("loginStatus.fail.userReset".equals(loginMessage)) {
		// do nothing
	} else if ("loginStatus.fail.concurrentLogin".equals(loginMessage)) {
		// do nothing
	} else if ("loginStatus.fail.invalidUserNameOrPassword".equals(loginMessage)) {
		// do nothing
	} else if ("loginStatus.fail.userBlockToken".equals(loginMessage)) {
		// do nothing
	} else if ("loginStatus.fail.userBlockAdmin".equals(loginMessage)) {
		// do nothing
	} else {
		loginMessage = "";
	}

	request.setAttribute("loginMessage", loginMessage);
%>
	<div id="wrapouter">
		<div id="header">
			<div id="login">
				<form id="loginForm" action="j_acegi_security_check"
					target="_parent" method="post">
					<ul class="links">
						<li>
							<input type="text" name="j_username" id="j_username"
								size="20" placeholder="User ID" tabindex="2" autocomplete="off"
								value="hendy" />
						</li>
						<li>
							<input type="password" name="j_password" id="j_password"
								size="20" placeholder="Password" tabindex="2" autocomplete="off"
								value="000000"  />
						</li>
						<li>
							<input type="submit" class="btn" name="login"
								value="<fmt:message key="button.login"/>" tabindex="3" />
						</li>
					</ul>
					<ul class="links">
						<li>
							<c:if
								test="${loginMessage ne null and loginMessage ne ''}">
								<div style="color: red;">
									<fmt:message key="${loginMessage}" />
								</div>
							</c:if>
						</li>
					</ul>
				</form>
			</div>
		</div>
		<!-- Content -->
		<div id="outer-login">
			<div id="banner">
				<img src="styles/mayora/images/banner.png" width="632"
					height="137" class="center" />
			</div>
			<!-- footer -->
			<div id="footer">				
		      <div>
		        <p class="footer">Direktorat Jenderal Sumber Daya dan Perangkat Pos dan Informatika &copy 2013</p>
		        <br />
		        <p class="footer">Baik Ditampilkan dengan Mozilla Firefox (3.6 Ke Atas)</p> 
		        <script type="text/javascript">
					function cekLoginStatus(){
						if ( window.opener != null ) {
							window.opener.location = "wellcome.html"
							window.close();
						}
					}
					cekLoginStatus();
				</script>
		      </div>
		      <div class="push-right"></div>
			</div>
		</div>
	</div>
</body>
</html>