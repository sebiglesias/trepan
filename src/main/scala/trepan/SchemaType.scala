package trepan

import org.apache.spark.sql.types.{DataType, StructType}

/** Unifies the way of obtaining a column's DataType
  *
  * @param columns a dataFrame's schema
  */
class SchemaType(columns: StructType) {

  /** Given a column name returns an Option[DataType]
    *
    * @param columnName Name of the column
    * @return returns Option[DataType]: None in the case that the column doesn't exist in the schema or Some(DataType)
    *         in the case that the column exists
    */
  def typeOf(columnName: String): Option[DataType] = {
    var dataType: Option[DataType] = None
    columns.foreach(r => {
      if (r.name.equals(columnName)) dataType = Some(r.dataType)
    })
    dataType
  }

}
