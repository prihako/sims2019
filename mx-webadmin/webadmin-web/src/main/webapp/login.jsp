<%@page import="java.util.Enumeration"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Web Admin SDPPI</title>
<link href="styles/style-MXAdmin/login.css" rel="stylesheet" type="text/css" />
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
  	<div id="wrapper">
    	<div id="logo"><img src="styles/style-MXAdmin/images/logo.png" width="139" height="26" /></div>
        <div id="login">
				<form id="loginForm" action="j_acegi_security_check"
					target="_parent" method="post">
            <ul class="links">
              <li>
                	<input type="text" name="j_username" id="j_username"
								size="20" placeholder="User ID" tabindex="2" autocomplete="off" />
              </li>
              <li>
                	<input type="password" name="j_password" id="j_password"
								size="20" placeholder="Password" tabindex="2" autocomplete="off" />
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
  </div>		
	<!-- Content -->
  <div id="outer-login">
  <div id="banner"><img src="styles/mayora/images/login_banner.png" width="972" height="165" /></div>
  </div>
    <!-- footer -->
    <div id="footer">
<!--       <div id="wrapper"> -->
<!--        	  <h3>CONTACT US</h3> -->
<!--           <div class="footer-inside"> -->
<!--           	<div class="span4"> -->
<!--             <p><strong>Sigma Inno Office</strong><br /> -->
<!--                 Menara Dea, 8th Floor<br /> -->
<!--                 Kawasan Mega Kuningan<br /> -->
<!--                 Jl. Mega Kuningan Barat IX Kav E.4.3. No.1<br /> -->
<!--                 Jakarta 12950, Indonesia<br /> -->
<!--                 t. 62.21. 576 2150<br /> -->
<!--                 f. 62.21. 576 2155<br /> -->
<!--                 e. website@sigma.co.id</p> -->
<!--          	</div>  -->
<!--             <div class="span4"> -->
<!--             <p><strong>Sigma Wisma BCA</strong><br /> -->
<!--                 Gd. Wisma BCA Lt. Dasar<br /> -->
<!--                 Jl. Kapt. Subijanto Dj.<br /> -->
<!--                 Bumi Serpong Damai<br /> -->
<!--                 Tangerang 15322, Indonesia<	br /> -->
<!--                 T. 62.21. 256 57 700<br /> -->
<!--                 F. 62.21. 290 04 390 -->
<!--             </p>     -->
<!--             </div>   -->
<!--       		<div class="span3 push-right"> -->

<!-- 		     </div> -->
<!--            </div>   -->
<!--     	</div> -->
	</div>
</div>    
</body>
</html>