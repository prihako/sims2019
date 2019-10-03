var sbDefaultVal=function(id,dVal,msg)
{
	if($("#"+id).val()==dVal)
	{
		$("#"+id).focus();
		alert(msg);
		return false;
	}
}