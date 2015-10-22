
import java.io.{BufferedReader, InputStreamReader, FileInputStream}
import java.util.zip.GZIPInputStream

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.bigquery.Bigquery.Builder
import com.google.api.services.bigquery.model._
import com.google.api.services.bigquery.{Bigquery, BigqueryScopes}
import scala.collection.JavaConverters._



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


  def main(args: Array[String]) {
    val projectId = "xplanauth"

    val bigquery = createAuthorizedClient

    //Create Fields
    val tableFieldSchema = new TableFieldSchema
    //tableFieldSchema.set("nome", "string")
    tableFieldSchema.setName("nome")
    tableFieldSchema.setType("string")

    //Create Schema
    val tableSchema = new TableSchema
    tableSchema.setFields(List(tableFieldSchema).asJava)
    //jtableSchema.set("nome", "STRING")
    //tableSchema.set

    //Create Table
    val table = new Table
    table.setSchema(tableSchema)
    //table.setId("testingid")
    table.setFriendlyName("testing friendly")

    //EXTERNAL DATA CONFIGURATION
    table.setExternalDataConfiguration(new ExternalDataConfiguration)
    val external = table.getExternalDataConfiguration.setSourceFormat("NEWLINE_DELIMITED_JSON")

    //TABLE REFERENCE
    table.setTableReference(new TableReference)
    table.getTableReference.setTableId("test") //THIS TABLE ID
    table.getTableReference.setDatasetId("testing") //DATASET ID
    table.getTableReference.setProjectId("xplanauth") //PROJECT ID

    table.getExternalDataConfiguration.setSourceUris(List("gs://gerenc/something.json").asJava)



    println(table)



    bigquery.tables().insert(projectId, "testing", table).execute()


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
