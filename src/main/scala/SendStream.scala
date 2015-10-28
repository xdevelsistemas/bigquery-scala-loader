import java.io._
import java.util
import com.google.api.services.bigquery.model.TableDataInsertAllRequest.Rows
import com.google.api.services.bigquery.model._
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration


/**
 * Created by pnakibar on 21/10/15.
 */
/*
JSON Pattern:
{
  "key":"value",
  "key2":"value2"
}
 */
object SendStream{

  def applyJsons(projectId: String, datasetId: String, tableId: String, intervalBetweenStreams: Long, jsons: List[String]) = {
    val jsonReaders = jsons.map(json => new JsonReader(new StringReader(json)))
    apply(projectId, datasetId, tableId, intervalBetweenStreams, jsonReaders)
  }

  def apply(projectId: String, datasetId: String, tableId: String, intervalBetweenStreams: Long, json: Map[String, String]): TableDataInsertAllResponse = {
    null
  }

  def apply(projectId: String, datasetId: String, tableId: String, intervalBetweenStreams: Long, jsonReaders: List[JsonReader]): Future[List[TableDataInsertAllResponse]] = Future {
    jsonReaders.zipWithIndex map {
      case (jsonReader: JsonReader, index: Int) => {
        val response = apply(projectId, datasetId, tableId, jsonReader)
        println(s"asd$index")
        if (index < jsonReaders.size - 1) Thread.sleep(intervalBetweenStreams)
        response
      }
    }
  }

  def apply(projectId: String, datasetId: String, tableId: String, jsonReader: JsonReader): TableDataInsertAllResponse = {
    val bigquery = BigqueryServiceFactory.getService
    val gson = new Gson
    val data = gson.fromJson[java.util.HashMap[String, AnyRef]](jsonReader, new util.HashMap[String, AnyRef]().getClass)

    StreamingSample.streamRow(bigquery, projectId, datasetId, tableId, new Rows().setJson(data))
  }


  def apply(projectId: String, datasetId: String, tableId: String, json: String): TableDataInsertAllResponse =
    apply(projectId, datasetId, tableId, new JsonReader(new StringReader(json)))

}
