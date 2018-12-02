

var vm = avalon.define({
	$id:'statisticorder',
	totalPageCount:-1,
    currentPage:1,
    count:10,
	name:'',
	kdyname:'',
	phone:'',
	type:'',
	status:'',
	payStatus:'',
	startTime:'',
    endTime:'',
	startDate:'',
	endDate:'',
	sendsOrderList:[],
	hasAdd:false,
	hasDelete:false,
	hasUpdate:false,
	hasLook:false,
	showExpress:false,
	expressName:'',
	expressNo:'',
	showCancelOrder:false,
	orderId:'',
	sendorder:{kdyName:'', kdyPhone:'',totalPrice:''},
	
	getStatisticorder:function()
	{
		if(!vm.changeDate()){
			return;
		}
		
		$.ajax({
		    url: "../delivery/doStatisticsOrder",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {kdyName:vm.kdyname, type:vm.type,status:vm.status,payStatus:vm.payStatus, startTime:vm.startTime, endTime:vm.endTime},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		vm.sendorder = res.data;
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
			
		/*$.ajax({
		    url: "../delivery/doPage",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {pageNo:pageNumber, pageSize:pageSize, kdyName:vm.kdyname, name:vm.name,phone:vm.phone, type:vm.type,status:vm.status,payStatus:vm.payStatus, startTime:vm.startTime, endTime:vm.endTime},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		vm.sendsOrderList = res.data.list;
	                
	                var morePageCount = 0;
	                if(res.data.totalPage > 0){
	                    morePageCount = res.data.totalPage - res.data.pageNumber;
	                }

	                if (vm.totalPageCount == -1 || (morePageCount + vm.currentPage) != vm.totalPageCount) {
	                	vm.totalPageCount = morePageCount + vm.currentPage;

	                    var options = {
	                        bootstrapMajorVersion : 3,
	                        currentPage : vm.currentPage,
	                        totalPages : vm.totalPageCount,
	                        onPageClicked : function(e, originalEvent, type, page) {
	                        	vm.currentPage = page;
	                        	vm.getConsumerList(vm.currentPage, vm.count);
	                        },
	                        onPageChanged : function(event, oldPage, newPage) {
	                        }};
	                    $('.pagination').bootstrapPaginator(options);
	                }
                }
		        
		    },
		    error: function() {
		    	console.log('error');
		    }
		});*/
	},
	 search :function () {
        vm.totalPageCount=-1;
        vm.currentPage=1;
        vm.getStatisticorder();
    },
     preset : function () {
        vm.name = '';
        vm.type ='';
        vm.status = '',
        vm.phone = '',
        vm.kdyname = '',
        vm.payStatus = '';
        
        vm.startDate = '';
        vm.endDate = '';
        vm.getStatisticorder();
    },
    changeDate:function()
    {
    	console.log(1111);
    	console.log(vm.startDate);
    	var str1 = vm.startDate.replace(/-/g,"/");
        var str2 = vm.endDate.replace(/-/g,"/");
        vm.startTime = new Date(str1).getTime();
        vm.endTime = new Date(str2).getTime();
        console.log(vm.startTime);
        if(vm.startTime > vm.endTime){
            alert('起始时间应小于结束时间');
            return false;
        }
        if(str1==""){
        	vm.startTime = null;
        }
        if(str2==""){
        	vm.endTime = null;
        }
        return true;
    },
    toLookExpress:function(obj){
    	
    	vm.expressName = obj.expressName;
    	vm.expressNo = obj.expressNo;
    	vm.showExpress = true;
    	
    },
    hideExpress:function(){
    	vm.showExpress = false;
    },
     toAU : function (id) {
        window.location = '#/consumer/au?id=' + id;
    },
    toshowCancelOrder:function(id){
    	
    	vm.orderId = id;
    	vm.showCancelOrder = true;
    },
    hideCancelOrder:function(){
    	vm.showCancelOrder = false;
    },
    toCancelOrder:function()
    {
    	$.ajax({
		    url: "../delivery/doCancleOrder",    //请求的url地址
		    dataType: "json",   //返回格式为json
		    data: {id:vm.orderId},    //参数值
		    type: "post",   //请求方式
		    success: function(res) {
		    	if (res.status == 1) {
		    		vm.showCancelOrder = false;
		    		if(vm.sendsOrderList.length == 1){
		    			vm.currentPage = 1;
                        vm.getConsumerList(1, vm.count);
                    }else{
                        vm.getConsumerList(vm.currentPage, vm.count);
                    }
                }
		    },
		    error: function() {
		    	console.log('error');
		    }
		});
    }
});
if(btns[0].has){
	vm.hasAdd = true;
}
if(btns[1].has){
	vm.hasDelete = true;
}
console.log(vm.hasDelete);
if(btns[2].has){
	vm.hasUpdate = true;
}
if(btns[3].has){
	vm.hasLook = true;
}
vm.getStatisticorder();
avalon.scan();
$('.input-datepicker').datetimepicker(
        {
        }).on('changeDate', function (ev) {
    $(this).datetimepicker('hide');
});