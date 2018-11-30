/**
 * 
 */

var vm = avalon.define({
	$id:'mymailings',
	orderList:[],
	submited:false,
	isUpdate:false,
	id:'',
	expressName:'',
	expressNo:'',
	yfPrice:'',
	getOrderList:function(status){
		$.ajax({
		    url: "../../../delivery/doSearchWxUserOrder",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {type:1, status:status},    //参数值
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
	showDialogShoujian:function(id){
		vm.id = id;
		$("#conformshoujian").addClass("show");
	},
	showDialogFahuo:function(id){
		vm.id = id;
		$("#conformfahuo").addClass("show");
	},
	hideDialog:function(select){
		$("#" +select ).removeClass("show");
	},
	confirm:function(status){
		var data = '';
		if(status == 5){
			if(vm.yfPrice == 0){
				alert("快递费不能为空！");
				return;
			}
			data = {id:vm.id,status:status,yfPrice:vm.yfPrice};
		}else{
			if(vm.expressName == ''){
				alert("快递公司名称不能为空！");
				return;
			}
			if(vm.expressNo == ''){
				alert("快递单号名称不能为空！");
				return;
			}
			data = {id:vm.id,status:status,expressName:vm.expressName,expressNo:vm.expressNo};
		}
		$.ajax({
		    url: "../../../delivery/doConfirmMailingOrder",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: data,    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		console.log('sucess');
		    		if(status == 5){
		    			vm.yfPrice = "";
		    			$("#conformshoujian").removeClass("show");
		    		}else{
		    			vm.expressName = '';
		    			vm.expressNo = ''
		    			$("#conformfahuo").removeClass("show");
		    		}
		    		alert("操作成功");
		    		vm.getOrderList(7);
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
    removeInput:function(name){
    	vm.consumer[name] = '';
    },
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});
vm.getOrderList(7);
avalon.scan();