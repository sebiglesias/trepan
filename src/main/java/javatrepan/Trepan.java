package javatrepan;

import org.apache.spark.ml.PredictionModel;
import org.apache.spark.sql.Dataset;
import javatrepan.Oracle;

import java.util.AbstractMap;
import java.util.List;
import java.util.PriorityQueue;


public class Trepan {
    private Dataset<Object> trainingDataSet;
    private Dataset<Object> testingDataSet;
    private Oracle oracle;

    public Trepan(Dataset<Object> trainingDataSet, Dataset<Object> testingDataSet, PredictionModel model) {
        this.testingDataSet = testingDataSet;
        this.trainingDataSet = trainingDataSet;
        this.oracle = new Oracle(trainingDataSet, model);
    }

    public void makeTree() {
        // Obtain the trainingDataSet's labels
        final List<Double> labels = this.oracle.getLabels(trainingDataSet);
        // this is a tuple/pair in java that represents the trainingDataset and their respective labels
        final AbstractMap.SimpleEntry<Dataset, List<Double>> trainingSet = new AbstractMap.SimpleEntry<>(trainingDataSet, labels);
        // The columns.length of the trainingDataSet
        final int dimensions = trainingDataSet.columns().length;
        final PriorityQueue<Node> sortedNodeQueue = new PriorityQueue<>();
        Node root = new Node(trainingSet, dimensions);
        root.setLeaf(false);
        sortedNodeQueue.add(root);

    }
}
