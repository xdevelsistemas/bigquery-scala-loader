import java.text.SimpleDateFormat
import java.util

import com.google.api.client.util.DateTime
import com.google.api.services.bigquery.model.{TableFieldSchema, TableSchema, Table}
import com.sun.org.glassfish.gmbal.Description
import collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

/**
 * Created by pnakibar on 22/10/15.
 */

/*
object TableSchema{
  def apply(map: Map[String, String], name: String, type_ :String): TableSchema = {
    var tfs = new TableFieldSchema
    var ts = new TableSchema

    map.foreach(j => tfs.set(j._1, j._2))
    tfs.setName(name)
    tfs.setType(type_)

    var aux:java.util.List[TableFieldSchema] = new util.ArrayList[TableFieldSchema]()
    aux.add(tfs)

    ts.setFields(aux)
    ts
  }

}
*/
object TableBuilder {
  def simpleTable(tableSchema: TableSchema, friendlyName: String):Table = {
    var t = new Table
    t.setSchema(tableSchema)
    t.setCreationTime(System.currentTimeMillis())
    t.setFriendlyName(friendlyName)

    t
  }

}
