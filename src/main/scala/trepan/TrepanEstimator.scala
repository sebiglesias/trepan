package trepan

import org.apache.spark.sql.DataFrame

trait TrepanEstimator {
  def newValue(): Any
  def getProbability(element: Any): Double
}

case class NominalTrepanEstimator(column: DataFrame) extends TrepanEstimator {

  var frequency: collection.mutable.Map[Any, Int] = collection.mutable.Map[Any, Int]().withDefaultValue(0)
  var accumulatedCounts: collection.mutable.Map[Any, Double] = collection.mutable.Map[Any, Double]().withDefaultValue(0)
  var numOfValues: Int = 0

  // fill frequency and count
  column.foreach {
    u =>
      frequency.update(u, frequency(u) + 1)
      numOfValues += 1
  }

  var sum: Double = 0
  // Accumulate probabilities
  frequency foreach( u => {
    sum = sum + getProbability(u._1)
    accumulatedCounts.update(u._1, sum)
  })
  // Sum of probabilities = 1
  accumulatedCounts.update(accumulatedCounts.last._1, 1)

  def newValue(): Any = {
    // If column is empty return None
    if (numOfValues == 0 ) return None
    // Return the first number that is bigger than a random num, smaller than 1
    val r = scala.util.Random.nextDouble()
    accumulatedCounts.filter(elem => elem._2 > r).head._1
  }

  def getProbability(element: Any): Double = {
    if (numOfValues == 0) return 0
    frequency(element) / numOfValues
  }
}

case class DiscreteTrepanEstimator(column: DataFrame) extends TrepanEstimator {

  var numOfValues: Int = 0



  override def newValue(): Any = {
    // If column is empty return None
    if (numOfValues == 0) return None


  }

  override def getProbability(element: Any): Double = ???
}
