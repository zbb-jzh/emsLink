/**
 * 
 */

var vm = avalon.define({
	$id:'my',
	orderList:[],
	submited:false,
	isUpdate:false,
	
    removeInput:function(name){
    	vm.consumer[name] = '';
    },
	goback:function()
	{
		window.location.href = '#/consumer/list';
	}
});

avalon.scan();