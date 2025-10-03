/*
 * @author David Bayona Lujan
 * Test automático de la clase Logica para comprobar que guardar y obtener
 * la última medición funciona correctamente usando una base de datos en memoria.
 */
const Logica = require("./Logica.js");

/*
 * Crea una base de datos en memoria, inserta una medición y comprueba
 * que la última medición devuelta coincide con los datos esperados usando async.
 */
async function testUltimaMedicion() {
  const logica = new Logica(":memory:");

  const esperado = {
    Tipo: "co2",
    Valor: 233,
    Contador: 5
  };

  try {
    await new Promise((resolve, reject) => {
      logica.db.run(`
        CREATE TABLE IF NOT EXISTS Mediciones (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          Tipo TEXT,
          Valor INTEGER,
          Contador INTEGER,
          Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
        )
      `, (err) => {
        if (err) reject(err);
        else resolve();
      });
    });

    // Ahora ya puedes probar como antes
    const id = await logica.guardarMedicion(esperado);
    console.log(`✅ Medición insertada con ID: ${id}`);

    const ultima = await logica.obtenerUltimaMedicion();

    if (!ultima) {
      console.log("❌ No hay mediciones en la base de datos");
      return;
    }

    console.log("Última medición obtenida:", ultima);

    let correcto = true;

    if (ultima.Tipo !== esperado.Tipo) {
      console.log(`❌ Tipo esperado: ${esperado.Tipo}, obtenido: ${ultima.Tipo}`);
      correcto = false;
    }
    if (ultima.Valor !== esperado.Valor) {
      console.log(`❌ Valor esperado: ${esperado.Valor}, obtenido: ${ultima.Valor}`);
      correcto = false;
    }
    if (ultima.Contador !== esperado.Contador) {
      console.log(`❌ Contador esperado: ${esperado.Contador}, obtenido: ${ultima.Contador}`);
      correcto = false;
    }

    console.log("Timestamp guardado en DB:", ultima.Timestamp);

    if (correcto) {
      console.log("✅ Test OK: la última medición coincide con los datos esperados");
    } else {
      console.log("❌ Test FAILED: la última medición no coincide con los datos esperados");
    }

  } catch (err) {
    console.error("ERROR durante el test:", err);
  }
}

testUltimaMedicion();