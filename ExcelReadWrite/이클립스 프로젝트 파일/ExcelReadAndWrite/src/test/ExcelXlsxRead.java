package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelXlsxRead {
	
	public static void main(String[] args) {
		File file = new File("D:\\park\\SCMINNO_PARK\\ExcelReadWrite\\테스트문서\\테스트문서.xlsx");
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			Workbook workBook = new XSSFWorkbook(is);
			NumberFormat numberFormat = NumberFormat.getInstance();
			// DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<String> colNames = new ArrayList<String>();
			
			int sheetCnt = workBook.getNumberOfSheets(); // 시트의 갯수를 가져옵니다.
			System.out.println("엑셀파일의 시트 갯수 : " + sheetCnt);
			
			if(sheetCnt > 0) {
				for(int i = 0; i < sheetCnt; i++) { // 시트의 갯수만큼 반복 시켜줍니다.
					Sheet sheet = workBook.getSheetAt(i); // i번째에 해당하는 시트를 가져옵니다.
					System.out.println(i + " 번째 시트");
					
					if(sheet != null) {
						int rowCnt = sheet.getPhysicalNumberOfRows(); // 현재 시트의 줄의 수를 가져옵니다.
						
						if(rowCnt > 0) {
							for(int j = 0; j < rowCnt; j++) {
								Row row = sheet.getRow(j); // j번째에 해당하는 row를 가져옵니다.
								System.out.println(j + " 번째 줄");
								
								if(row != null) {
									int cellCnt = row.getPhysicalNumberOfCells(); // 현재 시트 현재 줄의 셀 갯수를 가져옵니다.
									
									if(cellCnt > 0) {
										for(int k = 0; k < cellCnt; k++) { // 셀의 갯수 만큼 반복합니다.
											Cell cell = row.getCell(k); // k번째에 해당하는 cell을 가져옵니다.
											// System.out.println(k + " 번째 셀");
											
											if(cell != null) {
												int cellType = cell.getCellType(); // Enum class로 된 셀의 Type 즉, 서식을 가져옵니다.
												
												String value = "";
												
												switch(cellType) {
													case XSSFCell.CELL_TYPE_STRING: // 문자 서식 이 있는 cell
														value = cell.getStringCellValue();
														break;
													case XSSFCell.CELL_TYPE_NUMERIC: // 숫자 서식 이 있는 cell
														// 날짜 형식의 셀도 숫자 형식의 type으로 들어옵니다.
														// 날짜 형식을 판단하여 날짜 형식으로 value를 바꿔 줍니다.
														if(HSSFDateUtil.isCellDateFormatted(cell)) value = sf.format(cell.getDateCellValue());
														else value = numberFormat.format(cell.getNumericCellValue());
														break;
													case XSSFCell.CELL_TYPE_FORMULA: // 수식이 있는 cell
														// sum 이라는 필드는 나이 + 재산을 더하는 수식이 있는 셀로 숫자 형태로 바꾸어 줄 수 있도록 합니다.
														if(colNames.get(k).equals("SUM")) value = numberFormat.format(cell.getNumericCellValue());
														else value = cell.getCellFormula(); // ex) A1 + B1 의 수식이 나옴
														break;
													case XSSFCell.CELL_TYPE_BOOLEAN: // boolean
														value = cell.getBooleanCellValue() + "";
														break;
													case XSSFCell.CELL_TYPE_BLANK: // 공백
														value = cell.getStringCellValue();
														break;
													case XSSFCell.CELL_TYPE_ERROR: // 에러
														value = cell.getStringCellValue();
														break;
												}
												if(j == 0) colNames.add(value); // colName을 담고 있는 row
													
												System.out.print(value);
												System.out.print(" ");
											}
										}
										System.out.println("");
									}
								}
								// if(j == 0) System.out.println("컬럼명 : " + colNames);
							}
							System.out.println(" ");
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(is != null)
				try {
					is.close(); // 인풋 스트림을 닫아 줄수 있도록 합니다.
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
