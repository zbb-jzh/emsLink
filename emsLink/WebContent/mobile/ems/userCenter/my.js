/**
 * 
 */

var vm = avalon.define({
	$id:'mycenter',
	wxuser:{unusedNum:0,nickName:'', headimgUrl:'http://thirdwx.qlogo.cn/mmopen/vi_32/nUYFrDawI8YRpu5FhJfh98bAsicxbrTe8MZ0bCYYdDW1I86cMjILaibGBNFXvVrlRsXTtJB1ezd3lPfD6969kdDw/132'},
	headimgUrl:'http://thirdwx.qlogo.cn/mmopen/vi_32/nUYFrDawI8YRpu5FhJfh98bAsicxbrTe8MZ0bCYYdDW1I86cMjILaibGBNFXvVrlRsXTtJB1ezd3lPfD6969kdDw/132',
	submited:false,
	isUpdate:false,
	yhqNum:0,
	
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
	toSend:function(){
		window.location.href = 'mysends.html';
	},
	toMailing:function(){
		window.location.href = 'mymailings.html';
	},
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});

avalon.scan();