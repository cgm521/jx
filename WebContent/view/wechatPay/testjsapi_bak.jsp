<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%String path=request.getContextPath(); %>
<html>
    <head>
        <title>���ں�֧��������ҳ</title>
        <script language="javascript" src="<%=path %>/js/jquery.js"></script>
        <script language="javascript" src="<%=path %>/js/lazyloadv3.js"></script>
        <script src="<%=path %>/js/md5.js"></script>
        <script src="<%=path %>/js/sha1.js"></script>
        <script Language="javascript">
            //�̼Ҳ������޸Ĵ��ĸ�����������ҳ�����֧����ȨĿ¼�£���������֧���Ĺ����˺ŷ��ʴ�ҳ�棬���ɵ���֧����
            //�޸Ŀ�ʼ
            function getPartnerId()
            {//�滻partnerid
                return "1219295101";
            }
            
            function getPartnerKey()
            {//�滻partnerkey
                return "lapargay20140704lacafe20140705ll";
            }
			
            function getAppId()
            {//�滻appid
                return "wxc1437f5d60f3755e";
            }
            
            function getAppKey()
            {//�滻appkey
                return "nhO7AR1Xk96RgRgIXxmqWeECWbSSZtky3YfgapDPpkFJKV9CZmC19Gqzts1MEIFvl4k1Y52BKlwd1n5dLTOVuk07dkxyJbzneGPwXudrpmq1VZ0D6tx1v4F02RYCP5tb";
            }		
			//�޸ĵ��˽���
		
            //��������
            function Trim(str,is_global)
            {
                var result;
                result = str.replace(/(^\s+)|(\s+$)/g,"");
                if(is_global.toLowerCase()=="g") result = result.replace(/\s/g,"");
                return result;
            }
            function clearBr(key)
            {
                key = Trim(key,"g");
                key = key.replace(/<\/?.+?>/g,"");
                key = key.replace(/[\r\n]/g, "");
                return key;
            }
            
            //��ȡ�����
            function getANumber()
            {
                var date = new Date();
                var times1970 = date.getTime();
                var times = date.getDate() + "" + date.getHours() + "" + date.getMinutes() + "" + date.getSeconds();
                var encrypt = times * times1970;
                if(arguments.length == 1){
                    return arguments[0] + encrypt;
                }else{
                    return encrypt;
                }
                
            }

            //������package������̣�
            
            var oldPackageString;//��סpackage����������������ǩ��ʱȡ��
           
            function getPackage()
            {
                var banktype = "WX";
                var body = "��ʫ����";//��Ʒ������Ϣ�������ɲ�����ҳ���롣
                var fee_type = "1";//�������ͣ�����1ΪĬ�ϵ������
                var input_charset = "UTF-8";//�ַ���������ʹ��GBK����UTF-8
                var notify_url = "http://www.qq.com";//֧���ɹ���֪ͨ�õ�ַ
                var out_trade_no = ""+getANumber();//�����ţ��̻���Ҫ��֤���ֶζ��ڱ��̻���Ψһ��
                var partner = getPartnerId();//�����̻���
                var spbill_create_ip = "127.0.0.1";//�û��������ip�������Ҫ��ǰ�˻�ȡ������ʹ��127.0.0.1����ֵ
                var total_fee = 1;//�ܽ�
                var partnerKey = getPartnerKey();//���ֵ����������ֵ��һ���ǣ�ǩ����Ҫ�����������ɵĴ����ַ������ܺ����������key����Ҫ�̻��úñ���ġ�
                
                //���ȵ�һ������ԭ������ǩ����ע�����ﲻҪ���κ��ֶν��б��롣�����ǽ���������key=value�����ֵ���������������ַ���,������ַ������ƴ����key=XXXX������������ֶι̶������ֻ��Ҫ�������˳��������򼴿ɡ�
                var signString = "bank_type="+banktype+"&body="+body+"&fee_type="+fee_type+"&input_charset="+input_charset+"&notify_url="+notify_url+"&out_trade_no="+out_trade_no+"&partner="+partner+"&spbill_create_ip="+spbill_create_ip+"&total_fee="+total_fee+"&key="+partnerKey;
                
                var md5SignValue =  ("" + CryptoJS.MD5(signString)).toUpperCase();
                //Ȼ��ڶ�������ÿ����������urlת�룬������ĳ�������js����ô��Ҫʹ��encodeURIComponent�������б��롣
                
                
                banktype = encodeURIComponent(banktype);
                body=encodeURIComponent(body);
                fee_type=encodeURIComponent(fee_type);
                input_charset = encodeURIComponent(input_charset);
                notify_url = encodeURIComponent(notify_url);
                out_trade_no = encodeURIComponent(out_trade_no);
                partner = encodeURIComponent(partner);
                spbill_create_ip = encodeURIComponent(spbill_create_ip);
                total_fee = encodeURIComponent(total_fee);
                
                //Ȼ��������һ�������ﰴ��key��value����sign������ֵ��������������е��ַ���,����ٴ���sign=value
                var completeString = "bank_type="+banktype+"&body="+body+"&fee_type="+fee_type+"&input_charset="+input_charset+"&notify_url="+notify_url+"&out_trade_no="+out_trade_no+"&partner="+partner+"&spbill_create_ip="+spbill_create_ip+"&total_fee="+total_fee;
                completeString = completeString + "&sign="+md5SignValue;
                
                
                oldPackageString = completeString;//��סpackage����������������ǩ��ʱȡ��
                
                return completeString;
            }
            
            
            //������app����ǩ���Ĳ�����
            
            var oldTimeStamp ;//��סtimestamp������ǩ��ʱ��timestamp�봫���timestampʱ��һ��
            var oldNonceStr ; //��סnonceStr,����ǩ��ʱ��nonceStr�봫���nonceStr��һ��
                     
            function getTimeStamp()
            {
                var timestamp=new Date().getTime();
                var timestampstring = timestamp.toString();//һ��Ҫת���ַ���
                oldTimeStamp = timestampstring;
                return timestampstring;
            }
            
            function getNonceStr()
            {
                var $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
                var maxPos = $chars.length;
                var noceStr = "";
                for (i = 0; i < 32; i++) {
                    noceStr += $chars.charAt(Math.floor(Math.random() * maxPos));
                }
                oldNonceStr = noceStr;
                return noceStr;
            }
            
            function getSignType()
            {
                return "SHA1";
            }
            
            function getSign()
            {
                var app_id = getAppId().toString();
                var app_key = getAppKey().toString();
                var nonce_str = oldNonceStr;
                var package_string = oldPackageString;
                var time_stamp = oldTimeStamp;
                //��һ������������Ҫ����Ĳ�������appkey��һ��key��value�ֵ��������
                var keyvaluestring = "appid="+app_id+"&appkey="+app_key+"&noncestr="+nonce_str+"&package="+package_string+"&timestamp="+time_stamp;
                sign = CryptoJS.SHA1(keyvaluestring).toString();
                return sign;
            }
            
            
            
            
            </script>
        <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
        <meta id="viewport" name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1; user-scalable=no;" />
        
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
        <script language="javascript">
            function auto_remove(img){
                div=img.parentNode.parentNode;div.parentNode.removeChild(div);
                img.onerror="";
                return true;
            }
            
            function changefont(fontsize){
                if(fontsize < 1 || fontsize > 4)return;
                $('#content').removeClass().addClass('fontSize' + fontsize);
            }
            
            
            
            
            // ��΢���������������ڲ���ʼ����ᴥ��WeixinJSBridgeReady�¼���
                                      //���ں�֧��
             function callpay(){
              WeixinJSBridge.invoke('getBrandWCPayRequest',{
                                    "appId" : getAppId(), //���ں����ƣ����̻�����
                                    "timeStamp" : getTimeStamp(), //ʱ���
                                    "nonceStr" : getNonceStr(), //�����
                                    "package" : getPackage(),//��չ��
                                    "signType" : getSignType(), //΢��ǩ����ʽ:1.sha1
                                    "paySign" : getSign() //΢��ǩ��
                                    },function(res){
                                    	WeixinJSBridge.log(res.err_msg);  
                			            if(res.err_msg == "get_brand_wcpay_request:ok"){  
                			                alert("΢��֧���ɹ�");  
                			            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
                			                alert("�û�ȡ��֧��");  
                			            }else{  
                			                alert("֧��ʧ��");  
                			            }  
                                    // ʹ�����Ϸ�ʽ�ж�ǰ�˷���,΢���Ŷ�֣����ʾ��res.err_msg�����û�֧���ɹ��󷵻�ok����������֤�����Կɿ���
                                    //���΢���Ŷӽ��飬���յ�ok����ʱ�����̻���̨ѯ���Ƿ��յ����׳ɹ���֪ͨ�����յ�֪ͨ��ǰ��չʾ���׳ɹ��Ľ��棻����ʱδ�յ�֪ͨ���̻���̨�������ò�ѯ�����ӿڣ���ѯ�����ĵ�ǰ״̬����������ǰ��չʾ��Ӧ�Ľ��档
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
            <p>΢��֧��JSAPI����ҳ��</p>
            <p>�뽫�����빫���˺�֧��Ȩ�޵��ĸ������滻ҳ���еĲ�����partnerid��partnerkey��appid��appkey</p>
            <p>����ҳ����ڵ�֧����Ȩ����Ŀ¼�£�����΢�ź�����Ӱ����������ڹ����˺��ڷ�����ʴ�ҳ��<p>
			<p>���ɼ�鹫���˺�֧�������Ƿ�����</p>
            <p></p>
        	<button type="button" onclick="callpay()" >wx pay test</button>
      </div>
        
        
</body>
</html>