package com.andy.domain.common;

public class Page {

	private Integer page; // 第几页
	private Integer rows; // 每页多少条

	private Integer num; // 第几页
	private Integer size; // 每页多少条


	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Page(Integer page, Integer rows) {
		this.page = page;
		this.rows = rows;
	}

	public Page() {

	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

}
