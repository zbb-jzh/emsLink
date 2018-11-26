package com.future.link.wx;

import com.future.link.wx.controller.WXController;
import com.jfinal.config.Routes;

public class WXRouter extends Routes{

	@Override
	public void config() {
		// TODO Auto-generated method stub
		this.add("wx", WXController.class);
	}

}
