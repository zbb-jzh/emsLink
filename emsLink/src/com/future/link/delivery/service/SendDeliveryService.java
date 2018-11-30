package com.future.link.delivery.service;

import java.util.ArrayList;
import java.util.List;

import com.future.link.common.Result;
import com.future.link.consumer.model.Consumer;
import com.future.link.delivery.model.Coupon;
import com.future.link.delivery.model.Send;
import com.future.link.goods.model.Category;
import com.future.link.user.model.WxUser;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

public class SendDeliveryService {
	
	public static final SendDeliveryService service = Enhancer.enhance(SendDeliveryService.class);
	
	@Before(Tx.class)
	public Result addSendDelivery(Send send) {
		
		send.setCreateTime(ToolDateTime.getDate());
		
		Category category = Category.dao.findById(send.getTeamId());
		
		send.setKdyId(category.getKdyId());
		Consumer consumer = Consumer.dao.findFirst("select * from consumer_consumer where id = ?", category.getKdyId());
		if(null != consumer) {
			send.setKdyName(consumer.getName());
			send.setKdyPhone(consumer.getPhone());
		}
		send.setYfPrice(category.getYfPrice());
		
		send.setAddress(Category.dao.findById(send.getTownId()).getName() + category.getName() + send.getAddress());
		
		//代金券支付
		if(send.getStatus() == 3) {
			Coupon coupon = CouponService.service.getCoupon(send.getWxUserId());
			if(null != coupon) {
				coupon.setStatus(1);
				coupon.update();
			}
		}
		if(send.getPayStatus() == 2) {
			send.setPayStatus(0);
		}
		send.save();
		return new Result(Result.SUCCESS_STATUS, send);
		
	}
	
	/**
	 * 取消订单
	 * @param id
	 * @return
	 */
	public Result cancleOrder(int id) {
		
		Db.update("update delivery_send set status = 0 where id = ?", id);
		
		return new Result(Result.SUCCESS_STATUS, "成功");
	}
	
	/**
	 * 快递员确认送达快递
	 * @param id
	 * @return
	 */
	public Result confirmSendOrder(int id) {
		
		Db.update("update delivery_send set status = 2, payStatus = 1 where id = ?", id);
		
		return new Result(Result.SUCCESS_STATUS, "成功送达");
	}
	
	/**
	 * 快递员确认shoujian
	 * @param id
	 * @return
	 */
	public Result confirmMailingOrder(int id, int status, String expressName, String expressNo, String yfPrice) {
		
		if(status == 5) {
			Db.update("update delivery_send set status = ?, yfPrice = ? where id = ?", status, Double.valueOf(yfPrice), id);
		}else {
			Db.update("update delivery_send set status = ? , expressName = ?, expressNo = ? where id = ?", status,expressName, expressNo, id);
		}
		
		return new Result(Result.SUCCESS_STATUS, "成功送达");
	}
	
	/**
	 * 微信回调成功，修改订单状态
	 */
	public void updateOrderPayStatus(int id) {
		
		Db.update("update delivery_send set payStatus = 2 where id = ?", id);
	}
	
	/**
	 * 查询用户订单
	 * @param type
	 * @param status
	 * @param user  and wxUserId = ?  , user.getId()
	 * @return
	 */
	public Result searchWxUserOrder(int type, int status, WxUser user) {
		List<Send> list;
		if(status == 3) {
			list = Send.dao.find("select * from delivery_send where payStatus = 2 or payStatus = 3 and type = ? order by createTime desc ", type);
		}else if(status == 1) {
			list = Send.dao.find("select * from delivery_send where type = ? and payStatus = 0 order by createTime desc ", type);
		}else {
			list = Send.dao.find("select * from delivery_send where type = ? and status = ? order by createTime desc ", type, status);
		}
		
		return new Result(Result.SUCCESS_STATUS, list);
	}
	
	/**
	 * 查询快递员订单
	 * @param type
	 * @param status
	 * @param user  and kdyId = ?  , user.getId()
	 * @return
	 */
	public Result searchCourierOrder(int type, int status, Consumer user) {
		List<Send> list;
		
		list = Send.dao.find("select * from delivery_send where type = ? and status = ? ", type, status);
		
		return new Result(Result.SUCCESS_STATUS, list);
	}
	
	/**
     * 分页查询
     * @return
     */
    public Page<Send> page(int pageNumber, int pageSize, int type, int status, WxUser user){

        StringBuffer sql = new StringBuffer(" from delivery_send where type = ? and status = ? and wxUserId = ? ");
        List<Object> params = new ArrayList<>();
        params.add(type);
        params.add(status);
        
        params.add(user.getId());
        sql.append(" order by createTime desc");
        Page<Send> page = Send.dao.paginate(pageNumber, pageSize, "select * ", sql.toString(), params.toArray());
        return page;
    }
}
