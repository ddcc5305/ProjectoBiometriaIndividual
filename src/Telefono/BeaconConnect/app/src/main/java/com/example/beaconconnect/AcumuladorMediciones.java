package com.example.beaconconnect;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AcumuladorMediciones {

    private static final String ETIQUETA_LOG = ">>>>";

    private final int maxLecturas;
    private final String url;

    private List<Integer> co2List = new ArrayList<>();
    private List<Integer> tempList = new ArrayList<>();
    private List<Integer> ruidoList = new ArrayList<>();
    private int contadorAcumulado = 0;

    public AcumuladorMediciones(int maxLecturas, String url) {
        this.maxLecturas = maxLecturas;
        this.url = url;
    }

    public void agregarMedicion(String tipo, int valor, int contador) {
        contadorAcumulado++;

        switch (tipo) {
            case "co2": co2List.add(valor); break;
            case "temperatura": tempList.add(valor); break;
            case "ruido": ruidoList.add(valor); break;
            default: Log.w(ETIQUETA_LOG, "Tipo desconocido: " + tipo); return;
        }

        if (contadorAcumulado >= maxLecturas) {
            enviarPromedios();
            limpiarListas();
        }
    }

    private void enviarPromedios() {
        String timestamp = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES")).format(new Date());

        if (!co2List.isEmpty()) {
            int promedio = (int) co2List.stream().mapToInt(Integer::intValue).average().orElse(0);
            enviarRest("co2", promedio, contadorAcumulado, timestamp);
        }
        if (!tempList.isEmpty()) {
            int promedio = (int) tempList.stream().mapToInt(Integer::intValue).average().orElse(0);
            enviarRest("temperatura", promedio, contadorAcumulado, timestamp);
        }
        if (!ruidoList.isEmpty()) {
            int promedio = (int) ruidoList.stream().mapToInt(Integer::intValue).average().orElse(0);
            enviarRest("ruido", promedio, contadorAcumulado, timestamp);
        }
    }

    private void enviarRest(String tipo, int valor, int contador, String timestamp) {
        String cuerpoJson = String.format(new Locale("es", "ES"),
                "{ \"Tipo\": \"%s\", \"Valor\": %d, \"Contador\": %d, \"Timestamp\": \"%s\" }",
                tipo, valor, contador, timestamp);

        RestClient.hacerPeticion("POST", url, cuerpoJson, (codigo, cuerpo) -> {
            Log.d("POST Medida", "Código: " + codigo + ", Respuesta: " + cuerpo);
        });
    }

    private void limpiarListas() {
        co2List.clear();
        tempList.clear();
        ruidoList.clear();
        contadorAcumulado = 0;
    }
}