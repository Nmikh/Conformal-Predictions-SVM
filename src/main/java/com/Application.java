package com;

import com.models.Prediction;
import com.services.SymptomsService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Application {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        SymptomsService symptomsService = new SymptomsService();
//

        int k = 3;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", k);

//        //Prediction prediction = new Prediction();

        SymptomsResult symptomsResult = new SymptomsResult();

//        svm_model svmModel = symptomsService.svmTrain();
//        MatrixModel matrixModel = symptomsService.evaluateAllInstances("test.txt", conformalPrediction);
//        System.out.println(matrixModel);

        //int k = 2;
        //String svmType = "RBF_gamma_";

        String svmType = "RBF_";

//        symptomsResult.printFullResultInOneTxt(conformalPrediction, "resultsPOLYMain" + k);
//        symptomsResult.printAlphasResultInTwoTxt(conformalPrediction, "resultsPOLYMain" + k,
//                "alphaPositivePOLY" + k, "alphaNegativePOLY" + k);
//
//        try {
//            symptomsResult.printMainResultInOnePDF(conformalPrediction, "resultMainPDF" + k);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }

       // symptomsResult.printMatrixRegionPrediction(conformalPrediction);

        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_tables_Matrix_" + svmType + k, "svm sheet");

        symptomsResult.printMatrixRegionPrediction(conformalPrediction);


    }


}
