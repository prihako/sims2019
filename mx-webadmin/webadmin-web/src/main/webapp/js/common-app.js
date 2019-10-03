/**
 * @author ashadi.pratama
 * common javascript application
 */

var runApp,runTime;
var appDialogWidth=600;

/**
 * hide grid/list panel
 */
var sbAppHideListPanel=function(){
	$("#GRID-PANEL").fadeOut("fast");
}

/**
 * hide form panel
 */
var sbAppHideFormPanel=function(){
	$("#FORM-PANEL").fadeOut("fast");
}

/**
 * display grid/list panel
 */
var sbAppShowListPanel=function(){
	$("#GRID-PANEL").fadeIn("fast");
}

/**
 * display form panel
 */
var sbAppShowFormPanel=function(){
	$("#FORM-PANEL").fadeIn("fast");
}

/**
 * start submit process
 */
var sbAppStartSubmit=function(){
	var htmlContent="<div class='reloadGif'></div>";
	$("#COMMON-DIALOG").dialog({
		title : "MEMPROSES MINI WEB ADMIN",
		width : 800,
		modal : true,
		resizable : false,
		close:function(){
			$("#COMMON-DIALOG").dialog("close");
		}
	});
	
	$("#CONTENT-DIALOG").html("<div class='reloadGif'></div>");
	return true;
}

/**
 * @param responcseCode is response status process
 * @param msg is message response
 * @param hyp 
 */
var sbStopProcess=function(responcseCode,addUrl,msg){
	$("#COMMON-DIALOG").dialog( "close" );
	$("#COMMON-DIALOG").dialog({
		title : "PESAN HASIL MEMPROSES",
		width : 800,
		modal : true,
		resizable : false,
		close:function(){
			$(this).dialog( "close" );
			sbAppExecuteResponseCode(responcseCode);
		},
		buttons: [
			{
				text: "Tutup",
				click: function() {
						$(this).dialog( "close" );
						sbAppExecuteResponseCode(responcseCode);
				}
			}
		]
	});
	
	var contentMsg="";
	if (success == 2 || success == 3 || success == 4 || success == 5)
		contentMsg="<div class=\"alert alert-success\"><h4>SUKSES!</h4>"+msg.toUpperCase()+"</div>";
	else
		contentMsg="<div class=\"alert alert-error\"><h4>PERHATIAN!</h4>"+msg.toUpperCase()+"</div>";
	
	$("#CONTENT-DIALOG").html(contentMsg);
}

var sbAppExecuteResponseCode=function(responseCode){
	$("#target").attr("src","");
	
	if (responseCode == 1)
		sbAppResetForm();
	else if (success == 2){
		sbAppLoadGrid(addUrl);
		sbAppHideFormPanel();
		sbAppShowListPanel();
	}
	else if (responseCode == 3)
		sbAppLoadGrid(addUrl);
	else if (responseCode == 5){
		sbAppLoadGrid(hyp);
		sbAppHideFormPanel();
		sbAppShowListPanel();
		sbAppPrintReport(addUrl);
	}	
}

/**
 * cancel alert
 */
var sbStartCancel=function(){
	var ag=confirm('Apakah data ini akan dibatalkan?');
	if(ag==true)
	{
		start_submit();
	}
	else
		return false;
}

/**
 * delete alert
 */
var sbStartDelete=function(){
	var ag=confirm('Apakah data ini akan dihapus?');
	if(ag==true)
	{
		start_submit();
	}
	else
		return false;
}

/**
 * change status alert
 */
var sbStartStatus=function(){
	var ag=confirm('Apakah status akan diubah?');
	if(ag==true)
	{
		start_submit();
	}
	else
		return false;
}

/**
 * change position alert
 */
var sbStartPos=function(){
	var ag=confirm('Apakah posisi akan diubah?');
	if(ag==true)
	{
		start_submit();
	}
	else
		return false;
}


/**
 * @param addrUrl is url address data
 * load grid/list panel static name is MAIN-RELOAD-LIST
 */
var sbAppLoadGrid=function(addrUrl)
{
	//alert("Load");
	var addr=addrUrl.replace(/ /g,"+");
	$("#MAIN-RELOAD-LIST").html("<div class='reloadGif'></div>");
	$("#MAIN-RELOAD-LIST").load(addr);
}

/**
 * load form panel static name is MAIN-FORM
 */
var sbAppLoadMainForm=function(addrUrl){
	$("#MAIN-FORM").html("<div class='reloadGif'></div>");
	$("#MAIN-FORM").load(addrUrl);
}

/**
 * load panel by tag <div></div> according element id
 */
var sbAppMenuSelect=function(labelMenu,divId,addrUrl){
	$("#CONTENT-TITLE").html(labelMenu.toUpperCase());
	sbAppReloadByDiv(divId,addrUrl);
}

/**
 * reload div panel by id and url
 */
var sbAppReloadByDiv=function(divId,addrUrl){
	clearTimeout(runApp);
	clearTimeout(runTime);
	$("#"+divId).html("<div class='reloadGif'></div>");
	$("#"+divId).load(addrUrl);
}

/**
 * @param addr is url address
 */
var sbLoadDataDialog=function(addr)
{
	addr=addr.replace(/ /g,"+");
	$("#CONTENT-DIALOG").html("<div class='reloadGif'></div>");
	$("#CONTENT-DIALOG").load(addr);
}

var sbAppSetFieldFormValue=function(fieldId,fieldValue,isNumber){
	var formatVal=fieldValue;
	if(isNumber)
		formatVal=sbAppSetCurrencyFormat(fieldValue);
	$("#"+fieldId).attr("value",formatVal);
}


/**
 * @param idTxt is set input text as currency format input
 */
var sbAppSetNumberOfTextbox=function(idTxt){
	var nval=idTxt.split("||");
	for(i=0;i<nval.length;i++){
		setCurrencyTextbox(nval[i]);
	}
}

/**
 * @param idTxt is set input text as currency format input
 */
var sbAppSetCurrencyTextbox=function(idTxt){
	$("#"+idTxt)
		.css("text-align","right")
		.focus(function(){
			if($("#"+idTxt).val()=="0")
				$("#"+idTxt).attr("value","");
			else{
				sbAppSetDefaultFormatCurrencyTextField(idTxt);
				//console.log($("#"+idTxt).val());
			}
		})
		.blur(function(){
			if($("#"+idTxt).val()=="")
				$("#"+idTxt).attr("value","0");
			else{
				sbAppSetCurrencyFormatTextField(idTxt);
				//console.log($("#"+idTxt).val());
			}
		})
		.keyup(function(){
			var validKar="0123456789.,";
			var valInput=$("#"+idTxt).val();
			var nVal=valInput.length;
			
			var cek=true;
			var kata="";
			for(i=0;i<nVal;i++)
			{
				if(validKar.indexOf(valInput.charAt(i))>=0)
					kata=kata+valInput.charAt(i);
				else
					alert("Bukan karakter angka!");
			}
			$("#"+idTxt).attr("value",kata);
		});
	var vtext=$("#"+idTxt).val()=="" ? "0" : $("#"+idTxt).val();
	$("#"+idTxt).attr("value",vtext);
	
}

/**
 * @param vCurr is text currency
 * @returns text without dot mark
 */
var sbAppRemoveDotCurrency=function(vCurr){
	return vCurr.replace(/\./g,"");
}

/**
 * @param vCurr is text currency
 * @returns text without coma mark
 */
var sbAppRemoveComaCurrency=function(vCurr){
	return vCurr.replace(/\,/g,"");
}

/**
 * @param idTxt is set input text as default currency format input
 */
var sbAppSetDefaultFormatCurrencyTextField=function(idTxt){
	var ntext=sbAppRemoveDotCurrency($("#"+idTxt).val());
	ntext=ntext.replace(/\,/g,".");
	var arrText=ntext.split(".");
	if(arrText[1]!=undefined){
		nformat=sbAppGenerateDefaultCurrency(arrText[0])+"."+arrText[1];
	}
	else{
		nformat=sbAppGenerateDefaultCurrency(ntext);
	}
	//console.log(nformat);
	//$("#"+idTxt).attr("value",nformat);
	$("#"+idTxt).attr("value","");
}

var sbAppSetDefaultFormatCurrency=function(fieldValue){
	var ntext=sbAppRemoveDotCurrency(fieldValue);
	ntext=ntext.replace(/\,/g,".");
	var arrText=ntext.split(".");
	if(arrText[1]!=undefined)
		nformat=sbAppGenerateDefaultCurrency(arrText[0])+"."+arrText[1];
	else
		nformat=sbAppGenerateDefaultCurrency(ntext);
	
	return nformat;
}

/**
 * @param idTxt is set input text as currency format input
 */
var sbAppSetCurrencyFormatTextField=function(idTxt){
	var ntext=sbAppRemoveComaCurrency($("#"+idTxt).val());
	ntext=ntext.replace(/\./g,",");
	var arrText=ntext.split(",");
	if(arrText[1]!=undefined){
		nformat=sbAppGenerateNumberCurrency(arrText[0])+","+arrText[1];
	}
	else{
		nformat=sbAppGenerateNumberCurrency(ntext);
	}
	//console.log(nformat);
	//$("#"+idTxt).attr("value",nformat);
	$("#"+idTxt).attr("value","");
}


var sbAppSetCurrencyFormat=function(fieldValue){
	var ntext=sbAppRemoveComaCurrency(fieldValue);
	ntext=ntext.replace(/\./g,",");
	var arrText=ntext.split(",");
	if(arrText[1]!=undefined)
		nformat=sbAppGenerateNumberCurrency(arrText[0])+","+arrText[1];
	else
		nformat=sbAppGenerateNumberCurrency(ntext);
	
	return nformat;
}


/**
 * @param idTxt
 * @returns text format currency value from input text
 */
var sbAppGetCurrencyValueTextField=function(idTxt){
	var ntext=sbAppRemoveDotCurrency($("#"+idTxt).val());
	ntext=ntext.replace(/\,/g,".");
	return ntext;
}

/**
 * @param idTxt
 * @returns text format currency value from input text
 */
var sbAppGetCurrencyValueFromText=function(idTxt){
	var ntext=sbAppRemoveDotCurrency(idTxt);
	ntext=ntext.replace(/\,/g,".");
	return ntext;
}

/**
 * @param vCurr
 * @returns text number currency
 */
var sbAppGenerateNumberCurrency=function(vCurr){
	var curr="";
	var j=0;
	for(i=vCurr.length-1;i>=0;i--){
		curr=vCurr.charAt(i)+curr;
		if((j+1)%3==0 && j>0 && i>0)
			curr="."+curr;
		j++;
	}
	return curr;
}

/**
 * @param vCurr
 * @returns default number currency
 */
var sbAppGenerateDefaultCurrency=function(vCurr){
	var curr="";
	var j=0;
	for(i=vCurr.length-1;i>=0;i--){
		curr=vCurr.charAt(i)+curr;
		if((j+1)%3==0 && j>0 && i>0)
			curr=","+curr;
		j++;
	}
	return curr;
}

/**
 * @param addrUrl is url address detail page
 * @param titleDialog is title dialog
 * preview detail at pop up
 */
var sbAppDetailDialogBox=function(addrUrl,titleDialog){
	$("#COMMON-DIALOG").dialog({
		title : titleDialog,
		modal : true,
		width : appDialogWidth, 
		resizable : false,
		close:function(){
			$("#COMMON-DIALOG").dialog("close");
		}
	});
	
	$("#CONTENT-DIALOG").html("<div class='reloadGif'></div>");
	$("#CONTENT-DIALOG").load(addrUrl);
}

/**
 * close pop up active
 */
var sbAppCloseDialogBox=function(){
	$("#COMMON-DIALOG").dialog("close");
}

/**
 * @param rowId is row table id
 * @param setCss is css style name
 * set style row id
 */
var sbAppDisplayRowTabel=function(rowId,setCss){
	$("#"+rowId).css("display",setCss);
}

var sbAppSimpleSearchAutoComplete=function(txtSearch,addr){
	$("#"+txtSearch)
	.attr("placeHolder","Cari...")
	.autocomplete({
		source: addr,
		minLength: 1
	})
	.data( "ui-autocomplete" )._renderItem = function( ul, item ) {
		return $( "<li>" )
		.append( "<a><strong>" + item.label + "</strong><br>" + item.description + "</a>" )
		.appendTo( ul );
	};
}

/**
 * @param txtSearch
 * @param txtId
 * @param addrId
 * auto complete
 */
var sbAppSearchAutoComplete=function(txtSearch,txtId,addr){
	$("#"+txtSearch)
	.attr("placeHolder","Cari...")
	.autocomplete({
		source: addr,
		minLength: 1,
		select: function( event, ui ) {
			//alert(ui.item.value+" "+ui.item.label);
			//return false;
			$("#"+txtId).attr("value",ui.item.id);
		}
	});
}

/**
 * generate button existing table
 */
var sbGenerateTable=function(){
	$("table#GRID-TABLE")
	.each(function(){
		var gridTable=$(this);
		$("tr",gridTable).each(function(){
			var gridRow=$(this);
			var gridId=$(this).attr("id");
			$("td",gridRow).each(function(){
				var gridColumn=$(this);
				var manipulateButton=gridColumn.find("a").get();
				if($(manipulateButton).length>0){
					$(manipulateButton).each(function(){
						var title=$(this).attr("title");
						
						$(this).addClass("btn");
						$(this).addClass("btn-xs");
						$(this).addClass("btn-tertiary");
						
						//ADD ICON GRID BUTTON
						var faIcon="";
						if(title=="edit")
							faIcon="fa-edit";
						else if(title=="delete")
							faIcon="fa-trash-o";
						else if(title=="detail")
							faIcon="fa-search-plus";
						else if(title=="block")
							faIcon="fa-dot-circle-o";
						else if(title=="unblock")
							faIcon="fa-check-square";
						else if(title=="reset")
							faIcon="fa-key";
						else if(title=="role")
							faIcon="fa-cogs";
						
						$(this).html("<i class=\"fa "+faIcon+"\"></i>");
						
						//CLICK CONTAIN GRID BUTTON
						$(this).click(function(){
							if(title=="edit"){
								var URL_EDIT=PAGE_EDIT;
								PAGE_EDIT=URL_EDIT+gridId;
								showEditFormPanel();
								PAGE_EDIT=URL_EDIT;
								$("#FORM-PANEL-TITLE").html("EDIT");
							}
							else if(title=="delete"){
								if(confirm("Apakah data ini akan dihapus ?"))
									deleteProcess(gridId);
							}
							else if(title=="detail"){
								var URL_DETAIL=PAGE_DETAIL+gridId;
								sbAppDetailDialogBox(URL_DETAIL,TITLE_DIALOG);
							}
							else if(title=="block"){
								if(confirm("Apakah data ini akan diblok ?"))
									blockProcess(gridId);
							}
							else if(title=="unblock"){
								if(confirm("Apakah data yang diblok akan dikembalikan ?"))
									unBlockProcess(gridId);
							}
							else if(title=="reset"){
								if(confirm("Apakah data ini akan direset ?"))
									resetProcess(gridId);
							}
							else if(title=="role"){
								var URL_ROLE=PAGE_FORM_ROLE;
								PAGE_FORM_ROLE=URL_ROLE+gridId;
								showRoleMenuFormPanel();
								PAGE_FORM_ROLE=URL_ROLE;
								$("#FORM-PANEL-TITLE").html("PILIH MENU");
							}
								
						})
						.attr("href","javascript:;")
						;
					});
				} 
			});
		});
	});
}

var sbGridTableClass=function(addUrlList,orderBy){
	$("table#GRID-TABLE")
	.addClass("table")
	.addClass("table-bordered")
	.addClass("table-hover")
	.addClass("clear");
	
	$("#GRID-TABLE > thead > tr").each(function(){
		$("th",$(this)).each(function(){
			var fieldAttr=$(this).attr("field");
			var keyField="";
			if(fieldAttr!="" && fieldAttr!=undefined){
				if(orderBy=="" || orderBy==undefined)
					orderAttr="DESC";
				else{
					var orderArr=orderBy.split(";");
					orderAttr=orderArr[1];
					keyField=orderArr[0];
				}
				
				var keyOrder="ASC";
				var icoOrder="<i class=\"fa fa-caret-up\"></i>";
				if(fieldAttr==keyField){
					if(orderAttr=="ASC"){
						keyOrder="DESC";
						icoOrder="<i class=\"fa fa-caret-down\"></i>";
					}
				}
								
				var labelColumn=$(this).html();
				$(this).html("");
				var orderLink="&order="+fieldAttr+";"+keyOrder;
				if(addUrlList!=undefined)
					orderLink=addUrlList+orderLink;
				var link="<a class=\"btn btn-tertiary btn-xs btn-block\" href=\"javascript:;\" onclick=\"sbAppLoadGrid('"+orderLink+"')\">";
				if(keyField==fieldAttr)
					link+=icoOrder+" ";
				link+=labelColumn+"</a>";
				
				$(this).html(link);
			}
		});
	});
}

var sbInitGrid=function(){
	$("#MAIN-RELOAD-LIST").addClass("table-responsive");
};

var sbSetIconButton=function(buttonId,styleId){
	$("#"+buttonId).addClass("btn");
	$("#"+buttonId).addClass("btn-primary");
	$("#"+buttonId).addClass("btn-xs");
	$("#"+buttonId).attr("type","button");
	
	$("#"+buttonId).append("<i class=\"fa "+styleId+"\"></i>");
}

var sbSetAddButtonIcon=function(buttonId){
	var styleId="fa-file-text";
	sbSetIconButton(buttonId,styleId);
	$("#"+buttonId).append(" Tambah");
	
}

var sbSetSearchButtonIcon=function(buttonId){
	var styleId="fa-search";
	sbSetIconButton(buttonId,styleId);
	$("#"+buttonId).append(" Cari");
	
}

var sbSetBackButtonIcon=function(buttonId){
	var styleId="fa-chevron-circle-left";
	sbSetIconButton(buttonId,styleId);
	$("#"+buttonId).append(" Kembali");
	
}

var sbSetSaveButtonIcon=function(buttonId){
	var styleId="fa-save";
	sbSetIconButton(buttonId,styleId);
	$("#"+buttonId).append(" Simpan");
	
}

var sbSetResetButtonIcon=function(buttonId){
	var styleId="fa-file-o";
	sbSetIconButton(buttonId,styleId);
	$("#"+buttonId).attr("type","reset");
	$("#"+buttonId).append(" Reset");
	
}

var sbSetReportButtonIcon=function(buttonId){
	var styleId="fa-print";
	sbSetIconButton(buttonId,styleId);
	$("#"+buttonId).attr("type","submit");
	$("#"+buttonId).append(" Cetak");
	
}

var sbHeaderMenuClick=function(){
	$("#HEADER-MENU-LOGOUT").click(function(){
		return confirm("Apakah anda yakin untuk keluar dari aplikasi ?");
	});
}

var sbKeyUpCurr=function(id){
	var validKar="0123456789.,";
	var val=id.value;
	var nVal=val.length;
	
	var cek=true;
	var kata="";
	for(i=0;i<nVal;i++)
	{
		if(validKar.indexOf(val.charAt(i))<0)
			cek=false;
		else
			kata=kata+val.charAt(i);
		
	}
	id.value=kata;
	return cek;
}

var sbBlurCurr=function(id){
	var val=id.value;
	var nVal=val.length;
	
	if(val=="")
		kata="0";
	else
		kata=sbAppSetCurrencyFormat(val);
	
	id.value=kata;
}

var sbFocusCurr=function(id){
	var val=id.value;
	var nVal=val.length;
	
	if(val=="0")
		kata="";
	else
		kata=sbAppSetDefaultFormatCurrency(val);
	
	id.value=kata;
}

var sbAppSetCurrencyTextboxManual=function(idTxt){
	$("#"+idTxt).css("text-align","right");	
	$("#"+idTxt).attr("value","0");
	$("#"+idTxt).attr("onkeyup","sbKeyUpCurr(this)");
	$("#"+idTxt).attr("onblur","sbBlurCurr(this)");
	$("#"+idTxt).attr("onfocus","sbFocusCurr(this)");
}

var sbAppPrintReport=function(formatReport){
	//if(formatReport=="csv" || formatReport=="spread")
		return true;
	/*else{
		alert("Format laporan belum tersedia");
		return false;
	}*/
}

var sbDiffDayDTPicker=function(startDate,endDate){
	var dateStartSplit=startDate.split("-");
	var dateStartFormat=dateStartSplit[2]+"/"+dateStartSplit[1]+"/"+dateStartSplit[0]
	var dateStart=new Date(dateStartFormat);
	
	var dateEndSplit=endDate.split("-");
	var dateEndFormat=dateEndSplit[2]+"/"+dateEndSplit[1]+"/"+dateEndSplit[0]
	var dateEnd=new Date(dateEndFormat);
	
	var dateDiff = Math.abs(dateEnd-dateStart);
	var oneDay=1000*60*60*24;
	var numDay=parseInt((dateDiff/oneDay))+1;
		
	return numDay;
}
