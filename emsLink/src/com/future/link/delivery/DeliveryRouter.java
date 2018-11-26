package com.future.link.delivery;

import com.future.link.delivery.controller.DeliveryController;
import com.jfinal.config.Routes;

public class DeliveryRouter extends Routes{

	@Override
	public void config() {
		// TODO Auto-generated method stub
		this.add("delivery", DeliveryController.class);
	}

}
