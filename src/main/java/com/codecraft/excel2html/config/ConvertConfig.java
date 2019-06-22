package com.codecraft.excel2html.config;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.codecraft.excel2html.cons.ExcelConstant;
import com.codecraft.excel2html.htmlPrint.FilePrint;
import com.codecraft.excel2html.htmlPrint.IHtmlPrint;
import com.codecraft.excel2html.parse.ExcelCellStyleParse4Hssf;
import com.codecraft.excel2html.parse.ExcelContentParse;
import com.codecraft.excel2html.parse.ExcelParse4HSSF;
import com.codecraft.excel2html.parse.ExcelPicParse4HSSF;
import com.codecraft.excel2html.parse.IExcelCellStyleParse;
import com.codecraft.excel2html.parse.IExcelContentParse;
import com.codecraft.excel2html.parse.IExcelParse;
import com.codecraft.excel2html.parse.IExcelPicParse;
import com.codecraft.excel2html.picUpload.IExcelPicUpload;
import com.codecraft.excel2html.widget.IWidget;
import com.codecraft.excel2html.widget.basic.CheckWidget;
import com.codecraft.excel2html.widget.basic.DateWidget;
import com.codecraft.excel2html.widget.basic.EditorWidget;
import com.codecraft.excel2html.widget.basic.InputWidget;
import com.codecraft.excel2html.widget.basic.TextareaWidget;
import com.codecraft.excel2html.widget.calc.CalcNumWidget;
import com.codecraft.excel2html.widget.calc.CalcRltWidget;

/**
 * Excel解析的设置
 * @author zoro
 *
 */
public class ConvertConfig {
	//Excel解析器
	private IExcelParse excelParse;
	
	//pic解析器
	private IExcelPicParse excelPicParse;
	
	//样式解析器
	private IExcelCellStyleParse excelStyleParse;
	
	//内容解析器
	private IExcelContentParse excelContentParse;
	
	//图片上传
	private IExcelPicUpload picUpload;
	
	//HTML 输出
	private IHtmlPrint htmlPrint;
	
	//excel 格式
	private String excelType = ExcelConstant.EXCEL_TYPE_4_HSSF;
	
	//html domian
	private String htmlDomain = "";
	
	//js domain
	private String htmlJsVsersion = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	
	//prefix path
	private String prefixPath = "http://163.com";
	
	//excel最大行数
	private int maxRowNum = 300;
	
	//excel最大列数
	private int maxCellNum = 300;
	
	//测试宽度(testWidth为true时,会在控制台打印宽度)
	private boolean testWidth = false;
	
	//页面模式
	private boolean htmlView = true; 
	
	//页面默认控件
	private Set<IWidget> defaultRegWidget = null;
	
	//默认的是HSSF
	public ConvertConfig(){
		init();
	}
	
	/**
	 * 初始化
	 */
	@SuppressWarnings("serial")
	private void init(){
		excelParse = new ExcelParse4HSSF();
		excelPicParse = new ExcelPicParse4HSSF();
		excelStyleParse = new ExcelCellStyleParse4Hssf();
		excelContentParse = new ExcelContentParse();
		htmlPrint = new FilePrint();
		defaultRegWidget = new HashSet<IWidget>(){
			{
				//basic
				add(new InputWidget());
				add(new TextareaWidget());
				add(new EditorWidget());
				add(new CheckWidget());
				add(new DateWidget());
				
				//calc
				add(new CalcNumWidget());
				add(new CalcRltWidget());
			}
	};
		
	}

	public IExcelParse getExcelParse() {
		return excelParse;
	}

	public ConvertConfig setExcelParse(IExcelParse excelParse) {
		this.excelParse = excelParse;
		return this;
	}

	public IExcelPicParse getExcelPicParse() {
		return excelPicParse;
	}

	public ConvertConfig setExcelPicParse(IExcelPicParse excelPicParse) {
		this.excelPicParse = excelPicParse;
		return this;
	}

	public IExcelCellStyleParse getExcelStyleParse() {
		return excelStyleParse;
	}

	public ConvertConfig setExcelStyleParse(IExcelCellStyleParse excelStyleParse) {
		this.excelStyleParse = excelStyleParse;
		return this;
	}

	public IExcelContentParse getExcelContentParse() {
		return excelContentParse;
	}

	public ConvertConfig setExcelContentParse(IExcelContentParse excelContentParse) {
		this.excelContentParse = excelContentParse;
		return this;
	}

	public IExcelPicUpload getPicUpload() {
		return picUpload;
	}

	public ConvertConfig setPicUpload(IExcelPicUpload picUpload) {
		this.picUpload = picUpload;
		return this;
	}

	public IHtmlPrint getHtmlPrint() {
		return htmlPrint;
	}

	public ConvertConfig setHtmlPrint(IHtmlPrint htmlPrint) {
		this.htmlPrint = htmlPrint;
		return this;
	}

	public String getHtmlDomain() {
		return htmlDomain;
	}

	public ConvertConfig setHtmlDomain(String htmlDomain) {
		this.htmlDomain = htmlDomain;
		return this;
	}

	public String getHtmlJsVsersion() {
		return htmlJsVsersion;
	}

	public ConvertConfig setHtmlJsVsersion(String htmlJsVsersion) {
		this.htmlJsVsersion = htmlJsVsersion;
		return this;
	}

	public String getPrefixPath() {
		return prefixPath;
	}

	public ConvertConfig setPrefixPath(String prefixPath) {
		this.prefixPath = prefixPath;
		return this;
	}

	public int getMaxRowNum() {
		return maxRowNum;
	}

	public ConvertConfig setMaxRowNum(int maxRowNum) {
		this.maxRowNum = maxRowNum;
		return this;
	}

	public int getMaxCellNum() {
		return maxCellNum;
	}

	public ConvertConfig setMaxCellNum(int maxCellNum) {
		this.maxCellNum = maxCellNum;
		return this;
	}

	public String getExcelType() {
		return excelType;
	}

	public ConvertConfig setExcelType(String excelType) {
		this.excelType = excelType;
		return this;
	}

	public boolean getTestWidth() {
		return testWidth;
	}

	public ConvertConfig setTestWidth(boolean testWidth) {
		this.testWidth = testWidth;
		return this;
	}
	
	public ConvertConfig registerWidget(IWidget widget){
		if(widget != null){
			defaultRegWidget.add(widget);
		}
		return this;
	}
	
	public ConvertConfig registerWidgetAll(Collection<IWidget> colle){
		if(colle != null && colle.size() > 0){
			defaultRegWidget.addAll(colle);
		}
		return this;
	}

	public Set<IWidget> getDefaultRegWidget() {
		return defaultRegWidget;
	}

	public boolean getHtmlView() {
		return htmlView;
	}

	public ConvertConfig setHtmlView(boolean htmlView) {
		this.htmlView = htmlView;
		return this;
	}
}
