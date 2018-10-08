package trepan;

import org.apache.spark.ml.PredictionModel;
import org.apache.spark.mllib.stat.KernelDensity;
import org.apache.spark.sql.Dataset;

import java.util.List;

class Oracle {
    private PredictionModel model;
    private Dataset<Object> dataSet;
    private KernelDensity distributions;

    Oracle(Dataset<Object> dataSet, PredictionModel model) {
        this.dataSet = dataSet;
        this.model = model;
        this.distributions = new KernelDensity().setSample(this.dataSet.rdd()).setBandwidth(3.0);
    }

    List<Double> getLabels(Dataset set) {
        return set.javaRDD().map(row -> model.predict(row)).collect();
    }

}
