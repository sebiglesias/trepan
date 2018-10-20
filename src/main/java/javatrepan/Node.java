package javatrepan;

import org.apache.spark.sql.Dataset;

import java.util.*;

public class Node {

    private AbstractMap.SimpleEntry<Dataset, List<Double>> examples;
    private int totalSize;
    private boolean isLeaf;
    private Node left;
    private Node right;
    private SplitRule splitRule;
    private long examplesQuantity;
    private float priority;
    private double dominantClass;
    private double missClassified;
    private float fidelity;
    private float reach;


    public Node(AbstractMap.SimpleEntry<Dataset, List<Double>> examples, int totalSize) {
        this.isLeaf = true;
        this.left = null;
        this.right = null;
        this.splitRule = null;
        this.examples = examples;
        this.totalSize = totalSize;
        this.examplesQuantity = examples.getKey().count();
        if (examplesQuantity == 0) {
            this.priority = 0;
        } else {
            this.dominantClass = getDominantClass();
            this.missClassified = getMissClassified();
            this.fidelity = 1 - ((float) this.missClassified)/examplesQuantity;
            this.reach = ((float) examplesQuantity) / totalSize;
            this.priority = (-1) * this.reach * ( 1 - this.fidelity);
        }
    }

    private Double getDominantClass() {
        final List<Double> labels = this.examples.getValue();
        Map<Double, Integer> map = new HashMap<>();

        for (Double t : labels) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<Double, Integer> max = null;

        for (Map.Entry<Double, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }
        assert max != null;
        return max.getKey();
    }

    private int getMissClassified() {
        final List<Double> labels = this.examples.getValue();
        int count = 0;
        for (Double label : labels) {
            if (label != this.dominantClass) count++;
        }
        return count;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
