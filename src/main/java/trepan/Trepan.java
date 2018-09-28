package trepan;

import org.apache.spark.ml.Estimator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Row;

import java.nio.file.Path;


public class Trepan {
    private Dataset trainingDataSet;
    private Dataset testingDataSet;
    private Oracle oracle;

    public Trepan(Dataset<Row> trainingDataSet, Dataset<Row> testingDataSet, Estimator estimator) {
        this.testingDataSet = testingDataSet;
        this.trainingDataSet = trainingDataSet;
        this.oracle = new Oracle(trainingDataSet, estimator);
    }


}
