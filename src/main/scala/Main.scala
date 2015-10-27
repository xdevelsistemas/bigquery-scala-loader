
import java.io._
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.util.Collections
import java.util.zip.GZIPInputStream

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.bigquery.Bigquery.Builder
import com.google.api.services.bigquery.model.TableDataInsertAllRequest.Rows
import com.google.api.services.bigquery.model._
import com.google.api.services.bigquery.{Bigquery, BigqueryScopes}
import com.google.gson.{JsonSyntaxException, Gson}
import com.google.gson.stream.JsonReader
import scala.collection.JavaConverters._
import scala.collection.immutable.HashMap


/**
 * Created by pnakibar on 21/10/15.
 */
object Main{
  val STREAMS_PER_MINUTE = 60
  val PROJECT_ID = "xplanauth"
  val DATASET_ID = "testing"
  val TABLE_ID = "teste2"


  def main(args: Array[String]) {
    val json: String =
      "[" +
        "{" +
          "\"nome\":\"xxxxxxxx\"" +
        "}" +
      "]"

    val jsonReader = new JsonReader(new StringReader(json))

    val responses = StreamingSample.run(PROJECT_ID, DATASET_ID, TABLE_ID, jsonReader)

    while (responses.hasNext)
      println(responses.next())


  }
}
