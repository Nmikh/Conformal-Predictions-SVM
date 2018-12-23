package com;

import com.models.Prediction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

/**
 * Created by Admin on 22.12.2018.
 */
public class SettingsExcel {

    public XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        return style;
    }

    public XSSFCellStyle createStyleForCellStandart(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        //DataFormat formatDouble = workbook.createDataFormat();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //style.setDataFormat(formatDouble.getFormat("#0.00"));
        return style;
    }

    public XSSFCellStyle createStyleForCellDouble(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        DataFormat formatDouble = workbook.createDataFormat();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setDataFormat(formatDouble.getFormat("#0.00"));
        return style;
    }

    public XSSFCellStyle createStyleForCellPredict(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    public void createCellForTitle(XSSFSheet sheet, ArrayList<String> stringsName){
        XSSFWorkbook workbook = sheet.getWorkbook();
        XSSFCellStyle style = createStyleForTitle(workbook);
        Cell cell;
        Row row = sheet.createRow(0);
        for(int i = 0; i<stringsName.size(); i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(stringsName.get(i));
            cell.setCellStyle(style);
        }
    }

    public void createCellStandart(XSSFSheet sheet, ArrayList<Prediction> conformalPrediction){
        XSSFWorkbook workbook = sheet.getWorkbook();
        XSSFCellStyle style = createStyleForCellStandart(workbook);
        Cell cell;
        Row row;
        for(int i = 0; i<conformalPrediction.size(); i++){
            row = sheet.createRow(i+1);
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getId());
            cell.setCellStyle(style);

            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getpPositive());
            cell.setCellStyle(style);

            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getpNegative());
            cell.setCellStyle(style);

            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getRealClass());
            cell.setCellStyle(style);

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getPredictClass());
            cell.setCellStyle(style);

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getPredictClassSVM());
            cell.setCellStyle(style);

            if (conformalPrediction.get(i).getPredictClassSVM() == 1) {
                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue(conformalPrediction.get(i).getConfidencePositiveClassSVM() * 100);
                cell.setCellStyle(style);
            } else {
                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue(conformalPrediction.get(i).getConfidenceNegativeClassSVM() * 100);
                cell.setCellStyle(style);
            }

            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getConfidence());
            cell.setCellStyle(style);

            cell = row.createCell(8, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getCredibility() * 100);
            cell.setCellStyle(style);

            cell = row.createCell(9, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getAlphaPositive());
            cell.setCellStyle(style);

            cell = row.createCell(10, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getAlphaNegative());
            cell.setCellStyle(style);

            int countPositiveOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getPositiveSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getPositiveSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaPositive())
                    countPositiveOne++;
            }

            cell = row.createCell(11, CellType.NUMERIC);
            cell.setCellValue(countPositiveOne);
            cell.setCellStyle(style);

            int countNegativeOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getNegativeSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getNegativeSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaNegative())
                    countNegativeOne++;
            }

            cell = row.createCell(12, CellType.NUMERIC);
            cell.setCellValue(countNegativeOne);
            cell.setCellStyle(style);

            cell = row.createCell(13, CellType.STRING);
            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getDataObject()));
            cell.setCellStyle(style);

            cell = row.createCell(14, CellType.STRING);
            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getPositiveSupportVectorsAlphas()));
            cell.setCellStyle(style);

            cell = row.createCell(15, CellType.STRING);
            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getNegativeSupportVectorsAlphas()));
            cell.setCellStyle(style);
        }
    }

    public void createCellDouble(XSSFSheet sheet, ArrayList<Prediction> conformalPrediction){
        XSSFWorkbook workbook = sheet.getWorkbook();
        XSSFCellStyle styleStandart = createStyleForCellStandart(workbook);
        XSSFCellStyle styleDouble = createStyleForCellDouble(workbook);

        XSSFCellStyle styleAlphas = workbook.createCellStyle();
        styleAlphas.setBorderBottom(BorderStyle.THIN);
        styleAlphas.setBorderLeft(BorderStyle.THIN);
        styleAlphas.setBorderRight(BorderStyle.THIN);
        styleAlphas.setBorderTop(BorderStyle.THIN);
        styleAlphas.setWrapText(true);
        sheet.setColumnWidth(0, 10*256);
        sheet.setColumnWidth(13, 35*256);
        sheet.setColumnWidth(14, 200*256);
        sheet.setColumnWidth(15, 200*256);

        int range100TO95 = 0;
        int range95TO90 = 0;
        int range90TO80 = 0;

        Cell cell;
        Row row;
        for(int i = 0; i<conformalPrediction.size(); i++){
            row = sheet.createRow(i+1);
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getId());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getpPositive());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getpNegative());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getRealClass());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getPredictClass());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getPredictClassSVM());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(6, CellType.NUMERIC);
            if (conformalPrediction.get(i).getPredictClassSVM() == 1) {
                cell.setCellValue(conformalPrediction.get(i).getConfidencePositiveClassSVM() * 100);
            } else {
                cell.setCellValue(conformalPrediction.get(i).getConfidenceNegativeClassSVM() * 100);
            }
            cell.setCellStyle(styleDouble);

            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getConfidence()*100);
            cell.setCellStyle(styleDouble);

            cell = row.createCell(8, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getCredibility() * 100);
            cell.setCellStyle(styleDouble);

            cell = row.createCell(9, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getAlphaPositive());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(10, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getAlphaNegative());
            cell.setCellStyle(styleStandart);

            int countPositiveOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getPositiveSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getPositiveSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaPositive())
                    countPositiveOne++;
            }

            cell = row.createCell(11, CellType.NUMERIC);
            cell.setCellValue(countPositiveOne);
            cell.setCellStyle(styleStandart);

            int countNegativeOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getNegativeSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getNegativeSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaNegative())
                    countNegativeOne++;
            }

            cell = row.createCell(12, CellType.NUMERIC);
            cell.setCellValue(countNegativeOne);
            cell.setCellStyle(styleStandart);

            cell = row.createCell(13, CellType.STRING);
            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getDataObject()));
            cell.setCellStyle(styleStandart);

//            cell = row.createCell(14, CellType.STRING);
//            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getPositiveSupportVectorsAlphas()));
//            cell.setCellStyle(styleAlphas);
//
//
//            cell = row.createCell(15, CellType.STRING);
//            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getNegativeSupportVectorsAlphas()));
//            cell.setCellStyle(styleAlphas);

            if(conformalPrediction.get(i).getConfidence()>=0.95){
                range100TO95++;
            } else if(conformalPrediction.get(i).getConfidence()>=0.90
                    && conformalPrediction.get(i).getConfidence()<0.95){
                range95TO90++;
            } else if(conformalPrediction.get(i).getConfidence()>=0.80
                    && conformalPrediction.get(i).getConfidence()<0.90){
                range90TO80++;
            }
        }
        row = sheet.createRow(conformalPrediction.size()+2);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Confidence range:");
        cell.setCellStyle(styleAlphas);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Count");
        cell.setCellStyle(styleStandart);

        row = sheet.createRow(conformalPrediction.size()+3);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("95-100");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(range100TO95);
        cell.setCellStyle(styleStandart);

        row = sheet.createRow(conformalPrediction.size()+4);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("90-95");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(range95TO90);
        cell.setCellStyle(styleStandart);

        row = sheet.createRow(conformalPrediction.size()+5);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("80-90");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(range90TO80);
        cell.setCellStyle(styleStandart);

    }

    public void createCellTestColor(XSSFSheet sheet, ArrayList<Prediction> conformalPrediction){
        XSSFWorkbook workbook = sheet.getWorkbook();
        XSSFCellStyle styleForCellStandart = createStyleForCellStandart(workbook);
        XSSFCellStyle styleForCellDouble = createStyleForCellDouble(workbook);
        XSSFCellStyle styleForCellPredict = createStyleForCellPredict(workbook);


        Cell cell;
        Row row;

        for(int i = 0; i<conformalPrediction.size(); i++){
            row = sheet.createRow(i+1);
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getId());
            cell.setCellStyle(styleForCellStandart);

            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getpPositive());
            cell.setCellStyle(styleForCellStandart);

            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getpNegative());
            cell.setCellStyle(styleForCellStandart);

            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getRealClass());
            cell.setCellStyle(styleForCellStandart);

            if(conformalPrediction.get(i).getPredictClass()!=conformalPrediction.get(i).getRealClass()){
                cell = row.createCell(4, CellType.NUMERIC);
                cell.setCellValue(conformalPrediction.get(i).getPredictClass());
                cell.setCellStyle(styleForCellPredict);
            } else {
                cell = row.createCell(4, CellType.NUMERIC);
                cell.setCellValue(conformalPrediction.get(i).getPredictClass());
                cell.setCellStyle(styleForCellStandart);
            }

            if(conformalPrediction.get(i).getPredictClassSVM()!=conformalPrediction.get(i).getRealClass()) {
                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue(conformalPrediction.get(i).getPredictClassSVM());
                cell.setCellStyle(styleForCellPredict);
            } else {
                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue(conformalPrediction.get(i).getPredictClassSVM());
                cell.setCellStyle(styleForCellStandart);
            }



            cell = row.createCell(6, CellType.NUMERIC);
            if (conformalPrediction.get(i).getPredictClassSVM() == 1) {
                cell.setCellValue(conformalPrediction.get(i).getConfidencePositiveClassSVM() * 100);
            } else {
                cell.setCellValue(conformalPrediction.get(i).getConfidenceNegativeClassSVM() * 100);
            }
            cell.setCellStyle(styleForCellDouble);

            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getConfidence()*100);
            cell.setCellStyle(styleForCellDouble);

            cell = row.createCell(8, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getCredibility() * 100);
            cell.setCellStyle(styleForCellDouble);

            cell = row.createCell(9, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getAlphaPositive());
            cell.setCellStyle(styleForCellDouble);

            cell = row.createCell(10, CellType.NUMERIC);
            cell.setCellValue(conformalPrediction.get(i).getAlphaNegative());
            cell.setCellStyle(styleForCellDouble);

            int countPositiveOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getPositiveSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getPositiveSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaPositive())
                    countPositiveOne++;
            }

            cell = row.createCell(11, CellType.NUMERIC);
            cell.setCellValue(countPositiveOne);
            cell.setCellStyle(styleForCellStandart);

            int countNegativeOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getNegativeSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getNegativeSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaNegative())
                    countNegativeOne++;
            }

            cell = row.createCell(12, CellType.NUMERIC);
            cell.setCellValue(countNegativeOne);
            cell.setCellStyle(styleForCellStandart);

            cell = row.createCell(13, CellType.STRING);
            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getDataObject()));
            cell.setCellStyle(styleForCellStandart);

            cell = row.createCell(14, CellType.STRING);
            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getPositiveSupportVectorsAlphas()));
            cell.setCellStyle(styleForCellStandart);

            cell = row.createCell(15, CellType.STRING);
            cell.setCellValue(String.valueOf(conformalPrediction.get(i).getNegativeSupportVectorsAlphas()));
            cell.setCellStyle(styleForCellStandart);
        }
    }


}
