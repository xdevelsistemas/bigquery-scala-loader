import java.io.{File, FileInputStream, StringReader}
import java.util.concurrent.TimeUnit

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.gson.stream.JsonReader

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.SECONDS

/**
 * Created by pnakibar on 28/10/15.
 */
object Example extends App{
  //println(SendStream.bigquery.toString)


  //println(BigqueryServiceFactory.getService)
  //println(BigqueryServiceFactory.getService(new FileInputStream(key)))
  //println(SendStream.bigquery.toString)
  //val entry1:Map[Any, Any] = Map("name" -> "john doe")
  //SendStream.apply("xplanauth", "testing", "teste2", entry1)

  val key = new File("/home/pnakibar/Downloads/Gerencio-b44ebb669bf0(1).p12")
  val bigquery = BigqueryServiceFactory.getService(new FileInputStream(key))

  //SendStream.apply("projectId", "datasetId", "tableId", entry2)

  val entry2 = "{\"name\":\"john doe\"}"
  val sendStream = new SendStream(bigquery)
  sendStream.apply("projectId", "datasetId", "tableId", new JsonReader(new StringReader(entry2)))

  /*
  val entry3:List[Map[Any, Any]] = List(entry1, Map("name"->"douglas noel adams"), Map("name" -> "master yoda"))
  val f = SendStream.applyMaps("projectId", "datasetId", "tableId", 10, entry3)
  val result = Await.result(f, Duration.create(3, SECONDS))
  */
}
