package trepan

import org.apache.spark.sql.DataFrame

import scala.collection.immutable.Queue

/** Trepan Estimator class that resolves a decision tree out of another machine learning model
 * @constructor create a new Trepan estimator with a previously trained classification model represented by an
 *              oracle, a dataset and constraints for creating the tree
 * @param oracle an Oracle class that represents a machine learning model
 * @param dataSet the dataSet used for training the previous machine learning classification model
 * @param minSample Minimum sample needed for a node in the estimated decision tree
 * @param beamWidth
 * @param leafPurity Percentage of classes on one node to consider itself "pure"
 */
case class Trepan(oracle: Oracle, dataSet: DataFrame, minSample: Int, beamWidth: Int, leafPurity: Double) {

  /** Returns the root node of a decision tree
   *
   * @return The root node of the decision tree
   */
  def makeTree(): Node = {
    var queue = Queue[Node]()
    // Create more instances (= rows) if they are needed
    val root = Node(dataSet, Leaf, 1, minSample, oracle, Constraint(), leafPurity)
    queue = queue enqueue root

    // current tree size
    var treeSize: Int = 0

    // current amount of nodes
    var nodeCount: Int = 0

    while (queue.nonEmpty && treeSize < beamWidth) {
      val tuple = queue.dequeue
      val node: Node = tuple._1
      queue = tuple._2
      nodeCount+=1
      // split the node
      node.constructTest(leafPurity)
//      val examplesN = tuple(1)
//      val constraintsN = tuple(2)

      // candidate split
      // use features to build set of candidate splits
      // use examplesN and calls to Oracle(contraintsN) to evaluate splits


    }
    root
  }
}

case class Constraint() {

}
