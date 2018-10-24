package trepan

import org.apache.spark.sql.DataFrame
import scala.math.log10
import scala.collection.mutable.ListBuffer

/** Trepan tree Node
 *
 * @constructor Creates a nodes with a dataset, a nodetype (either Leaf or Internal), a reference to the oracle and
 *              the constraints imposed by the trepan estimator (minSample, leafPurity, constraint)
 * @param data Training examples that reach this node in the TREPAN decision tree
 * @param nodeType Object representing if node is a Leaf or Internal (non-Leaf)
 * @param treeReach Height of the node in the tree
 * @param minSample Minimum sample needed for a node in the estimated decision tree
 * @param oracle an Oracle class that represents a machine learning model used to obtain labels for query instances
 * @param constraint
 * @param leafPurity Percentage inherited by the TREPAN estimator of classes on one node to consider itself "pure"
 */
case class Node(data: DataFrame, var nodeType: NodeType, treeReach: Int, minSample: Long, oracle: Oracle, constraint: Constraint, leafPurity: Double) {

  /** get column types from the DataFrame schema */
  val columnTypes: SchemaType = new SchemaType(data.schema)

  /** Check if more instances on data are needed to fulfill with minSample required in a node */
  val numToBeCreated: Long = minSample - data.count
  if (numToBeCreated > 0) {
    /**
      * As the size of the training examples (data) is not enough, queryInstances need to be created from data's
      * column distributions and asking the oracle for their label
      */
    var distributions: List[TrepanEstimator] = List.empty[TrepanEstimator]
    drawInstances(distributions, numToBeCreated, data.schema.fields.length: Long)
  }

  /**
    * SparkSQL allows to perform SQL-like queries on the DataFrame, this query obtains the frequency of labels in the
    * sum of data and queryInstances
    */
  private val labelCount: DataFrame = data.groupBy("label").count().orderBy("count")

  /** Creates new instances (rows) from the training data's column distribution
    *
    * @param estimators
    * @param numToBeCreated Amount of queryInstances to create
    * @param numOfFeatures Amount of attributes (columns)
    * @return a set of queryInstances in the form of a DataFrame
    */
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

  def constructTest(leafPurity: Double): Unit = {
    var proportion: Double = 1
    if (data.count > 0) {
      proportion = labelCount.first().getLong(1) / data.count
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

/** Object used to identify a node type
  *
  * A Lead node is a node with no children, any node that is not a Leaf, will be considered an Internal node
  */
trait NodeType
case object Leaf extends NodeType
case object Internal extends NodeType
