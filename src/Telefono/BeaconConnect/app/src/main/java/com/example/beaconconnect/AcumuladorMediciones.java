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

    private final Map<String, Integer> ultimoContador = new HashMap<>();

    // Guarda el maxLecturas antes de calcular el promedio y enviar y la url del destino
    // N : maxLecturas & String : url -> AcumuladorMediciones() -> N : maxLecturas & String : url (campos privados de la clase)
    //
    public AcumuladorMediciones(int maxLecturas, String url) {
        this.maxLecturas = maxLecturas;
        this.url = url;
    }

    // Acumula una medición con su sensor y el contador
    // String : tipo & R : Valor & N : contadorExterno -> acumularMedicion()
    //
    public void acumularMedicion(String tipo, int valor, int contadorExterno) {
        int ultimo = ultimoContador.getOrDefault(tipo, -1);
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
    private void enviarPromedio(String tipo, int contadorExterno) {
        List<Integer> lista = mediciones.get(tipo);
        if (lista == null || lista.isEmpty()) return;

        int promedio = (int) lista.stream().mapToInt(Integer::intValue).average().orElse(0);
        String timestamp = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"))
                .format(new Date());

        LogicaFake.agregarMedicion(tipo, promedio, contadorExterno, timestamp);

        Log.d(ETIQUETA_LOG, ">>>> Se alcanzaron " + maxLecturas
                + " lecturas de " + tipo
                + ". Promedio enviado = " + promedio);
    }
}