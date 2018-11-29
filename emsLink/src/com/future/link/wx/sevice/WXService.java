package com.future.link.wx.sevice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.future.link.delivery.model.Coupon;
import com.future.link.user.model.WxUser;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.tx.Tx;

public class WXService {
	
	public static final WXService service = Enhancer.enhance(WXService.class);
	
	
	@Before(Tx.class)
    public void addWXUser(WxUser user) throws UnsupportedEncodingException {
		
		WxUser model = WxUser.dao.findFirst("select * from user_wx_user where openId = ? ", user.getOpenId());
        if(null != model){
        	model.setCity(user.getCity());
        	model.setCountry(user.getCountry());
        	model.setHeadimgUrl(user.getHeadimgUrl());
        	model.setNickName(URLEncoder.encode(user.getNickName(), "utf-8"));
        	
        	model.setPrivilege(user.getPrivilege());
        	model.setProvince(user.getProvince());
        	model.setSex(user.getSex());
        	model.update();
        }else {
        	
        	user.save();
        	//发放优惠券
        	Coupon coupon = new Coupon();
        	coupon.setWxUserId(user.getId());
        	coupon.setCreateTime(ToolDateTime.getDate());
        	coupon.setStatus(0);
        	coupon.save();
        }
        
    }

}
