/*遮罩*/	
function InitLayer(title){
		var showDesc = ((title!=null && title != "") ?title:"正在加载，请稍后…");
		var html="<div class='layer'><span>&nbsp;"+showDesc+"</span></div>";
		$('body').append(html);
		$('.layer').css({"width":"100%","height":"100%","padding":"50% 0",
			"text-align":"center","filter":"ALPHA(opacity=70)","opacity":"0.7",
			"background":"#000","z-index":"1101","position":"fixed","top":"0px","color":"#ccc",
			"margin-top":"-18px","text-shadow":"0px 0px 0px"
		});
		/*$('.layer').children('img').css({
			"width":"20px","height":"20px","position":"relative",
			"top":"4px"
		});*/
		/* .layer{
			width:100%;
			height:100%;
			padding: 50% 0;
			text-align:center;
			display:none;
			filter:ALPHA(opacity=70);
			opacity: 0.7; 
			background:#000;
			z-index:99;
			position:fixed;
			color:#ccc;
		}
		.layer img{
			width:20px;
			height:20px;
			position:relative;
			top:4px;
		}
		.layer span{
			height:20px;
			line-height:20px;
			margin:0px;
			display:inline-block;
		} */
		
	}

function closeLayer(){
	$('.layer').hide();
}