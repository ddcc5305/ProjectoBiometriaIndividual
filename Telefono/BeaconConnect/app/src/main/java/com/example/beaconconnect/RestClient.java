package com.example.beaconconnect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestClient {

    public interface RespuestaREST {
        void callback(int codigo, String cuerpo);
    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void hacerPeticion(String metodo, String urlDestino, String cuerpo, RespuestaREST laRespuesta) {
        executor.execute(() -> {
            int codigoRespuesta = -1;
            String cuerpoRespuesta = "";

            try {
                Log.d("RestClient", "Conectando a " + urlDestino);

                URL url = new URL(urlDestino);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(metodo);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoInput(true);

                if (!metodo.equals("GET") && cuerpo != null) {
                    connection.setDoOutput(true);
                    try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                        dos.writeBytes(cuerpo);
                        dos.flush();
                    }
                }

                codigoRespuesta = connection.getResponseCode();
                Log.d("RestClient", "Código recibido: " + codigoRespuesta);

                InputStream is = (codigoRespuesta >= 200 && codigoRespuesta < 400)
                        ? connection.getInputStream()
                        : connection.getErrorStream();

                if (is != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder acumulador = new StringBuilder();
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        acumulador.append(linea);
                    }
                    cuerpoRespuesta = acumulador.toString();
                    is.close();
                }

                connection.disconnect();

            } catch (Exception e) {
                Log.e("RestClient", "Error en petición: " + e.getMessage());
            }

            int finalCodigo = codigoRespuesta;
            String finalCuerpo = cuerpoRespuesta;
            laRespuesta.callback(finalCodigo, finalCuerpo);
        });
    }
}