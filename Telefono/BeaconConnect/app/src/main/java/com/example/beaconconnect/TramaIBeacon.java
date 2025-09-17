package com.example.beaconconnect;

import android.util.Log;

import java.util.Arrays;
// -----------------------------------------------------------------------------------
// @author: Jordi Bataller i Mascarell
// -----------------------------------------------------------------------------------
public class TramaIBeacon {
    private static final String ETIQUETA_LOG = ">>>>";
    private byte[] prefijo = null; // 6 bytes: length, type, companyID, iBeacon type, iBeacon length
    private byte[] uuid = null; // 16 bytes
    private byte[] major = null; // 2 bytes
    private byte[] minor = null; // 2 bytes
    private byte txPower = 0; // 1 byte

    private byte[] losBytes;

    private byte[] advFlags = null; // 3 bytes
    private byte[] advHeader = null; // 2 bytes
    private byte[] companyID = new byte[2]; // 2 bytes
    private byte iBeaconType = 0; // 1 byte
    private byte iBeaconLength = 0; // 1 byte

    public TramaIBeacon(byte[] bytes) {
        this.losBytes = bytes;

        // Validate packet length
        if (bytes == null || bytes.length < 27) {
            Log.e(ETIQUETA_LOG, "TramaIBeacon: Insufficient data length (" + (bytes == null ? 0 : bytes.length) + ")");
            return;
        }

        // Check iBeacon signature: Company ID (0x4C 0x00), iBeacon Type (0x02), iBeacon Length (0x15)
        if (bytes[2] == 0x4C && bytes[3] == 0x00 && bytes[4] == 0x02 && bytes[5] == 0x15) {
            prefijo = Arrays.copyOfRange(losBytes, 0, 5 + 1); // 6 bytes: length, type, companyID, iBeacon type, length
            advFlags = Arrays.copyOfRange(bytes, 0, 3); // 2 bytes
            advHeader = Arrays.copyOfRange(losBytes, 0, 1 + 1); // 2 bytes: length (0x1A), type (0xFF)
            companyID = Arrays.copyOfRange(losBytes, 2, 3 + 1); // 2 bytes: 0x4C 0x00
            iBeaconType = losBytes[4]; // 1 byte: 0x02
            iBeaconLength = losBytes[5]; // 1 byte: 0x15
            uuid = Arrays.copyOfRange(losBytes, 6, 21 + 1); // 16 bytes
            major = Arrays.copyOfRange(losBytes, 22, 23 + 1); // 2 bytes
            minor = Arrays.copyOfRange(losBytes, 24, 25 + 1); // 2 bytes

            txPower = losBytes[26]; // 1 byte
        } else {
            Log.e(ETIQUETA_LOG, "TramaIBeacon: Not an iBeacon packet");
        }
    }

    // Getters
    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getPrefijo() {
        return prefijo;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getUUID() {
        return uuid;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getMajor() {
        return major;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getMinor() {
        return minor;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte getTxPower() {
        return txPower;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getLosBytes() {
        return losBytes;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getAdvFlags() {
        return advFlags;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getAdvHeader() {
        return advHeader;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte[] getCompanyID() {
        return companyID;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte getiBeaconType() {
        return iBeaconType;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public byte getiBeaconLength() {
        return iBeaconLength;
    } // ()
} // class
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------