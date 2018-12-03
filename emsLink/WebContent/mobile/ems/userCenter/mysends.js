/**
 * 
 */

var vm = avalon.define({
	$id:'mysends',
	orderList:[],
	submited:false,
	isUpdate:false,
	orderId:'',
	starNum:0,
	evaluateContent:'',
	
	
	getOrderList:function(status){
		$.ajax({
		    url: "../../../delivery/doSearchWxUserOrder",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {type:2, status:status},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		console.log('sucess');
		    		vm.orderList = res.data;
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
	cancleOrder:function(id){
		$.ajax({
		    url: "../../../delivery/doCancleOrder",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {id:id},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		console.log('sucess');
		    		alert("取消成功");
		    		vm.getOrderList(1);
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
	openEvaluateOrder:function(id){
		vm.orderId = id;
		$("#pingjia").addClass("show");
	},
	closePingjia:function(){
		$("#pingjia").removeClass("show");
	},
	moduleStarsClick : function(num) {
		vm.starNum = num;
		var total = 5;
		for (var i = 1; i <= num; i++) {
			//console.log($(this).parent().children().length);
			$($(this).parent().children().slice(0,i)).removeClass().addClass('star-bright');
			$($(this).parent().children().slice(i,total)).removeClass().addClass('star-dark');
		}
	},
	evaluateOrder:function(){
		$.ajax({
		    url: "../../../delivery/doEvaluateOrder",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {orderId:vm.orderId, starNum:vm.starNum,content:vm.evaluateContent},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		console.log('sucess');
		    		$("#pingjia").removeClass("show");
		    		alert("评价成功");
		    		vm.getOrderList(2);
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
	lookEvaluate:function(id){
		vm.isLook = true;
		$.ajax({
		    url: "../../../delivery/doLookEvaluate",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {orderId:id},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		console.log('sucess');
		    		$("#lookpingjia").addClass("show");
		    		vm.starNum = res.data.starNum;
		    		vm.evaluateContent = res.data.content;
		    		
                }else{
                	alert(res.data);
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
	},
	closelookPingjia:function(){
		vm.starNum = 0;
		vm.evaluateContent = '';
		$("#lookpingjia").removeClass("show");
	},
    removeInput:function(name){
    	vm.consumer[name] = '';
    },
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});
vm.getOrderList(1);
avalon.scan();