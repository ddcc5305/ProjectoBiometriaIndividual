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

    private final Map<String, List<Double>> mediciones = new HashMap<>();

    private final Map<String, Double> ultimoContador = new HashMap<>();

    // Guarda el maxLecturas antes de calcular el promedio y enviar y la url del destino
    // N : maxLecturas -> AcumuladorMediciones() -> N : maxLecturas (campos privados de la clase)
    //
    public AcumuladorMediciones(int maxLecturas) {
        this.maxLecturas = maxLecturas;
    }

    // Acumula una medición con su sensor y el contador
    // String : tipo & R : Valor & N : contadorExterno -> acumularMedicion()
    //
    public void acumularMedicion(String tipo, double valor, double contadorExterno) {
        Double ultimo = ultimoContador.getOrDefault(tipo, Double.valueOf(-1));
        if (contadorExterno <= ultimo) {
            Log.d(ETIQUETA_LOG, "Se ignora medición antigua o repetida (" + tipo + ") con contador " + contadorExterno);
            return;
        }

        ultimoContador.put(tipo, contadorExterno);

        mediciones.computeIfAbsent(tipo, k -> new ArrayList<>()).add(valor);

        Log.d(ETIQUETA_LOG, "Medicion individual (" + tipo + "): " + valor
                + " | Contador: " + contadorExterno
                + " | Total acumuladas: " + mediciones.get(tipo).size());

        if (mediciones.get(tipo).size() >= maxLecturas) {
            enviarPromedio(tipo, contadorExterno);
            mediciones.get(tipo).clear();
        }
    }

    // Calcula y envía el promedio de un tipo de medición con la fecha
    // string : tipo & N : contadorExterno -> enviarPromedio()
    //
    private void enviarPromedio(String tipo, double contadorExterno) {
        List<Double> lista = mediciones.get(tipo);
        if (lista == null || lista.isEmpty()) return;

        double promedio = (double) lista.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        LogicaFake.agregarMedicion(tipo, promedio, contadorExterno);

        Log.d(ETIQUETA_LOG, ">>>> Se alcanzaron " + maxLecturas
                + " lecturas de " + tipo
                + ". Promedio enviado = " + promedio);
    }
}