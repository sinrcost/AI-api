package httpapi.utils.commons;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtil {

    public static Object[][] readExcel(int startRow,int endRow,int cellNum){
        Object[][] datasTest = new Object[endRow-startRow+1][1];
        try {
            Workbook workbook = WorkbookFactory.create(new File("src/test/resources/InterfaceTestCase.xlsx"));
            Sheet sheet = workbook.getSheetAt(0);
            for(int i=startRow;i<=endRow;i++){
                Row row = sheet.getRow(i - 1);
                Cell cell = row.getCell(cellNum - 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String cellValue = cell.getStringCellValue();
                datasTest[i-startRow][0] = cellValue;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasTest;
    }

    public static void writeExcel(int rowNum,int cellNum,String data){
        String path = "src/test/resources/InterfaceTestCase.xlsx";
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(path));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(rowNum-1);
            row.createCell(cellNum-1).setCellValue(data);
            FileOutputStream os = new FileOutputStream(path);
            workbook.write(os);//一定要写这句代码，否则无法将数据写入excel文档中
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
    public static Object[][] read(int startRow,int endRow,int startCell,int endCell) {
        Object[][] datas = new Object[endRow-startRow+1][endCell-startCell+1];
        try {
            //从工厂里拿workbook对象
            Workbook workbook = WorkbookFactory.create(new File("src/test/resources/rest_info_v2.xlsx"));
            //获取表单sheet
            Sheet sheet = workbook.getSheetAt(0);
            //获取row cell
            for (int i=startRow;i<=endRow;i++){
                Row row = sheet.getRow(i - 1);
                for(int j=startCell;j<=endCell;j++){
                    Cell cell = row.getCell(j - 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    String cellValue = cell.getStringCellValue();
                    datas[i-startRow][j-startCell] = cellValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }
*/
}
