package trepan

import org.apache.spark.sql.DataFrame

import scala.collection.immutable.Queue

case class Trepan(oracle: Oracle, dataSet: DataFrame, minSample: Int, beamWidth: Integer) {
  /*
  Returns the root node of a decision tree
   */
  def makeTree(): Node = {
    var queue = Queue[QueueNode]()
    val root = Node(dataSet, Leaf, 1)
    // Create more instances (= rows) if they are needed
    root.drawSample(minSample, oracle)

    queue = queue enqueue QueueNode(root, dataSet, Constraint())


    oracle.getSamples(dataSet, dataSet, minSample)
//    while (queue.nonEmpty && treeSize < beamWidth) {
//      val tuple = queue.dequeue._1
//      val examplesN = tuple(1)
//      val constraintsN = tuple(2)

      // candidate split
      // use features to build set of candidate splits
      // use examplesN and calls to Oracle(contraintsN) to evaluate splits


//    }
    queue.dequeue._1.getNode()
  }
}

case class Constraint() {

}
