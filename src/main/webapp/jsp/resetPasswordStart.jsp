<%@ page language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<html>
<s:head/>
<head>
<meta name="viewport" content="width=device-width">
<title><s:text name="title.resetPasswordStart"/></title>
<base>
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

<form action="resetPasswordStartProcess" method="post">
<table border="0" width="100%">
<tr><td align="center" colspan="2"><%=request.getRemoteUser() %></td></tr>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>

  <tr>
    <th align="right">
      <s:text name="prompt.email"/>
    </th>
    <td align="left">
    	<input type="text" name="email" size="65" maxlength="64"/>
    </td>
  </tr>

  <tr><td>&nbsp;</td></tr>

  <tr>
    <td align="right">
      <input type="submit" value=<s:text name="Submit"/> >
    </td>
    <td align="left">
      <input type="cancel" value=<s:text name="Cancel"/> onclick="document.forms.menuForm.submit()">
    </td>
  </tr>

</table>
</form>

<form style="display: none" name="menuForm" method="post" action="login"></form>


</body>
</html>
