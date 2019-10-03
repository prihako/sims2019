function validateForm() {
	startDate = new Date(document.getElementById("tglAwal").value);
	endDate = new Date(document.getElementById("tglAkhir").value);
	
	if(startDate > endDate) {
		alert("Tanggal awal tidak boleh lebih besar dari tanggal akhir.");
		return false;
	}	
	
	var oneDay=1000*60*60*24;
	limitDate = new Date(startDate.getTime()+(oneDay*4));
	if((endDate - limitDate) > 0) {
		alert("Untuk mengurangi beban server, maka range untuk tanggal report maksimal hanya 4 hari.");
		return false;
	}
	
	csvSelected = document.getElementById("cbCsv").checked;
	txtSelected = document.getElementById("cbTxt").checked;
	if(!csvSelected && !txtSelected) {
		alert("Anda harus memilih format file (TXT/CSV) sebelum generate.");
		return false;
	}
}