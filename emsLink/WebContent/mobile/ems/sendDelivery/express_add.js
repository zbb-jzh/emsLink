/**
 * 
 */

var vm = avalon.define({
	$id:'expressadd',
	send:{id:'',name:'', phone:'',townId:"",teamId:"",address:'', remark:""},
	address1:"",
	address2:"",
	address3:"",
	addressList:[],
	address1List:[],
	submited:false,
	isUpdate:false,
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
                }else{
                	alert(res.data);
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
	},
	add:function()
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
		if(vm.consumerId)
		{
			if(vm.consumerId == vm.consumer.parentId){
				alert('自己不能选择自己');
				return false;
			}
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
			    		alert("下单成功");
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
	pay:function(){
		$.ajax({
		    url: "../../../wx/wxPayH5",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {},    //参数值
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
    	vm.consumer[name] = '';
    },
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});
vm.getAddress();
avalon.scan();