package trepan

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.DataFrame

case class Oracle(model: PipelineModel) {
  def getSamples(trainingSet: DataFrame, featureSet: DataFrame, minSample: Integer): DataFrame = {
//    trainingSet.show()
//    trainingSet.rdd.map(row => row.)

    featureSet
  }
}
