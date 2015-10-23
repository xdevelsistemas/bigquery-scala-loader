
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
  def createAuthorizedClient = {
    val transport: HttpTransport = new NetHttpTransport
    val jsonFactory: JsonFactory = new JacksonFactory
    var credential: GoogleCredential = GoogleCredential.getApplicationDefault(transport, jsonFactory)

    if (credential.createScopedRequired())
      credential = credential.createScoped(BigqueryScopes.all())

    new Builder(transport, jsonFactory, credential).setApplicationName("Bigquery Samples").build()
  }

  def executeQuery(querySql: String, bigquery: Bigquery, projectId: String) = {
    val query = bigquery.jobs().query(
      projectId,
      new QueryRequest().setQuery(querySql)
    ).execute()

    val queryResult = bigquery.jobs().getQueryResults(
      query.getJobReference.getProjectId,
      query.getJobReference.getJobId).execute()

    queryResult.getRows.asScala
  }

  def printResults(rows: Iterable[TableRow]) = {
    println("\nQuery results:\n--------------\n")
    rows.foreach(row =>{
      row.getF.asScala.foreach(cell => {
        print(s"${cell.getV} ")
      })

      println
    })
  }

  def createTableFromSource(bigquery: Bigquery, projectId: String, tableId: String, datasetId: String, sourceUri: String) = {
    //Create Fields
    val tableFieldSchema = new TableFieldSchema
    //tableFieldSchema.set("nome", "string")
    tableFieldSchema.setName("nome")
    tableFieldSchema.setType("string")

    //Create Schema
    val tableSchema = new TableSchema
    tableSchema.setFields(List(tableFieldSchema).asJava)

    //Create Table
    val table = new Table
    table.setSchema(tableSchema)
    table.setFriendlyName("testing friendly")

    //EXTERNAL DATA CONFIGURATION
    table.setExternalDataConfiguration(new ExternalDataConfiguration)
    val external = table.getExternalDataConfiguration.setSourceFormat("NEWLINE_DELIMITED_JSON")

    //TABLE REFERENCE
    table.setTableReference(new TableReference)
    table.getTableReference.setTableId(tableId) //THIS TABLE ID
    table.getTableReference.setDatasetId(datasetId) //DATASET ID
    table.getTableReference.setProjectId(projectId) //PROJECT ID

    table.getExternalDataConfiguration.setSourceUris(List(sourceUri).asJava)

    bigquery.tables().insert(projectId, datasetId, table).execute()
  }
  def streamRow(bigquery:Bigquery,
                projectId: String,
                datasetId: String,
                tableId: String,
                row: TableDataInsertAllRequest.Rows): TableDataInsertAllResponse = {
    bigquery.tabledata.insertAll(
      projectId,
      datasetId,
      tableId,
      new TableDataInsertAllRequest().setRows(Collections.singletonList(row))
    ).execute()
  }
  def insertDataAsStream(bigquery:Bigquery, projectId: String, datasetId: String, tableId: String, rows: JsonReader) = {
    val gson = new Gson()
    rows.beginArray()

    new java.util.Iterator[TableDataInsertAllResponse] {
      override def hasNext: Boolean = {
        try rows.hasNext
        catch {case e: Exception => e.printStackTrace()}
        false
      }

      override def next(): TableDataInsertAllResponse = {
        try{
          val rowData = gson.fromJson[java.util.Map[String, AnyRef]](rows, new java.util.HashMap[String, AnyRef].getClass)
          streamRow(bigquery, projectId, datasetId, tableId, new Rows().setJson(rowData))
        }
        catch{
          case e: JsonSyntaxException => e.printStackTrace()
          case e: IOException => e.printStackTrace()
        }
        null
      }
    }
  }



  def main(args: Array[String]) {
    val projectId = "xplanauth"
    val tableId = "test"
    val datasetId = "testing"
    //val sourceUri = "gs://gerenc/something.json"

    val jsonExample = "[{\"nome\":\"maria\"}]"

    val bucket = "gerenc"
    val file = new File("/home/pnakibar/myjson.json")
    lazy val fileUri = s"gs://$bucket/${file.getName}"

    lazy val isGzip = file.getName.endsWith(".gz") || file.getName.endsWith(".gzip")

    //upload file
    val bigquery = createAuthorizedClient
    val loadFromFileJob = new Job()
    loadFromFileJob.setConfiguration(new JobConfiguration())
    loadFromFileJob.getConfiguration.setLoad(
      new JobConfigurationLoad()
        .setSourceFormat("DATASTORE_BACKUP")
        .setSourceUris(List(fileUri).asJava)
    )
    println(loadFromFileJob)
    bigquery.jobs().insert(projectId, loadFromFileJob).wait()


    //StreamingSample.run(projectId, datasetId, tableId, new JsonReader(new FileReader(file)))

    //bigquery.tabledata().insertAll(projectId, datasetId, tableId, new TableDataInsertAllRequest())
    /*
    try {
      StorageSample.uploadStream(
        file.getName,
        {if (isGzip) "file/gzip" else "application/json"}, //checks extension
        new FileInputStream(file),
        bucket)

      println("File uploaded sucessfully!")
    }
    catch{
      case e:IOException => e.printStackTrace()
      case e:GeneralSecurityException => e.printStackTrace()
    }
    */

    //load file to the biquery database



    /*
    val responses = insertDataAsStream(bigquery, projectId, datasetId, tableId, new JsonReader(new StringReader(jsonExample)))
    if (responses.hasNext)
      println(responses.next())
      */

    //createTableFromSource(bigquery, projectId, tableId, datasetId, sourceUri)

    /*
    val rows = executeQuery(
      "select * from transparenciaes.transpesDespesa limit 1",
      bigquery,
      projectId
    )

    printResults(rows)
  */
  }
}
