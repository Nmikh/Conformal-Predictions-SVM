package com;

import com.itextpdf.text.DocumentException;
import com.models.Prediction;
import com.services.SymptomsService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Application {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        SymptomsService symptomsService = new SymptomsService();

        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt");
        Prediction prediction = new Prediction();

//        svm_model svmModel = symptomsService.svmTrain();
//        MatrixModel matrixModel = symptomsService.evaluateAllInstances("test.txt", conformalPrediction);
//        System.out.println(matrixModel);

        int k = 2;
        prediction.printResult(conformalPrediction, "resultsPOLYMain" + k + ".txt");
        prediction.printResultTwoFiles(conformalPrediction, "resultsPOLYMain" + k + ".txt",
                "alphaPositivePOLY" + k + ".txt", "alphaNegativePOLY" + k + ".txt");

        try {
            prediction.printResultMainPDF(conformalPrediction, "resultMainPDF" + k + ".pdf");
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }


}
