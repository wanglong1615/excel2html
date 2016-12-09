package com.codecraft.excel2html.widget;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;

/**
 * 控件接口
 * @author zoro
 *
 */
public interface IWidget {

	// 编辑页面
	public String parseHtml4Edit(String widgetType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception;

	// 预览页面
	public String parseHtml4View(String widgetType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception;

	// 验证是否合法
	public void validate(String widgetType) throws Exception;

	// 判断是否是当前控件
	public boolean isCur(String widgetType);
}
