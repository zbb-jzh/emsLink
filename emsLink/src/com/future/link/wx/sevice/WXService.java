package com.future.link.wx.sevice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.future.link.delivery.model.Coupon;
import com.future.link.user.model.WxUser;
import com.future.link.utils.ToolDateTime;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;

public class WXService {
	
	public static final WXService service = Enhancer.enhance(WXService.class);
	
	
	@Before(Tx.class)
    public WxUser addWXUser(WxUser user) throws UnsupportedEncodingException {
		
		WxUser model = WxUser.dao.findFirst("select * from user_wx_user where openId = ? ", user.getOpenId());
        if(null != model){
        	if(StrKit.notBlank(user.getCity())) {
        		model.setCity(user.getCity());
        	}
        	
        	if(StrKit.notBlank(user.getCountry())) {
        		model.setCountry(user.getCountry());
        	}
        	
        	if(StrKit.notBlank(user.getHeadimgUrl())) {
        		model.setHeadimgUrl(user.getHeadimgUrl());
        	}
        	
        	if(StrKit.notBlank(user.getNickName())) {
        		model.setNickName(URLEncoder.encode(user.getNickName(), "utf-8"));
        	}
        	
        	if(StrKit.notBlank(user.getPrivilege())) {
        		model.setPrivilege(user.getPrivilege());
        	}
        	
        	if(StrKit.notBlank(user.getProvince())) {
        		model.setProvince(user.getProvince());
        	}
        	
        	if(StrKit.notBlank(user.getSex())) {
        		model.setSex(user.getSex());
        	}
        	
        	model.update();
        	return model;
        }else {
        	
        	user.save();
        	//发放优惠券
        	Coupon coupon = new Coupon();
        	coupon.setWxUserId(user.getId());
        	coupon.setCreateTime(ToolDateTime.getDate());
        	coupon.setStatus(0);
        	coupon.save();
        	return user;
        }
        
    }

}
