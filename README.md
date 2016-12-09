# excel2html

zoro,welcome to Open Source World!

convert excel to html.

Part of the code reference person code.

  //main test1
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
   } 
  
