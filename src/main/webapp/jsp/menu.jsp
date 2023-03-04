<%@ page language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<html>
<head>
<meta name="viewport" content="width=device-width">
<title><s:text name="app.title"/></title>
<base>
<s:head/>
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

<% if (request.isUserInRole("admin")) { %>
<s:a href="addUser"><s:text name="addUser"/></s:a> <br>
<% } %>
<%--user stuff (constraint in web.xml) --%>

<s:a href="changePassword"><s:text name="changePassword"/></s:a> <br>


<br>
<s:a href="viewPictures">cancel</s:a>

</body>
</html>
