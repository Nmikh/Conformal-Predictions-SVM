//package java;

import com.SymptomsResult;
import com.services.SymptomsService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Admin on 22.12.2018.
 */
public class testSVMPOLYEPS {
    SymptomsService symptomsService;
    private String dirName;

    @Before
    public void init() throws SQLException, IOException, ClassNotFoundException {
        symptomsService = new SymptomsService();
        dirName = "SVM_matrix_";
    }

    @Test
    public void SVMPOLY2EPS() throws IOException, SQLException {
        int degree = 2;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

    @Test
    public void SVMPOLY3EPS() throws IOException, SQLException {
        int degree = 3;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

    @Test
    public void SVMPOLY4EPS() throws IOException, SQLException {
        int degree = 4;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

    @Test
    public void SVMPOLY5EPS() throws IOException, SQLException {
        int degree = 5;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

    @Test
    public void SVMPOLY6EPS() throws IOException, SQLException {
        int degree = 6;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }


    @Test
    public void SVMPOLY7EPS() throws IOException, SQLException {
        int degree = 7;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

    @Test
    public void SVMPOLY8EPS() throws IOException, SQLException {
        int degree = 8;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

    @Test
    public void SVMPOLY9EPS() throws IOException, SQLException {
        int degree = 9;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

    @Test
    public void SVMRBF9EPS() throws IOException, SQLException {
        int degree = 3;
        String svmType = "RBF";
        SymptomsResult symptomsResult = new SymptomsResult();
        symptomsResult.printToExcelMatrixRPDifEpsTestMethod(symptomsService, "testNew.txt", dirName + svmType + "_" + degree, degree, svmType);
    }

}
