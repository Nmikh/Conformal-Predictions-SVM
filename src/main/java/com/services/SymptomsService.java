package com.services;

import com.DAO.SimptomsDAO;
import com.models.DataObject;
import com.models.LibSVMConfidence;
import com.models.Prediction;
import libsvm.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SymptomsService {
    SimptomsDAO simptomsDAO;
    double alphaPositive;
    double alphaNegative;

    public SymptomsService() throws SQLException, IOException, ClassNotFoundException {
        this.simptomsDAO = new SimptomsDAO();
    }

    public ArrayList<Prediction> getConformalPrediction(String fileName) throws IOException, SQLException {
        //read data from file
        //read the data from file and put it in the train module
        FileReader input = new FileReader(fileName);
        BufferedReader bufRead = new BufferedReader(input);
        String line = null;
        double pPositive;
        double pNegative;

        ArrayList<DataObject> dataSet = (ArrayList) simptomsDAO.findAllObjects();
        ArrayList<DataObject> testDataSet = new ArrayList<DataObject>();

        while ((line = bufRead.readLine()) != null) {
            String[] array = line.split(",");
            DataObject o = new DataObject(
                    Integer.parseInt(array[2]),
                    Integer.parseInt(array[3]),
                    Integer.parseInt(array[4]),
                    Integer.parseInt(array[5]),
                    Integer.parseInt(array[6]),
                    Integer.parseInt(array[7]),
                    Integer.parseInt(array[8]),
                    Integer.parseInt(array[9]),
                    Integer.parseInt(array[10]),
                    Integer.parseInt(array[1])
            );
            testDataSet.add(o);
        }

        ArrayList<Prediction> predictions = new ArrayList<Prediction>();
        svm_model svmModelPrediction = this.svmTrainConformalPredict(dataSet);
        List<LibSVMConfidence> svmPredictions = this.evaluateAllInstances(svmModelPrediction, testDataSet);

        for (int i = 0; i < testDataSet.size(); i++) {
            Prediction prediction = new Prediction();
            prediction.setRealClass(testDataSet.get(i).getCategory());

            ////////////////////////////////////
            //           pPositive           //
            //////////////////////////////////
            pPositive = pPositiveCalc(dataSet, testDataSet, i, prediction);

            ////////////////////////////////////
            //           pNegative           //
            //////////////////////////////////
            pNegative = pNegativeCalc(dataSet, testDataSet, i, prediction);


            //set prediction
            predictions.add(setPrediction(prediction, pPositive, pNegative, i, testDataSet.get(i), svmPredictions.get(i)));
        }
        return predictions;
    }

    public double pPositiveCalc(ArrayList<DataObject> dataSet, ArrayList<DataObject> testDataSet, int i, Prediction prediction) throws SQLException {
        testDataSet.get(i).setCategory(1);
        dataSet.add(testDataSet.get(i));
        svm_model svmModelPositive = this.svmTrainConformalPredict(dataSet);
        int positiveAlphaIndex = -1; // index of the checking object in model
        this.alphaPositive = 0; // alpha of the checking object
        int countAlphasPositive = 0; // positive alphas in train dataSet
        ArrayList<Double> positiveSupportVectorsAlphas = new ArrayList<Double>(); // alphas of support vectors where y=1
        double pPositive = 0;
        //get positive alphas in train dataSet
        for (int k = 0; k < dataSet.size(); k++) {
            if (dataSet.get(k).getCategory() == 1) {
                countAlphasPositive++;
            }
        }
        //get index of the checking object in model
        //if positiveAlphaIndex == -1 - alpha of the checking object = 0
        for (int j = 0; j < svmModelPositive.sv_indices.length; j++) {
            if (svmModelPositive.sv_indices[j] == dataSet.size()) {
                positiveAlphaIndex = j;
                break;
            }
        }
        //if positiveAlphaIndex != -1 - alpha of the checking object - support vector
        //svmModelPositive.sv_coef - massive of alpha * y
        if (positiveAlphaIndex != -1) {
            this.alphaPositive = Math.abs(svmModelPositive.sv_coef[0][positiveAlphaIndex]);
        }

        ArrayList<Double> supportVectorsAlphas = new ArrayList<Double>();
        //found alphas of support vectors where y=1
        for (int j = 0; j < svmModelPositive.sv_indices.length; j++) {
            if (svmModelPositive.sv_coef[0][j] > 0) {
                positiveSupportVectorsAlphas.add(svmModelPositive.sv_coef[0][j]);
            }
            supportVectorsAlphas.add(Math.abs(svmModelPositive.sv_coef[0][j]));
        }

        prediction.setPositiveSupportVectorsAlphas(supportVectorsAlphas);

        // if alpha of the checking object = 0
        if (this.alphaPositive == 0) {
            pPositive =
                    (double) dataSet.size()
                            /
                            (double) dataSet.size();
        } else {
            int countPPositive = 0;
            for (int j = 0; j < supportVectorsAlphas.size(); j++) {
                if (this.alphaPositive <= supportVectorsAlphas.get(j)) {
                    countPPositive++;
                }
            }
            pPositive = (double) countPPositive
                    /
                    (double) dataSet.size();

        }
        dataSet.remove(dataSet.size() - 1);
        return pPositive;
    }

    public double pNegativeCalc(ArrayList<DataObject> dataSet, ArrayList<DataObject> testDataSet, int i, Prediction prediction) throws SQLException {
        testDataSet.get(i).setCategory(-1);
        dataSet.add(testDataSet.get(i));
        svm_model svmModelNegative = this.svmTrainConformalPredict(dataSet);

        int negativeAlphaIndex = -1; // index of the checking object in model
        this.alphaNegative = 0; // alpha of the checking object
        int countAlphasNegative = 0; // negative alphas in train dataSet
        ArrayList<Double> negativeSupportVectorsAlphas = new ArrayList<Double>(); // alphas of support vectors where y=-1
        double pNegative = 0;
        //get negative alphas in train dataSet
        for (int k = 0; k < dataSet.size(); k++) {
            if (dataSet.get(k).getCategory() == -1) {
                countAlphasNegative++;
            }
        }
        //get index of the checking object in model
        //if negativeAlphaIndex == -1 - alpha of the checking object = 0
        for (int j = 0; j < svmModelNegative.sv_indices.length; j++) {
            if (svmModelNegative.sv_indices[j] == dataSet.size()) {
                negativeAlphaIndex = j;
                break;
            }
        }
        //if negativeAlphaIndex != -1 - alpha of the checking object - support vector
        //svmModelNegative.sv_coef - massive of alpha * y
        if (negativeAlphaIndex != -1) {
            this.alphaNegative = Math.abs(svmModelNegative.sv_coef[0][negativeAlphaIndex]);
        }

        ArrayList<Double> supportVectorsAlphas = new ArrayList<Double>();
        //found alphas of support vectors where y=-1
        for (int j = 0; j < svmModelNegative.sv_indices.length; j++) {
//            if (svmModelNegative.sv_coef[0][j] > 0) {
//                negativeSupportVectorsAlphas.add(svmModelNegative.sv_coef[0][j]);
//            }
            if (svmModelNegative.sv_coef[0][j] < 0) {
                negativeSupportVectorsAlphas.add(Math.abs(svmModelNegative.sv_coef[0][j]));
            }
            supportVectorsAlphas.add(Math.abs(svmModelNegative.sv_coef[0][j]));
        }

        prediction.setNegativeSupportVectorsAlphas(supportVectorsAlphas);

        // if alpha of the checking object = 0
        if (this.alphaNegative == 0) {
            pNegative =
                    (double) dataSet.size()
                            /
                            (double) dataSet.size();
        } else {
            int countPNegative = 0;
            for (int j = 0; j < supportVectorsAlphas.size(); j++) {
                if (this.alphaNegative <= supportVectorsAlphas.get(j)) {
                    countPNegative++;
                }
            }
            pNegative = (double) countPNegative
                    /
                    (double) dataSet.size();
        }
        dataSet.remove(dataSet.size() - 1);
        return pNegative;
    }

    public Prediction setPrediction(Prediction prediction, double pPositive, double pNegative, int i,
                                    DataObject dataObject, LibSVMConfidence libSVMConfidence) {
        double confidence = 0;
        double credibility = 0;
        prediction.setpPositive(pPositive);
        prediction.setpNegative(pNegative);
        if (pPositive > pNegative) {
            prediction.setStatus("pPositive > pNegative");
            prediction.setPredictClass(1);
            confidence = 1 - pNegative;
            credibility = pPositive;
        } else if (pNegative > pPositive) {
            prediction.setPredictClass(-1);
            prediction.setStatus("pNegative > pPositive");
            confidence = 1 - pPositive;
            credibility = pNegative;
        } else if (pPositive==pNegative){
            prediction.setStatus("pNegative = pPositive");
            prediction.setPredictClass(1);
            confidence = 1-pNegative;
            credibility = pPositive;

        }
        prediction.setId(i);
        prediction.setConfidence(confidence);
        prediction.setCredibility(credibility);
        prediction.setAlphaPositive(this.alphaPositive);
        prediction.setAlphaNegative(this.alphaNegative);
        prediction.setDataObject(dataObject);
        prediction.setPredictClassSVM(libSVMConfidence.getPrediction());
        prediction.setConfidencePositiveClassSVM(libSVMConfidence.getPositiveConfidence());
        prediction.setConfidenceNegativeClassSVM(libSVMConfidence.getNegativeConfidence());
        return prediction;
    }

    public svm_model svmTrainConformalPredict(ArrayList<DataObject> dataset) throws SQLException {
        int recordSize = dataset.size();

        double nodeValues[][] = new double[recordSize][]; //jagged array used to store values
        int nodeIndexes[][] = new int[recordSize][];//jagged array used to store node indexes
        double nodeClassLabels[] = new double[recordSize];//store class lavels

        //Now store data values
        for (int i = 0; i < dataset.size(); i++) {
            int dataClass;
            if (dataset.get(i).getCategory() == 1)
                dataClass = 1;
            else
                dataClass = -1;
            nodeClassLabels[i] = dataClass;

            LinkedList<Integer> listIndx = new LinkedList<Integer>();
            LinkedList<Double> listVal = new LinkedList<Double>();

            listIndx.add(1);
            listVal.add((double) dataset.get(i).getClumpThickness());

            listIndx.add(2);
            listVal.add((double) dataset.get(i).getUniformityOfCellSize());

            listIndx.add(3);
            listVal.add((double) dataset.get(i).getUniformityOfCellShape());

            listIndx.add(4);
            listVal.add((double) dataset.get(i).getMarginalAdhesion());

            listIndx.add(5);
            listVal.add((double) dataset.get(i).getSingleEpithelialCellSize());

            listIndx.add(6);
            listVal.add((double) dataset.get(i).getBareNuclei());

            listIndx.add(7);
            listVal.add((double) dataset.get(i).getBlandChromatin());

            listIndx.add(8);
            listVal.add((double) dataset.get(i).getNormalNucleoli());

            listIndx.add(9);
            listVal.add((double) dataset.get(i).getMitoses());

            if (listVal.size() > 0) {
                nodeValues[i] = new double[listVal.size()];
                nodeIndexes[i] = new int[listIndx.size()];
            }
            for (int m = 0; m < listVal.size(); m++) {
                nodeIndexes[i][m] = listIndx.get(m);
                nodeValues[i][m] = listVal.get(m);
            }
        }

        svm_problem prob = new svm_problem();
        int dataCount = recordSize;
        prob.y = new double[dataCount];
        prob.l = dataCount;
        prob.x = new svm_node[dataCount][];

        for (int i = 0; i < dataCount; i++) {
            prob.y[i] = nodeClassLabels[i];
            double[] values = nodeValues[i];
            int[] indexes = nodeIndexes[i];
            prob.x[i] = new svm_node[values.length];
            for (int j = 0; j < values.length; j++) {
                svm_node node = new svm_node();
                node.index = indexes[j];
                node.value = values[j];
                prob.x[i][j] = node;
            }
        }

        svm_parameter param = new svm_parameter();
        param.probability = 1;
        param.gamma = 0.5;
        param.nu = 0.5;
        param.C = 1;
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.POLY;
        param.cache_size = 20000;
        param.eps = 0.001;
        param.degree = 2;

        svm_model model = svm.svm_train(prob, param);

        return model;
    }


    //write code to test all instances from given file
    public List<LibSVMConfidence> evaluateAllInstances(svm_model model, ArrayList<DataObject> testDataSet) throws IOException {

        List<LibSVMConfidence> libSVMPredictions = new ArrayList<LibSVMConfidence>();

        int recordSize = testDataSet.size();
        System.out.println("DataSet Size: " + testDataSet.size());

        double nodeValues[][] = new double[recordSize][]; //jagged array used to store values
        int nodeIndexes[][] = new int[recordSize][];//jagged array used to store node indexes

        for (int i = 0; i < testDataSet.size(); i++) {

            LinkedList<Integer> listIndx = new LinkedList<Integer>();
            LinkedList<Double> listVal = new LinkedList<Double>();

            listIndx.add(1);
            listVal.add((double) testDataSet.get(i).getClumpThickness());

            listIndx.add(2);
            listVal.add((double) testDataSet.get(i).getUniformityOfCellSize());

            listIndx.add(3);
            listVal.add((double) testDataSet.get(i).getUniformityOfCellShape());

            listIndx.add(4);
            listVal.add((double) testDataSet.get(i).getMarginalAdhesion());

            listIndx.add(5);
            listVal.add((double) testDataSet.get(i).getSingleEpithelialCellSize());

            listIndx.add(6);
            listVal.add((double) testDataSet.get(i).getBareNuclei());

            listIndx.add(7);
            listVal.add((double) testDataSet.get(i).getBlandChromatin());

            listIndx.add(8);
            listVal.add((double) testDataSet.get(i).getNormalNucleoli());

            listIndx.add(9);
            listVal.add((double) testDataSet.get(i).getMitoses());

            if (listVal.size() > 0) {
                nodeValues[i] = new double[listVal.size()];
                nodeIndexes[i] = new int[listIndx.size()];
            }
            for (int m = 0; m < listVal.size(); m++) {
                nodeIndexes[i][m] = listIndx.get(m);
                nodeValues[i][m] = listVal.get(m);
            }
        }

        int positive = 0;
        int negative = 0;
        for (int i = 0; i < recordSize; i++) {
            int tmpIndexes[] = nodeIndexes[i];
            double tmpValues[] = nodeValues[i];
            LibSVMConfidence v = evaluateSingleInstance(tmpIndexes, tmpValues, model);
            libSVMPredictions.add(v);
        }
        return libSVMPredictions;
    }

    //write the code to test single feature each time by using SVM
    public LibSVMConfidence evaluateSingleInstance(int[] indexes, double[] values, svm_model model) {
        svm_node[] nodes = new svm_node[values.length];
        for (int i = 0; i < values.length; i++) {
            svm_node node = new svm_node();
            node.index = indexes[i];
            node.value = values[i];
            nodes[i] = node;
        }

        int totalClasses = svm.svm_get_nr_class(model);
        int[] labels = new int[totalClasses];
        svm.svm_get_labels(model, labels);

        double[] probEstimates = new double[totalClasses];
        double v = svm.svm_predict_probability(model, nodes, probEstimates);
        LibSVMConfidence libSVMConfidence = new LibSVMConfidence(probEstimates[0], probEstimates[1], v);
        return libSVMConfidence;
    }
}
