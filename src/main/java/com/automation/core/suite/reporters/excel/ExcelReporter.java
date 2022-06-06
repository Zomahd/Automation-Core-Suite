package com.automation.core.suite.reporters.excel;

import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;
import com.automation.core.suite.configuration.BrowserFactory;
import com.automation.core.suite.models.Browser;
import com.automation.core.suite.utils.AutomationCoreReportUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExcelReporter implements IReporter {

    protected int totalTestCases = 0;
    protected XSSFCellStyle numberCellStyle;
    protected XSSFCellStyle dateCellStyle;

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        String templatePath = AutomationCoreConfigWrapper.INSTANCE.get("excel.report.template", "/reports/AutomationCoreReportTemplate.xlsx");

        try {
            FileOutputStream output =
                    new FileOutputStream(outputDirectory + "TestReport.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(ExcelReporter.class.getResourceAsStream(templatePath));
            XSSFSheet dashboardSheet = workbook.getSheetAt(0);
            XSSFSheet resultsSheet = workbook.getSheet("Results");

            dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("m/d/yy h:mm"));

            numberCellStyle = workbook.createCellStyle();
            numberCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.00"));

            if (resultsSheet != null) {
                processResultsSheet(resultsSheet, suites);
            }

            if (dashboardSheet != null) {
                processDashboardSheet(dashboardSheet);
            }

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                workbook.getSheetAt(i).setForceFormulaRecalculation(true);
            }

            workbook.write(output);
        } catch (IOException ioe) {
            System.err.println("Could not read Excel report template (" + templatePath + ")");
        }
    }

    protected void processDashboardSheet(XSSFSheet sheet) {
        Cell titleCell = findCellByText(sheet, "{TITLE}");
        Cell dateCell = findCellByText(sheet, "{DATE}");
        Cell browserCell = findCellByText(sheet, "{BROWSERS}");
        Cell totalCell = findCellByText(sheet, "{TOTAL}");

        if (titleCell != null) {
            titleCell.setCellValue(AutomationCoreConfigWrapper.INSTANCE.get("excel.report.title", "AutomationCore Suite Report"));
        }

        if (dateCell != null) {
            dateCell.setCellValue(new Date());
            dateCell.setCellStyle(dateCellStyle);
        }

        if (browserCell != null) {
            for (Browser browser : BrowserFactory.getBrowsers()) {
                browserCell.setCellValue(browser.toString());
                browserCell = nextRow(browserCell);
            }
        }

        if (totalCell != null) {
            totalCell.setCellValue(totalTestCases);
        }
    }


    protected void processResultsSheet(XSSFSheet sheet, List<ISuite> suites) {
        int rowIndex = 2;

        for (ISuite suite : suites) {
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                ITestContext context = suiteResult.getTestContext();

                for (ITestResult result : context.getFailedTests().getAllResults()) {
                    processResultsSheetRow(sheet, suite, result, "FAILED", rowIndex++);
                }

                for (ITestResult result : context.getSkippedTests().getAllResults()) {
                    processResultsSheetRow(sheet, suite, result, "SKIPPED", rowIndex++);
                }

                for (ITestResult result : context.getPassedTests().getAllResults()) {
                    processResultsSheetRow(sheet, suite, result, "PASSED", rowIndex++);
                }
            }
        }

        if (rowIndex > 2) {
            CellCopyPolicy copyPolicy = new CellCopyPolicy();
            copyPolicy.setCopyCellValue(true);
            sheet.copyRows(2, rowIndex, 1, copyPolicy);
        }

        deleteRow(sheet, rowIndex);
        deleteRow(sheet, rowIndex - 1);

        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 8));
    }

    protected void processResultsSheetRow(XSSFSheet sheet, ISuite suite, ITestResult result, String status, int rowIndex) {
        sheet.shiftRows(rowIndex, rowIndex, 1);
        XSSFRow row = sheet.getRow(rowIndex);

        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        setCellValue(row, 0, suite.getName());
        setCellValue(row, 1, AutomationCoreReportUtils.getClassTitle(result));
        setCellValue(row, 2, AutomationCoreReportUtils.getMethodTitle(result, false, true));
        setCellValue(row, 3, AutomationCoreReportUtils.getMethodParametersAsString(result, true));
        setCellValue(row, 4, AutomationCoreReportUtils.getBrowser(result));
        setCellValue(row, 5, status);
        setCellValue(row, 6, ((double) (result.getEndMillis() - result.getStartMillis())) / 1000);
        setCellStyle(row, 6, numberCellStyle);
        setCellValue(row, 7, new Date(result.getStartMillis()));
        setCellStyle(row, 7, dateCellStyle);
        setCellValue(row, 8, new Date(result.getEndMillis()));
        setCellStyle(row, 8, dateCellStyle);

        totalTestCases++;
    }

    protected void setCellValue(Row row, int column, String value) {
        getOrCreateCell(row, column).setCellValue(value);
    }

    protected void setCellValue(Row row, int column, double value) {
        getOrCreateCell(row, column).setCellValue(value);
    }

    protected void setCellValue(Row row, int column, Date value) {
        getOrCreateCell(row, column).setCellValue(value);
    }

    protected void setCellStyle(Row row, int column, CellStyle cellStyle) {
        getOrCreateCell(row, column).setCellStyle(cellStyle);
    }

    protected Cell getOrCreateCell(Row row, int column) {
        Cell cell = row.getCell(column);

        if (cell == null) {
            cell = row.createCell(column);
        }

        return cell;
    }

    protected Cell nextRow(Cell cell) {
        int rowIndex = cell.getRowIndex() + 1;
        Row row = cell.getSheet().getRow(rowIndex);

        if (row == null) {
            row = cell.getSheet().createRow(rowIndex);
        }

        return getOrCreateCell(row, cell.getColumnIndex());
    }

    protected void deleteRow(XSSFSheet sheet, int rowNumber) {
        XSSFRow row = sheet.getRow(rowNumber);
        if (row != null) {
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i) != null) {
                    row.removeCell(row.getCell(i));
                }
            }
            sheet.removeRow(row);
        }
    }

    protected Cell findCellByText(XSSFSheet sheet, String text) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(text)) {
                        return cell;
                    }
                }
            }
        }

        return null;
    }
}
