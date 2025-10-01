const sqlite3 = require("sqlite3").verbose();
const path = require("path");

class Logica {
  constructor(nombreBD) {
    const rutaBD = path.resolve(nombreBD);
    this.db = new sqlite3.Database(rutaBD, (err) => {
      if (err) {
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
      if (err) console.error("Error creando tabla:", err.message);
    });
  }

  guardarMedicion(json) {
    return new Promise((resolve, reject) => {
      const tipo = json.tipo || json.Tipo;
      const valor = Number(json.valor || json.Valor);
      const contador = parseInt(json.contador || json.Contador) || 0;
      
      const fecha = new Date();
      const timestamp = fecha.toLocaleString("es-ES", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit"
      });

      if (!tipo || isNaN(valor) || !timestamp) {
        return reject(new Error("Datos inválidos"));
      }

      this.db.run(
        "INSERT INTO Mediciones (Tipo, Valor, Contador, Timestamp) VALUES (?,?,?,?)",
        [tipo, valor, contador, timestamp],
        function (err) {
          if (err) {
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