function swapValuesSubmit() {
	codeValue = value=document.getElementById("code").value
	if(codeValue != null || codeValue != "")
		document.getElementById("codeValue") = codeValue;
}

function swapValuesLoad() {
	document.getElementById("code").value=document.getElementById("codeValue").value;
}