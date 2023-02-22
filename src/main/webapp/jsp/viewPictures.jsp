<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" %>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<html><%--<html:html locale="true">  --%> 
<head>
<link href="/familyPhotos/common/css/styles.css" rel="stylesheet">
<meta name="viewport" content="width=device-width">
<title><s:text name="app.title"/></title>


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
if (index+${length}-1 < indexMax) document.images.next.src="/familyPhotos/common/images/arrow_small_right.gif";
	else document.images.next.src="/familyPhotos/common/images/arrow_small_right_disabled.gif";
	if (index > indexMin) document.images.prev.src="/familyPhotos/common/images/arrow_small_left.gif";
	else document.images.prev.src="/familyPhotos/common/images/arrow_small_left_disabled.gif";
}

function decrement() {
	if (index > indexMin) document.forms.prev.submit();
}

function increment() {
	if (index < indexMax) document.forms.next.submit();
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
<%--<html:errors/> --%>
	
<%--	
<html:messages id="message" message="true">
<table><tr><td align="center">
<b><font color="green"><s:bean name="message"/></font></b>
</td></tr></table>
<hr>
</html:messages>
 --%>
<s:set var="user" value="username"/>

<div class="container">
  <div class="header">
<span>    
<s:if test="viewCriteria != null && viewCriteria != ''">&nbps;
<s:bean name="viewCriteria" />
</s:if>
</span>    


<center>
<span>
<img style="float:left" name="prev" src="/familyPhotos/common/images/arrow_small_left.gif" onclick="decrement()">&nbsp;

<%--
<s:if test="user == ''">
<s:a page="/loginProcess.do">login</s:a>
</s:if>
<s:if test="user != ''">
<s:bean name="user"/>
<img src="/familyPhotos/common/images/logout.png" width=22 height=20 style="float:center" onclick="logoff()">
<form style="display: none" name="logoffForm" action="/familyPhotos/logoff.do" ></form>
</s:if>
 --%>
 
<img style="float:right" name="next" src="/familyPhotos/common/images/arrow_small_right.gif" onclick="increment()">
</span>
</center>

 </div>
  <div class="body">
    <div class="content">

<%--Show some small versions of photos, length for number of photos is set in ApplicationResources.properties. --%>
<s:iterator status="status" value="#session.pictures" >
   <s:if test="(#status.index >= #session.offset) && (#status.index < #session.offset+#session.length)">
      <img width="165px" 
      alt="<s:property value='summary'/>" onclick="viewPictureDetails('<s:property value='%{#status.index}'/>')"  
      src="/familyPhotos/getImage?size=thumbnails&index=<s:property value='#status.index'/>" 
      >
   </s:if>
</s:iterator>

    </div> <!-- scrollable content -->
  </div> <!-- .body -->
  <center><div class="footer">
  </div></center>
  
</div> <!-- container -->

     	<form style="display: none" name="prev" action="/familyPhotos/viewPicturesProcess"> 
   		<input type="hidden" name="offset" value="{#session.offset}">
   		<input type="hidden" name="action" value="prevPictures">       	
   		</form>      

	   	<form style="display: none" name="next" action="/familyPhotos/viewPicturesProcess"> 
   		<input type="hidden" name="offset" value="{#session.offset}">
   		<input type="hidden" name="action" value="nextPictures">       	
   		</form>      
   		
   		<form style="display: none" name="aForm" method="post" action="/familyPhotos/viewPicture">
   		<input type="hidden" name="action" value="viewPicture">
   		<%--<input type="hidden" name="index" value="%{index}">  --%>
   		<input type="hidden" name="index" value="1">
   		</form>

</body>
</html>
