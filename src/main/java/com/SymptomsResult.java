package com;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.models.Prediction;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parser.Parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

        String fileName = outputFileName+".txt";
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

    public void printAlphasResultInTwoTxt(ArrayList<Prediction> conformalPrediction, String outputFileNameMain, String outputFileNameAlphasPos, String  outputFileNameAlphasNeg) throws IOException {
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

            writerAlphasPos.write(" | " );
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
            writerAlphasNeg.write(" | " );
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

    public void printFullResultInOneExcel(ArrayList<Prediction> conformalPrediction, String outputFileName, String sheetName) throws IOException{

        SettingsExcel settingsExcel = new SettingsExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);


        ArrayList<String> stringsName;
        stringsName = Parser.getTitle("titleName");

        settingsExcel.createCellForTitle(sheet, stringsName);
        settingsExcel.createCellDouble(sheet, conformalPrediction);
        //settingsExcel.createCellTestColor(sheet, conformalPrediction);

        File file = new File("C:/SVM2/" + outputFileName +"/" +outputFileName + ".xlsx");
        file.getParentFile().mkdirs();
        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());
    }
}
