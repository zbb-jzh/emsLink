package com.future.link.delivery.service;

import com.future.link.common.Result;
import com.future.link.delivery.model.Coupon;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;

public class CouponService {
	
	public static final CouponService service = Enhancer.enhance(CouponService.class);
	
	/**
	 * 新增
	 * @param coupon
	 * @return
	 */
	public Result addCoupon(Coupon coupon) {
		
		coupon.setStatus(0);
		coupon.setCreateTime(ToolDateTime.getDate());
		coupon.save();
		return new Result(Result.SUCCESS_STATUS, "添加成功");
		
	}

	/**
	 * 统计用户未使用的优惠券数量
	 * @return
	 */
	public int countUnusedCoupon(int userId) {
		
		int num = Db.queryInt("select count(id) from delivery_coupon where wxUserId = ? and status = 0", userId);
		return num;
		
	}
}
