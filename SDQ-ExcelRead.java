package com.kyun.project_poi;


import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;


public class ExcelReadTest 
{
	public static void main(String[] args) throws Exception {
		File folder = new File("C:\\Users\\kimsm\\Desktop\\test");
		//결과보고서 양식 고정
		int rowNo = 7;
		int DBIndex = 2;
		int versionIndex = 5;

		for (File file : folder.listFiles()) {
			FileInputStream xlsxFile = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
			//두 번째 시트
			XSSFSheet sheet = workbook.getSheetAt(1);

			XSSFRow row = sheet.getRow(rowNo);
			XSSFCell DB = row.getCell(DBIndex);
			XSSFCell version = row.getCell(versionIndex);

			String DBvalue = "";
			if(DB==null) {

			}else{
				switch(DB.getCellType()) {
					case XSSFCell.CELL_TYPE_FORMULA:
					DBvalue = DB.getCellFormula();
					break;

					case XSSFCell.CELL_TYPE_NUMERIC:
					DBvalue = DB.getNumericCellValue() + "";
					break;

					case XSSFCell.CELL_TYPE_STRING:
					DBvalue = DB.getStringCellValue() + "";
					break;

					case XSSFCell.CELL_TYPE_BLANK:
					DBvalue = DB.getBooleanCellValue() + "";
					break;

					case XSSFCell.CELL_TYPE_ERROR:
					DBvalue = DB.getErrorCellValue() + "";
					break;
				}
			}
			String versionvalue = "";
			if(version==null) {

			}else{
				switch(version.getCellType()) {
					case XSSFCell.CELL_TYPE_FORMULA:
					versionvalue = version.getCellFormula();
					break;

					case XSSFCell.CELL_TYPE_NUMERIC:
					versionvalue = version.getNumericCellValue() + "";
					break;

					case XSSFCell.CELL_TYPE_STRING:
					versionvalue = version.getStringCellValue() + "";
					break;

					case XSSFCell.CELL_TYPE_BLANK:
					versionvalue = version.getBooleanCellValue() + "";
					break;

					case XSSFCell.CELL_TYPE_ERROR:
					versionvalue = version.getErrorCellValue() + "";
					break;
				}
			}
			System.out.println("DBMS : "+DBvalue);
			System.out.println("version : "+versionvalue);
		}
	}
}
