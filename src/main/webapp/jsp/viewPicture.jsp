<%@ page language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<s:set var="picSize" value="%{#request.picSize}"/>
<s:set var="index" value="%{#request.index}"/>
<s:set var="picturesSize" value="%{#session.picturesSize}"/>
<s:set var="picture" value="%{#request.picture}"/>
	
<html>
<head>
<meta name="viewport" content="width=device-width">
<title><s:text name="app.title"/></title>
<%--<html:base/>  --%> 

<script>
var picSize = "${requestScope.picSize}";
var index = "${requestScope.index}";
var indexMin = 0;
var indexMax = "${sessionScope.picturesSize}" - 1; 

function updateImages() {
    if (index < indexMax) document.images.next.src="/familyPhotos/common/images/arrow_small_right.gif";
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

function showList() {
	document.forms.showListForm.submit();
}

function setPicSizeNow(size) {
	picSize = size;
	
	if (picSize=="medium") {
		document.images.picImage.className="medium";
		picSize="1024x768";
	}
	else {
		document.images.picImage.className="";
	}
	//TODO: persist medium for prev, next display
	
    psf='/familyPhotos/getImage.do?size='+picSize+'&index=<s:property value="#index" />'; 
	document.images.picImage.src=psf;
	document.forms.prev.elements.picSize.value=picSize;
	document.forms.next.elements.picSize.value=picSize;	
}

function setPicSize(size) {
	picSize = size;
	document.forms.prev.elements.picSize.value=picSize;
	document.forms.next.elements.picSize.value=picSize;	
}

function viewPicturePopup() {
	centerPopup('/familyPhotos/jsp/viewPcturePopup.htm','pic', 1024, 868); //768?
}

function viewFilmStripPopup() {
	centerPopup('/familyPhotos/jsp/viewFilmStripPopup.htm','pic', 1024, 868); //768?
}

</script>
<script src="/familyPhotos/common/scripts/popup.js"></script>
<style type="text/css">
form {
margin-top: 0;
margin-bottom: 0;
}

.imageBox img {
height: auto;
max-width: 100%;
}

.imageBox {
border: medium solid;
}

.medium {
width: 50%;
}

</style>
</head>

<body topmargin="0" bgcolor="white" onload="setPicSize(<s:property value='#picSize' />);updateImages();" >
<%--
<html:errors/> 

<html:messages id="message" message="true">
  <table width="100%"><tr><td align="center">
<b><font color="green"><bean:write name="message"/></font></b>
</td></tr></table>
<hr>
</html:messages>
--%>

<table>
<tr>
<td valign="top" align="left" width="50%">
<img name="prev" src="/familyPhotos/common/images/arrow_small_left.gif" onclick="decrement()">&nbsp;
      <form name="prev" method="post" action="/familyPhotos/viewPictureProcess"> 
      <input type="hidden" name="action" value="prevPicture">         
      <input type="hidden" name="picSize" >
      <input type="hidden" name="index" value=<s:property value="#index"/>>
   	  </form>      
</td>

<td valign="top" align="left">
<img name="up" src="/familyPhotos/common/images/arrow_small_up.gif" onclick="showList()">&nbsp; <!-- function name must be different than form name else calls form  -->
       <form name="showListForm" method="post" action="/familyPhotos/viewPictures" >
       <input type="hidden" name="action" value="viewPictures">        
       </form>    
</td>

<td valign="top" align="right" width="95%">
<img name="next" src="/familyPhotos/common/images/arrow_small_right.gif" onclick="increment()">
      <form name="next" method="post" action="/familyPhotos/viewPictureProcess"> 
      <input type="hidden" name="action" value="nextPicture">         
      <input type="hidden" name="picSize" >
      <input type="hidden" name="index" value=<s:property value="#index"/>>
      </form>      
</td>
 
 </tr>
 </table>

<div class="imageBox">
<img name="picImage" src='/familyPhotos/getImage?size=<s:property value="#picSize" />&index=<s:property value="#index" />' />
</div>

<!-- picture details start -->
<hr>
<table border="0" width="100%">
  <tr>
    <td align="left" valign="top">
       #<s:property value="#picture.MD5"/>  <s:property value="#picture.summary"/>
    </td>
  </tr>
<%--
  <tr>
  	<td>
  	<b><bean:message key="Date"/>: </b><bean:write name="picture" property="date"/>
  	</td>
  </tr>
--%>  
  <tr>
  	<td>
  	<b><s:text name="Description"/>: </b><s:property value="#picture.description"/>
  	</td>
  </tr>
<%--
  <tr>
  	<td>
  	<b><bean:message key="Place"/>: </b><bean:write name="picture" property="place"/>
  	</td>
  </tr>
    <tr>
  	<td>
  	<b><bean:message key="People"/>: </b>
 
<script>
  var count = 1;
</script>
<logic:iterate id="person" name="peopleInPic">
<script>
  if (count > 1) document.write(',');
</script>  
<bean:write name="person" property="fullName" />
<script>
  count++;
</script>  
</logic:iterate>
  	
  	</td>
  </tr>
      <tr>
  	<td>
  	<b><bean:message key="Event"/>: </b><bean:write name="picture" property="event"/>
  	</td>
  </tr>
  <tr>
  	<td>
  	<b><bean:message key="Owner"/>: </b><bean:write name="picture" property="owner"/>
  	</td>
  </tr>
  <tr>
  	<td>
  	<b><bean:message key="ViewGroup"/>: </b><bean:write name="picture" property="viewGroup"/>
  	</td>
  </tr>
--%> 
 
<!--  
  <tr>
  	<td>
  	<b><bean:message key="SrcFileName"/>: </b><bean:write name="picture" property="srcFileName"/>
  	</td>
  </tr>
-->  
</table>

<%--
<html:form action="/download"> 
   		<input type="hidden" name="fileName" value=<bean:write name="picture" property="fileName"/>>
   		<input type="hidden" name="srcFileName" value="<bean:write name="picture" property="srcFileName"/>">
   		<input type="hidden" name="size" value="1024x768">
   		<html:submit property="submit" value="Download" />
</html:form>  
--%>

<%--
<logic:match name="picture" property="owner" value="<%= request.getRemoteUser() %>">
<html:form action="/editPicture2"> 
<html:submit property="submit" value="Edit" />
<input type="hidden" name="index" value="<%= index %>">
</html:form>  
</logic:match>
 --%>

<%--
<!-- only show Comment button if registered user and does not own the picture -->
<% if (request.isUserInRole("user")) { %>
<logic:notMatch name="picture" property="owner" value="<%= request.getRemoteUser() %>">
<html:form action="/commentPicture"> 
<html:submit property="submit" value="Comment" />
<input type="hidden" name="picId" value="<bean:write name="picture" property="id" /> ">
</html:form>  
</logic:notMatch>
<% } %>

<hr>
<logic:iterate id="comments" name="comments4pic">
<b>
<bean:write name="comments" property="date"/>&nbsp;&nbsp;
<bean:write name="comments" property="username"/>&nbsp;&nbsp;
</b>
<bean:write name="comments" property="text"/>
<hr>
</logic:iterate>
 --%>
  
</body>
</html>
