import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * TODO: Insert description here. (generated by elibixby)
 */
public class BigqueryServiceFactory {

    private static Bigquery service = null;
    private static Object service_lock = new Object();

    public static Bigquery getService() throws IOException{
        if(service==null){
            synchronized(service_lock){
                if(service==null){
                    service=createAuthorizedClient();
                }
            }
        }
        return service;
    }
    public static Bigquery getService(InputStream stream) throws IOException{
        if(service==null){
            synchronized(service_lock){
                if(service==null){
                    service=createAuthorizedClient(stream);
                }
            }
        }
        return service;
    }



    // [START get_service]
    private static Bigquery createAuthorizedClient() throws IOException {
        Collection<String> BIGQUERY_SCOPES = BigqueryScopes.all();
        HttpTransport TRANSPORT = new NetHttpTransport();
        JsonFactory JSON_FACTORY = new JacksonFactory();
        GoogleCredential credential = GoogleCredential.getApplicationDefault(TRANSPORT, JSON_FACTORY);
        if(credential.createScopedRequired()){
            credential = credential.createScoped(BIGQUERY_SCOPES);
        }
        return new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credential).setApplicationName("BigQuery Samples").build();
    }
    // [END get_service]

    private static Bigquery createAuthorizedClient(InputStream stream) throws IOException {
        Collection<String> BIGQUERY_SCOPES = BigqueryScopes.all();
        HttpTransport TRANSPORT = new NetHttpTransport();
        JsonFactory JSON_FACTORY = new JacksonFactory();
        GoogleCredential credential = GoogleCredential.fromStream(stream, TRANSPORT, JSON_FACTORY);
        if(credential.createScopedRequired()){
            credential = credential.createScoped(BIGQUERY_SCOPES);
        }
        return new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credential).setApplicationName("BigQuery Samples").build();
    }


}

