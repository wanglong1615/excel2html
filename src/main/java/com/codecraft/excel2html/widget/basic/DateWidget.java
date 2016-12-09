package com.codecraft.excel2html.widget.basic;

import java.util.Map;

import com.codecraft.excel2html.cons.ExcelConstant;
import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.utils.WidgeUtils;
import com.codecraft.excel2html.widget.AbsWidget;
/**
 *  date--时间控件
 */
public class DateWidget extends AbsWidget {

	public String WIDGE_NAME = "date".toLowerCase();

	@Override
	public String parseHtml4Edit(String widgeType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		double height = td.getHeightNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String textAlign = td.getTextAlign();
		String fontStyle = getFontStyle(td);
		if (isCur(widgeType)) {
			validate(widgeType);
			int length = 12;
			width = width-2-2;//表格长度-padding-边框
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String format = attrs.get("format")!=null? attrs.get("format"):"";//格式化
			String auth = attrs.get("auth")!=null? attrs.get("auth").toLowerCase():"";//编写者(控件id需要转为小写)
			if("".equals(format)){
				format = "yyyy年MM月dd日";
			}
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' auth='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle;background-color:%s; word-wrap: break-word; word-break: break-all;%s' onclick=\"WdatePicker({dateFmt:'%s'});\" oninput='changeEvent(this);' onpropertychange='changeEvent(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarFocusEvent(this);' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, auth, width, height, textAlign, ExcelConstant.WIDGET_BACKGROUND_COLOR, fontStyle, format, length/2));
			return WIDGE_NAME;
		} 
		return "";
	}

	@Override
	public String parseHtml4View(String widgeType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		double height = td.getHeightNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String textAlign = td.getTextAlign();
		String fontStyle = getFontStyle(td);
		if (isCur(widgeType)) {
			validate(widgeType);
			int length = 12;//时间 
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String auth = attrs.get("auth")!=null? attrs.get("auth"):"";//编写者
			width = width-2-2;//表格长度-padding-边框
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='false' contenteditable='false' showmenu='true' auth='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, auth, width, height, textAlign, fontStyle, length/2));
			return WIDGE_NAME;
		} 
		return "";
	}

	@Override
	public void validate(String content){
	}

	@Override
	public boolean isCur(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
