package com.codecraft.excel2html.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * 跨行 跨列信息
 * @author zoro
 *
 */
public class RowColumnSpan {
	/*
	 * 记录跨行与跨列信息,key为 topRow + topCol ,value为 topCol + bottomRow
	 * 
	 *           topRow
	 *         ________
	 *  	  |        |
	 *  topCol|        |bottomRow
	 *        |________|
	 *        
	 *           bottomRow 
	 */
	private Map<String, String> rowColunmSpanMap = new HashMap<String, String>();//跨行与跨列的对象
	
	/*
	 * <table>
	 * 		<tr>
	 * 			<td colspan='2'></td>
	 * 			<td></td>      ----->需要把此td过滤掉,rowColunmSpanFilter就是这个作用.
	 * 			<td></td>
	 * 		<tr>	
	 * 	  	<tr>
	 * 			<td></td>
	 * 			<td></td>
	 * 			<td></td>
	 * 		<tr>	
	 * 
	 * </table>
	 * 
	 */
	private Set<String> rowColunmSpanFilter = new HashSet<String>();

	public Map<String, String> getRowColunmSpanMap() {
		return rowColunmSpanMap;
	}

	public void setRowColunmSpanMap(Map<String, String> rowColunmSpanMap) {
		this.rowColunmSpanMap = rowColunmSpanMap;
	}

	public Set<String> getRowColunmSpanFilter() {
		return rowColunmSpanFilter;
	}

	public void setRowColunmSpanFilter(Set<String> rowColunmSpanFilter) {
		this.rowColunmSpanFilter = rowColunmSpanFilter;
	}
}
