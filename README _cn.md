# excel2html

zoro,欢迎来到开源世界!

Excel2Html顾名思义将Excel转换为html.(注意:部分代码参照于别人.)
目前仅支持Excel2003格式.

如果表格的样式不对,那么请注意以下两点:
1.hssf默认字体为10px宋体.
sheet.getColumnWidthInPixels : 
Please note, that this method works correctly only for workbooks with the default font size (Arial 10pt for .xls and Calibri 11pt for .xlsx).
2.excel中的内容不要充满整个单元格.(单元格内容过大会将table撑大)

 	 //测试代码
  	public static void main(String[] args) throws Exception{
			File file = new File("C:/Temp/1.xls");
			ConvertConfig config = new ConvertConfig();
			config.setHtmlPrint(new FilePrint("test"));
			config.setMaxRowNum(500).setMaxCellNum(500).setExcelType("HSSF");
			Excel2Html excel2Html = new Excel2Html(config);
			FileInputStream fis = new FileInputStream(file);
			excel2Html.conver(fis, 0);
			fis.close();
  	} 
  

扩展:支持input textarea date checkbox calc控件,并且可以对数据持久化.