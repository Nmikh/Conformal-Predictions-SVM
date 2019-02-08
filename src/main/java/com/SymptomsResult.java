package com;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.models.Prediction;
import com.services.SymptomsService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parser.Parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Admin on 22.12.2018.
 */
public class SymptomsResult {


    public void printFullResultInOneTxt(ArrayList<Prediction> conformalPrediction, String outputFileName) throws IOException {
        int percent = 0;
        int amountOfTruePrediction = 0;
        int amountSVMandPrediction = 0;

        String fileName = outputFileName + ".txt";
        NumberFormat formatterCC = new DecimalFormat("#0.00");
        NumberFormat formatterID = new DecimalFormat("#000");

        File result = new File(fileName);
        result.delete();
        result = new File(fileName);


        FileWriter writer = new FileWriter(result, true);
        writer.write("|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");
        writer.write("|  ID |            p1          |          p-1          | True class | Prediction | Prediction SVM | Confidence SMV(%) | Confidence(%) | Credibility(%) |   Alpha Positive  |   Alpha Negative  | CP | CN |                 Object                |                                                                                                                                    Alphas for positive                                                                                                                                                                                                                                   |                                                                                                                                    Alphas for Negative                                                                                                                                      |\r\n");
//        writer.write("|  ID |            p1          |          p-1          | True class | Prediction | Prediction SVM | Confidence SMV(%) | Confidence(%) | Credibility(%) |   Alpha Positive  |   Alpha Negative  |                 Object                |\r\n");
        for (int i = 0; i < conformalPrediction.size(); i++) {
//            if (conformalPrediction.get(i).getCredibility() < 1) {

            writer.write("| " + formatterID.format(conformalPrediction.get(i).getId()) + " |");

            if (conformalPrediction.get(i).getpPositive() == 1) {
                writer.write("            " + conformalPrediction.get(i).getpPositive() + "         |");
            } else {
                writer.write("   " + conformalPrediction.get(i).getpPositive() + "  |");
            }
            if (conformalPrediction.get(i).getpNegative() == 1) {
                writer.write("            " + conformalPrediction.get(i).getpNegative() + "        |");
            } else {
                writer.write("   " + conformalPrediction.get(i).getpNegative() + "  |");
            }


            if (conformalPrediction.get(i).getRealClass() == 1) {
                writer.write("      " + conformalPrediction.get(i).getRealClass() + "     |");
            } else {
                writer.write("     " + conformalPrediction.get(i).getRealClass() + "     |");
            }
            if (conformalPrediction.get(i).getPredictClass() == 1) {
                writer.write("      " + conformalPrediction.get(i).getPredictClass() + "     |");
            } else {
                writer.write("     " + conformalPrediction.get(i).getPredictClass() + "     |");
            }
            if (conformalPrediction.get(i).getPredictClassSVM() == 1) {
                writer.write("        " + (int) conformalPrediction.get(i).getPredictClassSVM() + "       |");
                writer.write("       " + formatterCC.format(conformalPrediction.get(i).getConfidencePositiveClassSVM() * 100) + "       |");
            } else {
                writer.write("       " + (int) conformalPrediction.get(i).getPredictClassSVM() + "       |");
                writer.write("       " + formatterCC.format(conformalPrediction.get(i).getConfidenceNegativeClassSVM() * 100) + "       |");
            }
            writer.write("     " + formatterCC.format(conformalPrediction.get(i).getConfidence() * 100) + "     |");
            if ((conformalPrediction.get(i).getCredibility() * 100) < 100) {
                writer.write("       " + formatterCC.format(conformalPrediction.get(i).getCredibility() * 100) + "    |");
            } else {
                writer.write("      " + formatterCC.format(conformalPrediction.get(i).getCredibility() * 100) + "    |");
            }
            if (conformalPrediction.get(i).getAlphaPositive() == 1 || conformalPrediction.get(i).getAlphaPositive() == 0) {
                writer.write("         " + conformalPrediction.get(i).getAlphaPositive() + "       |");
            } else {
                writer.write("" + conformalPrediction.get(i).getAlphaPositive() + "|");
            }
            if (conformalPrediction.get(i).getAlphaNegative() == 1 || conformalPrediction.get(i).getAlphaNegative() == 0) {
                writer.write("         " + conformalPrediction.get(i).getAlphaNegative() + "       |");
            } else {
                writer.write("" + conformalPrediction.get(i).getAlphaNegative() + "|");
            }

            int countPositiveOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getPositiveSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getPositiveSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaPositive())
                    countPositiveOne++;
            }

            int countNegativeOne = 0;
            for (int j = 0; j < conformalPrediction.get(i).getNegativeSupportVectorsAlphas().size(); j++) {
                if (conformalPrediction.get(i).getNegativeSupportVectorsAlphas().get(j) >= conformalPrediction.get(i).getAlphaNegative())
                    countNegativeOne++;
            }
            writer.write(" " + countPositiveOne + " |");
            writer.write(" " + countNegativeOne + " |");

            writer.write(" " + conformalPrediction.get(i).getDataObject() + " |");
            writer.write("" + conformalPrediction.get(i).getPositiveSupportVectorsAlphas() + "  ||||  ");
            writer.write("" + conformalPrediction.get(i).getNegativeSupportVectorsAlphas() + " |\r\n");
            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass())
                percent++;
            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass()
                    && conformalPrediction.get(i).getCredibility() == 1)
                amountOfTruePrediction++;
            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass() &&
                    conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClassSVM()) {
                amountSVMandPrediction++;
            }
        }
//        }
        writer.write("|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");
        writer.write("Percent of true: " + (double) percent / (double) conformalPrediction.size() + "\r\n");
        writer.write("Amount of true prediction: " + amountOfTruePrediction + "\r\n");
        writer.write("Accuracy(%): " + (double) amountOfTruePrediction / (double) conformalPrediction.size() + "\r\n");
        writer.write("amountSVMandPrediction: " + amountSVMandPrediction + "\r\n");

        writer.flush();


        System.out.println("Percent of true: " + (double) percent / (double) conformalPrediction.size());
        System.out.println("Amount of true prediction: " + amountOfTruePrediction);
        System.out.println("Accuracy(%): " + (double) amountOfTruePrediction / (double) conformalPrediction.size());
        System.out.println("amountSVMandPrediction: " + amountSVMandPrediction);
    }

    public void printAlphasResultInTwoTxt(ArrayList<Prediction> conformalPrediction, String outputFileNameMain, String outputFileNameAlphasPos, String outputFileNameAlphasNeg) throws IOException {
        int percent = 0;
        int amountOfTruePrediction = 0;
        int amountSVMandPrediction = 0;

        String fileNameMain = outputFileNameMain + ".txt";
        String fileNameAlphasPos = outputFileNameAlphasPos + ".txt";
        String fileNameAlphasNeg = outputFileNameAlphasNeg + ".txt";
        NumberFormat formatterCC = new DecimalFormat("#0.00");
        NumberFormat formatterID = new DecimalFormat("#000");

        File resultMain = new File(fileNameMain);
        resultMain.delete();
        resultMain = new File(fileNameMain);


        FileWriter writerMain = new FileWriter(resultMain, true);


        writerMain.write("|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");
        // writerMain.write("|  ID |            p1          |          p-1          | True class | Prediction | Prediction SVM | Confidence SMV(%) | Confidence(%) | Credibility(%) |   Alpha Positive  |   Alpha Negative  |                 Object                |                                                                  Alphas for positive                                                                   |                                                                  Alphas for Negative                                                                   |\r\n");
        writerMain.write("|  ID |            p1          |          p-1          | True class | Prediction | Prediction SVM | Confidence SMV(%) | Confidence(%) | Credibility(%) |   Alpha Positive  |   Alpha Negative  |\r\n");
        for (int i = 0; i < conformalPrediction.size(); i++) {
//            if (conformalPrediction.get(i).getCredibility() < 1) {

            writerMain.write("| " + formatterID.format(conformalPrediction.get(i).getId()) + " |");

            if (conformalPrediction.get(i).getpPositive() == 1) {
                writerMain.write("            " + conformalPrediction.get(i).getpPositive() + "         |");
            } else {
                writerMain.write("   " + conformalPrediction.get(i).getpPositive() + "  |");
            }
            if (conformalPrediction.get(i).getpNegative() == 1) {
                writerMain.write("            " + conformalPrediction.get(i).getpNegative() + "        |");
            } else {
                writerMain.write("   " + conformalPrediction.get(i).getpNegative() + "  |");
            }


            if (conformalPrediction.get(i).getRealClass() == 1) {
                writerMain.write("      " + conformalPrediction.get(i).getRealClass() + "     |");
            } else {
                writerMain.write("     " + conformalPrediction.get(i).getRealClass() + "     |");
            }
            if (conformalPrediction.get(i).getPredictClass() == 1) {
                writerMain.write("      " + conformalPrediction.get(i).getPredictClass() + "     |");
            } else {
                writerMain.write("     " + conformalPrediction.get(i).getPredictClass() + "     |");
            }
            if (conformalPrediction.get(i).getPredictClassSVM() == 1) {
                writerMain.write("        " + (int) conformalPrediction.get(i).getPredictClassSVM() + "       |");
                writerMain.write("       " + formatterCC.format(conformalPrediction.get(i).getConfidencePositiveClassSVM() * 100) + "       |");
            } else {
                writerMain.write("       " + (int) conformalPrediction.get(i).getPredictClassSVM() + "       |");
                writerMain.write("       " + formatterCC.format(conformalPrediction.get(i).getConfidenceNegativeClassSVM() * 100) + "       |");
            }
            writerMain.write("     " + formatterCC.format(conformalPrediction.get(i).getConfidence() * 100) + "     |");
            if ((conformalPrediction.get(i).getCredibility() * 100) < 100) {
                writerMain.write("       " + formatterCC.format(conformalPrediction.get(i).getCredibility() * 100) + "    |");
            } else {
                writerMain.write("      " + formatterCC.format(conformalPrediction.get(i).getCredibility() * 100) + "    |");
            }
            if (conformalPrediction.get(i).getAlphaPositive() == 1 || conformalPrediction.get(i).getAlphaPositive() == 0) {
                writerMain.write("         " + conformalPrediction.get(i).getAlphaPositive() + "       |");
            } else {
                writerMain.write("" + conformalPrediction.get(i).getAlphaPositive() + "|");
            }
            if (conformalPrediction.get(i).getAlphaNegative() == 1 || conformalPrediction.get(i).getAlphaNegative() == 0) {
                writerMain.write("         " + conformalPrediction.get(i).getAlphaNegative() + "       |\r\n");
            } else {
                writerMain.write("" + conformalPrediction.get(i).getAlphaNegative() + "|\r\n");
            }

            //writerMain.write(" " + conformalPrediction.get(i).getDataObject() + " |\r\n");

            // writerMain.write("" + conformalPrediction.get(i).getNegativeSupportVectorsAlphas() + " |\r\n");

            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass())
                percent++;
            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass()
                    && conformalPrediction.get(i).getCredibility() == 1)
                amountOfTruePrediction++;
            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass() &&
                    conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClassSVM()) {
                amountSVMandPrediction++;
            }
        }
//        }
        writerMain.write("|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");
        writerMain.write("Percent of true: " + (double) percent / (double) conformalPrediction.size() + "\r\n");
        writerMain.write("Amount of true prediction: " + amountOfTruePrediction + "\r\n");
        writerMain.write("Accuracy(%): " + (double) amountOfTruePrediction / (double) conformalPrediction.size() + "\r\n");
        writerMain.write("amountSVMandPrediction: " + amountSVMandPrediction + "\r\n");

        writerMain.flush();

        File resultAlphasPos = new File(fileNameAlphasPos);
        resultAlphasPos.delete();
        resultAlphasPos = new File(outputFileNameAlphasPos);

        FileWriter writerAlphasPos = new FileWriter(resultAlphasPos, true);

        writerAlphasPos.write("|  ID |                                                                  Alphas for positive                                                                   |\r\n");
        for (int i = 0; i < conformalPrediction.size(); i++) {

            writerAlphasPos.write("| " + formatterID.format(conformalPrediction.get(i).getId()) + " |");
            for (int cp = 0; cp < conformalPrediction.get(i).getPositiveSupportVectorsAlphas().size(); cp++) {
                writerAlphasPos.write("" + formatterCC.format(conformalPrediction.get(i).getPositiveSupportVectorsAlphas().get(cp)) + ", ");
            }

            writerAlphasPos.write(" | ");
            writerAlphasPos.write("\r\n");
        }
        writerAlphasPos.flush();
        writerAlphasPos.write("|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");


        File resultAlphasNeg = new File(fileNameAlphasNeg);
        resultAlphasNeg.delete();
        resultAlphasNeg = new File(fileNameAlphasNeg);

        FileWriter writerAlphasNeg = new FileWriter(resultAlphasNeg, true);

        writerAlphasNeg.write("|  ID |                                                                  Alphas for Negative                                                                   |\r\n");
        for (int i = 0; i < conformalPrediction.size(); i++) {
            writerAlphasNeg.write("| " + formatterID.format(conformalPrediction.get(i).getId()) + " |");
            for (int cn = 0; cn < conformalPrediction.get(i).getNegativeSupportVectorsAlphas().size(); cn++) {
                writerAlphasNeg.write("" + formatterCC.format(conformalPrediction.get(i).getNegativeSupportVectorsAlphas().get(cn)) + ", ");
            }
            writerAlphasNeg.write(" | ");
            writerAlphasNeg.write("\r\n");
        }
        writerAlphasNeg.flush();
        writerAlphasNeg.write("|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");


        System.out.println("Percent of true: " + (double) percent / (double) conformalPrediction.size());
        System.out.println("Amount of true prediction: " + amountOfTruePrediction);
        System.out.println("Accuracy(%): " + (double) amountOfTruePrediction / (double) conformalPrediction.size());
        System.out.println("amountSVMandPrediction: " + amountSVMandPrediction);
    }

    public void printMainResultInOnePDF(ArrayList<Prediction> conformalPrediction, String outputFileNameMain) throws IOException, DocumentException {
        int percent = 0;
        int amountOfTruePrediction = 0;
        int amountSVMandPrediction = 0;

        String fileNameMain = outputFileNameMain + ".pdf";
        NumberFormat formatterCC = new DecimalFormat("#0.00");
        NumberFormat formatterID = new DecimalFormat("#000");
        NumberFormat formatterClass = new DecimalFormat("#0");

        File resultMain = new File(fileNameMain);
        resultMain.delete();
        resultMain = new File(fileNameMain);

        Document document = new Document();


        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(
                    fileNameMain));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();

        PdfPTable table = new PdfPTable(11); // 2 columns.
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        //Set Column widths
        float[] columnWidths = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
        table.setWidths(columnWidths);

        PdfPCell cellId = new PdfPCell(new Paragraph("ID"));
        cellId.setPaddingLeft(5);
        cellId.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellId.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellPP = new PdfPCell(new Paragraph("p1"));
        cellPP.setPaddingLeft(10);
        cellPP.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPP.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellPN = new PdfPCell(new Paragraph("p-1"));
        cellPN.setPaddingLeft(10);
        cellPN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPN.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellTrueClass = new PdfPCell(new Paragraph("True class"));
        cellTrueClass.setPaddingLeft(10);
        cellTrueClass.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTrueClass.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellPrediction = new PdfPCell(new Paragraph("Prediction"));
        cellPrediction.setPaddingLeft(10);
        cellPrediction.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPrediction.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellPredictionSVM = new PdfPCell(new Paragraph("Prediction SVM"));
        cellPredictionSVM.setPaddingLeft(10);
        cellPredictionSVM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPredictionSVM.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellConfidenceSVM = new PdfPCell(new Paragraph("Confidence SVM(%)"));
        cellConfidenceSVM.setPaddingLeft(10);
        cellConfidenceSVM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellConfidenceSVM.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellConfidence = new PdfPCell(new Paragraph("Confidence"));
        cellConfidence.setPaddingLeft(10);
        cellConfidence.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellConfidence.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellCredibility = new PdfPCell(new Paragraph("Credibility"));
        cellCredibility.setPaddingLeft(10);
        cellCredibility.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellCredibility.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellAlphaPositive = new PdfPCell(new Paragraph("Alpha Positive"));
        cellAlphaPositive.setPaddingLeft(10);
        cellAlphaPositive.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAlphaPositive.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cellAlphaNegative = new PdfPCell(new Paragraph("Alpha Negative"));
        cellAlphaNegative.setPaddingLeft(10);
        cellAlphaNegative.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAlphaNegative.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cellId);
        table.addCell(cellPP);
        table.addCell(cellPN);
        table.addCell(cellTrueClass);
        table.addCell(cellPrediction);
        table.addCell(cellPredictionSVM);
        table.addCell(cellConfidenceSVM);
        table.addCell(cellConfidence);
        table.addCell(cellCredibility);
        table.addCell(cellAlphaPositive);
        table.addCell(cellAlphaNegative);

        for (int i = 0; i < conformalPrediction.size(); i++) {
            cellId = new PdfPCell(new Paragraph(formatterID.format(conformalPrediction.get(i).getId())));
            cellId.setPaddingLeft(5);
            cellId.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellId.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellPP = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getpPositive())));
            cellPP.setPaddingLeft(10);
            cellPP.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellPP.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellPN = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getpNegative())));
            cellPN.setPaddingLeft(10);
            cellPN.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellPN.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellTrueClass = new PdfPCell(new Paragraph(formatterClass.format(conformalPrediction.get(i).getRealClass())));
            cellTrueClass.setPaddingLeft(10);
            cellTrueClass.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTrueClass.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellPrediction = new PdfPCell(new Paragraph(formatterClass.format(conformalPrediction.get(i).getPredictClass())));
            cellPrediction.setPaddingLeft(10);
            cellPrediction.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellPrediction.setVerticalAlignment(Element.ALIGN_MIDDLE);

            if (conformalPrediction.get(i).getPredictClassSVM() == 1) {
                cellPredictionSVM = new PdfPCell(new Paragraph(formatterClass.format(conformalPrediction.get(i).getPredictClassSVM())));
                cellPredictionSVM.setPaddingLeft(10);
                cellPredictionSVM.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellPredictionSVM.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cellConfidenceSVM = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getConfidencePositiveClassSVM() * 100)));
                cellConfidenceSVM.setPaddingLeft(10);
                cellConfidenceSVM.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellConfidenceSVM.setVerticalAlignment(Element.ALIGN_MIDDLE);
            } else {
                cellPredictionSVM = new PdfPCell(new Paragraph(formatterClass.format(conformalPrediction.get(i).getPredictClassSVM())));
                cellPredictionSVM.setPaddingLeft(10);
                cellPredictionSVM.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellPredictionSVM.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cellConfidenceSVM = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getConfidenceNegativeClassSVM() * 100)));
                cellConfidenceSVM.setPaddingLeft(10);
                cellConfidenceSVM.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellConfidenceSVM.setVerticalAlignment(Element.ALIGN_MIDDLE);
            }

            cellConfidence = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getConfidence() * 100)));
            cellConfidence.setPaddingLeft(10);
            cellConfidence.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellConfidence.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellCredibility = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getCredibility() * 100)));
            cellCredibility.setPaddingLeft(10);
            cellCredibility.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCredibility.setVerticalAlignment(Element.ALIGN_MIDDLE);


            cellAlphaPositive = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getAlphaPositive())));
            cellAlphaPositive.setPaddingLeft(10);
            cellAlphaPositive.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellAlphaPositive.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellAlphaNegative = new PdfPCell(new Paragraph(formatterCC.format(conformalPrediction.get(i).getAlphaNegative())));
            cellAlphaNegative.setPaddingLeft(10);
            cellAlphaNegative.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellAlphaNegative.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table.addCell(cellId);
            table.addCell(cellPP);
            table.addCell(cellPN);
            table.addCell(cellTrueClass);
            table.addCell(cellPrediction);
            table.addCell(cellPredictionSVM);
            table.addCell(cellConfidenceSVM);
            table.addCell(cellConfidence);
            table.addCell(cellCredibility);
            table.addCell(cellAlphaPositive);
            table.addCell(cellAlphaNegative);
        }

        document.add(table);
        document.close();
        writer.close();


//        FileWriter writerMain = new FileWriter(resultMain, true);
//
//
//        writerMain.write("|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");
//        // writerMain.write("|  ID |            p1          |          p-1          | True class | Prediction | Prediction SVM | Confidence SMV(%) | Confidence(%) | Credibility(%) |   Alpha Positive  |   Alpha Negative  |                 Object                |                                                                  Alphas for positive                                                                   |                                                                  Alphas for Negative                                                                   |\r\n");
//        writerMain.write("|  ID |            p1          |          p-1          | True class | Prediction | Prediction SVM | Confidence SMV(%) | Confidence(%) | Credibility(%) |   Alpha Positive  |   Alpha Negative  |\r\n");
//        for (int i = 0; i < conformalPrediction.size(); i++) {
////            if (conformalPrediction.get(i).getCredibility() < 1) {
//
//            writerMain.write("| " + formatterID.format(conformalPrediction.get(i).getId()) + " |");
//
//            if (conformalPrediction.get(i).getpPositive() == 1) {
//                writerMain.write("            " + conformalPrediction.get(i).getpPositive() + "         |");
//            } else {
//                writerMain.write("   " + conformalPrediction.get(i).getpPositive() + "  |");
//            }
//            if (conformalPrediction.get(i).getpNegative() == 1) {
//                writerMain.write("            " + conformalPrediction.get(i).getpNegative() + "        |");
//            } else {
//                writerMain.write("   " + conformalPrediction.get(i).getpNegative() + "  |");
//            }
//
//
//            if (conformalPrediction.get(i).getRealClass() == 1) {
//                writerMain.write("      " + conformalPrediction.get(i).getRealClass() + "     |");
//            } else {
//                writerMain.write("     " + conformalPrediction.get(i).getRealClass() + "     |");
//            }
//            if (conformalPrediction.get(i).getPredictClass() == 1) {
//                writerMain.write("      " + conformalPrediction.get(i).getPredictClass() + "     |");
//            } else {
//                writerMain.write("     " + conformalPrediction.get(i).getPredictClass() + "     |");
//            }
//            if (conformalPrediction.get(i).getPredictClassSVM() == 1) {
//                writerMain.write("        " + (int) conformalPrediction.get(i).getPredictClassSVM() + "       |");
//                writerMain.write("       " + formatterCC.format(conformalPrediction.get(i).getConfidencePositiveClassSVM() * 100) + "       |");
//            } else {
//                writerMain.write("       " + (int) conformalPrediction.get(i).getPredictClassSVM() + "       |");
//                writerMain.write("       " + formatterCC.format(conformalPrediction.get(i).getConfidenceNegativeClassSVM() * 100) + "       |");
//            }
//            writerMain.write("     " + formatterCC.format(conformalPrediction.get(i).getConfidence() * 100) + "     |");
//            if ((conformalPrediction.get(i).getCredibility() * 100) < 100) {
//                writerMain.write("       " + formatterCC.format(conformalPrediction.get(i).getCredibility() * 100) + "    |");
//            } else {
//                writerMain.write("      " + formatterCC.format(conformalPrediction.get(i).getCredibility() * 100) + "    |");
//            }
//            if (conformalPrediction.get(i).getAlphaPositive() == 1 || conformalPrediction.get(i).getAlphaPositive() == 0) {
//                writerMain.write("         " + conformalPrediction.get(i).getAlphaPositive() + "       |");
//            } else {
//                writerMain.write("" + conformalPrediction.get(i).getAlphaPositive() + "|");
//            }
//            if (conformalPrediction.get(i).getAlphaNegative() == 1 || conformalPrediction.get(i).getAlphaNegative() == 0) {
//                writerMain.write("         " + conformalPrediction.get(i).getAlphaNegative() + "       |\r\n");
//            } else {
//                writerMain.write("" + conformalPrediction.get(i).getAlphaNegative() + "|\r\n");
//            }
//
//            //writerMain.write(" " + conformalPrediction.get(i).getDataObject() + " |\r\n");
//
//            // writerMain.write("" + conformalPrediction.get(i).getNegativeSupportVectorsAlphas() + " |\r\n");
//
//            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass())
//                percent++;
//            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass()
//                    && conformalPrediction.get(i).getCredibility() == 1)
//                amountOfTruePrediction++;
//            if (conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClass() &&
//                    conformalPrediction.get(i).getRealClass() == conformalPrediction.get(i).getPredictClassSVM()) {
//                amountSVMandPrediction++;
//            }
//        }
////        }
//        writerMain.write("|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|\r\n");
//        writerMain.write("Percent of true: " + (double) percent / (double) conformalPrediction.size() + "\r\n");
//        writerMain.write("Amount of true prediction: " + amountOfTruePrediction + "\r\n");
//        writerMain.write("Accuracy(%): " + (double) amountOfTruePrediction / (double) conformalPrediction.size() + "\r\n");
//        writerMain.write("amountSVMandPrediction: " + amountSVMandPrediction + "\r\n");
//
//        writerMain.flush();


//        System.out.println("Percent of true: " + (double) percent / (double) conformalPrediction.size());
//        System.out.println("Amount of true prediction: " + amountOfTruePrediction);
//        System.out.println("Accuracy(%): " + (double) amountOfTruePrediction / (double) conformalPrediction.size());
//        System.out.println("amountSVMandPrediction: " + amountSVMandPrediction);
    }

    public void printFullResultInOneExcel(ArrayList<Prediction> conformalPrediction, String outputFileName, String sheetName) throws IOException {

        SettingsExcel settingsExcel = new SettingsExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);


        ArrayList<String> stringsName;
        stringsName = Parser.getTitle("titleName");

        settingsExcel.createCellForTitle(sheet, stringsName);

        settingsExcel.createCellDouble(sheet, conformalPrediction);

        settingsExcel.createCellRange80To100(sheet, conformalPrediction);

        settingsExcel.createCellMatrixRegionPrediction(sheet, getMatrixRegionPrediction(conformalPrediction), conformalPrediction.size() + 10);

        //settingsExcel.createCellTestColor(sheet, conformalPrediction);

        File file = new File("C:/SVM2/" + outputFileName + "/" + outputFileName + ".xlsx");
        file.getParentFile().mkdirs();
        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());

//        System.out.println("Region Prediction:\n");
//        printMatrixRegionPrediction(conformalPrediction);
    }

    public void printRegionPrediction_old(ArrayList<Prediction> conformalPrediction) {
        int row = 5;
        int column = 5;
        int[][] matrix = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrix[i][j] = 0;
            }
        }


        int i;
        int j = 0;
        double significance;

        for (int k = 0; k < conformalPrediction.size(); k++) {

            significance = 0.01;
            i = 0;

            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == 1) {
                j = 0;
            }
            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == 1) {
                j = 1;
            }
            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == -1) {
                j = 2;
            }
            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == -1) {
                j = 3;
            }

            if (conformalPrediction.get(k).getpPositive() >= significance) {
                matrix[i][j] = matrix[i][j] + 1;
            }
            if (conformalPrediction.get(k).getpNegative() >= significance) {
                matrix[i][j] = matrix[i][j] + 1;
            }
            if (conformalPrediction.get(k).getpPositive() < 0 && conformalPrediction.get(k).getpNegative() < 0) {
                matrix[i][4] = matrix[i][4] + 1;
            }
            significance = 0.05;
            for (i = 1; i < row; i++) {
                if (conformalPrediction.get(k).getpPositive() >= significance) {
                    matrix[i][j] = matrix[i][j] + 1;
                }
                if (conformalPrediction.get(k).getpNegative() >= significance) {
                    matrix[i][j] = matrix[i][j] + 1;
                }
                if (conformalPrediction.get(k).getpPositive() < significance && conformalPrediction.get(k).getpNegative() < significance) {
                    matrix[i][4] = matrix[i][4] + 1;
                }
                significance = significance + 0.05;
            }
        }

        System.out.println();
        System.out.println("Total object: " + conformalPrediction.size());
        System.out.println("Region Prediction:\n");
        for (i = 0; i < row; i++) {
            for (j = 0; j < column; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printMatrixRegionPrediction(ArrayList<Prediction> conformalPrediction) {
        int row = 5;
        int column = 5;
        //int[][] matrix = new int[row][column];
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < column; j++) {
//                matrix[i][j] = 0;
//            }
//        }
        int[][] matrix = getMatrixRegionPrediction(conformalPrediction);


//        int i;
//        int j = 0;
//        double significance;
//
//        for (int k = 0; k < conformalPrediction.size(); k++) {
//
//            significance = 0.01;
//            i = 0;
//
//            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == 1) {
//                j = 0;
//                if (conformalPrediction.get(k).getpPositive() >= significance) {
//                    matrix[i][j] = matrix[i][j] + 1;
//                } else if (conformalPrediction.get(k).getpPositive() < significance) {
//                    matrix[i][4] = matrix[i][4] + 1;
//                }
//            }
//            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == 1) {
//                j = 1;
//                if (conformalPrediction.get(k).getpPositive() >= significance) {
//                    matrix[i][j] = matrix[i][j] + 1;
//                } else if (conformalPrediction.get(k).getpPositive() < significance) {
//                    matrix[i][4] = matrix[i][4] + 1;
//                }
//            }
//            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == -1) {
//                j = 2;
//                if (conformalPrediction.get(k).getpNegative() >= significance) {
//                    matrix[i][j] = matrix[i][j] + 1;
//                } else if (conformalPrediction.get(k).getpNegative() < significance) {
//                    matrix[i][4] = matrix[i][4] + 1;
//                }
//            }
//            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == -1) {
//                j = 3;
//                if (conformalPrediction.get(k).getpNegative() >= significance) {
//                    matrix[i][j] = matrix[i][j] + 1;
//                } else if (conformalPrediction.get(k).getpNegative() < significance) {
//                    matrix[i][4] = matrix[i][4] + 1;
//                }
//            }
//
//            significance = 0.05;
//            for (i = 1; i < row; i++) {
//                switch (j){
//                    case 0:
//                        if (conformalPrediction.get(k).getpPositive() >= significance) {
//                            matrix[i][j] = matrix[i][j] + 1;
//                        } else if (conformalPrediction.get(k).getpPositive() < significance) {
//                            matrix[i][4] = matrix[i][4] + 1;
//                        }
//                        break;
//
//                    case 1:
//                        if (conformalPrediction.get(k).getpPositive() >= significance) {
//                            matrix[i][j] = matrix[i][j] + 1;
//                        } else if (conformalPrediction.get(k).getpPositive() < significance) {
//                            matrix[i][4] = matrix[i][4] + 1;
//                        }
//                        break;
//
//                    case 2:
//                        if (conformalPrediction.get(k).getpNegative() >= significance) {
//                            matrix[i][j] = matrix[i][j] + 1;
//                        } else if (conformalPrediction.get(k).getpNegative() < significance) {
//                            matrix[i][4] = matrix[i][4] + 1;
//                        }
//                        break;
//
//                    case 3:
//                        if (conformalPrediction.get(k).getpNegative() >= significance) {
//                            matrix[i][j] = matrix[i][j] + 1;
//                        } else if (conformalPrediction.get(k).getpNegative() < significance) {
//                            matrix[i][4] = matrix[i][4] + 1;
//                        }
//                        break;
//                }
//                significance = significance + 0.05;
//            }
//        }

        System.out.println();
        System.out.println("Total object: " + conformalPrediction.size());
        System.out.println("Region Prediction:");
        System.out.println("Real:    1 -1  1 -1 Empty");
        System.out.println("Predict: 1  1 -1 -1 Empty");
        for (int i = 0; i < row; i++) {
            switch (i) {
                case 0:
                    System.out.print("0.01     ");
                    break;
                case 1:
                    System.out.print("0.05     ");
                    break;
                case 2:
                    System.out.print("0.1      ");
                    break;
                case 3:
                    System.out.print("0.15     ");
                    break;
                case 4:
                    System.out.print("0.2      ");
                    break;
            }
            for (int j = 0; j < column; j++) {
                System.out.print(matrix[i][j] + " ");

            }
            System.out.println();
        }
    }

    public int[][] getMatrixRegionPrediction(ArrayList<Prediction> conformalPrediction) {
        int row = 5;
        int column = 5;
        int[][] matrix = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrix[i][j] = 0;
            }
        }

        int i;
        int j = 0;
        double significance;

        for (int k = 0; k < conformalPrediction.size(); k++) {

            significance = 0.01;
            i = 0;

            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == 1) {
                j = 0;
                if (conformalPrediction.get(k).getpPositive() >= significance) {
                    matrix[i][j] = matrix[i][j] + 1;
                } else if (conformalPrediction.get(k).getpPositive() < significance) {
                    matrix[i][4] = matrix[i][4] + 1;
                }
            }
            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == 1) {
                j = 1;
                if (conformalPrediction.get(k).getpPositive() >= significance) {
                    matrix[i][j] = matrix[i][j] + 1;
                } else if (conformalPrediction.get(k).getpPositive() < significance) {
                    matrix[i][4] = matrix[i][4] + 1;
                }
            }
            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == -1) {
                j = 2;
                if (conformalPrediction.get(k).getpNegative() >= significance) {
                    matrix[i][j] = matrix[i][j] + 1;
                } else if (conformalPrediction.get(k).getpNegative() < significance) {
                    matrix[i][4] = matrix[i][4] + 1;
                }
            }
            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == -1) {
                j = 3;
                if (conformalPrediction.get(k).getpNegative() >= significance) {
                    matrix[i][j] = matrix[i][j] + 1;
                } else if (conformalPrediction.get(k).getpNegative() < significance) {
                    matrix[i][4] = matrix[i][4] + 1;
                }
            }

            significance = 0.05;
            for (i = 1; i < row; i++) {
                switch (j) {
                    case 0:
                        if (conformalPrediction.get(k).getpPositive() >= significance) {
                            matrix[i][j] = matrix[i][j] + 1;
                        } else if (conformalPrediction.get(k).getpPositive() < significance) {
                            matrix[i][4] = matrix[i][4] + 1;
                        }
                        break;

                    case 1:
                        if (conformalPrediction.get(k).getpPositive() >= significance) {
                            matrix[i][j] = matrix[i][j] + 1;
                        } else if (conformalPrediction.get(k).getpPositive() < significance) {
                            matrix[i][4] = matrix[i][4] + 1;
                        }
                        break;

                    case 2:
                        if (conformalPrediction.get(k).getpNegative() >= significance) {
                            matrix[i][j] = matrix[i][j] + 1;
                        } else if (conformalPrediction.get(k).getpNegative() < significance) {
                            matrix[i][4] = matrix[i][4] + 1;
                        }
                        break;

                    case 3:
                        if (conformalPrediction.get(k).getpNegative() >= significance) {
                            matrix[i][j] = matrix[i][j] + 1;
                        } else if (conformalPrediction.get(k).getpNegative() < significance) {
                            matrix[i][4] = matrix[i][4] + 1;
                        }
                        break;
                }
                significance = significance + 0.05;
            }
        }
        return matrix;
    }


    /////////
    public void printToExcelTestMethod(String outputFileName, String sheetName) throws IOException {

        SettingsExcel settingsExcel = new SettingsExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);


        ArrayList<String> stringsName;
        stringsName = Parser.getTitle("titleName");

//        settingsExcel.createCellForTitle(sheet, stringsName);
//
//        settingsExcel.createCellDouble(sheet, conformalPrediction);
//
//        settingsExcel.createCellRange80To100(sheet, conformalPrediction);
//
//        settingsExcel.createCellMatrixRegionPrediction(sheet, getMatrixRegionPrediction(conformalPrediction), conformalPrediction.size() + 10);


        //settingsExcel.createCellTestColor(sheet, conformalPrediction);

        File file = new File("C:/SVM2/" + outputFileName + "/" + outputFileName + ".xlsx");
        file.getParentFile().mkdirs();
        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());

//        System.out.println("Region Prediction:\n");
//        printMatrixRegionPrediction(conformalPrediction);
    }


    public void printToExcelMatrixRPDifEpsTestMethod(SymptomsService symptomsService, String inputTestFileName, String outputFileName, int degree, String svmType) throws IOException, SQLException {

//        SettingsExcel settingsExcel = new SettingsExcel();
//
//
//        XSSFWorkbook workbook = new XSSFWorkbook();

        File file = new File("C:/SVM6_TEST_RegionPrediction/" + outputFileName + ".xlsx");
        file.getParentFile().mkdirs();
        FileOutputStream outFile = new FileOutputStream(file);


        SettingsExcel settingsExcel = new SettingsExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();

//        if(file.exists() && file.length() != 0){
//            try {
//                workbook = (XSSFWorkbook) WorkbookFactory.create(file);
//            } catch (InvalidFormatException e) {
//                e.printStackTrace();
//            }
//            sheet = workbook.createSheet(outputFileName);
//            styleStandart = settingsExcel.createStyleForCellStandart(workbook);
//        } else {
//            workbook = new XSSFWorkbook();
//            sheet = workbook.createSheet(outputFileName);
//            styleStandart = settingsExcel.createStyleForCellStandart(workbook);
//        }
        XSSFSheet sheet = workbook.createSheet(outputFileName);
        XSSFCellStyle styleStandart = settingsExcel.createStyleForCellStandart(workbook);

        Row row;
        Cell cell;

        int indentRow = 0;

        row = sheet.createRow(indentRow);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Real: 1\nPredict: 1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Real: -1\nPredict: 1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Real: 1\nPredict: -1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Real: -1\nPredict: -1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Empty");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Uncertain predictions");
        cell.setCellStyle(styleStandart);

        double[] significance = {0.01, 0.05, 0.1, 0.15, 0.2};

        int column = 6;
        int[] matrix = new int[column];
//        for (int j = 0; j < column; j++) {
//            matrix[j] = 0;
//        }


        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction(inputTestFileName, degree, 0.001, svmType);

        for (int i = 0; i < significance.length; i++) {
            for (int j = 0; j < column; j++) {
                matrix[j] = 0;
            }
            for (int k = 0; k < conformalPrediction.size(); k++) {
                for (int j = 0; j < column; j++) {
                    switch (j) {
                        case 0:
                            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == 1) {
                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
                                    matrix[5] = matrix[5] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
                                    matrix[4] = matrix[4] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])
                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
                                    matrix[j] = matrix[j] + 1;
                                }

                            }
                            break;

                        case 1:
                            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == 1) {
                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
                                    matrix[5] = matrix[5] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
                                    matrix[4] = matrix[4] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])
                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
                                    matrix[j] = matrix[j] + 1;
                                }

                            }
//                            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == 1) {
////                                if (conformalPrediction.get(k).getpPositive() >= significance[i]) {
//                                if (conformalPrediction.get(k).getpPositive() <= significance[i]) {
//                                    matrix[j] = matrix[j] + 1;
////                                } else if (conformalPrediction.get(k).getpPositive() < significance[i]) {
//                                } else if (conformalPrediction.get(k).getpPositive() > significance[i]) {
//                                    matrix[5] = matrix[5] + 1;
//                                } else if (conformalPrediction.get(k).getpPositive() == conformalPrediction.get(k).getpNegative()) {
//                                    matrix[4] = matrix[4] + 1;
//                                }

//                                if(conformalPrediction.get(k).getpNegative() >= significance[i]){
//                                if(conformalPrediction.get(k).getpNegative() <= significance[i]){
//                                    matrix[j] = matrix[j] + 1;
//                                }
//                            }


                            break;

                        case 2:
                            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == -1) {
                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
                                    matrix[5] = matrix[5] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
                                    matrix[4] = matrix[4] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])
                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
                                    matrix[j] = matrix[j] + 1;
                                }
                            }
//                            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == -1) {
////                                if (conformalPrediction.get(k).getpNegative() >= significance[i]) {
//                                if (conformalPrediction.get(k).getpNegative() <= significance[i]) {
//                                    matrix[j] = matrix[j] + 1;
////                                } else if (conformalPrediction.get(k).getpNegative() < significance[i]) {
//                                } else if (conformalPrediction.get(k).getpNegative() > significance[i]) {
//                                    matrix[5] = matrix[5] + 1;
//                                } else if (conformalPrediction.get(k).getpPositive() == conformalPrediction.get(k).getpNegative()) {
//                                    matrix[4] = matrix[4] + 1;
//                                }

//                                if(conformalPrediction.get(k).getpPositive() >= significance[i]){
//                                if(conformalPrediction.get(k).getpPositive() <= significance[i]){
//                                    matrix[j] = matrix[j] + 1;
//                                }
//                            }
                            break;

                        case 3:
                            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == -1) {
                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
                                    matrix[5] = matrix[5] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
                                    matrix[4] = matrix[4] + 1;
                                }
                                if ((conformalPrediction.get(k).getpPositive() >= significance[i]
                                        && conformalPrediction.get(k).getpNegative() < significance[i])
                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
                                    matrix[j] = matrix[j] + 1;
                                }

                            }
//                            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == -1) {
////                                if (conformalPrediction.get(k).getpNegative() >= significance[i]) {
//                                if (conformalPrediction.get(k).getpNegative() <= significance[i]) {
//                                    matrix[j] = matrix[j] + 1;
////                                } else if (conformalPrediction.get(k).getpNegative() < significance[i]) {
//                                } else if (conformalPrediction.get(k).getpNegative() > significance[i]) {
//                                    matrix[5] = matrix[5] + 1;
//                                } else if (conformalPrediction.get(k).getpPositive() == conformalPrediction.get(k).getpNegative()) {
//                                    matrix[4] = matrix[4] + 1;
//                                }
//
////                                if(conformalPrediction.get(k).getpPositive() >= significance[i]){
////                                if(conformalPrediction.get(k).getpPositive() <= significance[i]){
////                                    matrix[j] = matrix[j] + 1;
////                                }
//                            }
                            break;
                    }
                }
            }
            settingsExcel.createCellRowMatrixRegionPrediction(sheet, matrix, significance[i], indentRow + i);
        }

//        File file = new File("C:/SVM4_TEST_RegionPrediction/" + "test" + "/" + "test" + ".xlsx");
//        file.getParentFile().mkdirs();
//        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());
        outFile.close();
    }


    public void printTableAndMatrix(ArrayList<Prediction> conformalPrediction, String outputFileName) throws IOException {
        SettingsExcel settingsExcel = new SettingsExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(outputFileName);
        XSSFCellStyle styleStandart = settingsExcel.createStyleForCellStandart(workbook);

        ArrayList<String> stringsName;
        stringsName = Parser.getTitle("titleName80To99");

        settingsExcel.createCellForTitle(sheet, stringsName);

//        settingsExcel.createCellDouble(sheet, conformalPrediction);
        settingsExcel.createCellTable80To99(sheet, conformalPrediction);

        ////////////////////

        Row row;
        Cell cell;

        int indentRow = conformalPrediction.size() + 2;

        row = sheet.createRow(indentRow);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Real: 1\nPredict: 1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Real: -1\nPredict: 1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Real: 1\nPredict: -1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Real: -1\nPredict: -1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Empty");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Uncertain predictions");
        cell.setCellStyle(styleStandart);

        double[] significance = {0.01, 0.05, 0.1, 0.15, 0.2};

        int column = 6;
        int[] matrix = new int[column];

        for (int i = 0; i < significance.length; i++) {
            for (int j = 0; j < column; j++) {
                matrix[j] = 0;
            }
            for (int k = 0; k < conformalPrediction.size(); k++) {
                if ((conformalPrediction.get(k).getpPositive() < significance[i]
                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
                    matrix[4] = matrix[4] + 1;
                } else if (conformalPrediction.get(k).getpPositive() >= significance[i]
                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
                    matrix[5] = matrix[5] + 1;
                } else if(conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == 1){
                    matrix[0] = matrix[0] + 1;
                } else if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == 1){
                    matrix[1] = matrix[1] + 1;
                } else if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == -1){
                    matrix[2] = matrix[2] + 1;
                } else if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == -1){
                    matrix[3] = matrix[3] + 1;
                }

//                for (int j = 0; j < column; j++) {
//                    switch (j) {
//                        case 0:
//                            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == 1) {
//                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
//                                    matrix[5] = matrix[5] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
//                                    matrix[4] = matrix[4] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])
//                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
//                                    matrix[j] = matrix[j] + 1;
//                                }
//
//                            }
//                            break;
//
//                        case 1:
//                            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == 1) {
//                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
//                                    matrix[5] = matrix[5] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
//                                    matrix[4] = matrix[4] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])
//                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
//                                    matrix[j] = matrix[j] + 1;
//                                }
//
//                            }
//
//                            break;
//
//                        case 2:
//                            if (conformalPrediction.get(k).getRealClass() == 1 && conformalPrediction.get(k).getPredictClass() == -1) {
//                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
//                                    matrix[5] = matrix[5] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
//                                    matrix[4] = matrix[4] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])
//                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
//                                    matrix[j] = matrix[j] + 1;
//                                }
//                            }
//                            break;
//
//                        case 3:
//                            if (conformalPrediction.get(k).getRealClass() == -1 && conformalPrediction.get(k).getPredictClass() == -1) {
//                                if (conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i]) {
//                                    matrix[5] = matrix[5] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])) {
//                                    matrix[4] = matrix[4] + 1;
//                                } else if ((conformalPrediction.get(k).getpPositive() >= significance[i]
//                                        && conformalPrediction.get(k).getpNegative() < significance[i])
//                                        || (conformalPrediction.get(k).getpPositive() < significance[i]
//                                        && conformalPrediction.get(k).getpNegative() >= significance[i])) {
//                                    matrix[j] = matrix[j] + 1;
//                                }
//                            }
//                            break;
//                    }
//                }
            }
            settingsExcel.createCellRowMatrixRegionPrediction(sheet, matrix, significance[i], indentRow + i);
        }

////////////////////
        File file = new File("C:/SVM_MatrixTestFor/" + outputFileName + ".xlsx");
        file.getParentFile().mkdirs();
        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());
        outFile.close();
    }

}

