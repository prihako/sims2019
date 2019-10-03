// JavaScript Document

function setTipeEksekusi(){
	if (document.mainForm.RDN_TIPE_EKSEKUSI[0].checked) {
		document.mainForm.LST_TGL.disabled = true;		
		document.mainForm.LST_BULAN.disabled = true;
		document.mainForm.LST_THN_BAYAR.disabled = true;
		document.mainForm.LST_TGL_TERJADWAL.disabled = true;		
		document.mainForm.LST_TGL_BERAKHIR.disabled = true;
		document.mainForm.LST_BLN_BERAKHIR.disabled = true;
		document.mainForm.LST_THN_BERAKHIR.disabled = true;
	}
	if (document.mainForm.RDN_TIPE_EKSEKUSI[1].checked) {
		document.mainForm.LST_TGL.disabled = false;		
		document.mainForm.LST_BULAN.disabled = false;
		document.mainForm.LST_THN_BAYAR.disabled = false;
		document.mainForm.LST_TGL_TERJADWAL.disabled = true;		
		document.mainForm.LST_TGL_BERAKHIR.disabled = true;
		document.mainForm.LST_BLN_BERAKHIR.disabled = true;
		document.mainForm.LST_THN_BERAKHIR.disabled = true;
	}
	if (document.mainForm.RDN_TIPE_EKSEKUSI[2].checked) {
		document.mainForm.LST_TGL.disabled = true;		
		document.mainForm.LST_BULAN.disabled = true;
		document.mainForm.LST_THN_BAYAR.disabled = true;
		document.mainForm.LST_TGL_TERJADWAL.disabled = false;		
		document.mainForm.LST_TGL_BERAKHIR.disabled = false;
		document.mainForm.LST_BLN_BERAKHIR.disabled = false;
		document.mainForm.LST_THN_BERAKHIR.disabled = false;
	}
}
