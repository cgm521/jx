<script type="text/javascript" src="<c:url value="/js/jquery.alerts.js"/>"></script>
<link type="text/css" href="<c:url value="/css/jquery.alerts.css" />" rel="stylesheet" />
<style type="text/css">
/* Custom dialog styles */
#popup_container.style_1 {
	text-align: center;
	border: 0;
	border-radius: 0;
	font-size:100%;
	width:90%;
}

#popup_container.style_1 #popup_title {
	display: none;
}

#popup_container.style_1 #popup_content {
	background: none;
	padding: 1em 0em;
}

#popup_container.style_1 #popup_message {
	padding-left: 0em;
	padding-top:10px;
	padding-bottom:10px;
	width:98%;
}

#popup_container.style_1 #popup_panel{
	width:100%;
	margin-top:10px;
	margin-left:0;
	margin-right:0;
}

#popup_container.style_1 INPUT[type="button"] {
	width:45%;
	padding-top:7px;
	padding-bottom:7px;
	background-color: #ffb400;
	border: 0;
	border-radius: 4px;
	color: #ffffff;
	text-align: center;
	outline: none;
}

#popup_container.style_1 INPUT[type="text"]{
	border:1px solid #D5D5D5;
	border-radius:4px;
	height:35px;
	width:90%;
}

#popup_container.style_1 INPUT[type="tel"]{
	border:1px solid #D5D5D5;
	border-radius:4px;
	height:35px;
	width:90%;
}
</style>

<script type="text/javascript">
	function alertMsg(msg){
		$.alerts.dialogClass = "style_1"; // set custom style class
   		return jAlert(msg, '');
	}
	
	function confirmMsg(msg,callback){
		$.alerts.dialogClass = "style_1"; // set custom style class
	    jConfirm(msg, '', function (r){
	    	if(r){
	    		callback(r);
	    	}
	    });
	}
	
	function promptMsg(msg,callback){
		$.alerts.dialogClass = "style_1"; // set custom style class
		jPrompt(msg, '', '', function (r){
	    	if(r){
	    		callback(r);
	    	}
	    });
	}
	
	function promptMsg_num(msg,callback){
		$.alerts.dialogClass = "style_1"; // set custom style class
		jPrompt_num(msg, '', '', function (r){
	    	if(r){
	    		callback(r);
	    	}
	    });
	}
</script>