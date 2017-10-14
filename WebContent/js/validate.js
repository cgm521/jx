
Validate = function(config){
	//初始化要验证的对象
	$.extend(this,Validate.defaults,config);
	
	//验证对象数组
	this.validateArray = [];
	
	//验证错误信息数组
	this.errorArray = [];
	
	//验证是否通过
	this.sub_success=true;
};

Validate.defaults = {
	validateItem: [{
		/**
		 * 要验证的对象ID
		 */
		validateObj: '',
		
		/**
		 * 要验证对象的类型
		 */
		type: 'text',
		
		/**
		 * 验证的类型
		 */
		validateType: [],
		
		/**
		 * 参数
		 */
		param:[],
		
		/**
		 * 要验证对象的提示信息
		 */
		error: [],
		
		/**
		 * 自定义验证方法
		 */
		handler : $.noop
	}]
};

Validate.prototype = {
	
	_addValidate : function (){
		var _validateItem = this.validateItem;
		for(var i = 0 ; i < _validateItem.length ; i++){
			//无论传过来得是ID还是对象都转换成JQUERY对象
			_validateItem[i].validateObj = typeof _validateItem[i].validateObj ==='string'?$('#'+_validateItem[i].validateObj):_validateItem[i].validateObj;
			if(_validateItem[i].type=='select'){
				if(!_validateItem[i].validateObj.val()||_validateItem[i].validateObj.val()===''||_validateItem[i].validateObj.val().indexOf('请选择')!=-1){
					this.errorArray.push({
						validateObj:_validateItem[i].validateObj,
						error:_validateItem[i].error[0]?_validateItem[i].error[0]:'下拉验证不通过！'
							});
					this.sub_success = false;
				}
			}else if(_validateItem[i].type=='text'){
				for(var j = 0 ; j< _validateItem[i].validateType.length ; j++){
					
					//true代表验证通过
					var isPass = true;
					
					if(_validateItem[i].validateType[j]=='canNull'){//是否为空
						if(_validateItem[i].param[j]=='F' && _validateItem[i].validateObj.val()===''){
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'验证空不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}else if(_validateItem[i].param[j]=='T'&& _validateItem[i].validateObj.val()===''){
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='maxLength'){//最长度
				    	if(_validateItem[i].validateObj.val().length >_validateItem[i].param[j]) {
							this.errorArray.push({
									validateObj:_validateItem[i].validateObj,
									error:_validateItem[i].error[j]?_validateItem[i].error[j]:'超长验证不通过！'
										});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='minLength'){//最短长度
				    	if(_validateItem[i].validateObj.val().length <_validateItem[i].param[j]) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'超短验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='maxValue'){//最大值
				    	var objvalue = parseFloat(_validateItem[i].validateObj.val()==''?0:_validateItem[i].validateObj.val());
				    	_validateItem[i].param[j] = parseFloat(_validateItem[i].param[j]);
						if(objvalue>_validateItem[i].param[j]) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'最大验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='minValue'){//最小值
				    	var objvalue = parseFloat(_validateItem[i].validateObj.val()==''?0:_validateItem[i].validateObj.val());
				    	_validateItem[i].param[j] = parseFloat(_validateItem[i].param[j]);
						if(objvalue<=_validateItem[i].param[j]) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'最小验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='num'){//数字 //当为T时，数字可以为空
				    	if(_validateItem[i].param[j]=='F'||(_validateItem[i].param[j]=='T'&&_validateItem[i].validateObj.val()!='')){
							var objvalue = _validateItem[i].validateObj.val();
							if(!objvalue.match("^([+-]?)\\d*\\.?\\d+$")) {
								this.errorArray.push({
									validateObj:_validateItem[i].validateObj,
									error:_validateItem[i].error[j]?_validateItem[i].error[j]:'数字验证不通过！'
										});
								this.sub_success = false;
								isPass=false;
							}
						}else{
							isPass=true;
						}
				    }else if(_validateItem[i].validateType[j]=='num1'){//数字 大于0的整数 //当为T时，数字可以为空
				    	if(_validateItem[i].param[j]=='F'||(_validateItem[i].param[j]=='T'&&_validateItem[i].validateObj.val()!='')){
							var objvalue = _validateItem[i].validateObj.val();
							if(!objvalue.match("^[0-9]*[0-9][0-9]*$")) {
								this.errorArray.push({
									validateObj:_validateItem[i].validateObj,
									error:_validateItem[i].error[j]?_validateItem[i].error[j]:'数字验证不通过'
										});
								this.sub_success = false;
								isPass=false;
							}
						}else{
							isPass=true;
						}
				    }else if(_validateItem[i].validateType[j]=='num2'){//0以上的数字 包括0和小数
				    	if(_validateItem[i].param[j]=='F'||(_validateItem[i].param[j]=='T'&&_validateItem[i].validateObj.val()!='')){
							var objvalue = _validateItem[i].validateObj.val();
							if(!objvalue.match("^([+]?)\\d*\\.?\\d+$")) {
								this.errorArray.push({
									validateObj:_validateItem[i].validateObj,
									error:_validateItem[i].error[j]?_validateItem[i].error[j]:'数字验证不通过！'
										});
								this.sub_success = false;
								isPass=false;
							}
						}else{
							isPass=true;
						}
				    }else if(_validateItem[i].validateType[j]=='zimu'){//正能为字母
				    	if(_validateItem[i].param[j]=='F'||(_validateItem[i].param[j]=='T'&&_validateItem[i].validateObj.val()!='')){
							var objvalue = _validateItem[i].validateObj.val();
							if(!objvalue.match("[a-zA-Z]+")) {
								this.errorArray.push({
									validateObj:_validateItem[i].validateObj,
									error:_validateItem[i].error[j]?_validateItem[i].error[j]:'字母验证不通过！'
										});
								this.sub_success = false;
								isPass=false;
							}
						}else{
							isPass=true;
						}
				    }else if(_validateItem[i].validateType[j]=='intege'){//正整数 //当为T时，正整数可以为空，不能为0
				    	if(_validateItem[i].param[j]=='F'||(_validateItem[i].param[j]=='T'&&_validateItem[i].validateObj.val()!='')){
				    		var objvalue = _validateItem[i].validateObj.val();
							if(!objvalue.match("^[0-9]*[1-9][0-9]*$")) {
								this.errorArray.push({
									validateObj:_validateItem[i].validateObj,
									error:_validateItem[i].error[j]?_validateItem[i].error[j]:'正整数验证不通过！'
										});
								this.sub_success = false;
								isPass=false;
							}
				    	}else{
							isPass=true;
						}
				    	
				    }else if(_validateItem[i].validateType[j]=='integer'){//正整数 and 0
				    		var objvalue = _validateItem[i].validateObj.val();
							if(!objvalue.match(/^\d+$/)) {
								this.errorArray.push({
									validateObj:_validateItem[i].validateObj,
									error:_validateItem[i].error[j]?_validateItem[i].error[j]:'正整数验证不通过！'
										});
								this.sub_success = false;
								isPass=false;
							}else{
								isPass=true;
							}
				    	
				    }else if(_validateItem[i].validateType[j]=='decmal'){//浮点数
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'浮点数验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='email'){//邮件
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'邮件验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='mobile'){//手机号
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("^(13|14|15|17|18)[0-9]{9}$")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'手机号验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='picture'){//图片
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'图片验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='date'){//日期
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'日期验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='telephone'){//电话
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("(\\d{3}-|\\d{4}-)?(\\d{8}|\\d{7})")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'电话验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='idCard'){//身份证
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("([\\d]{6}(19|20)[\\d]{2}((0[1-9])|(10|11|12))([0|1|2][\\d]{1}|(30|31))[\\d]{3}[xX\\d{1}])|(^[0-9]{8}((0[1-9])|(10|11|12))([0|1|2][0-9]|(30|31))[0-9]{3}$)")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'身份证验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='zipCode'){//邮编
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("^\\d{6}$")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'邮编验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='userName'){//用户名
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("^\\w+$")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'用户名验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='handler'){//自定义验证方法
			        	if(!_validateItem[i].handler.call(this)){
			        		this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'输入不相等验证不通过！'
									});
			        		this.sub_success = false;
							isPass=false;
			        	}
				    }else if(_validateItem[i].validateType[j]=='accordance'){//是否相等
						if(_validateItem[i].param[j].val()!= _validateItem[i].validateObj.val()){
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'输入不相等验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='ip'){//ip地址
				    	var objvalue = _validateItem[i].validateObj.val();
						if(!objvalue.match("(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])")) {
							this.errorArray.push({
								validateObj:_validateItem[i].validateObj,
								error:_validateItem[i].error[j]?_validateItem[i].error[j]:'IP验证不通过！'
									});
							this.sub_success = false;
							isPass=false;
						}
				    }else if(_validateItem[i].validateType[j]=='withOutSpecialChar'){//验证不能包含特殊字符
				    	if(_validateItem[i].validateObj.val()!=''){
				    		var objvalue = _validateItem[i].validateObj.val();
				    		var regArray=new Array("`","~","!","@","#","$","^","&","*","=","|","{","}","'","%",":","；",
				    				"'",",","\\","\\\\","[","\\","\\\\","]","<",">","?","！","@","#","￥","…","…",
				    				"&","*","&",";","【","】","‘","；","：","”","“","。","，","、","？");
				    		var len = regArray.length;
				    		for(var iu=0;iu<len;iu++){
				    			if (objvalue.indexOf(regArray[iu])!=-1){
				    				this.errorArray.push({
				    					validateObj:_validateItem[i].validateObj,
				    					error:_validateItem[i].error[j]?_validateItem[i].error[j]:'不能包含特殊字符！'
				    				});
				    				this.sub_success = false;
				    				isPass=false;
				    				continue;
				    		    }else{
				    		    	isPass=true;
				    		    }
				    		}
				    	}
				    }
					
					//如果验证失败则跳出循环转入下一验证对象
					if(!isPass){
						break;
					}
				}
			}
		}
	},
	/*提交时验证方法*/
	_submitValidate : function(){
		this.sub_success=true;
		this.errorArray = [];
		this._addValidate();
		$('#form_error_content').remove();
		for(var i = 0 ; i < this.validateArray.length ; i++){
			this.validateArray[i]();
		}
		if(this.sub_success){
			return true;
		}else{
		    
		    for(var i=0; i<this.errorArray.length; i++)
		    {
		    	var valObj = this.errorArray[i].validateObj;
		    	//创建错误显示信息
		    	var msgDiv = $('<div class="validateMsg"></div>'); 
		    	msgDiv.css({
		    		'top': valObj.position().top + 3,
		    		'left': valObj.position().left,
		    		'height': valObj.height()-2,
		    		'width': valObj.width()-4,
		    		'line-height': valObj.height()+'px'
		    	}).html(this.errorArray[i].error)
		    	.insertAfter(valObj)
		    	.bind('click.validate',function(){
		    		$(this).prev().focus();
		    		$(this).siblings('.validateMsg').remove();
		    		$(this).remove();
		    	});
		    	
		    }
		    return false;
		}
	}
};
