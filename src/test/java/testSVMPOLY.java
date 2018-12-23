package java;

import com.SymptomsResult;
import com.models.Prediction;
import com.services.SymptomsService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Admin on 22.12.2018.
 */
public class testSVMPOLY {
    SymptomsService symptomsService;

    @Before
    public void init() throws SQLException, IOException, ClassNotFoundException {
        symptomsService = new SymptomsService();
    }

    @Test
    public void SVMPOLY2() throws IOException, SQLException {
        int degree = 2;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree);
        SymptomsResult symptomsResult = new SymptomsResult();
        String svmType = "POLY_";
        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_TEST_eps_01_" + svmType + degree, "svm sheet");
    }

    @Test
    public void SVMPOLY4() throws IOException, SQLException {
        int degree = 4;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree);
        SymptomsResult symptomsResult = new SymptomsResult();
        String svmType = "POLY_";
        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_" + svmType + degree, "svm sheet");
    }

    @Test
    public void SVMPOLY5() throws SQLException, IOException, ClassNotFoundException {
        int degree = 5;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree);
        SymptomsResult symptomsResult = new SymptomsResult();
        String svmType = "POLY_";
        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_" + svmType + degree, "svm sheet");
    }

    @Test
    public void SVMPOLY6() throws SQLException, IOException, ClassNotFoundException {
        int degree = 6;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree);
        SymptomsResult symptomsResult = new SymptomsResult();
        String svmType = "POLY_";
        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_" + svmType + degree, "svm sheet");
    }

    @Test
    public void SVMPOLY7() throws SQLException, IOException, ClassNotFoundException {
        int degree = 7;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree);
        SymptomsResult symptomsResult = new SymptomsResult();
        String svmType = "POLY_";
        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_" + svmType + degree, "svm sheet");
    }

    @Test
    public void SVMPOLY8() throws SQLException, IOException, ClassNotFoundException {
        int degree = 8;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree);
        SymptomsResult symptomsResult = new SymptomsResult();
        String svmType = "POLY_";
        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_" + svmType + degree, "svm sheet");
    }

    @Test
    public void SVMPOLY9() throws SQLException, IOException, ClassNotFoundException {
        int degree = 9;
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree);
        SymptomsResult symptomsResult = new SymptomsResult();
        String svmType = "POLY_";
        symptomsResult.printFullResultInOneExcel(conformalPrediction, "SVM_" + svmType + degree, "svm sheet");
    }

}
