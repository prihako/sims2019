function MM_goToURL() {
var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
function MM_openBrWindow(theURL,winName,features) {
window.open(theURL,winName,features);
}
var isIE = window.navigator.appVersion.indexOf("compatible") > -1 && window.navigator.appVersion.indexOf("MSIE") > -1;
function formRefresh(form) {
if (renderIsOk) {
form.events.refresh();
}
}
function setJsStatus() {
var li = document.getElementsByTagName("input");
for (var i = 0; i < li.length; i++) {
var cid = li[i].id;
if (cid.indexOf("jsStatus") >= 0) {
li[i].value = "1";
}
}
}
function unsetJsStatus() {
var li = document.getElementsByTagName("input");
for (var i = 0; i < li.length; i++) {
var cid = li[i].id;
if (cid.indexOf("jsStatus") >= 0) {
li[i].value = "0";
}
}
}
//document.oncontextmenu=function() {return false;};