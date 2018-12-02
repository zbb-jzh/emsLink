/**
 * 
 */

var vm = avalon.define({
	$id:'mycenter',
	consumer:{name:0,phone:''},
	headimgUrl:'http://thirdwx.qlogo.cn/mmopen/vi_32/nUYFrDawI8YRpu5FhJfh98bAsicxbrTe8MZ0bCYYdDW1I86cMjILaibGBNFXvVrlRsXTtJB1ezd3lPfD6969kdDw/132',
	submited:false,
	isUpdate:false,
	yhqNum:0,
	
	getUser:function(){
		$.ajax({
		    url: "../../../delivery/getCourierUser",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 100) {
		    		console.log('sucess');
		    		vm.consumer = res.data;
		    		
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
	toSend:function(){
		window.location.href = 'mysends.html';
	},
	toMailing:function(){
		window.location.href = 'mymailings.html';
	},
	toLoginOut:function(){
		$.ajax({
		    url: "../../../user/loginout",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if(res.status == 100){
		    		 window.location.href = "login.html";
		    	}else{
		    		alert(res.data);
		    	}
		        console.log('sucess');
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
	},
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});
vm.getUser();
avalon.scan();