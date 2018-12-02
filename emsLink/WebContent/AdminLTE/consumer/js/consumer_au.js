
var vm = avalon.define({
	$id:'consumerau',
	consumerId:getUrlData('id'),
	consumer:{id:'',name:'', type:'1', description:'',contactPerson:'',phone:'',contact:'',address:'',parentId:'',userName:'',userPwd:''},
	submited:false,
	getConsumer:function()
	{
		if(vm.consumerId)
		{
			$.ajax({
			    url: "../consumer/doGetById",    //请求的url地址
			    dataType: "json",   //返回格式为json
			    data: {id:vm.consumerId},    //参数值
			    type: "post",   //请求方式
			    success: function(res) {
			    	if (res.status == 1) {
			    		vm.consumer = res.data;
	                }
			    },
			    error: function() {
			    	console.log('error');
			    }
			});
		}
	},
	add:function()
	{
		vm.submited = true;
		if(vm.consumer.name == '' && vm.consumer.phone == ''){
			return;
		}
		if(vm.consumerId)
		{
			
			$.ajax({
			    url: "../consumer/doUpdate",    //请求的url地址
			    dataType: "json",   //返回格式为json
			    data: param({consumer: vm.consumer}),    //参数值
			    type: "post",   //请求方式
			    success: function(res) {
			    	if (res.status == 1) {
			    		console.log('sucess');
			    		vm.goback();
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
			    url: "../consumer/doAddNew",    //请求的url地址
			    dataType: "json",   //返回格式为json
			    data: param({consumer: vm.consumer}),    //参数值
			    type: "post",   //请求方式
			    success: function(res) {
			    	if (res.status == 1) {
			    		console.log('sucess');
			    		vm.goback();
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
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});

vm.getConsumer();
avalon.scan();