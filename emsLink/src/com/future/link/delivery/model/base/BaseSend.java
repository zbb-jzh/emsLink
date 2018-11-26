package com.future.link.delivery.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSend<M extends BaseSend<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return get("phone");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		return get("address");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setTownId(java.lang.Long townId) {
		set("townId", townId);
	}

	public java.lang.Long getTownId() {
		return get("townId");
	}

	public void setTeamId(java.lang.Long teamId) {
		set("teamId", teamId);
	}

	public java.lang.Long getTeamId() {
		return get("teamId");
	}

	public void setWxUserId(java.lang.Integer wxUserId) {
		set("wxUserId", wxUserId);
	}

	public java.lang.Integer getWxUserId() {
		return get("wxUserId");
	}

	public void setKdyId(java.lang.String kdyId) {
		set("kdyId", kdyId);
	}

	public java.lang.String getKdyId() {
		return get("kdyId");
	}

	public void setYfPrice(java.lang.Double yfPrice) {
		set("yfPrice", yfPrice);
	}

	public java.lang.Double getYfPrice() {
		return get("yfPrice");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

}
