package com.codecraft.excel2html.widget.calc;

import java.util.Map;

import com.codecraft.excel2html.cons.ExcelConstant;
import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.utils.StringsUtils;
import com.codecraft.excel2html.utils.WidgeUtils;
import com.codecraft.excel2html.widget.AbsWidget;

/**
 * 计算数据来源
 */
public class CalcNumWidget extends AbsWidget {

	public static final String WIDGE_NAME = "numtext".toLowerCase();

	@Override
	public String parseHtml4Edit(String widgeType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String textAlign = td.getTextAlign();
		String fontStyle = getFontStyle(td);
		if (isCur(widgeType)) {
			validate(widgeType);
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			width = width-2-2;//表格长度-padding-边框
			String val = attrs.get("val")!=null ? attrs.get("val") : "";//计算值
			String text = attrs.get("text")!=null ?  attrs.get("text") : "";//显示值
			
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			if(StringsUtils.isEmpty(val) && StringsUtils.isEmpty(text)){
				sb.append(String.format(
						"<input id='%s' name='%s' type='text' widgeType='%s' val='%s' text='%s' size='%s' oninput='calc(this);' onpropertychange='calc(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;background-color:%s;%s'/>",
						tagId, tagId, WIDGE_NAME, val, text, length, width, textAlign, ExcelConstant.WIDGET_BACKGROUND_COLOR, fontStyle));
			}else{
				sb.append(String.format(
						"<span id='%s' name='%s' type='text' widgeType='%s' val='%s' text='%s' size='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s'>%s</span>",
						tagId, tagId, WIDGE_NAME, val, text, length, width, textAlign, fontStyle, text));
			}
			return WIDGE_NAME;
		}
		return "";
	}

	@Override
	public String parseHtml4View(String widgeType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String textAlign = td.getTextAlign();
		String fontStyle = getFontStyle(td);
		if (isCur(widgeType)) {
			validate(widgeType);
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			width = width-2-2;//表格长度-padding-边框
			String val = attrs.get("val")!=null ? attrs.get("val") : "" ;//计算值
			String text = attrs.get("text")!=null ? attrs.get("text") : "";//显示值
			
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			sb.append(String.format(
						"<span id='%s' name='%s' type='text' widgeType='%s' val='%s' text='%s' size='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s'>%s</span>",
						tagId, tagId, WIDGE_NAME, val, text, length, width, textAlign, fontStyle, text));
			return WIDGE_NAME;
		}
		return "";
	}

	@Override
	public void validate(String content)throws Exception {
		
	}

	@Override
	public boolean isCur(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
