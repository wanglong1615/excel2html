package com.codecraft.excel2html.entity;

import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * excel2003 picture
 * @author zoro
 *
 */
public class ExcelPicture {
	private String url;//图片url
	private final HSSFWorkbook workbook;
	private final HSSFSheet sheet;
	private final HSSFPictureData pictureData;
	private final EscherClientAnchorRecord clientAnchor;
	private int sheetIndex;//所属sheet index
	private PictureStyle picStyleBo;//图片样式

	public ExcelPicture(HSSFWorkbook workbook, HSSFSheet sheet,
			HSSFPictureData pictureData, EscherClientAnchorRecord clientAnchor,int sheetIndex) {
		this.workbook = workbook;
		this.sheet = sheet;
		this.pictureData = pictureData;
		this.clientAnchor = clientAnchor;
		this.sheetIndex = sheetIndex;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public EscherClientAnchorRecord getClientAnchor() {
		return clientAnchor;
	}

	public HSSFPictureData getPictureData() {
		return pictureData;
	}

	public byte[] getData() {
		return pictureData.getData();
	}

	public String suggestFileExtension() {
		return pictureData.suggestFileExtension();
	}
	
	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	/**
	 * 推测图片中心所覆盖的单元格，这个值不一定准确，但通常有效
	 * 
	 * @return the row0
	 */
	public short getRow0() {
		int row1 = getRow1();
		int row2 = getRow2();
		if (row1 == row2) {
			return (short) row1;
		}

		int heights[] = new int[row2 - row1 + 1];
		for (int i = 0; i < heights.length; i++) {
			heights[i] = getRowHeight(row1 + i);
		}

		// HSSFClientAnchor 中 dx 只能在 0-1023 之间,dy 只能在 0-255 之间
		// 表示相对位置的比率，不是绝对值
		int dy1 = getDy1() * heights[0] / 255;
		int dy2 = getDy2() * heights[heights.length - 1] / 255;
		return (short) (getCenter(heights, dy1, dy2) + row1);
	}

	private short getRowHeight(int rowIndex) {
		HSSFRow row = sheet.getRow(rowIndex);
		short h = row == null ? sheet.getDefaultRowHeight() : row.getHeight();
		return h;
	}
	
	/**
	 * 获得单元格高度
	 */
	public double getHeight(int startIndex, int endIndex){
		double height = 0;
		if(startIndex > endIndex){
			return height;
		}
		for(int i = startIndex; i <= endIndex ; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			double h =  row.getHeightInPoints()* 1.35;
			height += h;
		}
		return height;
	}
	
	/**
	 * 获得单元格宽度
	 */
	public double getWidth(int startIndex, int endIndex){
		double width = 0;
		if(startIndex > endIndex){
			return width;
		}
		//HSSFRow firstRow = sheet.getRow(0);
		//这里取0有问题，所以取1
		HSSFRow firstRow = sheet.getRow(1);
		for(int i = startIndex; i <= endIndex ; i++){
			HSSFCell cell = firstRow.getCell(i);
			if(cell == null){
				continue;
			}
			double h = sheet.getColumnWidthInPixels(i); //像素
			width += h;
		}
		return width;
	}
	
	/**
	 * 获得顶端偏移量
	 */
	public int getTop(short startIndex) {
		int top = 0;
		if(startIndex == 0){
			return top;
		}
		for(int i = 0; i < startIndex ; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			double h =  row.getHeightInPoints();
			top += h;
		}
		double sum = top * 1.35;
		return (int)sum;
	}

	/**
	 * 获得左边偏移量
	 */
	public int getLeft(short startIndex) {
		int left = 0;
		if(startIndex == 0){
			return left;
		}
		//HSSFRow firstRow = sheet.getRow(0);
		//这里取0有问题，所以取1
		HSSFRow firstRow = sheet.getRow(1);
		for(int i = 0; i < startIndex ; i++){
			HSSFCell cell = firstRow.getCell(i);
			if(cell == null){
				continue;
			}
			double h = sheet.getColumnWidth(i) / 32 *0.89;
			left += h;
		}
		return left;
	}

	/**
	 * 推测图片中心所覆盖的单元格，这个值不一定准确，但通常有效
	 * 
	 * @return the col0
	 */
	@SuppressWarnings("deprecation")
	public short getCol0() {
		short col1 = getCol1();
		short col2 = getCol2();

		if (col1 == col2) {
			return col1;
		}

		int widths[] = new int[col2 - col1 + 1];
		for (int i = 0; i < widths.length; i++) {
			widths[i] = sheet.getColumnWidth(col1);
		}
		// HSSFClientAnchor 中 dx 只能在 0-1023 之间,dy 只能在 0-255 之间
		// 表示相对位置的比率，不是绝对值
		int dx1 = getDx1() * widths[0] / 1023;
		int dx2 = getDx2() * widths[widths.length - 1] / 1023;

		return (short) (getCenter(widths, dx1, dx2) + col1);
	}

	/**
	 * 给定各线段的长度，以及起点相对于起点段的偏移量，终点相对于终点段的偏移量， 求中心点所在的线段
	 * 
	 * @param a
	 *            the a 各线段的长度
	 * @param d1
	 *            the d1 起点相对于起点段
	 * @param d2
	 *            the d2 终点相对于终点段的偏移量
	 * 
	 * @return the center
	 */
	protected static int getCenter(int[] a, int d1, int d2) {
		// 线段长度
		int width = a[0] - d1 + d2;
		for (int i = 1; i < a.length - 1; i++) {
			width += a[i];
		}

		// 中心点位置
		int c = width / 2 + d1;
		int x = a[0];
		int cno = 0;

		while (c > x) {
			x += a[cno];
			cno++;
		}

		return cno;
	}

	/**
	 * 左上角所在列
	 * 
	 * @return the col1
	 */
	public short getCol1() {
		return clientAnchor.getCol1();
	}

	/**
	 * 右下角所在的列
	 * 
	 * @return the col2
	 */
	public short getCol2() {
		return clientAnchor.getCol2();
	}

	/**
	 * 左上角的相对偏移量
	 * 
	 * @return the dx1
	 */
	public short getDx1() {
		return clientAnchor.getDx1();
	}

	/**
	 * 右下角的相对偏移量
	 * 
	 * @return the dx2
	 */
	public short getDx2() {
		return clientAnchor.getDx2();
	}

	/**
	 * 左上角的相对偏移量
	 * 
	 * @return the dy1
	 */
	public short getDy1() {
		return clientAnchor.getDy1();
	}

	/**
	 * 右下角的相对偏移量
	 * 
	 * @return the dy2
	 */
	public short getDy2() {
		return clientAnchor.getDy2();
	}

	/**
	 * 左上角所在的行
	 * 
	 * @return the row1
	 */
	public short getRow1() {
		return clientAnchor.getRow1();
	}

	/**
	 * 右下角所在的行
	 * 
	 * @return the row2
	 */
	public short getRow2() {
		return clientAnchor.getRow2();
	}

	public PictureStyle getPicStyleBo() {
		return picStyleBo;
	}

	public void setPicStyleBo(PictureStyle picStyleBo) {
		this.picStyleBo = picStyleBo;
	}
}
