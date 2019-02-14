package com.andy.domain.common;

import java.util.List;

/**
 * @author mu
 * 公共的vo
 */
public class PageVo {
	@SuppressWarnings("rawtypes")
	private List rows;
	private int total;
	@SuppressWarnings("rawtypes")
	private List footer;
	
	private Integer page; // 第几页

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@SuppressWarnings("rawtypes")
	public List getFooter() {
		return footer;
	}

	@SuppressWarnings("rawtypes")
	public void setFooter(List footer) {
		this.footer = footer;
	}

}
