package com.codecraft.excel2html.entity;

import java.util.HashMap;
import java.util.Map;
/**
 * excel table tr
 * @author zoro
 *
 */
public class ExcelTableTr {
	private int rowNum = -1;//tr所属列
	private String height = "";//tr的height
	private int lastColNum = -1;//tr的结束列
	private Map<Integer, ExcelTableTd> tdMap = new HashMap<Integer, ExcelTableTd>();//td map

	public Map<Integer, ExcelTableTd> getTdMap() {
		return tdMap;
	}

	public void setTdMap(Map<Integer, ExcelTableTd> tdMap) {
		this.tdMap = tdMap;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getLastColNum() {
		return lastColNum;
	}

	public void setLastColNum(int lastColNum) {
		this.lastColNum = lastColNum;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
