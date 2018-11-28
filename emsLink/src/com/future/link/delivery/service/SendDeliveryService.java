package com.future.link.delivery.service;

import java.util.ArrayList;
import java.util.List;

import com.future.link.common.Result;
import com.future.link.consumer.model.Consumer;
import com.future.link.delivery.model.Send;
import com.future.link.goods.model.Category;
import com.future.link.user.model.WxUser;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class SendDeliveryService {
	
	public static final SendDeliveryService service = Enhancer.enhance(SendDeliveryService.class);
	
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
		send.save();
		return new Result(Result.SUCCESS_STATUS, "添加成功");
		
	}
	
	public Result cancleOrder(int id) {
		
		Db.update("update delivery_send set status = 0 where id = ?", id);
		
		return new Result(Result.SUCCESS_STATUS, "成功");
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
		if(status == 2) {
			list = Send.dao.find("select * from delivery_send where status = 2 or status = 3 and type = ? ", type);
		}else {
			list = Send.dao.find("select * from delivery_send where type = ? and status = ? ", type, status);
		}
		
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
