package trepan

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DecimalType, IntegerType, StringType, StructField}

import scala.collection.mutable.ListBuffer
/*
 * Trepan tree Node
 */
case class Node(data: DataFrame, nodeType: NodeType, treeReach: Int) {

  // Use data to construct a model Mr of the distribution of instances covered by this node
  def drawSample(minSample: Long, oracle: Oracle): Option[DataFrame] = {
    val numToBeCreated: Long = minSample - dataSize()
    if (numToBeCreated > 0) {
      // TODO: Continue from here SEBIIIII
      val estimators = data.schema.fields.map {
        case StructField(name, IntegerType, _, _) => getNominalEstimator(data.groupBy(data.col(name)).count())
        case StructField(name, StringType, _, _) => getNominalEstimator(data.groupBy(data.col(name)).count())
        case StructField(name, DecimalType(), _, _) => getDiscreteEstimator(data.groupBy(data.col(name)).count())
      }
      drawInstances(estimators, numToBeCreated, data.schema.fields.length: Long)
    } else {
      None
    }
  }

  private def drawInstances(estimators: Array[TrepanEstimator], numToBeCreated: Long, numOfFeatures: Long): Option[DataFrame] = {
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

  def getNominalEstimator(col: DataFrame): TrepanEstimator = {
    NominalTrepanEstimator(col)
  }

  def getDiscreteEstimator(col: DataFrame): TrepanEstimator = {
    DiscreteTrepanEstimator(col)
  }
}

trait NodeType
case object Leaf extends NodeType
case object Internal extends NodeType

case class QueueNode(node: Node, trainingExamples: DataFrame, constraint: Constraint) {
  def getNode(): Node = {
    node
  }
}