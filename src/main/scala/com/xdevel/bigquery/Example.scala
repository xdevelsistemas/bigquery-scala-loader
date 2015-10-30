package com.xdevel.bigquery

import java.io.StringReader

import com.google.gson.stream.JsonReader
import com.xdevel.bigquery.samples.BigqueryServiceFactory

/**
 * Created by pnakibar on 28/10/15.
 */
object Example extends App{
  val bigquery = BigqueryServiceFactory.getService

  val entry2 = "{\"name\":\"john doe\"}"
  val sendStream = new SendStream(bigquery)
  sendStream.apply("projectId", "datasetId", "tableId", new JsonReader(new StringReader(entry2)))

}
