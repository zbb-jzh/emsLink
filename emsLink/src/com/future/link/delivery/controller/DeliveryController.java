package com.future.link.delivery.controller;

import com.future.link.common.Result;
import com.future.link.delivery.model.Send;
import com.future.link.delivery.service.SendDeliveryService;
import com.future.link.goods.service.CategoryService;
import com.future.link.user.model.User;
import com.future.link.user.model.WxUser;
import com.future.link.utils.Constant;
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
	 * 查询用户快递订单
	 */
	public void doPage() {
		
		//类型，1是邮寄，2是代送
		int type = this.getParaToInt("type");
		//状态 1未支付，2，微信支付，3券支付
		int status = this.getParaToInt("status");
		
		int pageNo = this.getParaToInt("pageNo");
		
		int pageSize = this.getParaToInt("pageSize");
		
		WxUser user=(WxUser) this.getRequest().getSession().getAttribute("wxuser");
		
		renderJson(SendDeliveryService.service.page(pageNo,pageSize,type, status, user));
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
	 * 获取村
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
		
		renderJson(new Result(Constant.SUCCESS, user));
	}

}
