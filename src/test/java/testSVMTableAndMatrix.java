import com.SymptomsResult;
import com.models.Prediction;
import com.services.SymptomsService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Admin on 12.01.2019.
 */
public class testSVMTableAndMatrix {
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
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMPOLY3EPS() throws IOException, SQLException {
        int degree = 3;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMPOLY4EPS() throws IOException, SQLException {
        int degree = 4;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMPOLY5EPS() throws IOException, SQLException {
        int degree = 5;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMPOLY6EPS() throws IOException, SQLException {
        int degree = 6;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMPOLY7EPS() throws IOException, SQLException {
        int degree = 7;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMPOLY8EPS() throws IOException, SQLException {
        int degree = 8;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMPOLY9EPS() throws IOException, SQLException {
        int degree = 9;
        String svmType = "POLY";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

    @Test
    public void SVMRBF9EPS() throws IOException, SQLException {
        int degree = 3;
        String svmType = "RBF";
        SymptomsResult symptomsResult = new SymptomsResult();
        ArrayList<Prediction> conformalPrediction = symptomsService.getConformalPrediction("testNew.txt", degree, 0.001, svmType);
        symptomsResult.printTableAndMatrix(conformalPrediction, dirName + svmType + "_" + degree);
    }

}
