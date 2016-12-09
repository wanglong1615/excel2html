package com.codecraft.excel2html.widget;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTdStyle;
import com.codecraft.excel2html.entity.ExcelWidget;
import com.codecraft.excel2html.utils.StringsUtils;

/**
 * 控件接口
 * @author zoro
 *
 */
public abstract class AbsWidget implements IWidget{

	/**
	 * 获得当前控件id
	 * @param table
	 * @return
	 */
	protected String getTagId(ExcelTable table,String widgetType){
		int startIdValue = table.getStartIdValue();
		String tagId = String.format("A%s", table.getWidgetList().size() + startIdValue);
		
		ExcelWidget widget = new ExcelWidget();
		widget.setTagId(tagId);
		widget.setWidgetType(widgetType);
		table.getWidgetList().add(widget);
		return tagId;
	}
	
	/**
	 *  获得td字体样式 
	 * @param td
	 * @return
	 */
	protected String getFontStyle(ExcelTableTd td) {
		ExcelTableTdStyle bo = td.getTdStyle();
		String fontSize = bo.getFontHeight();//字体大小
		String fontFamily = bo.getFontName();
		StringBuffer sb = new StringBuffer("");
		
		if(!StringsUtils.isEmpty(fontSize)){
			sb.append(String.format("font-size:%s;", fontSize));
		}
		if(!StringsUtils.isEmpty(fontFamily)){
			sb.append(String.format("font-family:%s;", fontFamily));
		}
		return sb.toString();
	}
	
	
}
