package com.lim.demos.metadata.common.util;

import com.lim.demos.metadata.common.entity.TableColumnMetaData;
import com.lim.demos.metadata.common.entity.TableMetaData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.lim.demos.metadata.common.constants.DatasourceConstants.*;

/**
 * ExportUtil
 * <p>导出工具类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/8/16 下午3:33
 */
public class ExportUtil {

    /** 目标文件生成位置 */
    private static String getFilePath(String databaseName, String tableSchemaName, String fileSuffix) {
        return USER_DIR + FILE_SEPARATOR + PROJECT_NAME + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources" +  FILE_SEPARATOR + databaseName + "." + tableSchemaName + "." + fileSuffix;
    }

    /**
     * 方法：exportCsv
     * <p>将表的列信息导出为csv </p>
     *
     * @param databaseName 数据库名称
     * @param tableSchemaName 数据库名称
     * @param tableColumns 表的列信息
     * @since 2024/6/3 上午10:27
     * @author lim
     */
    public static void exportCsv(String databaseName, String tableSchemaName, Map<TableMetaData, List<TableColumnMetaData>> tableColumns) {
        final String targetFileName = getFilePath(databaseName, tableSchemaName, "xlsx");
        // 1. 创建工作簿
        Workbook file = new XSSFWorkbook();
        // 1.1 创建CreationHelper，用于超链接的创建
        CreationHelper creationHelper = file.getCreationHelper();
        // 1.2 创建document类型的超链接
        Hyperlink documentLink = null;
        Set<TableMetaData> tableSet = tableColumns.keySet();
        // 2. 创建目录sheet
        Sheet sheet = file.createSheet("数据表目录");
        // 创建行对象
        Row firstRow = sheet.createRow(0);
        // 创建单元格并设置值
        Cell firstRowFirstCell = firstRow.createCell(0);
        firstRowFirstCell.setCellValue("编号");
        Cell firstRowSecondCell = firstRow.createCell(1);
        firstRowSecondCell.setCellValue("表名");
        Cell firstRowThirdCell = firstRow.createCell(2);
        firstRowThirdCell.setCellValue("表描述");
        int catalogueIndex = 0;
        for (TableMetaData table : tableSet) {
            catalogueIndex += 1;
            Row row = sheet.createRow(catalogueIndex);
            Cell id = row.createCell(0);
            id.setCellValue(catalogueIndex);

            Cell tblName = row.createCell(1);
            tblName.setCellValue(table.getTableName());
            documentLink = creationHelper.createHyperlink(HyperlinkType.DOCUMENT);
            documentLink.setAddress("#'表" + catalogueIndex + "'!A1");
            row.getCell(1).setHyperlink(documentLink);

            Cell tblComment = row.createCell(2);
            tblComment.setCellValue(table.getTableComment());
        }
        // 遍历 tableNames
        int sheetIndex = 0;
        for (TableMetaData table : tableSet) {
            sheetIndex += 1;
            // 创建工作表
            sheet = file.createSheet("表" + sheetIndex);
            // 创建行对象
            firstRow = sheet.createRow(0);
            // 第一行固定为表名
            // 创建单元格并设置值
            firstRowFirstCell = firstRow.createCell(0);
            firstRowFirstCell.setCellValue(table.getTableName());
            firstRowSecondCell = firstRow.createCell(1);
            firstRowSecondCell.setCellValue(table.getTableComment());
            // 创建行对象
            Row secondRow = sheet.createRow(1);
            // 第二行固定
            // 第二行第一列
            Cell secondRowFirstCell = secondRow.createCell(0);
            secondRowFirstCell.setCellValue("列名");
            // 第二行第二列
            Cell secondRowSecondCell = secondRow.createCell(1);
            secondRowSecondCell.setCellValue("#");
            // 第二行第三列
            Cell secondRowThirdCell = secondRow.createCell(2);
            secondRowThirdCell.setCellValue("数据类型");
            // 第二行第四列
            Cell secondRowFourthCell = secondRow.createCell(3);
            secondRowFourthCell.setCellValue("长度");
            // 第二行第五列
            Cell secondRowFifthCell = secondRow.createCell(4);
            secondRowFifthCell.setCellValue("标度");
            // 第二行第六列
            Cell secondRowSixthCell = secondRow.createCell(5);
            secondRowSixthCell.setCellValue("非空");
            // 第二行第七列
            Cell secondRowSeventhCell = secondRow.createCell(6);
            secondRowSeventhCell.setCellValue("自动生成");
            // 第二行第八列
            Cell secondRowEighthCell = secondRow.createCell(7);
            secondRowEighthCell.setCellValue("自动递增");
            // 第二行第九列
            Cell secondRowNinthCell = secondRow.createCell(8);
            secondRowNinthCell.setCellValue("默认");
            // 第二行第十列
            Cell secondRowTenthCell = secondRow.createCell(9);
            secondRowTenthCell.setCellValue("描述");
            List<TableColumnMetaData> tableColumnMateDatas = tableColumns.get(table);

            // 填充数据
            TableColumnMetaData tableColumnMetaData;
            Row row;
            for (int i = 0; i < tableColumnMateDatas.size(); i++) {
                tableColumnMetaData = tableColumnMateDatas.get(i);
                row = sheet.createRow(2 + i);
                row.createCell(0).setCellValue(tableColumnMetaData.getColumnName());
                row.createCell(1).setCellValue(tableColumnMetaData.getIndex());
                row.createCell(2).setCellValue(tableColumnMetaData.getColumnType());
                if (Objects.nonNull(tableColumnMetaData.getColumnLength())) {
                    if ("varchar".equals(tableColumnMetaData.getColumnType()) && tableColumnMetaData.getColumnLength() == 0) {
                        row.createCell(3).setCellValue("");
                    } else {
                        row.createCell(3).setCellValue(tableColumnMetaData.getColumnLength());
                    }
                } else {
                    row.createCell(3).setCellValue("");
                }
                if (Objects.nonNull(tableColumnMetaData.getColumnScale()) && !tableColumnMetaData.getColumnScale().equals(0)) {
                    row.createCell(4).setCellValue(tableColumnMetaData.getColumnScale());
                } else {
                    row.createCell(4).setCellValue("[NULL]");
                }
                row.createCell(5).setCellValue(tableColumnMetaData.getColumnIsNotNull());
                row.createCell(6).setCellValue(tableColumnMetaData.getColumnAutoGenerate());
                row.createCell(7).setCellValue(tableColumnMetaData.getColumnAutoIncrement());
                row.createCell(8).setCellValue(StringUtils.isNotBlank(tableColumnMetaData.getColumnDefault()) ? tableColumnMetaData.getColumnDefault() : "[NULL]");
                row.createCell(9).setCellValue(tableColumnMetaData.getColumnDescribe());
            }
        }

        // 导出到文件
        try (FileOutputStream outputStream = new FileOutputStream(targetFileName)) {
            file.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 方法：exportWord
     * <p>将表的列信息导出为word文档 </p>
     *
     * @param databaseName 数据库名称
     * @param tableSchemaName 数据库名称
     * @param tableColumns 表的列信息
     * @since 2024/8/15 下午17:28
     * @author lim
     */
    public static void exportWord(String databaseName, String tableSchemaName, Map<TableMetaData, List<TableColumnMetaData>> tableColumns) {
        final String targetFileName = getFilePath(databaseName, tableSchemaName, "doc");
        // 转出为word文档
        XWPFDocument file = new XWPFDocument();

        Set<TableMetaData> tableNames = tableColumns.keySet();
        XWPFTable docTable = null;
        int tblIndex = 0;
        // 遍历 tableNames
        for (TableMetaData table : tableNames) {
            tblIndex += 1;

            XWPFParagraph tblTitle = file.createParagraph();
            XWPFRun tblTitleRun = tblTitle.createRun();
            // 加粗、字体大小、标题
            tblTitleRun.setText(tblIndex + " 表名称:" + table.getTableName() + (StringUtils.isNotBlank(table.getTableComment()) ? "（" + table.getTableComment() + "）" : ""));
            tblTitleRun.setBold(true);
            tblTitleRun.setFontSize(15);
            // 由于H1是段落样式，我们可以直接设置段落的样式且居左
            tblTitle.setAlignment(ParagraphAlignment.LEFT);
            tblTitle.setStyle("Heading1");

            // 创建一个10列的表格
            docTable = file.createTable(1 + tableColumns.get(table).size(), 10);
            XWPFTableCell cell0 = docTable.getRow(0).getCell(0);
            cell0.setColor("B0C4DE"); cell0.setText("编号");
            XWPFTableCell cell1 = docTable.getRow(0).getCell(1);
            cell1.setColor("B0C4DE"); cell1.setText("字段名称");
            XWPFTableCell cell2 = docTable.getRow(0).getCell(2);
            cell2.setColor("B0C4DE"); cell2.setText("数据类型");
            XWPFTableCell cell3 = docTable.getRow(0).getCell(3);
            cell3.setColor("B0C4DE"); cell3.setText("长度");
            XWPFTableCell cell4 = docTable.getRow(0).getCell(4);
            cell4.setColor("B0C4DE"); cell4.setText("标度");
            XWPFTableCell cell5 = docTable.getRow(0).getCell(5);
            cell5.setColor("B0C4DE"); cell5.setText("非空");
            XWPFTableCell cell6 = docTable.getRow(0).getCell(6);
            cell6.setColor("B0C4DE"); cell6.setText("自动生成");
            XWPFTableCell cell7 = docTable.getRow(0).getCell(7);
            cell7.setColor("B0C4DE"); cell7.setText("自动递增");
            XWPFTableCell cell8 = docTable.getRow(0).getCell(8);
            cell8.setColor("B0C4DE"); cell8.setText("默认");
            XWPFTableCell cell9 = docTable.getRow(0).getCell(9);
            cell9.setColor("B0C4DE"); cell9.setText("描述");
            for (int i = 0; i < tableColumns.get(table).size(); i++) {
                TableColumnMetaData tableColumnMetaData = tableColumns.get(table).get(i);
                docTable.getRow(i + 1).getCell(0).setText(String.valueOf(tableColumnMetaData.getIndex()));
                docTable.getRow(i + 1).getCell(1).setText(tableColumnMetaData.getColumnName());
                docTable.getRow(i + 1).getCell(2).setText(tableColumnMetaData.getColumnType());
                if (Objects.nonNull(tableColumnMetaData.getColumnLength())) {
                    if ("varchar".equals(tableColumnMetaData.getColumnType()) && tableColumnMetaData.getColumnLength() == 0) {
                        docTable.getRow(i + 1).getCell(3).setText("");
                    } else {
                        docTable.getRow(i + 1).getCell(3).setText(String.valueOf(tableColumnMetaData.getColumnLength()));
                    }
                } else {
                    docTable.getRow(i + 1).getCell(3).setText("");
                }
                if (Objects.nonNull(tableColumnMetaData.getColumnScale()) && !tableColumnMetaData.getColumnScale().equals(0)) {
                    docTable.getRow(i + 1).getCell(4).setText(String.valueOf(tableColumnMetaData.getColumnScale()));
                } else {
                    docTable.getRow(i + 1).getCell(4).setText("[NULL]");
                }
                docTable.getRow(i + 1).getCell(5).setText(tableColumnMetaData.getColumnIsNotNull() ? "是" : "否");
                docTable.getRow(i + 1).getCell(6).setText(tableColumnMetaData.getColumnAutoGenerate() ? "是" : "否");
                docTable.getRow(i + 1).getCell(7).setText(tableColumnMetaData.getColumnAutoIncrement() ? "是" : "否");
                docTable.getRow(i + 1).getCell(8).setText(StringUtils.isNotBlank(tableColumnMetaData.getColumnDefault()) ? tableColumnMetaData.getColumnDefault() : "[NULL]");
                docTable.getRow(i + 1).getCell(9).setText(tableColumnMetaData.getColumnDescribe());
            }

        }

        // 导出到文件
        try (FileOutputStream outputStream = new FileOutputStream(targetFileName)) {
            file.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
