/*
 * @author David Bayona Lujan
 * Servidor Node.js con Express que expone una API REST para
 * guardar y obtener mediciones de sensores usando SQLite.
 */
const express = require("express");
const bodyParser = require("body-parser");
const path = require("path");
const Logica = require("./Logica.js");

// Ruta absoluta para la base de datos dentro de httpdocs
const rutaBD = path.resolve(__dirname, "mediciones.db");
const logica = new Logica(rutaBD);

const app = express();
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, ".")));

// POST: guardar medicion
app.post("/api/mediciones", async (req, res) => {
  try {
    const id = await logica.guardarMedicion(req.body);
    res.json({ ok: true, id });
  } catch (err) {
    res.status(400).json({ error: err.message, body: req.body });
  }
});

// GET: obtener la ultima medicion
app.get("/api/mediciones/ultima", async (req, res) => {
  try {
    const ultima = await logica.obtenerUltimaMedicion();
    res.json(ultima || {});
  } catch (err) {
    res.status(500).json({ error: "Error obteniendo medición", detalles: err.message });
  }
});

app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "index.html"));
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Servidor Node.js corriendo en puerto ${PORT}`);
});