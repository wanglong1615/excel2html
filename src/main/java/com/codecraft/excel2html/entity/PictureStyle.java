package com.codecraft.excel2html.entity;


/**
 * 图片样式(可排序)
 * @author zoro
 *
 */
public class PictureStyle implements Comparable<PictureStyle>{
	private int sheetIndex = 0;//sheet index 
	
	private String url;//图片url
	
	private int top;//上偏移量
	
	private int left;//左偏移量
	
	private double width;//宽度
	
	private double height;//高度
	
	public PictureStyle() {

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	@Override
	public int compareTo(PictureStyle obj) {
		if(obj.getSheetIndex() < this.getSheetIndex()){
			return 1;
		}else if(obj.getSheetIndex() > this.getSheetIndex()){
			return -1;
		}else{
			if(obj.getSheetIndex() < this.getSheetIndex()){
				return 1;
			}else if(obj.getSheetIndex() > this.getSheetIndex()){
				return -1;
			}else{
				if(obj.getTop() < this.getTop()){
					return 1;
				}else if(obj.getTop() > this.getTop()){
					return -1;
				}else{
					if(obj.getLeft() < this.getLeft()){
						return 1;
					}else if(obj.getLeft() > this.getLeft()){
						return -1;
					}else{
						return 0;
					}
				}
			}
		}
	}
}
