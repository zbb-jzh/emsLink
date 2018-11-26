package com.future.link.wx.sevice;

import com.future.link.user.model.WxUser;
import com.jfinal.aop.Enhancer;

public class WXService {
	
	public static final WXService service = Enhancer.enhance(WXService.class);
	
	
    public void addWXUser(WxUser user) {
        if(isExist(user.getOpenId())){
        	user.update();
        }
        user.save();
    }

    public boolean isExist(String openId) {
    	
    	WxUser model = WxUser.dao.findFirst("select * from user_wx_user where openId = ? ", openId);

        if(model == null){
            return false;
        }
        return true;
    }

}
