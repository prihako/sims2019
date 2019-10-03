//		Membatasi jumlah 2 angka dibelakang koma, angka 0 di depan tidak diperbolehkan, cukup berikan class="numberWithComma" pada textbox jika ingin menggunakan fungsi ini
		$(document).ready(function() {
	
			$('.testWithComma').keydown(function(e){ 
			
				//control button
				if ( e.ctrlKey && (e.which == 65 || e.which==97)) {
					return true;
				}else if ( e.ctrlKey && (e.which == 86 || e.which==118)) {
					return true;
				}else if ( e.ctrlKey && (e.which == 99 || e.which==67)) {
					return true;
				}else if ( e.ctrlKey && (e.which == 88 || e.which==120)) {
					return true;
				}
				
				//allow bacspace, delete, arrow key, 
				if (e.which == 8 || e.which == 0 || e.which == 46 || e.which == 190 || e.which == 9) {
					return true;
				}
	
				//prevent zero in first position
				if (this.value.length == 0 && e.which == 48 ){
					return false;
				}
	
				//prevent enter more than 2 number after decimal point
			   	if ((e.which != 46 || $(this).val().indexOf('.') != -1) && (e.which < 48 || e.which > 57)) {
					return false;
				}
			   	if(($(this).val().indexOf('.') != -1) && ($(this).val().substring($(this).val().indexOf('.'),$(this).val().indexOf('.').length).length>2 )){
					return false;
				}
	
			});
			
//			Membatasi hanya angka saja yang boleh diinput, angka 0 di depan tidak diperbolehkan, cukup berikan class="numberWithoutComma" pada textbox jika ingin menggunakan fungsi ini
			$('.numberWithoutComma').keydown(function(e){
				
				//control button
				if ( e.ctrlKey && (e.which == 65 || e.which==97)) {
					return true;
				}else if ( e.ctrlKey && (e.which == 86 || e.which==118)) {
					return true;
				}else if ( e.ctrlKey && (e.which == 99 || e.which==67)) {
					return true;
				}else if ( e.ctrlKey && (e.which == 88 || e.which==120)) {
					return true;
				}
				
				//allow bacspace, delete, arrow key, 
				if (e.which == 8 || e.which == 0 || e.which == 46) {
					return true;
				}
				
				//prevent zero in first position
				if (this.value.length == 0 && e.which == 48 ){
					return false;
				}

				if ( (e.which > 31) && (e.which < 48 || e.which > 57) ) {
						return false;
				}
			});
		
		});
		
		