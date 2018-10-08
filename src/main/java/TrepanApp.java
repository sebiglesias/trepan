import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import trepan.Trepan;

public class TrepanApp {
    public static void main(String[] args) {
        final String path = "src/main/resources/breastcancer.data";

        final SparkSession spark = SparkSession.builder().master("local").appName("Trepan").getOrCreate();

        final Dataset dataSet = spark.read().format("libsvm").option("inferSchema", "true").load(path);
        dataSet.show();
        // splits[1] = train; splits[0] = test;
        final Dataset[] split = dataSet.randomSplit(new double[]{0.8, 0.2}, 12345L);

        // TODO: Load trained model

        // TBR: Train a NN
        int[] layers = new int[] { 2, 3, 5, 5 };
        final MultilayerPerceptronClassifier estimator = new MultilayerPerceptronClassifier()
                .setLayers(layers)
                .setTol(1E-4) //Set the convergence tolerance of iterations. Smaller value will lead to higher accuracy with the cost of more iterations. Default is 1E-4.
                .setBlockSize(128) // Sets the value of param [[blockSize]], where, the default is 128.
                .setSeed(12345L) // Set the seed for weights initialization if weights are not set
                .setMaxIter(100); // Set the maximum number of iterations. Default is 100.

        final MultilayerPerceptronClassificationModel fit = estimator.fit(split[0]);
        //


        final Trepan trepan = new Trepan(split[0], split[1], fit);
    }
}
