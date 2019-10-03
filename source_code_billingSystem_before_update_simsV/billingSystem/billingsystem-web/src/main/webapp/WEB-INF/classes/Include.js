var included_files = new Array();
	
function include_once(filename, type) {
    if (!in_array(filename)) {
        included_files[included_files.length] = filename;
        if (type == "css") {
        	include_css(filename);
        } else if (type == "js") {
			include_javascript(filename);
        }        
    }
}
	
function in_array(filename) {
    for (var i = 0; i < included_files.length; i++) {
        if (included_files[i] == filename) {
            return true;
        }
    }
    return false;
	
}

function include_javascript(script_filename) {
	var html_doc = document.getElementsByTagName('head').item(0);
    var js = document.createElement('script');
    js.setAttribute('language', 'javascript');
    js.setAttribute('type', 'text/javascript');
    js.setAttribute('src', script_filename);
    html_doc.appendChild(js);
    return false;
}		

function include_css(css_filename) {
	var html_doc = document.getElementsByTagName('head').item(0);
    var js = document.createElement('link');
    js.setAttribute('type', 'text/css');
    js.setAttribute('href', css_filename);
    html_doc.appendChild(js);
    return false;
}		
