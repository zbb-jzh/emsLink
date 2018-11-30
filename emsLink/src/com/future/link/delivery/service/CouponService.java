package com.future.link.delivery.service;

import java.util.List;

import com.future.link.common.Result;
import com.future.link.delivery.model.Coupon;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Enhancer;

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
	public int countUnusedCoupon(long userId) {
		
		List<Coupon> list = Coupon.dao.find("select * from delivery_coupon where wxUserId = ? and status = 0", userId);
		if(null == list) {
			return 0;
		}else {
			return list.size();
		}
	}
	
	/**
	 * 获取用户最早得到的优惠券
	 * @return
	 */
	public Coupon getCoupon(long userId) {
		return Coupon.dao.findFirst("select * from delivery_coupon where wxUserId = ? and status = 0 order by createTime", userId);
	}
}
