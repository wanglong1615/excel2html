package com.codecraft.excel2html.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.codecraft.excel2html.config.ConvertConfig;

/**
 * excel table
 * @author zoro
 *
 */
public class ExcelTable {
	private ExcelHeader header;//页眉
	
	private ExcelHeader footer;//页脚
	
	private boolean hasEditor = false;//是否有编辑器  
	
	private int firstRowNum = -1;//起始行(sheet.getFirstRowNum())
	
	private int lastRowNum = -1;//结束行(sheet.getLastRowNum();)
	
	private int widthRowNum  = -1;//width行(此行是程序自动添加)
	
	private int cellSize;//列数(firstRow.getLastCellNum() - firstRow.getFirstCellNum())
	
	private int startIdValue = 0;//多个sheet时控件id的初始值 (第一个sheet为0,第二个sheet初始值从上个sheet累加)
	
	private JSONObject excelIdMap = new JSONObject();//Excel id与 tag id的关联关系
	
	private JSONObject tagIdMap = new JSONObject();//tag id与Excel id的关联关系
	
	private Map<Integer, ExcelTableTr> trMap = new HashMap<Integer, ExcelTableTr>();//tr map
	
	private List<ExcelPicture> picList = new ArrayList<ExcelPicture>();//图片列表
	
	private List<PictureStyle> picStyleList = new ArrayList<PictureStyle>();//图片样式
	
	private List<ExcelWidget> widgetList = new ArrayList<ExcelWidget>();//控件列表 
	
	private RowColumnSpan rowColumnSpan;//行列跨行信息
	
	private ConvertConfig config;//转换配置
	
	private int usedPicIndex = 0;//图片被使用的index

	public ExcelHeader getHeader() {
		return header;
	}

	public void setHeader(ExcelHeader header) {
		this.header = header;
	}

	public ExcelHeader getFooter() {
		return footer;
	}

	public void setFooter(ExcelHeader footer) {
		this.footer = footer;
	}

	public boolean isHasEditor() {
		return hasEditor;
	}

	public void setHasEditor(boolean hasEditor) {
		this.hasEditor = hasEditor;
	}

	public int getFirstRowNum() {
		return firstRowNum;
	}

	public void setFirstRowNum(int firstRowNum) {
		this.firstRowNum = firstRowNum;
	}

	public int getLastRowNum() {
		return lastRowNum;
	}

	public void setLastRowNum(int lastRowNum) {
		this.lastRowNum = lastRowNum;
	}

	public int getCellSize() {
		return cellSize;
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}

	public int getStartIdValue() {
		return startIdValue;
	}

	public void setStartIdValue(int startIdValue) {
		this.startIdValue = startIdValue;
	}

	public JSONObject getExcelIdMap() {
		return excelIdMap;
	}

	public void setExcelIdMap(JSONObject excelIdMap) {
		this.excelIdMap = excelIdMap;
	}

	public JSONObject getTagIdMap() {
		return tagIdMap;
	}

	public void setTagIdMap(JSONObject tagIdMap) {
		this.tagIdMap = tagIdMap;
	}

	public Map<Integer, ExcelTableTr> getTrMap() {
		return trMap;
	}

	public void setTrMap(Map<Integer, ExcelTableTr> trMap) {
		this.trMap = trMap;
	}

	public List<ExcelPicture> getPicList() {
		return picList;
	}

	public void setPicList(List<ExcelPicture> picList) {
		this.picList = picList;
	}

	public List<ExcelWidget> getWidgetList() {
		return widgetList;
	}

	public void setWidgetList(List<ExcelWidget> widgetList) {
		this.widgetList = widgetList;
	}

	public ConvertConfig getConfig() {
		return config;
	}

	public void setConfig(ConvertConfig config) {
		this.config = config;
	}

	public List<PictureStyle> getPicStyleList() {
		return picStyleList;
	}

	public void setPicStyleList(List<PictureStyle> picStyleList) {
		this.picStyleList = picStyleList;
	}

	public RowColumnSpan getRowColumnSpan() {
		return rowColumnSpan;
	}

	public void setRowColumnSpan(RowColumnSpan rowColumnSpan) {
		this.rowColumnSpan = rowColumnSpan;
	}

	public int getWidthRowNum() {
		return widthRowNum;
	}

	public void setWidthRowNum(int widthRowNum) {
		this.widthRowNum = widthRowNum;
	}

	public int getUsedPicIndex() {
		return usedPicIndex;
	}

	public void setUsedPicIndex(int usedPicIndex) {
		this.usedPicIndex = usedPicIndex;
	}
}
