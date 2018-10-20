package trepan

case class Feature(name: String, featureType: FeatureType) {

}

trait FeatureType

case object Nominal extends FeatureType
case object Continuous extends FeatureType