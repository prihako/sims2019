function getFilterValue(id, value) { 
	var tmp = document.getElementById("filterVal").value;
	var newTmp = '';

	if(tmp != ''){
		var arr = document.getElementById("filterVal").value.split(",");
		var replaced = false;

		for (i=0;i<arr.length;i++){
			if(arr[i] == '')
				continue;

			if(arr[i].indexOf(id) > -1){
				if(value != ''){
					arr[i] = arr[i].replace(/:.*/gi, ":" + value);  
				}else{
					arr[i] = '';
				}
				replaced = true;
			}

			if(i == arr.length-1 && value != ''){
				newTmp += arr[i];
			}else if (i < arr.length-1 && value != ''){
				newTmp += arr[i] + ",";
			}else{
				newTmp += arr[i];
			}
		}

		if(!replaced)
			newTmp = newTmp  + "," + id +":" + value;
		tmp = newTmp;
		document.getElementById("filterVal").value = tmp;
	}
	else{
		document.getElementById("filterVal").value = id +":"+ value;
	}
}

function adjustSpan() {
	var lastRow = document.getElementById('tableView').rows.length - 1;
	var cell =document.getElementById('tableView').rows[lastRow].cells;
	var cellLength = document.getElementById('tableView').rows[0].cells.length;
	cell[0].colSpan=""+cellLength - 1+"";
}

function selectRadio(currentCheckbox) {
	var box = document.getElementById("box");
	var inputList = box.getElementsByTagName("input"); 
	for(index = 0; index < inputList.length; index++) {
		checkbox = inputList[index];
		if(checkbox.id.match(/.*_radio/gi)) {
			if(checkbox.checked) checkbox.checked = false;
		}
	}
	
	currentCheckbox.checked = true;
}