package com.future.link.delivery.controller;

import com.future.link.common.Result;
import com.future.link.delivery.model.Send;
import com.future.link.delivery.service.SendDeliveryService;
import com.future.link.goods.service.CategoryService;
import com.future.link.utils.Constant;
import com.jfinal.core.Controller;

public class DeliveryController extends Controller{
	
	
	/**
	 * 添加代送快递
	 */
	public void addSendDelivery() {
		
		Send send = getModel(Send.class);
		renderJson(SendDeliveryService.service.addSendDelivery(send));
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

}
