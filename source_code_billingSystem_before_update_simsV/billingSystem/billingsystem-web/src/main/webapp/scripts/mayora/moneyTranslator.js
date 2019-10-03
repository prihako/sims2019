function formatmoney(thisone, d, t){
    if(thisone.value.indexOf(t)<0){
    var wd="w";
    var amount="";
    if (d!=".") thisone.value = thisone.value.replace(d, ".");
    if ( (thisone.value.indexOf(".")>15) ^ (thisone.value.indexOf(".")<0 && thisone.value.length>15) ) {
		alert("Panjang Maksimal untuk Uang adalah 15 Digit");
		thisone.value = "0";
    }
    if (thisone.value.length==0) thisone.value=0;
    var tnumber=parseFloat(thisone.value);
    tnumber=tnumber.toFixed(2);
    tempnum=tnumber.toString();
    amount = tempnum.substring(0, tempnum.length-3);
    decimal = tempnum.substring(tempnum.length-2, tempnum.length);
    newAmount = "";
    for (i=amount.length;i>0;i--) {
      tchar = amount.charAt(i-1);
      if ( (amount.length-i)%3==0 && amount.length!=i  ) {
        newAmount = tchar + t + newAmount;
      }
      else {
        newAmount = tchar + newAmount;
      }
    }
    thisone.value=newAmount + d + decimal;
    }
}
function unformatmoney(thisone, d, t) {
    var strValue = thisone.value;
    while (strValue.indexOf(t)>=0) {
      strValue = strValue.replace(t, "");
    }
    if (t!=",") strValue = strValue.replace(d, ".");
    if (strValue=="") thisone.value=""
    else if (parseFloat(strValue)==0) thisone.value=""
    else thisone.value = parseFloat(strValue).toString();
    if (t!=",") thisone.value = thisone.value.replace(".", d);
}
function keypressmoney(thisone, evt, d) {
    Evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    var dCode;
    if (d == ".") dCode=46
    else dCode=44;
    if (charCode == dCode) {  // decimal separator
       if (thisone.value.indexOf(d)<0) return true;
       else return false;
    }
    else if (charCode>=37 && charCode<=40) {  // arrow keycode
      return true;
    }
    else if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      status = "This field accepts numbers only.";
      return false;
    }
    status = "";
    return true;
}