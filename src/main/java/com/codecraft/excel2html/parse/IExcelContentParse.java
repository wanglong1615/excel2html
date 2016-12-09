package com.codecraft.excel2html.parse;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTr;

public interface IExcelContentParse {
	public String parseConten(ExcelTable table, ExcelTableTr tr,
			ExcelTableTd td) throws Exception;
}
