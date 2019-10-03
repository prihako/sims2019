/**
 * @author ashadi.pratama
 * javascript form submit process
 */


var appSbtTitleMsg="ESB MINI WEBADMIN";

/**
 * 
 * @param ket_det
 * @returns {Boolean}
 */
var sbAppValidSubmit=function(code){
	
	switch(code)
	{
		case "sbbranch" :
			if(sbDefaultVal("TXT-KODE-ALFA","","Tolong masukkan kode alfa")==false)
				return false;
			else if(sbDefaultVal("TXT-NAMA-ALFA","","Tolong masukkan nama alfa")==false)
				return false;
			else if(sbDefaultVal("TXT-KODE-CABANG","","Tolong masukkan kode cabang")==false)
				return false;
			else if(sbDefaultVal("TXT-NAMA-CABANG","","Tolong masukkan nama cabang")==false)
				return false;
			else
				return true;
		break;
		
		case "sbtranslimit" :
			if(sbDefaultVal("TXT-KODE-TRANS","","Tolong masukkan kode transaksi")==false)
				return false;
			else if(sbDefaultVal("TXT-NAMA-TRANS","","Tolong masukkan nama transaksi")==false)
				return false;
			else if(sbDefaultVal("TXT-MAX-ACCUMULATE","0","Tolong masukkan maksimal frekuensi transaksi")==false)
				return false;
			else
				return true;
		break;
		
		case "sbrole" :
			if(sbDefaultVal("TXT-ROLE-NAMA","","Tolong masukkan nama role")==false)
				return false;
			else if(sbDefaultVal("TXT-ROLE_DESC","","Tolong masukkan deskripsi role")==false)
				return false;
			else
				return true;
		break;
		
		case "sbpassword" :
			if(sbDefaultVal("TXT-NEW-PASSWORD","","Tolong masukkan password baru")==false)
				return false;
			else if($("#TXT-NEW-PASSWORD").val()!=$("#TXT-CONFIRM-PASSWORD").val()){
				alert("Konfirmasi password tidak sesuai, tolong masukkan konfirmasi password yang benar");
				$("#TXT-CONFIRM-PASSWORD").focus();
			}
			else
				return true;
		break;
		
		case "sbmenu" :
			if(sbDefaultVal("TXT-MENU-NAME","","Tolong masukkan nama menu")==false)
				return false;
			else
				return true;
		break;
		
		case "sbuser" :
			if(sbDefaultVal("TXT-USER-NAME","","Tolong masukkan nama menu")==false)
				return false;
			else if(sbDefaultVal("TXT-USER-CODE","","Tolong masukkan nama menu")==false)
				return false;
			else
				return true;
		break;
		
		case "sbuserblock" :
			if(sbDefaultVal("TXT-USERNAME","","Tolong masukkan username")==false)
				return false;
			else
				return true;
		break;
		
		default : alert("Unknown process!!!");return false;
	}
	
}

/**
 * set attribute of text field 
 * sequence attribute looping split by delimiter and 
 */
var sbAttrTextField=function(txtId,attrVal){
	
	var loopAttr=attrVal.split("||");
	for(i=0;i<loopAttr.length;i++){
		var splitAttr=loopAttr[i].split(":");
		$("#"+txtId).attr(splitAttr[0],splitAttr[1]);
	}
	
	$("#"+txtId).addClass("form-control");
}


var sbAppSetMandatoryField=function(txtId){
	$("#"+txtId).addClass("parsley-validated");
	$("#"+txtId).attr("data-required","true");
	$("#"+txtId).css("background-color","#d5fae4");
}

