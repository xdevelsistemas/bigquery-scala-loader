import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.bigquery.Bigquery.Projects;
import com.google.api.services.bigquery.model.*;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BigQueryJavaServiceAccount {

    public static void main(String[] args) throws IOException, InterruptedException, GeneralSecurityException {

        final HttpTransport TRANSPORT = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = new JacksonFactory();
        List<String> scopes = new LinkedList<String>();
        //scopes.add(BigqueryScopes.BIGQUERY);
        GoogleCredential credential = new  GoogleCredential.Builder()
                .setTransport(TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId("102279528686-8b884tf2l796tsj0uvbr52phao4rumr5@developer.gserviceaccount.com")
                .setServiceAccountScopes(Collections.singleton(BigqueryScopes.BIGQUERY))
                .setServiceAccountPrivateKeyFromP12File(new File("/home/pnakibar/Downloads/Gerencio-af2a81722410.p12"))
                .build();

        System.out.println(credential.refreshToken());

        Bigquery bq = null; //BigqueryServiceFactory.getService(credential);

        System.out.println(bq.datasets().list("xplanauth").execute());

        /*
        Bigquery bigquery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("BigQuery Samples")
                .build();


        Projects.List projectListRequest = bigquery.projects().list();
        ProjectList projectList = projectListRequest.execute();
        List<ProjectList.Projects> projects = projectList.getProjects();
        System.out.println("Available projects\n----------------\n");
        for (ProjectList.Projects project : projects) {
            System.out.format("%s\n", project.getProjectReference().getProjectId());
        }
        */
    }
}
