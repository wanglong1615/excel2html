package com.codecraft.excel2html.parse;

import java.io.InputStream;

import com.codecraft.excel2html.config.ConvertConfig;
import com.codecraft.excel2html.entity.ExcelTable;
/**
 * excel解析器
 * @author zoro
 *
 */
public interface IExcelParse {
	public ExcelTable parse(InputStream input, ConvertConfig config) throws Exception;
	
	public ExcelTable parse(InputStream input,int sheetIndex, ConvertConfig config) throws Exception;
}
