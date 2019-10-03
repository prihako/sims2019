$(document).ready(function() {
	$("input#transactionCode").autocomplete({
		width: 300,
		max: 10,
		delay: 100,
		minLength: 1,
		autoFocus: true,
		cacheLength: 1,
		scroll: true,
		highlight: false,
		source: function(request, response) {
			request["transactionCode"]=document.getElementById("transactionCode").value;
			$.ajax({
				url: "JSON/AjaxRequestAutoComplete",
				dataType: "json",
				data: request,
				success: function( data, textStatus, jqXHR) {
					console.log( data);
					var items = data; 
					response(items);
				},
				error: function(jqXHR, textStatus, errorThrown){
					console.log(textStatus);
					console.log(errorThrown);
				}
			});
		}

	});
	
	$("input#transactionDesc").autocomplete({
		width: 300,
		max: 10,
		delay: 100,
		minLength: 1,
		autoFocus: true,
		cacheLength: 1,
		scroll: true,
		highlight: false,
		source: function(request, response) {
			request["transactionDesc"]=document.getElementById("transactionDesc").value;
			$.ajax({
				url: "JSON/AjaxRequestAutoComplete",
				dataType: "json",
				data: request,
				success: function( data, textStatus, jqXHR) {
					console.log( data);
					var items = data; 
					response(items);
				},
				error: function(jqXHR, textStatus, errorThrown){
					console.log(textStatus);
					console.log(errorThrown);
				}
			});
		}

	});
	
	$("input#endpointCode").autocomplete({
		width: 300,
		max: 10,
		delay: 100,
		minLength: 1,
		autoFocus: true,
		cacheLength: 1,
		scroll: true,
		highlight: false,
		source: function(request, response) {
			request["endpointCode"]=document.getElementById("endpointCode").value;
			$.ajax({
				url: "JSON/AjaxRequestAutoComplete",
				dataType: "json",
				data: request,
				success: function( data, textStatus, jqXHR) {
					console.log( data);
					var items = data; 
					response(items);
				},
				error: function(jqXHR, textStatus, errorThrown){
					console.log(textStatus);
					console.log(errorThrown);
				}
			});
		}

	});
	
});