package com.future.link.delivery.service;

import com.future.link.common.Result;
import com.future.link.delivery.model.Send;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Enhancer;

public class SendDeliveryService {
	
	public static final SendDeliveryService service = Enhancer.enhance(SendDeliveryService.class);
	
	public Result addSendDelivery(Send send) {
		
		send.setCreateTime(ToolDateTime.getDate());
		
		return new Result(Result.SUCCESS_STATUS, "添加成功");
		
	}

}
