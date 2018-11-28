/**
 * 
 */

var vm = avalon.define({
	$id:'mymailings',
	orderList:[],
	submited:false,
	isUpdate:false,
	
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