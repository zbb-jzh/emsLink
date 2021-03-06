package com.future.link.user.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
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

	public void setPassword(java.lang.String password) {
		set("password", password);
	}

	public java.lang.String getPassword() {
		return get("password");
	}

	public void setDepartment(java.lang.String department) {
		set("department", department);
	}

	public java.lang.String getDepartment() {
		return get("department");
	}

	public void setSex(java.lang.Integer sex) {
		set("sex", sex);
	}

	public java.lang.Integer getSex() {
		return get("sex");
	}

	public void setEmail(java.lang.String email) {
		set("email", email);
	}

	public java.lang.String getEmail() {
		return get("email");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		return get("address");
	}

	public void setCreateTime(java.lang.Long createTime) {
		set("createTime", createTime);
	}

	public java.lang.Long getCreateTime() {
		return get("createTime");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setSalary(java.lang.Integer salary) {
		set("salary", salary);
	}

	public java.lang.Integer getSalary() {
		return get("salary");
	}

	public void setPost(java.lang.String post) {
		set("post", post);
	}

	public java.lang.String getPost() {
		return get("post");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

	public void setConsumerId(java.lang.String consumerId) {
		set("consumerId", consumerId);
	}

	public java.lang.String getConsumerId() {
		return get("consumerId");
	}

	public void setTwoPassword(java.lang.String twoPassword) {
		set("twoPassword", twoPassword);
	}

	public java.lang.String getTwoPassword() {
		return get("twoPassword");
	}

}
