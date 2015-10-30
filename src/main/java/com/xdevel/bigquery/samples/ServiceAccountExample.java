package com.xdevel.bigquery.samples;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;

import java.io.File;
import java.io.IOException;

/**
 * Created by pnakibar on 30/10/15.
 */
public class ServiceAccountExample {
    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static Bigquery bigquery;

    public static void main(String[] args) {
        try {
            try {

                GoogleCredential credential = new  GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                        .setJsonFactory(JSON_FACTORY)
                        .setServiceAccountId("102279528686-olt1vbm9hadglpk8mftc6oktdfcg0vd2.apps.googleusercontent.com")
                        .setServiceAccountScopes(BigqueryScopes.all())
                        .setServiceAccountPrivateKeyFromP12File(new File("/home/pnakibar/Downloads/Gerencio-af2a81722410.p12"))
                                //.setRefreshListeners(refreshListeners)
                                //.setServiceAccountUser("email.com")
                        .build();


                credential.refreshToken();

                bigquery = new Bigquery.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                        //.setApplicationName("GoogleBigQuery/1.0")
                        .build();

                System.out.println(bigquery.datasets().list("gerenc"));
                //listDatasets(bigquery, "publicdata");

                return;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }
}
