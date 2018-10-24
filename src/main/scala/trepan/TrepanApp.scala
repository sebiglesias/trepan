package trepan

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.udf
import org.apache.spark.ml.linalg.{Vector, Vectors}

/** Main App that obtains a TREPAN tree out of a trained neural network */
object TrepanApp extends App {
  /** Create a spark session with WARN logLevel */
  val spark = SparkSession
    .builder()
    .appName("Trepan Scala App")
    .master("local")
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")

  /** Read a csv with the data*/
  val csv: DataFrame = spark
    .read
    .format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/Iris.csv")

  // Transform csv
  val toVec4 = udf[Vector, Double, Double, Double, Int] { (a, b, c, d) =>
    Vectors.dense(a, b, c, d)
  }

  val encodeLabel = udf[Int, String] {
    case "Iris-setosa" => 0
    case "Iris-versicolor" => 1
    case "Iris-virginica" => 2
  }

  private val dataSet: DataFrame = csv
    .withColumn(
      "features",
      toVec4(
        csv("sepal-length"),
        csv("sepal-width"),
        csv("petal-length"),
        csv("petal-width")
      )
    ).withColumn("label", encodeLabel(csv("class"))).select("features", "label").toDF()

  // Show first 20 entries in dataset
  dataSet.show()

  // get training and test split
  private val split: Array[Dataset[Row]] = dataSet.randomSplit(Array(0.8, 0.2), seed = 12345L)

  // Load trained model or generate one out of csv
  val classifier: MultilayerPerceptronClassifier = new MultilayerPerceptronClassifier()
    .setLayers(Array(2, 3, 5, 5))
    .setTol(1E-4)
    .setBlockSize(128)
    .setSeed(12345L)
    .setMaxIter(100)

  private val pipeline: Pipeline = new Pipeline().setStages(Array(classifier))

  private val model: PipelineModel = pipeline.fit(split(0))

  // Trepan algorithm
  private val frame: DataFrame = csv.withColumn("label", encodeLabel(csv("class"))).select("sepal-length", "sepal-width", "petal-length", "petal-width", "label")
  frame.printSchema()
  private val trepan = Trepan(Oracle(model), frame, 10, 10, 0.95)

  private val tree: Node = trepan.makeTree()

}
