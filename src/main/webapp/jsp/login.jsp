<%@ page language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<script>
function home() {    
	document.forms.homeForm.submit();
}
</script>

<html>
<head>
<meta name="viewport" content="width=device-width">
<title><s:text name="app.title"/></title>
<base>
<s:head />
</head>
<body bgcolor="white">

<s:if test="hasActionErrors()">
   <div>
      <s:actionerror/>
   </div>
</s:if>

<s:if test="hasActionMessages()">
<div>
<s:actionmessage/>
</div>
</s:if>
	
<form name="homeForm" method="post" action="logoff"></form>	

<form method="post" action="loginProcess">	
<table border="0" width="100%">

  <tr>
    <th align="right">
      <s:text name="prompt.username"/>
    </th>
    <td align="left">
    	<input type="text" name="username"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      <s:text name="prompt.password"/>
    </th>
    <td align="left">
    	<input type="password" name="password"/>
    </td>
  </tr>

  <tr>
    <td align="right">
      <input type="submit" value=<s:text name="Submit"/> >
    </td>
    <td align="left">
    	<input type="button" class="small" name="submit" onClick=home() value=<s:text name="Cancel"/> >
    </td>
  </tr>
</table>
</form>

</body>
</html>
