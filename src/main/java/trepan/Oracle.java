package trepan;

import org.apache.spark.ml.Estimator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Oracle {
    private Estimator estimator;
    private Dataset<Row> dataSet;

    Oracle(Dataset<Row> dataSet, Estimator estimator) {
        this.dataSet = dataSet;
        this.estimator = estimator;
    }

}
