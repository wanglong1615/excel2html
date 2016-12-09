package com.codecraft.excel2html.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.codecraft.excel2html.config.ConvertConfig;
import com.codecraft.excel2html.cons.ExcelConstant;
import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.generator.HtmlGenerator;
import com.codecraft.excel2html.htmlPrint.FilePrint;
import com.codecraft.excel2html.htmlPrint.IHtmlPrint;
import com.codecraft.excel2html.parse.IExcelParse;
import com.codecraft.excel2html.utils.ExcelUtils;

/**
 * 用户只需调用conver方法即可
 * @author zoro
 *
 */
public class Excel2Html{
	//转换配置
	private ConvertConfig config = null;
	
	public Excel2Html(){
		this.config = new ConvertConfig();
	}
	
	public Excel2Html(ConvertConfig config){
		this.config = config;
	}
	
	/**
	 * 转换所有的sheet
	 * @param is
	 * @throws Exception
	 */
	public void conver(InputStream is) throws Exception{
		IExcelParse excelParse = config.getExcelParse();
		//解析excel(用户可自定义)
		ExcelTable table = excelParse.parse(is, config);
		table.setConfig(config);
		
		//生成html页面
		HtmlGenerator htmlGener = new HtmlGenerator();
		String html =  htmlGener.toHtml(table);
		
		//输出html(用户可自定义)
		IHtmlPrint print = config.getHtmlPrint();
		print.print(html);
	}
	
	/**
	 * 转换指定sheet
	 * @param is
	 * @param sheetIndex
	 * @throws Exception
	 */
	public void conver(InputStream is,int sheetIndex) throws Exception{
		if(!config.getExcelType().equalsIgnoreCase(ExcelConstant.EXCEL_TYPE_4_HSSF)){
			throw new Exception("目前不支持'" + config.getExcelType() + "'类型!");
		}
		
		IExcelParse excelParse = config.getExcelParse();
		//解析excel(用户可自定义)
		ExcelTable excelTable = excelParse.parse(is, sheetIndex, config);
		//生成html页面
		HtmlGenerator htmlGener = new HtmlGenerator();
		String html =  htmlGener.toHtml(excelTable);
		
		//输出html(用户可自定义)
		IHtmlPrint print = config.getHtmlPrint();
		print.print(html);
	}

	public ConvertConfig getConfig() {
		return config;
	}

	public void setConfig(ConvertConfig config) {
		this.config = config;
	}
	
	//main test
	public static void main(String[] args) throws Exception{
		//one convert
		File file = new File("C:/Temp/1.xls");
		ConvertConfig config = new ConvertConfig();
		config.setHtmlPrint(new FilePrint("test"));
		
		config.setMaxRowNum(500).setMaxCellNum(500).setExcelType("HSSF");
		Excel2Html excel2Html = new Excel2Html(config);
		FileInputStream fis = new FileInputStream(file);
		excel2Html.conver(fis, 0);
		fis.close();
		
		//batch convert
//		File dir = new File("C:/Temp");
//		File[] listFiles = dir.listFiles(new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String name) {
//				if(name.endsWith(".xls")){
//					return true;
//				}
//				return false;
//			}
//		});
//		
//		ConvertConfig config = new ConvertConfig();
//		for(File file : listFiles){
//			String fileName = file.getName();
//			if(fileName.indexOf(".") != -1){
//				fileName = fileName.substring(0, fileName.indexOf("."));
//			}
//			config.setHtmlPrint(new FilePrint(fileName));
//			
//			config.setMaxRowNum(500).setMaxCellNum(500).setExcelType("HSSF");
//			Excel2Html excel2Html = new Excel2Html(config);
//			FileInputStream fis = new FileInputStream(file);
//			excel2Html.conver(fis, 0);
//			fis.close();
//		}
	}
}
