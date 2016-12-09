package com.codecraft.excel2html.parse;

import java.util.Set;

import com.codecraft.excel2html.config.ConvertConfig;
import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTr;
import com.codecraft.excel2html.utils.StringsUtils;
import com.codecraft.excel2html.widget.IWidget;

/**
 * excel内容解析器
 * @author zoro
 *
 */
public class ExcelContentParse implements IExcelContentParse{
	/**
	 * 解析html内容 (包含控件)
	 */
	@Override
	public String parseConten(ExcelTable table, ExcelTableTr tr, ExcelTableTd td)
			throws Exception {
		ConvertConfig config = table.getConfig();
		Set<IWidget> allRegWidget = config.getDefaultRegWidget();
		boolean view = config.getHtmlView();
		String content = td.getContent().trim();
		StringBuilder sb = new StringBuilder();
		String widgetType = parseWidgetType(content);
		if(!StringsUtils.isEmpty(widgetType)){
			for(IWidget widget : allRegWidget){
				String result = "";
				if(view){
					result = widget.parseHtml4Edit(widgetType, sb, table, td);
				}else{
					result = widget.parseHtml4View(widgetType, sb, table, td);
				}
				if(!"".equals(result)){
					return result;
				}else{
					return "";
				}
			}
		}
		return content;
	}
	
	private String parseWidgetType(String content){
		if(StringsUtils.isEmpty(content)){
			return "";
		}
		int startIndex = content.indexOf("${");
		int endIndex = content.indexOf("}",startIndex);
		if(startIndex != -1 && endIndex != -1) {
			String widgetType = content.substring(startIndex + 2, endIndex);
			return widgetType;
		}
		return "";
	}
}
