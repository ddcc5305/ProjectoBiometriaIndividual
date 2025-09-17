package com.example.beaconconnect;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
// -----------------------------------------------------------------------------------
// @author: David Bayona Lujan
// Clase utilizada para filtrar el mayor y el menor por su id de sensor y sus datos
// -----------------------------------------------------------------------------------
public class FiltroMedicionesIBeacon {

    private static final String ETIQUETA_LOG = ">>>>";

    public static final int TIPO_CO2 = 11;
    public static final int TIPO_TEMPERATURA = 12;
    public static final int TIPO_RUIDO = 13;

    // Método que procesa la trama para obtener el mayor y el menor filtrarlo y devolver tipo de medición junto a su dato
    // TramaIBeacon tib : bytes --> procesarTrama () --> resultado : String, N
    public static Map<String, Integer> procesarTrama(TramaIBeacon tib) {

        Map<String, Integer> resultado = new HashMap<>();

        if (tib.getMajor() == null || tib.getMinor() == null) {
            Log.e(ETIQUETA_LOG, "Trama inválida: major o minor nulo");
            return resultado;
        }

        int majorInt = Utilidades.bytesToInt(tib.getMajor());
        int minorInt = Utilidades.bytesToInt(tib.getMinor());

        int tipoMedicion = (majorInt >> 8) & 0xFF;
        int contador = majorInt & 0xFF;

        resultado.put("contador", contador);

        switch (tipoMedicion) {
            case TIPO_CO2:
                resultado.put("co2", minorInt);
                break;

            case TIPO_TEMPERATURA:
                resultado.put("temperatura", minorInt);
                break;

            case TIPO_RUIDO:
                resultado.put("ruido", minorInt);
                break;

            default:
                Log.w(ETIQUETA_LOG, "Tipo de medición desconocido: " + tipoMedicion);
                resultado.put("desconocido", minorInt);
        }

        return resultado;
    }
}