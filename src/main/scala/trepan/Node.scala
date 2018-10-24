package trepan

import org.apache.spark.sql.DataFrame

import scala.math.log10
import scala.collection.mutable.ListBuffer
/*
 * Trepan tree Node
 */
case class Node(data: DataFrame, var nodeType: NodeType, treeReach: Int, minSample: Long, oracle: Oracle, constraint: Constraint, leafPurity: Double) {

  // get column types from schema
  val columnTypes: SchemaType = new SchemaType(data.schema)

  // Check if more instances on data are needed
  val numToBeCreated: Long = minSample - dataSize()
  if (numToBeCreated > 0) {
    var distributions: List[TrepanEstimator] = List.empty[TrepanEstimator]
    drawInstances(distributions, numToBeCreated, data.schema.fields.length: Long)
  }

  // Assign node classLabel
  private val labelCount: DataFrame = data.groupBy("label").count().orderBy("count")

  private def drawInstances(estimators: List[TrepanEstimator], numToBeCreated: Long, numOfFeatures: Long): Option[DataFrame] = {
    var instances = ListBuffer()
    var num = 0
    var feat = 0
    for (num <- 1 to numToBeCreated.toInt) {
      var newInstance = ListBuffer()
      for (feat <- 1 to numOfFeatures.toInt) {
        estimators(feat)
//        newInstance += estimators(feat).newValue()
      }

//      instances += (newInstance.toList)
    }
    Some(data)
  }

  def dataSize(): Long = {
    data.count
  }

  def constructTest(leafPurity: Double): Unit = {
    var proportion: Double = 1
    if (dataSize() > 0) {
      proportion = labelCount.first().getLong(1) / dataSize()
    }

    if (proportion > leafPurity) nodeType = Leaf
    else {
      // pick feature with best infoGain
      // per each attribute (column), calculate infoGain
      val columns = data.columns
      val infoGains = columns.foreach(columnName => {
        infoGainOfAttribute(data, columnName)
      })

      // pick attribute with highest infoGain

      // if infoGain == 0 => make leaf
      // else splitNode using that attribute
    }
  }

  // equivalent of computeInfoGain
  private def infoGainOfAttribute(instances: DataFrame, columnName: String): Double = {
    var infoGain = getEntropy(instances)
    splitData(instances, columnName, 0)
    infoGain
  }

  private def getEntropy(instances: DataFrame): Double = {
    val classCounts = instances.groupBy("label").count().orderBy("count")
    var entropy: Double = 0
//    classCounts.map( row => {
//      val classCount = row.getInt(1)
//      if (classCount > 0) entropy -=  classCount * classCount / (log10(classCount) / log10(2))
//    })
    val numberOfInstances = instances.count()
    entropy /= numberOfInstances
    entropy + (log10(numberOfInstances) / log10(2))
  }

  private def splitData(instances: DataFrame, columnName: String, value: Integer) : List[DataFrame] = {
    List(instances)
  }
}

trait NodeType
case object Leaf extends NodeType
case object Internal extends NodeType
