package org.example.gateway.xlsx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.gateway.ExportGateway;
import org.example.model.domain.TransactionsReportRow;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.JxlsException;
import org.jxls.common.Size;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.jxls.transform.poi.PoiTransformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsxExportGateway implements ExportGateway {
    private static final Logger LOGGER = LogManager.getLogger(XlsxExportGateway.class);
    private static final String XLSX_TEMPLATES_PATH = "/xlsx_templates/";
    private static final int EXCEL_COLUMN_WIDTH_UNIT = 256;
    private static final int MAX_EXCEL_COLUMN_WIDTH = 255 * EXCEL_COLUMN_WIDTH_UNIT;

    @Override
    public ByteArrayOutputStream exportTransactions(String sheetName, List<TransactionsReportRow> rows, double totalAmount) {
        String templateFileName = "transactions_template.xlsx";
        Map<String, Object> data = new HashMap<>();
        data.put("rows", rows);
        data.put("total", totalAmount);
        Map<String, Class<? extends Command>> commands = Map.of(AutoColumnWidthCommand.name, AutoColumnWidthCommand.class);
        byte[] bytes = fillTemplateWithData(data, templateFileName, commands);
        return writeWithJxls(bytes, sheetName, templateFileName);
    }

    private byte[] fillTemplateWithData(Map<String, Object> data, String templateFileName, Map<String, Class<? extends Command>> commands) {
        String templatePath = XLSX_TEMPLATES_PATH + templateFileName;
        try (InputStream in = getClass().getResourceAsStream(templatePath)) {
            LOGGER.debug("Started building [{}] export file", templatePath);

            JxlsPoiTemplateFillerBuilder builder = JxlsPoiTemplateFillerBuilder.newInstance()
                    .withTemplate(in);

            commands.forEach(builder::withCommand);

            return builder.buildAndFill(data);
        } catch (IOException | JxlsException e) {
            LOGGER.error("Failed to build [{}] export file", templatePath, e);
            throw new RuntimeException(e);
        }
    }

    private ByteArrayOutputStream writeWithJxls(byte[] fileData, String sheetName, String templateFileName) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileData);
             Workbook workbook = new XSSFWorkbook(bis);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.setSheetName(0, sheetName);
            workbook.write(outputStream);

            LOGGER.debug("Finished building [{}] export file", templateFileName);
            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class AutoColumnWidthCommand extends AbstractCommand {
        public static final String name = "autoColumnWidth";
        private Area area;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Size applyAt(CellRef cellRef, Context context) {
            Size size = this.area.applyAt(cellRef, context);

            if (size.equals(Size.ZERO_SIZE)) {
                return size;
            }

            PoiTransformer transformer = (PoiTransformer) area.getTransformer();
            Workbook workbook = transformer.getWorkbook();
            Sheet sheet = workbook.getSheet(cellRef.getSheetName());

            int numberOfColumns = size.getWidth();

            for (int i = 0; i < numberOfColumns; i++) {
                sheet.setColumnWidth(i, calculateAutoColumnFitWidth(sheet, i));
            }

            return size;
        }

        @Override
        public Command addArea(Area area) {
            super.addArea(area);
            this.area = area;
            return this;
        }

        // source: https://github.com/StudioChaos/PlantInventoryDatabase/blob/af4dd6fb151c20c60e256aa1dc2b2662186a15b3/org/apache/poi/xssf/usermodel/XSSFSheet.java#L500
        private int calculateAutoColumnFitWidth(Sheet sheet, int column) {
            double width = SheetUtil.getColumnWidth(sheet, column, true);
            if (width != -1.0) {
                width *= EXCEL_COLUMN_WIDTH_UNIT;
                if (width > MAX_EXCEL_COLUMN_WIDTH) {
                    width = MAX_EXCEL_COLUMN_WIDTH;
                }
            }
            return Math.toIntExact(Math.round(width >= MAX_EXCEL_COLUMN_WIDTH ? width : width + 1000));
        }
    }

    public static class MergeColumnCellsWithSameValuesCommand extends AbstractCommand {
        public static final String name = "mergeColumnCellsWithSameValues";
        private Area area;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Size applyAt(CellRef cellRef, Context context) {
            int columnIndex = (int) context.getVar("mergeColumnIndex");
            Size size = this.area.applyAt(cellRef, context);

            if (size.equals(Size.ZERO_SIZE)) {
                return size;
            }

            PoiTransformer transformer = (PoiTransformer) area.getTransformer();
            Workbook workbook = transformer.getWorkbook();
            Sheet sheet = workbook.getSheet(cellRef.getSheetName());
            mergeSameValueCells(sheet, cellRef.getRow(), columnIndex, size.getHeight());

            return size;
        }

        @Override
        public Command addArea(Area area) {
            super.addArea(area);
            this.area = area;
            return this;
        }

        private void mergeSameValueCells(Sheet sheet, int startRow, int column, int numRows) {
            int mergeStart = startRow;
            String previousValue = getCellValue(sheet, startRow, column);

            for (int i = startRow + 1; i < startRow + numRows; i++) {
                String currentValue = getCellValue(sheet, i, column);
                if (!currentValue.equals(previousValue)) {
                    if (i - mergeStart > 1)
                        sheet.addMergedRegion(new CellRangeAddress(mergeStart, i - 1, column, column));
                    mergeStart = i;
                    previousValue = currentValue;
                }
            }

            if (startRow + numRows - mergeStart > 1)
                sheet.addMergedRegion(new CellRangeAddress(mergeStart, startRow + numRows - 1, column, column));
        }

        private String getCellValue(Sheet sheet, int row, int column) {
            Row r = sheet.getRow(row);
            if (r == null) return "";
            Cell cell = r.getCell(column);
            if (cell == null) return "";
            return cell.toString().trim();
        }
    }
}
