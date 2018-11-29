/**
 * 
 */

var vm = avalon.define({
	$id:'expressadd',
	expressList:[{id:1,name:'圆通快递'},{id:2,name:'申通快递'},{id:3,name:'汇通快递'},{id:4,name:'中通快递'},{id:5,name:'韵达快递'},{id:6,name:'天天快递'}],
	send:{id:'',name:'', phone:'',townId:"",teamId:"",address:'', remark:"", yfPrice:'',type:2,expressName:'',status:0},
	wxuser:{unusedNum:0,nickName:'', headimgUrl:''},
	address1:"",
	address2:"",
	address3:"",
	addressList:[],
	address1List:[],
	submited:false,
	isUpdate:false,
	index:1,
	arr: [],
	expressName:'韵达快递',
	getConsumer:function()
	{
		if(vm.consumerId)
		{
			vm.isUpdate = true;
			$.ajax({
			    url: "../../../consumer/doGetById",    //请求的url地址
			    dataType: "json",   //返回格式为json
			    data: {id:vm.consumerId},    //参数值
			    type: "post",   //请求方式
			    success: function(res) {
			    	if (res.status == 1) {
			    		vm.consumer = res.data;
	                }else if(res.status == -114){
	                	window.location.href = "../checkpwd/check_pwd.html";
	                }
			    },
			    error: function() {
			    	console.log('error');
			    }
			});
		}
	},
	getWxUser:function(){
		$.ajax({
		    url: "../../../delivery/getWxUser",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		console.log('sucess');
		    		vm.wxuser = res.data;
		    		//window.location.href = "consumer_node.html";
		    		//vm.goback();
                }else{
                	alert(res.data);
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
	},
	getAddress:function(){
		
		$.ajax({
		    url: "/link/delivery/doGetParent",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 100) {
		    		vm.addressList = res.data;
		    		vm.address1 = vm.addressList[0].id;
		    		$.ajax({
		    		    url: "/link/delivery/getChildren",    //请求的url地址
		    		    dataType: "json",   //返回格式为json
		    		    data: {pid:vm.addressList[0].id},    //参数值
		    		    type: "post",   //请求方式
		    		    success: function(res) {
		    		    	if (res.status == 100) {
		    		    		vm.address1List = res.data;
		    		    		vm.address2 = vm.address1List[0].id;
		    		    		vm.send.yfPrice = vm.address1List[0].yfPrice;
		                    }else{
		                    	alert(res.data);
		                    }
		    		    },
		    		    error: function() {
		    		    	console.log('error');
		    		    }
		    		});
                }else{
                	alert(res.data);
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
		
	},
	chagneZhen:function(){
		
		$.ajax({
		    url: "/link/delivery/getChildren",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {pid:vm.address1},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 100) {
		    		vm.address1List = res.data;
		    		vm.address2 = vm.address1List[0].id;
		    		vm.send.yfPrice = vm.address1List[0].yfPrice;
                }else{
                	alert(res.data);
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
	},
	chagneCun:function(){
		
		for(var i=0; i<vm.address1List.length; i++){
			if(vm.address2 == vm.address1List[i].id){
				vm.send.yfPrice = vm.address1List[i].yfPrice;
			}
			
		}
	},
	add:function(status)
	{
		//vm.submited = true;
		if(vm.send.name == ''){
			alert("姓名不能为空！");
			return false;
		}
		if(vm.send.phone == ''){
			alert("手机号不能为空！");
			return false;
		}
		
		var regExpP = /^1[345789]\d{9}$/; //手机号

	    if (!regExpP.test(vm.send.phone)) { //test检测$('#user_phone').val()是否符合regExp格式
	    	alert("手机号格式不对！");
	        return ;
	    }
		
		if(vm.send.address == ''){
			alert("庄名不能为空！");
			return false;
		}
		vm.send.townId = vm.address1;
		vm.send.teamId = vm.address2;
		if(vm.expressName == ''){
			alert("快递公司不能为空！");
			return false;
		}
		var slectedExpress = vm.expressName + "--";
		
		for(var i=2; i<=vm.index; i++){
			if($("#selectExpress" + i + " option:selected").text()){
				slectedExpress += $("#selectExpress" + i + " option:selected").text() + "--";
			}
		}
		vm.send.expressName = slectedExpress;
		
		vm.send.status = status;
		
		if(vm.consumerId)
		{
			$.ajax({
			    url: "../../../delivery/addSendDelivery",    //请求的url地址
			    dataType: "json",   //返回格式为json
			    data: param({send: vm.send}),    //参数值
			    type: "post",   //请求方式
			    success: function(res) {
			    	if (res.status == 1) {
			    		console.log('sucess');
			    		alert("注册成功");
			    		//vm.goback();
	                }else{
	                	alert(res.data);
	                }
			    },
			    error: function() {
			    	console.log('error');
			    }
			});
		}else{
			$.ajax({
			    url: "../../../delivery/addSendDelivery",    //请求的url地址
			    dataType: "json",   //返回格式为json
			    data: param({send: vm.send}),    //参数值
			    type: "post",   //请求方式
			    success: function(res) {
			    	if (res.status == 1) {
			    		console.log('sucess');
			    		if(status == 2){
			    			vm.pay(res.data.id, res.data.yfPrice);
			    		}else{
			    			alert("下单成功");
			    		}
			    		
			    		//window.location.href = "consumer_node.html";
			    		//vm.goback();
	                }else{
	                	alert(res.data);
	                }
			    },
			    error: function() {
			    	console.log('error');
			    }
			});
		}
	},
	addExpress:function(){
		//++vm.index;
		var num = vm.index;
		var html = '<div class="ui-form-item ui-form-item-show ui-border-b" id="express'+ ++vm.index +'"><label >快递公司</label>'+
        '<div class="ui-select-group" style="overflow: unset;">'
				+'<div class="ui-select" style="width: 50%;">'+
                '<select  style="height: 100%;" id="selectExpress'+ vm.index +'">';
                for(var i=0; i<vm.expressList.length; i++){
                	html += '<option ms-attr-value="'+ vm.expressList[i].id +'">'+ vm.expressList[i].name +'</option>'
                }
                 html +='</select></div></div><button class="ui-btn ui-btn-primary" style="float: right;" ms-click="deleteExpress('+ vm.index +')">删除</button></div>';
		/*$("#express" + num).after(html);
		setTimeout(function(){
	        console.log("再次扫描")
	        avalon.scan(document.body);
	    },3000);*/
                 vm.arr.push(html);
	},
	deleteExpress:function(index){
		//--vm.index;
		$("#express" + index).remove();
		
	},
	pay:function(orderId, totalfee){
		
		$.ajax({
		    url: "../../../wx/wxPayH5",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {orderId:orderId, totalfee:totalfee},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		console.log('sucess');
		    		//alert("下单成功");
		    		WeixinJSBridge.invoke('getBrandWCPayRequest',{
		                "appId" : res.data.appid,
		                "timeStamp" : res.data.timeStamp,
		                "nonceStr" : res.data.nonceStr,
		                "package" : res.data.package,
		                "signType" : "MD5",
		                "paySign" : res.data.paySign
		            },function(res){
		                WeixinJSBridge.log(res.err_msg);
		                if(res.err_msg == "get_brand_wcpay_request:ok"){
		                	alert("支付成功");

		                }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
		                	alert("用户取消支付");
		                }else{
		                	alert("支付失败");
		                }
		            })
                }else{
                	alert(res.data);
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
	},
    removeInput:function(name){
    	vm.send[name] = '';
    },
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});
vm.getAddress();
vm.getWxUser();
avalon.scan();