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
<link href="common/css/styles.css" rel="stylesheet">
<style>

body {
  overflow: hidden; 
}

.container {
   height: 100%;
   display: grid;
   grid-template-columns: 1fr;
   grid-template-rows: 20px 1fr 30px;
}

.header {
  padding: 1px;
}

.footer {
 font-style: italic;
 padding: 1px;
}

.body {
 display: grid;
 grid-template-columns: 1fr;
 overflow: hidden;
}

.content {
 overflow-y: scroll;
 overflow-x: hidden;
 padding: 1px;
 margin-top: 5px;
 margin-bottom: 5px;
}

</style>

<script>
function viewPictureDetails(n) {
	if (indexMax > -1) {
		document.forms.aForm.elements.index.value=n;
		document.forms.aForm.submit();
	}
}

var index = ${offset}
var indexMin = 0;
var indexMax = ${picturesSize-1};

function updateImages() {
if (index+${length}-1 < indexMax) document.images.next.src="common/images/arrow_small_right.gif";
	else document.images.next.src="common/images/arrow_small_right_disabled.gif";
	if (index > indexMin) document.images.prev.src="common/images/arrow_small_left.gif";
	else document.images.prev.src="common/images/arrow_small_left_disabled.gif";
// img down removed, apparently
//	if (indexMax > -1) document.images.down.src="/pics_struts/common/images/arrow_small_down.gif";
//	else document.images.down.src="/pics_struts/common/images/arrow_small_down_disabled.gif";
}

function decrement() {
	if (index > indexMin) document.forms.prev.submit();
}

function increment() {
//	if (index < indexMax) document.forms.next.submit();
	if (index+${length}-1 < indexMax) document.forms.next.submit();
}

function search() {
	document.forms.searchPictures.submit();
}

function logoff() {
	document.forms.logoffForm.submit();
}

function login() {
	document.forms.loginForm.submit();
}

</script>

</head>

<body bgcolor="white" onload="updateImages()">
<s:actionerror/>

<%-- better, not yet improving...for now just replacing existing content
<s:if test="hasActionErrors()">
   <div>
      <s:actionerror/>
   </div>
</s:if>
 --%>
 	
<s:if test="hasActionMessages()">
<table><tr><td align="center">
<b><font color="green"><s:actionmessage/></font></b>
</td></tr></table>
<hr>
 </s:if>
 
<s:set var="user" value="%{#session.username}"/>

<div class="container">
  <div class="header">
  <!-- hide search option for now 
    <img width=22 height=20 style="float:left"  src="/pics_struts/common/images/ku1ziz.svg" onclick="search()">
   	<form style="display: none" name="searchPictures" action="/pics_struts/searchPictures.do" ></form>
 -->
<span>    
<s:if test="viewCriteria != null && viewCriteria != ''">&nbps; 
<s:property value="viewCriteria" />
</s:if> 
</span>    

<%-- menu is dysfunctional
<!-- start menu -->
<span class="dropdown" style="margin-right: 20%;">
  <table>
  <tr>
  <td><bean:write name="user"/></td>
  <td><img src="/pics_struts/common/images/dropdown.png" /></td>
  </tr>
  </table>

<div class="items">
<label style="margin-left:6px;" onclick="globeForm.submit()"><bean:message key="OtherLanguage"/></label>
<form name="globeForm" action="/pics_struts/prefsProcess.do"> 
<input type="hidden" name="action" value="toggleLanguage">       	
</form>      
<br>

<% if (request.isUserInRole("user")) { %>
  <html:link page="/profile.do"><bean:message key="profile"/></html:link><br>
  <html:link page="/upload.do"><bean:message key="Upload"/></html:link><br>
  <html:link page="/editPictures.do"><bean:message key="edit"/></html:link><br>
<% } %>

<% if (request.isUserInRole("unregistered")) { %>
  <html:link page="/register.do"><bean:message key="register"/></html:link><br>
<% } %>

<% if (request.isUserInRole("admin")) { %>
  <html:link page="/admin.do">admin</html:link><br>
<% } %>

  <html:link page="/searchPictures.do"><bean:message key="search"/></html:link><br>
  <html:link page="/logoff.do"><bean:message key="logoff"/></html:link><br>

</div>
</span>
<!-- end menu -->
 --%>

<center>
<span>
<img style="float:left" name="prev" src="common/images/arrow_small_left.gif" onclick="decrement()">&nbsp;

<%-- for now keep it simple, public viewable photos only so no need to log in
<s:property value="#user"/>
<s:if test="%{#session.username==''}">
<s:a action="/loginProcess">login</s:a>
</s:if>
<s:else>
<s:property value="#user"/>
<img src="common/images/logout.png" width=22 height=20 style="float:center" onclick="logoff()">
<form style="display: none" name="logoffForm" action="logoff" ></form>
</s:else>
  --%>
 
<img style="float:right" name="next" src="common/images/arrow_small_right.gif" onclick="increment()">
</span>
</center>

<!-- choosing display width of 330px simply because that looks best on my Avid 579 phone  which
supposedly has 720x1440 pixels but acts as though it has 360x720 pixels ???. At any rate thumbnails
pics are blurry at 330 so resized them to 360 then reduce size here (or in other app) to fit,
may also allow user to choose thumbnail size or on mobile set it to match width dynamically.
For now going with what works best for me. -->    
 </div>
  <div class="body">
    <div class="content">

<%--Show some small versions of photos, length for number of photos is set in ApplicationResources.properties. --%>
<s:iterator status="status" value="#session.pictures" >
   <s:if test="(#status.index >= #session.offset) && (#status.index < #session.offset+#session.length)">
      <img width="165px" 
      alt="<s:property value='summary'/>" onclick="viewPictureDetails('<s:property value='%{#status.index}'/>')"  
      src="getImage?size=thumbnails&index=<s:property value='#status.index'/>" 
      >
   </s:if>
</s:iterator>

    </div> <!-- scrollable content -->
  </div> <!-- .body -->
  <center><div class="footer">
  </div></center>
  
</div> <!-- container -->

     	<form style="display: none" name="prev" action="viewPicturesProcess"> 
   		<input type="hidden" name="action" value="prevPictures">       	
   		</form>      

	   	<form style="display: none" name="next" action="viewPicturesProcess"> 
   		<input type="hidden" name="action" value="nextPictures">       	
   		</form>      
   		
   		<form style="display: none" name="aForm" method="post" action="viewPicture">
   		<input type="hidden" name="index" value="{#request.index}">
   		</form>

</body>
</html>
