# TODO
- [x] Criar tabela no big query
- [x] Enviar arquivo para o cloud storage
- [x] Modularizar para streams/minuto 
- [x] Adicionar possibilidade de autenticar pela biblioteca
- [] Documentar

# Como gerar credencial de conta
1. Abra o **Google Developer's console**
2. Em *APIs & Auth* selecione *Credentials*
3. Você pode criar uma nova credencial em *Add credentials*, crie uma **Services Account**
4. Em **Key type** selecione **JSON** e faça seu download
5. Depois é só direcionar para a sua aplicação aonde está o *.json* contendo a **credencial**

# Utilizando no código
## Scala
```scala
	//autenticação:

	//jeito 1 de ler uma credencial:
	val bigquery = BigqueryServiceFactory.getService(new FileInputStream(new File("/filepath/to/your/credential.json")))

	//jeito 2: passando apenas o filepath
	val bigquery = BigqueryServiceFactory.getService("/filepath/to/your/credential.json")

	//jeito3: lendo o padrão que está no sistema, autenticado pelo gcloud client
	val bigquery = BigqueryServiceFactory.getService


	val datasetList = bigquery.datasets().list("projectic").execute()
	println(datasetList) //json com a resposta dos datasets


	//como inserir dados
	val entry2 = "{\"name\":\"john doe\"}"
  	val sendStream = new SendStream(bigquery)
  	sendStream.apply("projectId", "datasetId", "tableId", new JsonReader(new StringReader(entry2)))
```


