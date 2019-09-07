package com.qsyout.core.entity;

import java.util.List;

@SuppressWarnings("rawtypes")
public class PageInfo {
	
	private Object total;
	private List list;

	public PageInfo(Object total, List list) {
		super();
		this.total = total;
		this.list = list;
	}

	public PageInfo() {
		super();
	}

	public Object getTotal() {
		return total;
	}

	public void setTotal(Object total) {
		this.total = total;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
}
