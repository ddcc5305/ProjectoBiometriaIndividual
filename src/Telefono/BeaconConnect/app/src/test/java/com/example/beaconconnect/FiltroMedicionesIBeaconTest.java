package com.example.beaconconnect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class FiltroMedicionesIBeaconTest {

    @Test
    public void testProcesarTramaCO2() {
        byte[] trama = new byte[27];

        // iBeacon header
        trama[2] = 0x4C;  // company ID
        trama[3] = 0x00;
        trama[4] = 0x02;  // iBeacon type
        trama[5] = 0x15;  // iBeacon length

        // UUID ficticio (16 bytes)
        for (int i = 6; i <= 21; i++) {
            trama[i] = (byte) i;
        }

        // Major = 0x0B01 (CO2)
        trama[22] = 0x0B;
        trama[23] = 0x01;

        // Minor = 0x012C (300)
        trama[24] = 0x01;
        trama[25] = 0x2C;

        // txPower
        trama[26] = (byte) 0xC5;

        TramaIBeacon tib = new TramaIBeacon(trama);

        Map<String, Integer> resultado = FiltroMedicionesIBeacon.procesarTrama(tib);

        assertEquals(Integer.valueOf(300), resultado.get("co2"));
        assertEquals(Integer.valueOf(1), resultado.get("contador"));
    }

    @Test
    public void testProcesarTramaDesconocida() {
        byte[] trama = new byte[27];

        // iBeacon header
        trama[2] = 0x4C;
        trama[3] = 0x00;
        trama[4] = 0x02;
        trama[5] = 0x15;

        // UUID ficticio
        for (int i = 6; i <= 21; i++) {
            trama[i] = (byte) i;
        }

        // Major desconocido = 0x2005
        trama[22] = 0x20;
        trama[23] = 0x05;

        // Minor = 0x0064 (100)
        trama[24] = 0x00;
        trama[25] = 0x64;

        // txPower
        trama[26] = (byte) 0xC5;

        TramaIBeacon tib = new TramaIBeacon(trama);

        Map<String, Integer> resultado = FiltroMedicionesIBeacon.procesarTrama(tib);

        assertEquals(Integer.valueOf(100), resultado.get("desconocido"));
        assertEquals(Integer.valueOf(5), resultado.get("contador"));
    }
}