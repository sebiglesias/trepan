import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import trepan.Trepan;

public class TrepanApp {
    public static void main(String[] args) {
        final String path = "src/main/resources/iris.csv";

        final SparkSession spark = SparkSession.builder().master("local").appName("Trepan").getOrCreate();

        final Dataset<Row> dataset = spark.read().format("csv").option("inferSchema", "true").load(path);
        dataset.show();
        // splits[1] = train; splits[0] = test;
        final Dataset<Row>[] split = dataset.randomSplit(new double[]{0.7, 0.3});
        // TODO: Load trained model
        final MultilayerPerceptronClassifier estimator = new MultilayerPerceptronClassifier();

        final Trepan trepan = new Trepan(split[0], split[1], estimator);
    }
}
