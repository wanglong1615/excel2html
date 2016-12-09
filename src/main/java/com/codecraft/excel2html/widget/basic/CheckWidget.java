package com.codecraft.excel2html.widget.basic;

import java.util.Map;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.utils.WidgeUtils;
import com.codecraft.excel2html.widget.AbsWidget;
/**
 * 单选控件
 * @author zoro
 *
 */
public class CheckWidget extends AbsWidget {

	public static final String WIDGE_NAME = "chk".toLowerCase();

	@Override
	public String parseHtml4Edit(String widgeType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String fontStyle = getFontStyle(td);
		if (isCur(widgeType)) {
			validate(widgeType);
			width = width-2-2;//表格长度-padding-边框
			
			String[] valueArray = null;
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String value = attrs.get("value")!=null? attrs.get("value"):"";//属性值
			if("".equals(value)){//兼容以前的写法
				if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
					String values = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
					valueArray = values.split(",");
				}
			}else{
				valueArray = value.split(",");
			}
			sb.append("<div widgeType='chkDiv'>");
			String alias = tagId;//别名,不重复即可
			for(int i = 0; i < valueArray.length; i++ ){
				String v = valueArray[i];
				if(i != 0){
					tagId = getTagId(table, WIDGE_NAME);
				}
				sb.append(String.format(
						"<input id='%s' name='%s' type='checkbox' alias='%s' enabled='enabled' widgeType='%s' style='padding:1px;%s' value='%s'/>%s",
						tagId, tagId, alias, WIDGE_NAME, fontStyle, v, v));
			}
			sb.append("</div>");
			return WIDGE_NAME;
		} 
		return "";
	}

	@Override
	public String parseHtml4View(String widgeType, StringBuilder sb, ExcelTable table, ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String fontStyle = getFontStyle(td);
		if (isCur(widgeType)) {
			validate(widgeType);
			width = width-2-2;//表格长度-padding-边框
			String[] valueArray = null;
			if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
				String values = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
				valueArray = values.split(",");
			}
			sb.append("<div widgeType='chkDiv'>");
			String alias = tagId;//别名,不重复即可
			for(int i = 0; i < valueArray.length; i++ ){
				String v = valueArray[i];
				if(i != 0){	
					tagId = getTagId(table, WIDGE_NAME);
				}
				sb.append(String.format(
						"<input id='%s' name='%s' type='checkbox' alias='%s' disabled='disabled' widgeType='%s' style='padding:1px;%s' value='%s'/>%s",
						tagId, tagId, alias, WIDGE_NAME, fontStyle, v, v));
			}
			sb.append("</div>");
			return WIDGE_NAME;
		} 
		return "";
	}

	@Override
	public void validate(String content){
		
	}

	@Override
	public boolean isCur(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME + "[")){//必须是startWith
			return true;
		}
		return false;
	}
}
