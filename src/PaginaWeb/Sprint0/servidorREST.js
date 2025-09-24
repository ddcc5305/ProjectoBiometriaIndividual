const express = require("express");
const bodyParser = require("body-parser");
const path = require("path");
const fs = require("fs");
const Logica = require("./Logica.js");

// Ruta absoluta para la base de datos dentro de httpdocs
const rutaBD = path.resolve(__dirname, "mediciones.db");
const logica = new Logica(rutaBD);

const app = express();
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, "."))); // sirve index.html y favicon

// Función auxiliar para guardar logs
function logError(err) {
    const logLine = `${new Date().toISOString()} - ${err.stack || err.message}\n`;
    fs.appendFileSync(path.join(__dirname, 'error.log'), logLine, { encoding: 'utf8' });
}

// POST: guardar medición
app.post("/api/mediciones", async (req, res) => {
    try {
        const tipo = req.body.tipo || req.body.Tipo;
        const valor = Number(req.body.valor || req.body.Valor);
        const contador = parseInt(req.body.contador || req.body.Contador) || 0;
        const timestamp = req.body.timestamp || req.body.Timestamp;

        if (!tipo || isNaN(valor) || !timestamp) {
            return res.status(400).json({ error: "Datos inválidos", body: req.body });
        }

        const id = await logica.guardarMedicion(tipo, valor, contador, timestamp);
        res.json({ ok: true, id });
    } catch (err) {
        logError(err);
        res.status(500).json({ error: "Error guardando medición", detalles: err.message });
    }
});

// GET: obtener la última medición
app.get("/api/mediciones/ultima", async (req, res) => {
    try {
        const ultima = await logica.obtenerUltimaMedicion();
        res.json(ultima || {});
    } catch (err) {
        logError(err);
        res.status(500).json({ error: "Error obteniendo medición", detalles: err.message });
    }
});

// Servir index.html
app.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, "index.html"));
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Servidor Node.js corriendo en puerto ${PORT}`);
});