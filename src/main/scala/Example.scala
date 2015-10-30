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
  val bigquery = BigqueryServiceFactory.getService

  val entry2 = "{\"name\":\"john doe\"}"
  val sendStream = new SendStream(bigquery)
  sendStream.apply("projectId", "datasetId", "tableId", new JsonReader(new StringReader(entry2)))

}
