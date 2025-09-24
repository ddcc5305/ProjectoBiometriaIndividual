const sqlite3 = require("sqlite3").verbose();
const fs = require("fs");
const path = require("path");

class Logica {
  constructor(nombreBD) {
    // Usamos ruta absoluta
    const rutaBD = path.resolve(nombreBD);
    this.db = new sqlite3.Database(rutaBD, (err) => {
      if (err) {
        this.logError(err);
        console.error("Error abriendo la BD:", err.message);
      }
    });

    this.db.run(`CREATE TABLE IF NOT EXISTS Mediciones (
      Id INTEGER PRIMARY KEY AUTOINCREMENT,
      Tipo TEXT NOT NULL,
      Valor REAL NOT NULL,
      Contador INTEGER DEFAULT 0,
      Timestamp TEXT NOT NULL
    )`, (err) => {
      if (err) this.logError(err);
    });
  }

  // Función para registrar errores
  logError(err) {
    const logLine = `${new Date().toISOString()} - ${err.stack || err.message}\n`;
    fs.appendFileSync(path.join(__dirname, "error.log"), logLine, { encoding: "utf8" });
  }

  guardarMedicion(tipo, valor, contador, timestamp) {
  return new Promise((resolve, reject) => {
    if (!tipo || isNaN(valor) || !timestamp) {
      return reject(new Error("Datos inválidos"));
    }
    this.db.run(
      "INSERT INTO Mediciones (Tipo, Valor, Contador, Timestamp) VALUES (?,?,?,?)",
      [tipo, valor, contador, timestamp],
      function (err) {
        if (err) {
          console.error("Error SQLite:", err.message, err);
          reject(err);
        } else {
          resolve(this.lastID);
        }
      }
    );
  });
}

  obtenerUltimaMedicion() {
    return new Promise((resolve, reject) => {
      this.db.get(
        "SELECT * FROM Mediciones ORDER BY Id DESC LIMIT 1",
        [],
        (err, row) => {
          if (err) reject(err);
          else resolve(row);
        }
      );
    });
  }
}

module.exports = Logica;