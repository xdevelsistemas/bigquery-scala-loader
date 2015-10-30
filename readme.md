# Generating account credentian
1. Open your **Google Developer's console**
2. In *APIs & Auth* select *Credentials*
3. Create a new **Services Account** inside *Add credentials*
4. Select**JSON** as your **Key type** and download it!

# Code Examples 
## Scala
```scala
	//auth
	//As a file
	val bigquery = com.xdevel.bigquery.samples.BigqueryServiceFactory.getService("/filepath/to/your/credential.json")

	//Reading the default credential, if you set it with gcloud client utility
	val bigquery = com.xdevel.bigquery.samples.BigqueryServiceFactory.getService

	//example: reading a project dataset
	val datasetList = bigquery.datasets().list("projectID").execute()
	println(datasetList) //json com a resposta dos datasets


	//inserting data into a bigquery table:
  	val sendStream = new com.xdevel.bigquery.SendStream(bigquery)
	val entry2 = "{\"name\":\"john doe\"}"
  	sendStream.apply("projectId", "datasetId", "tableId", new JsonReader(new StringReader(entry2)))
```


