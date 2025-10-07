package com.example.beaconconnect;

import android.util.Log;

import java.util.Locale;

public class LogicaFake {

    private static final String ETIQUETA_LOG = ">>>>";

    private String url = "https://dbayluj.upv.edu.es/api/mediciones";

    private String gas = "";
    private Double valor;

    public LogicaFake(String gas, double valor){
        this.gas = gas;
        this.valor = valor;
    }

    // Agrega una medición con su sensor, el contador y la url a hacer peticion
    // String : tipo & R : Valor & N : contadorExterno -> acumularMedicion() -> enviarRest()
    //
    public static void agregarMedicion(String tipo, double valor, double contador) {
        LogicaFake logica = new LogicaFake(tipo, valor);
        logica.enviarRest(tipo, valor, contador);
    }

    // Prepara la petición rest y llama a su respectiva clase
    // String : tipo & R : Valor & N : contador & String : timestamp -> enviarRest()
    //
    private void enviarRest(String tipo, double valor, double contador) {
        String cuerpoJson = String.format(Locale.US,
                "{ \"Tipo\": \"%s\", \"Valor\": %.2f, \"Contador\": %d }",
                tipo, valor, (int) contador);

        Log.d(ETIQUETA_LOG, "EnviamosREST de: " + cuerpoJson);
        RestClient.hacerPeticion("POST", url, cuerpoJson, (codigo, cuerpo) -> {
            Log.d("POST Medida", "Código: " + codigo + ", Respuesta: " + cuerpo);
        });
    }
}