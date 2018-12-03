package com.future.link.delivery.service;

import com.future.link.common.Result;
import com.future.link.delivery.model.Evaluate;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;

public class EvaluateService {
	
	public static final EvaluateService service = Enhancer.enhance(EvaluateService.class);
	
	
	@Before(Tx.class)
	public Result addEvaluate(long orderId, int starNum, String content) {
		
		Evaluate evaluate = new Evaluate();
		
		evaluate.setOrderId(orderId);
		evaluate.setStarNum(starNum);
		evaluate.setContent(content);
		evaluate.setCreateTime(ToolDateTime.getDate());
		
		evaluate.save();
		
		Db.update("update delivery_send set evaluateStatus = 1 where id = ?", orderId);
		
		return new Result(Result.SUCCESS_STATUS, "添加成功");
		
	}
	
	public Result getEvaluate(long orderId) {
		
		
		return new Result(Result.SUCCESS_STATUS, Evaluate.dao.findFirst("select * from delivery_evaluate where orderId = ?", orderId));
	}

}
