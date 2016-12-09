package com.codecraft.excel2html.widget.basic;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.widget.AbsWidget;

public class EditorWidget extends AbsWidget {

	public static final String WIDGE_NAME = "editor".toLowerCase();
	
	@Override
	public String parseHtml4Edit(String widgeType, StringBuilder sb,
			ExcelTable table, ExcelTableTd td) throws Exception {
		return "";
	}

	@Override
	public String parseHtml4View(String widgeType, StringBuilder sb,
			ExcelTable table, ExcelTableTd td) throws Exception {
		return "";
	}

	@Override
	public void validate(String widgeType) throws Exception {
		
	}

	@Override
	public boolean isCur(String widgeType) {
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
