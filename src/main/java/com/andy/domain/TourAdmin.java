package com.andy.domain;

import com.andy.domain.common.Page;

import java.io.Serializable;

public class TourAdmin extends Page implements Serializable {
	private Long admin_id;
	
	private String login_name;
	
	private String admin_name;
	
	private String passwd;

	private String resource_codes;
	
	private String group_ids;

	public Long getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Long admin_id) {
		this.admin_id = admin_id;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getResource_codes() {
		return resource_codes;
	}

	public void setResource_codes(String resource_codes) {
		this.resource_codes = resource_codes;
	}

	public String getGroup_ids() {
		return group_ids;
	}

	public void setGroup_ids(String group_ids) {
		this.group_ids = group_ids;
	}

	@Override
	public String toString() {
		return "TourAdmin{" +
				"admin_id=" + admin_id +
				", login_name='" + login_name + '\'' +
				", admin_name='" + admin_name + '\'' +
				", passwd='" + passwd + '\'' +
				", resource_codes='" + resource_codes + '\'' +
				", group_ids='" + group_ids + '\'' +
				'}';
	}
}
