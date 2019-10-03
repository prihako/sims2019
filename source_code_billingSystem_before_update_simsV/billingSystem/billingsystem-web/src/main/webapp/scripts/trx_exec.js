function setExecutionType(thisForm){
	var radioExecNow = document.getElementById("radioExecNow");
	var radioExecAtDate = document.getElementById("radioExecAtDate");
	var radioExecRec = document.getElementById("radioExecRec");

	//radioExecAtDate
	var radioExecAtDateDate = document.getElementById("radioExecAtDateDate");
	var radioExecAtDateMonth = document.getElementById("radioExecAtDateMonth");
	var radioExecAtDateYear = document.getElementById("radioExecAtDateYear");

	// radioExecRec
	var radioExecRecDayInterval = document.getElementById("radioExecRecDayInterval");
	var radioExecRecDay = document.getElementById("radioExecRecDay");
	var radioExecRecDate = document.getElementById("radioExecRecDate");

	var radioExecRecDayIntervalValue = document.getElementById("radioExecRecDayIntervalValue");
	var radioExecRecDayValue = document.getElementById("radioExecRecDayValue");
	var radioExecRecDateValue = document.getElementById("radioExecRecDateValue");

	var radioExecRecEndDate = document.getElementById("radioExecRecEndDate");
	var radioExecRecEndMonth = document.getElementById("radioExecRecEndMonth");
	var radioExecRecEndYear = document.getElementById("radioExecRecEndYear");

	//radioExecAtDate
	radioExecAtDateDate.disabled = true;
	radioExecAtDateMonth.disabled = true;
	radioExecAtDateYear.disabled = true;

	// radioExecRec
	radioExecRecDayInterval.disabled = true;
	radioExecRecDay.disabled = true;
	radioExecRecDate.disabled = true;

	radioExecRecDayIntervalValue.disabled = true;
	radioExecRecDayValue.disabled = true;
	radioExecRecDateValue.disabled = true;

	radioExecRecEndDate.disabled = true;
	radioExecRecEndMonth.disabled = true;
	radioExecRecEndYear.disabled = true;

	/*
	radioExecNow
	*/
	if ( radioExecNow.checked ){
	}

	/*
	radioExecAtDate
	*/
	if ( radioExecAtDate.checked ){
		radioExecAtDateDate.disabled = false;
		radioExecAtDateMonth.disabled = false;
		radioExecAtDateYear.disabled = false;
	}

	/*
	radioExecRec
	*/
	if ( radioExecRec.checked ){
		radioExecRecDayInterval.disabled = false;
		radioExecRecDay.disabled = false;
		radioExecRecDate.disabled = false;

		if( radioExecRecDayInterval.checked ) {
			radioExecRecDayIntervalValue.disabled = false;
		}
		if( radioExecRecDay.checked ) {
			radioExecRecDayValue.disabled = false;
		}
		if( radioExecRecDate.checked ) {
			radioExecRecDateValue.disabled = false;
		}

		radioExecRecEndDate.disabled = false;
		radioExecRecEndMonth.disabled = false;
		radioExecRecEndYear.disabled = false;

	}
}


function setTipeEksekusi(thisForm){
	var radio1 = document.getElementById("radio_PembayaranSekarang");
	var radio2 = document.getElementById("radio_PembayaranPadaTanggal");
	var radio3 = document.getElementById("radio_PembayaranRutinTiapTanggal");

	var comboDate 	= thisForm.date;
	var comboMonth 	= thisForm.month;
	var comboYears 	= thisForm.years;
	var comboTtg 	= document.getElementById("combo_PembayaranRutinTiapTanggal");

	var comboDate0 	= thisForm.date_0;
	var comboMonth0 = thisForm.month_0;
	var comboYears0 = thisForm.years_0;

	if (radio1.checked) {
		disablePadaTanggal(thisForm);
		disableRecurring(thisForm);
	}

	if (radio2.checked) {
		enablePadaTanggal(thisForm);
		disableRecurring(thisForm);
		if(comboDate[0].selected && comboMonth[0].selected && comboYears[0].selected){
			setNowDate(comboDate, comboMonth, comboYears);
		}
	}

	if(radio3!=null){
		if (radio3.checked) {
			disablePadaTanggal(thisForm);
			enableRecurring(thisForm);
			if(comboDate0[0].selected && comboMonth0[0].selected && comboYears0[0].selected){
				setNowDate(comboDate0, comboMonth0, comboYears0);
			}
		}
	}
}

function enablePadaTanggal(thisForm){
	var comboDate 	= thisForm.date;
	var comboMonth 	= thisForm.month;
	var comboYears 	= thisForm.years;

	comboDate.disabled = false;
	comboMonth.disabled = false;
	comboYears.disabled = false;
}

function disablePadaTanggal(thisForm){
	var comboDate 	= thisForm.date;
	var comboMonth 	= thisForm.month;
	var comboYears 	= thisForm.years;

	comboDate[0].selected = true;
	comboMonth[0].selected = true;
	comboYears[0].selected = true;

	comboDate.disabled = true;
	comboMonth.disabled = true;
	comboYears.disabled = true;
}

function enableRecurring(thisForm){
	var comboTtg 	= document.getElementById("combo_PembayaranRutinTiapTanggal");
	var comboDate0 	= thisForm.date_0;
	var comboMonth0 = thisForm.month_0;
	var comboYears0 = thisForm.years_0;

	if(comboTtg!=null){
		comboTtg.disabled = false;
	}

	if(comboDate0!=null){
		comboDate0.disabled = false;
	}

	if(comboMonth0!=null){
		comboMonth0.disabled = false;
	}

	if(comboYears0!=null){
		comboYears0.disabled = false;
	}
}

function disableRecurring(thisForm){
	var comboTtg 	= document.getElementById("combo_PembayaranRutinTiapTanggal");
	var comboDate0 	= thisForm.date_0;
	var comboMonth0 = thisForm.month_0;
	var comboYears0 = thisForm.years_0;

	if(comboTtg!=null){
		comboTtg[0]	.selected = true;
		comboTtg.disabled = true;
	}

	if(comboDate0!=null){
		comboDate0[0].selected = true;
		comboDate0.disabled = true;
	}

	if(comboMonth0!=null){
		comboMonth0[0].selected = true;
		comboMonth0.disabled = true;
	}

	if(comboYears0!=null){
		comboYears0[0].selected = true;
		comboYears0.disabled = true;
	}
}

function validationExec(thisForm){

	var submitStatus = true;

	var radio1 = document.getElementById("radio_PembayaranSekarang");
	var radio2 = document.getElementById("radio_PembayaranPadaTanggal");
	var radio3 = document.getElementById("radio_PembayaranRutinTiapTanggal");

	var comboDate 		= thisForm.date;
	var comboMonth 		= thisForm.month;
	var comboYears 		= thisForm.years;
	var comboTtg 		= document.getElementById("combo_PembayaranRutinTiapTanggal");
	var comboDate0 		= thisForm.date_0;
	var comboMonth0   	= thisForm.month_0;
	var comboYears0   	= thisForm.years_0;

	if (radio1.checked) {
		return true;
	}

	if (radio2.checked) {
		if(comboDate[0].selected){
			alert(alert_date_date_must_select);
			submitStatus = false;
			comboDate.focus();
			thisForm.onsubmit=null;
			return false;
		}

		if(comboMonth[0].selected){
			alert(alert_date_month_must_select);
			submitStatus = false;
			comboMonth.focus();
			thisForm.onsubmit=null;
			return false;
		}

		if(comboYears[0].selected ){
			alert(alert_date_year_must_select);
			submitStatus = false;
			comboYears.focus();
			thisForm.onsubmit=null;
			return false;
		}

		if(!isDateTrue(comboDate, comboMonth, comboYears)){
			submitStatus = false;
			thisForm.onsubmit=null;
			return false;
		}

		if(!isDateBackDate(comboDate, comboMonth, comboYears)){
			submitStatus = false;
			thisForm.onsubmit=null;
			return false;
		}

		if(isNowDate(comboDate, comboMonth, comboYears)){
			submitStatus = false;
			thisForm.onsubmit=null;
			radio1.checked=true;
			return false;
		}
	}

	if(radio3 != null){
	if (radio3.checked) {
			if(comboTtg[0].selected){
				alert(alert_date_date_rec_must_select);
				comboTtg.focus();
				submitStatus = false;
				thisForm.onsubmit=null;
				return false;
			}
			if(comboDate0[0].selected){
				alert(alert_date_date_must_select);
				submitStatus = false;
				comboDate0.focus();
				thisForm.onsubmit=null;
				return false;
			}

			if(comboMonth0[0].selected){
				alert(alert_date_month_must_select);
				submitStatus = false;
				comboMonth0.focus();
				thisForm.onsubmit=null;
				return false;
			}
			if(comboYears0[0].selected ){
				alert(alert_date_year_must_select);
				submitStatus = false;
				comboYears0.focus();
				thisForm.onsubmit=null;
				return false;
			}

			if(!isDateTrue(comboDate0, comboMonth0, comboYears0)){
				submitStatus = false;
				thisForm.onsubmit=null;
				return false;
			}

			if(!isRecurringValid(comboTtg, comboDate0, comboMonth0, comboYears0)){
				submitStatus = false;
				thisForm.onsubmit=null;
				return false;
			}
		}
	}
	return submitStatus;
}

function isRecurringValid(recElem, dateElem, monthElem, yearsElem){
	var testNewDate = new Date();

	submitStatusRec = false;

	testNewMo 	= testNewDate.getMonth() + 1;
	testNewDay 	= testNewDate.getDate();
	testNewYr 	= testNewDate.getFullYear();//'2007';

	dateRec 	= getElementValue(recElem);
	date 		= getElementValue(dateElem);
	month 		= getElementValue(monthElem);
	yearsVal 	= getElementValue(yearsElem);

	if(yearsVal <= testNewYr){// check if the year selected equal or less than now
		if(month <= testNewMo){// check if the month selected equal or less than now
			if(testNewMo==month && testNewYr==yearsVal){// check if the month and year selected equal to now
				if(testNewDay >= date || testNewDay > dateRec){
					alert(alert_date_date_backdate);
					submitStatusRec = false;
					recElem.focus();
					return false;
				}else{
					alert(alert_date_select_at_date);
					submitStatusRec = false;
					recElem.focus();
					return false;
				}
			}else if(testNewYr==yearsVal && ((month - testNewMo) == 1)){ // if the year equal and the next month - todays month = 1
				if(dateRec > date){
					alert(alert_date_rec_cannot_greather_than_limit);
					submitStatusRec = false;
					recElem.focus();
					return false;
				}else{
					alert("selisih bulan == 1");
					submitStatusRec = true;
					return true;
				}
			}else if(((testNewYr-yearsVal)== 1) && ((month - testNewMo) == -1)){ // if end of year equal and the next month - todays month = 1
				if(dateRec > date){
					alert(alert_date_rec_cannot_greather_than_limit);
					submitStatusRec = false;
					recElem.focus();
					return false;
				}else{
					submitStatusRec = true;
					return true;
				}
			}else{
				submitStatusRec = true;
				return true;
			}
		}else{
			submitStatusRec = true;
			return true;
		}
	}else{
		submitStatusRec = true;
		return true;
	}

	return submitStatusRec;
}

function setNowDate(comboDate, comboMonth, comboYears){

	var nowDate = new Date();

	nowMo 	= nowDate.getMonth() + 1;
	nowDay 	= nowDate.getDate();
	nowYr 	= nowDate.getFullYear();//'2007';

	comboDate[nowDay].selected=true;
	comboMonth[nowMo].selected=true;

	lengthz = comboYears.length;
	for(i = 0; i < lengthz; i++){
		if(comboYears[i].text == nowYr){
			comboYears[i].selected=true;
		}
	}
}

// 0 index is pembayaran pada tanggal radio
// 1 index is pembayaran rutin tiap tanggal radio
function driveRB(thisForm, index){
	var radio1 = document.getElementById("radio_PembayaranSekarang");
	var radio2 = document.getElementById("radio_PembayaranPadaTanggal");
	var radio3 = document.getElementById("radio_PembayaranRutinTiapTanggal");

	if(index == 0){
		radio2.checked = true;
	}else if(index == 1){
		if(radio3!=null)
			radio3.checked = true;
	}
}