<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%String path=request.getContextPath(); %>
<html>
    <head>
        <title>公众号支付测试网页</title>
      
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
		<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
		<link rel="stylesheet" href="<%=path %>/css/default/global.css" />
		<link rel="stylesheet" href="<%=path %>/css/default/pubitem/menulist.css" />
        <style>
            body { margin:0;padding:0;background:#eae9e6; }
            body,p,table,td,th { font-size:14px;font-family:helvetica,Arial,Tahoma; }
            h1 { font-family:Baskerville,HelveticaNeue-Bold,helvetica,Arial,Tahoma; }
            a { text-decoration:none;color:#385487;}
            
            
            .container {  }
            .title { }
            #content {padding:30px 20px 20px;color:#111;box-shadow:0 1px 4px #ccc; background:#f7f2ed;  }
            .seeAlso { padding:15px 20px 30px; }
            
            .headpic div { margin:20px 0 0;}
            .headpic img { display:block;}
            
            .title h1 { font-size:22px;font-weight:bold;padding:0;margin:0;line-height:1.2;color:#1f1f1f; }
            .title p { color:#aaa;font-size:12px;margin:5px 0 0;padding:0;font-weight:bold;}
            .pic { margin:20px 0; }
            .articlecontent img { display:block;clear:both;box-shadow:0px 1px 3px #999; margin:5px auto;}
            .articlecontent p { text-indent: 2em; font-family:Georgia,helvetica,Arial,Tahoma;line-height:1.4; font-size:16px; margin:20px 0;  }
            
            
            .seeAlso h3 { font-size:16px;color:#a5a5a5;}
            .seeAlso ul { margin:0;padding:0; }
            .seeAlso li {  font-size:16px;list-style-type:none;border-top:1px solid #ccc;padding:2px 0;}
            .seeAlso li a { border-bottom:none;display:block;line-height:1.1; padding:13px 0; }
            
            .clr{ clear:both;height:1px;overflow:hidden;}
            
            
            .fontSize1 .title h1 { font-size:20px; }
            .fontSize1 .articlecontent p {  font-size:14px; }
            .fontSize1 .weibo .nickname,.fontSize1 .weibo .comment  { font-size:11px; }
            .fontSize1 .moreOperator { font-size:14px; }
            
            .fontSize2 .title h1 { font-size:22px; }
            .fontSize2 .articlecontent p {  font-size:16px; }
            .fontSize2 .weibo .nickname,.fontSize2 .weibo .comment  { font-size:13px; }
            .fontSize2 .moreOperator { font-size:16px; }
            
            .fontSize3 .title h1 { font-size:24px; }
            .fontSize3 .articlecontent p {  font-size:18px; }
            .fontSize3 .weibo .nickname,.fontSize3 .weibo .comment  { font-size:15px; }
            .fontSize3 .moreOperator { font-size:18px; }
            
            .fontSize4 .title h1 { font-size:26px; }
            .fontSize4 .articlecontent p {  font-size:20px; }
            .fontSize4 .weibo .nickname,.fontSize4 .weibo .comment  { font-size:16px; }
            .fontSize4 .moreOperator { font-size:20px; }
            
            .jumptoorg { display:block;margin:16px 0 16px; }
            .jumptoorg a {  }
            
            .moreOperator a { color:#385487; }
            
            .moreOperator .share{ border-top:1px solid #ddd; }
            
            .moreOperator .share a{ display:block;border:1px solid #ccc;border-radius:4px;margin:20px 0;border-bottom-style:solid;background:#f8f7f1;color:#000; }
            
            .moreOperator .share a span{ display:block;padding:10px 10px;border-radius:4px;text-align:center;border-top:1px solid #eee;border-bottom:1px solid #eae9e3;font-weight:bold; }
            
            .moreOperator .share a:hover,
            .moreOperator .share a:active { background:#efedea; }
            @media only screen and (-webkit-min-device-pixel-ratio: 2) {
            }
            </style>
            
         <script src="<%=path%>/js/jquery-1.11.0.min.js"></script>
		<script src="<%=path%>/js/jquery.mobile-1.4.0.min.js"></script>
		<script src="<%=path%>/js/layer.js"></script>
		<script language="javascript" src="<%=path %>/js/jquery.js"></script>
        <script language="javascript" src="<%=path %>/js/lazyloadv3.js"></script>
        <script src="<%=path%>/js/md5.js"></script>
        <script src="<%=path%>/js/sha1.js"></script>
        <input type="text" id="appid" name="appid" value="appid:${APPID}"/>
        <input type="text" id="timestamp" name="timestamp" value="timestamp:${TIMESTAMP}"/>
        <input type="text" id="nonce" name="nonce" value="nonceStr:${NONCE}"/>
        <input type="text" id="package" name="package" value="packagevalue:${PACKEGEVALUE}"/>
        <input type="text" id="signtype" name="signtype" value="signtype:${SIGNTYPE}"/>
        <input type="text" id="sign" name="sign" value="sign:${SIGN}"/>
		<script type="text/javascript">
           function auto_remove(img){
               div=img.parentNode.parentNode;div.parentNode.removeChild(div);
               img.onerror="";
               return true;
           }
            
            function changefont(fontsize){
                if(fontsize < 1 || fontsize > 4)return;
                $('#content').removeClass().addClass('fontSize' + fontsize);
            }
            
            
            
            
            // 当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
                                      //公众号支付
             function callpay(){
              WeixinJSBridge.invoke('getBrandWCPayRequest',{
                                    "appId" : $("#appid").val(), //公众号名称，由商户传入
                                    "timeStamp" : $("#timestamp").val(), //时间戳
                                    "nonceStr" : $("#nonce").val(), //随机串
                                    "package" : $("#package").val(),//扩展包
                                    "signType" : $("#signtype").val(), //微信签名方式:1.sha1
                                    "paySign" : $("#sign").val() //微信签名
                                    },function(res){
                                    	WeixinJSBridge.log(res.err_msg);  
                                    	alert(res.err_msg);
                			            if(res.err_msg == "get_brand_wcpay_request:ok"){  
                			                alert("微信支付成功");  
                			            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
                			                alert("用户取消支付");  
                			            }else{  
                			                alert("支付失败");  
                			            }  
                                    // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
                                    //因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
                                    }); 
            }                                                          
                                      
                                      
                                      
            WeixinJSBridge.log('yo~ ready.');
            if(jQuery){
                jQuery(function(){
                       
                       var width = jQuery('body').width() * 0.87;
                       jQuery('img').error(function(){
                                           var self = jQuery(this);
                                           var org = self.attr('data-original1');
                                           self.attr("src", org);
                                           self.error(function(){
                                                      auto_remove(this);
                                                      });
                                           });
                       jQuery('img').each(function(){
                                          var self = jQuery(this);
                                          var w = self.css('width');
                                          var h = self.css('height');
                                          w = w.replace('px', '');
                                          h = h.replace('px', '');
                                          if(w <= width){
                                          return;
                                          }
                                          var new_w = width;
                                          var new_h = Math.round(h * width / w);
                                          self.css({'width' : new_w + 'px', 'height' : new_h + 'px'});
                                          self.parents('div.pic').css({'width' : new_w + 'px', 'height' : new_h + 'px'});
                                          });
                       });
            }
            </script>
    </head>
    <body>
	
        <div class="WCPay">
            <p>微信支付JSAPI测试页面</p>
            <p>请将您申请公众账号支付权限的四个参数替换页面中的参数：partnerid、partnerkey、appid、appkey</p>
            <p>将此页面放在的支付授权测试目录下，测试微信号需添加白名单，并在公众账号内发起访问此页面<p>
			<p>即可检查公众账号支付功能是否正常</p>
            <p></p>
        	<button type="button" onclick="callpay()" >微信支付</button>
      </div>
        
        
</body>
</html>