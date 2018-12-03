package com.future.link.delivery.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.future.link.common.Result;
import com.future.link.consumer.model.Consumer;
import com.future.link.delivery.model.Send;
import com.future.link.delivery.service.CouponService;
import com.future.link.delivery.service.EvaluateService;
import com.future.link.delivery.service.SendDeliveryService;
import com.future.link.goods.service.CategoryService;
import com.future.link.user.model.User;
import com.future.link.user.model.WxUser;
import com.future.link.utils.Constant;
import com.future.link.utils.ToolDateTime;
import com.jfinal.core.Controller;

public class DeliveryController extends Controller{
	
	
	/**
	 * 添加代送快递
	 */
	public void addSendDelivery() {
		
		Send send = getModel(Send.class);
		WxUser user=(WxUser) this.getRequest().getSession().getAttribute("wxuser");
		if(null != user) {
			send.setWxUserId(user.getId());
		}
		
		renderJson(SendDeliveryService.service.addSendDelivery(send));
	}
	
	/**
	 * 查询用户快递订单
	 */
	public void doSearchWxUserOrder() {
		
		//类型，1是邮寄，2是代送
		int type = this.getParaToInt("type");
		//状态 1未支付，2，微信支付，3券支付
		int status = this.getParaToInt("status");
		
		WxUser user=(WxUser) this.getRequest().getSession().getAttribute("wxuser");
		
		renderJson(SendDeliveryService.service.searchWxUserOrder(type, status, user));
	}
	
	/**
	 * 查询快递员，代送快递
	 */
	public void doSearchCourierOrder() {
		
		//类型，1是邮寄，2是代送
		int type = this.getParaToInt("type");
		//状态 1未支付，2，微信支付，3券支付
		int status = this.getParaToInt("status");
				
		User user=(User) this.getRequest().getSession().getAttribute(Constant.SESSION_USER);
				
		renderJson(SendDeliveryService.service.searchCourierOrder(type, status, user));
	}
	
	/**
	 * 快递员确认送达
	 */
	public void doConfirmSendOrder() {
		int id = this.getParaToInt("id");
		renderJson(SendDeliveryService.service.confirmSendOrder(id));
	}
	
	/**
	 * 快递员确认送达
	 */
	public void doConfirmMailingOrder() {
		int id = this.getParaToInt("id");
		int status = this.getParaToInt("status");
		String expressName = this.getPara("expressName");
		String expressNo = this.getPara("expressNo");
		String yfPrice = this.getPara("yfPrice");
		renderJson(SendDeliveryService.service.confirmMailingOrder(id, status, expressName, expressNo, yfPrice));
	}
	
	/**
	 * 查询用户快递订单
	 */
	public void doPage() {
		
		//类型，1是邮寄，2是代送
		String type = this.getPara("type");
		//状态 0未支付，1货到付款，2，微信支付，3券支付
		String status = this.getPara("status");
		
		String payStatus = this.getPara("payStatus");
		
		int pageNo = this.getParaToInt("pageNo");
		
		int pageSize = this.getParaToInt("pageSize");
		
		String kdyName = this.getPara("kdyName");
		
		String name = this.getPara("name");
		
		String phone = this.getPara("phone");
		
		long startTime = 0;
        long endTime = 0;
        if(this.getParaToLong("startTime") != null){
            startTime = this.getParaToLong("startTime");
        }
        
        if(this.getParaToLong("endTime") != null){
            endTime = this.getParaToLong("endTime");
        }
		
		WxUser user=(WxUser) this.getRequest().getSession().getAttribute("wxuser");
		
		renderJson(new Result(Result.SUCCESS_STATUS, SendDeliveryService.service.page(pageNo,pageSize,type, status,payStatus,kdyName, name,phone, startTime, endTime, user)));
	}
	
	/**
	 * 订单盘点
	 */
	public void doStatisticsOrder() {
		
		String kdyName = this.getPara("kdyName");
		
		String payStatus = this.getPara("payStatus");
		
		//类型，1是邮寄，2是代送
		String type = this.getPara("type");
		//状态 0未支付，1货到付款，2，微信支付，3券支付
		String status = this.getPara("status");
		
		long startTime = 0;
        long endTime = 0;
        if(this.getParaToLong("startTime") != null){
            startTime = this.getParaToLong("startTime");
        }
        
        if(this.getParaToLong("endTime") != null){
            endTime = this.getParaToLong("endTime");
        }
        
        renderJson(new Result(Result.SUCCESS_STATUS, SendDeliveryService.service.statisticsOrder(kdyName, payStatus, status, type, startTime, endTime)));
	}
	
	/**
	 * 取消订单
	 */
	public void doCancleOrder() {
		int id = this.getParaToInt("id");
		renderJson(SendDeliveryService.service.cancleOrder(id));
	}
	
	/**
	 * 获取镇
	 */
	public void doGetParent()
	{
		renderJson(new Result(Constant.SUCCESS, CategoryService.service.getParent()));
	}
	
	/**
	 * 获取庄列表 
	 */
	public void getChildren()
	{
		Long pid = this.getParaToLong("pid");
		renderJson(new Result(Constant.SUCCESS, CategoryService.service.getChildrenRecursive(pid)));
	}
	
	/**
	 * 获取商品分类，树形结构
	 */
	public void doTree()
	{
		renderJson(CategoryService.service.tree());
	}
	
	/**
	 * 获取微信用户信息
	 */
	public void getWxUser() {
		WxUser user=(WxUser) this.getRequest().getSession().getAttribute("wxuser");
		//user.setNickName(URLDecoder.decode(user.getNickName()));
		user.setUnusedNum(CouponService.service.countUnusedCoupon(user.getId()));
		int type = this.getParaToInt("type");
		Send send = SendDeliveryService.service.findNewSend(user, type);
		if(null != send) {
			user.setTownId(send.getTownId());
			user.setTeamId(send.getTeamId());
		}
		renderJson(new Result(Constant.SUCCESS, user));
	}
	
	/**
	 * 获取快递员用户信息
	 */
	public void getCourierUser() {
		User user=(User) this.getRequest().getSession().getAttribute(Constant.SESSION_USER);
		
		Consumer consumer = Consumer.dao.findById(user.getConsumerId());
		
		SimpleDateFormat fmt1= new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    	SimpleDateFormat fmt2= new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    	
    	Date now = new Date();
    	long startTime = ToolDateTime.parse(fmt1.format(now), "yyyy-MM-dd HH:mm:ss").getTime();
    	long endTime = ToolDateTime.parse(fmt2.format(now), "yyyy-MM-dd HH:mm:ss").getTime();
    	
    	System.out.println("今天的开始时间："+fmt1.format(now));
    	System.out.println("今天的结束时间："+fmt2.format(now));
		Send send = SendDeliveryService.service.statisticsOrder(consumer.getName(), "1", "", "", startTime, endTime);
		consumer.setWithdraws(send.getTotalPrice());
		renderJson(new Result(Constant.SUCCESS, consumer));
	}
	
	/**
	 * 评价
	 */
	public void doEvaluateOrder() {
		
		long orderId = this.getParaToLong("orderId");
		int starNum = this.getParaToInt("starNum");
		
		String content = this.getPara("content");
		
		renderJson(EvaluateService.service.addEvaluate(orderId, starNum, content));
	}
	
	/**
	 * 查看评价
	 * 
	 */
	public void doLookEvaluate() {
		long orderId = this.getParaToLong("orderId");
		
		renderJson(EvaluateService.service.getEvaluate(orderId));
	}
}
