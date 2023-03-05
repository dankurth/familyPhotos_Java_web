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
<title><s:text name="title.changePassword"/></title>
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

<form action="changePasswordProcess">
<table border="0" width="100%">
<tr><td align="center" colspan="2"><%=request.getRemoteUser() %></td></tr>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>

  <tr>
    <th align="right">
      <s:text name="prompt.currentPassword"/>
    </th>
    <td align="left">
    	<input type="password" name="currentPassword" size="27" maxlength="25"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      <s:text name="prompt.newPassword"/>
    </th>
    <td align="left">
    	<input type="password" name="newPassword" size="27" maxlength="25"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      <s:text name="prompt.verifyNewPassword"/>
    </th>
    <td align="left">
    	<input type="password" name="newPassword2" size="27" maxlength="25"/>
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

<form style="display: none" name="menuForm" method="post" action="menu"></form>

Password must contain at least one digit [0-9]<br>
Password must contain at least one lower case character [a-z]<br>
Password must contain at least one upper case character [A-Z]<br>
Password must contain at least one character from !@#&(){}[]:;',?*~$^+-=<><br>
Password must be at least 8 and no more than 24 characters long<br>
Password may contain only digits,letters or: !@#&(){}[]:;',?*~$^+-=<>


</body>
</html>
