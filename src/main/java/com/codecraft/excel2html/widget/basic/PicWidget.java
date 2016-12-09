package com.codecraft.excel2html.widget.basic;

import java.util.List;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.PictureStyle;
import com.codecraft.excel2html.widget.AbsWidget;

public class PicWidget extends AbsWidget {

	public static final String WIDGE_NAME = "pic".toLowerCase();
	
	@Override
	public String parseHtml4Edit(String widgetType, StringBuilder sb,
			ExcelTable table, ExcelTableTd td) throws Exception {
		return parseContent(table);
	}

	@Override
	public String parseHtml4View(String widgetType, StringBuilder sb,
			ExcelTable table, ExcelTableTd td) throws Exception {
		return parseContent(table);
	}
	
	@Override
	public void validate(String widgetType) throws Exception {
		
	}

	@Override
	public boolean isCur(String widgetType) {
		if(widgetType.equalsIgnoreCase(WIDGE_NAME)){
			return true;
		}
		return false;
	}

	/**
	 * 解析图片
	 * @param table
	 * @return
	 */
	public String parseContent(ExcelTable table){
		List<PictureStyle> picStyleList = table.getPicStyleList();
		if(picStyleList == null || picStyleList.size() == 0){
			return "";
		}
		int index = table.getUsedPicIndex();
		PictureStyle picStyle = null;
		if(picStyleList.size() > index){
			picStyle = picStyleList.get(index);
			table.setUsedPicIndex(index++);
		}
		if(picStyle == null){
			return "";
		}
		double width = picStyle.getWidth();
		double height = picStyle.getHeight();
		String picUrl = picStyle.getUrl();
		String picHtml = String.format(
								"<img src='%s' style='padding:2px;width:%spx;height:%spx;' />", picUrl, width-4,height);//减去4 表示左右两边2px的留白
		return picHtml;
	}
}
