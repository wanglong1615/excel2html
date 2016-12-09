# excel2html

zoro,欢迎来到开源世界!

Excel2Html顾名思义 将Excel转换为html.

注意:部分代码参照于别人.

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
  
