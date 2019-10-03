function formatDate(inputDate) {
	var st = inputDate.value;
	var d = new Date(st.replace(/(\d{2})-(\d{2})-(\d{4})/, '$2-$1-$3'));
	
	var monthMap = new Array();
	monthMap[0] = "01";
	monthMap[1] = "02";
	monthMap[2] = "03";
	monthMap[3] = "04";
	monthMap[4] = "05";
	monthMap[5] = "06";
	monthMap[6] = "07";
	monthMap[7] = "08";
	monthMap[8] = "09";
	monthMap[9] = "10";
	monthMap[10] = "11";
	monthMap[11] = "12";

	date = d.getDate();
	month = monthMap[d.getMonth()];
	year = d.getFullYear();
	return date+"-"+month+"-"+year;
}