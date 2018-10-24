package trepan

import org.apache.spark.sql.types.{DataType, StructType}

class SchemaType(columns: StructType) {

  /*
   * Given a column name returns an Option[DataType]
   * @attr columnName
   * @return Option[DataType]
   */
  def typeOf(columnName: String): Option[DataType] = {
    var dataType: Option[DataType] = None
    columns.foreach(r => {
      if (r.name.equals(columnName)) dataType = Some(r.dataType)
    })
    dataType
  }

}
