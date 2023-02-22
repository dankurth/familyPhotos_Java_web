function createCenteredPopup(url, name, width, height, toolbarEnabled, resizeEnabled, scrollbarsEnabled) {
  if ( document.all )
    var maxWidth = screen.width, maxHeight = screen.height; // IE
  else if ( document.layers )
    var maxWidth = window.outerWidth, maxHeight = window.outerHeight; // Netscape
  else
    var maxWidth = 250, maxHeight = 250;
  
  var url1 = url;
  
  // create a unique URL for this page so that IE won't find the page in its cache
  var random = (new Date()).getTime();
  if (-1 == url1.indexOf("?"))
    url1 += "?.rand=" + random;
  else
    url1 += "&.rand=" + random;
  
  var left = (maxWidth - width)/2, top = (maxHeight - height)/2;
  var features = "toolbar=" + toolbarEnabled + " ,resizable=" + resizeEnabled + ",scrollbars=" + scrollbarsEnabled + ",width=" + width + ",height=" + height + ",screenX=" + left + ",screenY=" + top + ",top=" + top + ",left=" + left + "";
  var popup = window.open(url1,name,features);
  popup.focus(); 

  if (name != "details") document.picWindow = popup;
  //save it back at caller so they can close it
}

function centerPopupResize(url,name,width,height)
{
  createCenteredPopup(url, name, width, height, 'no', 'yes', 'no');    
}

function centerPopup(url,name,width,height)
{
  createCenteredPopup(url, name, width, height, 'no', 'no', 'no');
}


function reset() {
  var bodyForm = document.forms["bodyForm"];
  bodyForm.reset();
}

function redirectAndClose(url) {
   window.opener.location.href=url;
   window.close();
}

