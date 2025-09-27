package com.example.beaconconnect;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
// -----------------------------------------------------------------------------------
// @author: David Bayona Lujan
// Clase utilizada para acumular las distintas mediciones de dinstintos tipos y preparar le peticion rest
// -----------------------------------------------------------------------------------
public class AcumuladorMediciones {

    private static final String ETIQUETA_LOG = ">>>>";

    private final int maxLecturas;
    private final String url;

    private final Map<String, List<Integer>> mediciones = new HashMap<>();

    // Guarda el maxLecturas antes de calcular el promedio y enviar y la url del destino
    // N : maxLecturas & String : url -> AcumuladorMediciones() -> N : maxLecturas & String : url (campos privados de la clase)
    //
    public AcumuladorMediciones(int maxLecturas, String url) {
        this.maxLecturas = maxLecturas;
        this.url = url;
    }

    // Agrega una medición con su sensor y el contador
    // String : tipo & R : Valor & N : contadorExterno -> agregarMedicion()
    //
    public void agregarMedicion(String tipo, int valor, int contadorExterno) {
        mediciones.computeIfAbsent(tipo, k -> new ArrayList<>()).add(valor);
        Log.d(ETIQUETA_LOG, "Medicion individual: " + valor + "Contador: " + contadorExterno);

        if (mediciones.get(tipo).size() >= maxLecturas) {
            enviarPromedio(tipo, contadorExterno);
            mediciones.get(tipo).clear();
        }
    }

    // Calcula y envía el promedio de un tipo de medición con la fecha
    // string : tipo & N : contadorExterno -> enviarPromedio()
    //
    private void enviarPromedio(String tipo, int contadorExterno) {
        List<Integer> lista = mediciones.get(tipo);
        if (lista == null || lista.isEmpty()) return;

        int promedio = (int) lista.stream().mapToInt(Integer::intValue).average().orElse(0);
        String timestamp = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"))
                .format(new Date());

        enviarRest(tipo, promedio, contadorExterno, timestamp);

        Log.d(ETIQUETA_LOG, "Promedio " + tipo + ": " + promedio + " | Contador externo: " + contadorExterno);
    }

    // Prepara la petición rest y llama a su respectiva clase
    // String : tipo & R : Valor & N : contador & String : timestamp -> enviarRest()
    //
    private void enviarRest(String tipo, int valor, int contador, String timestamp) {
        String cuerpoJson = String.format(new Locale("es", "ES"),
                "{ \"Tipo\": \"%s\", \"Valor\": %d, \"Contador\": %d, \"Timestamp\": \"%s\" }",
                tipo, valor, contador, timestamp);

        RestClient.hacerPeticion("POST", url, cuerpoJson, (codigo, cuerpo) -> {
            Log.d("POST Medida", "Código: " + codigo + ", Respuesta: " + cuerpo);
        });
    }
}