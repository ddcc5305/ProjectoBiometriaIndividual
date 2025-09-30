const Logica = require("./Logica.js");

async function testUltimaMedicion() {
  const logica = new Logica("mediciones.db");

  const esperado = {
    Tipo: "co2",
    Valor: 233,
    Contador: 5
  };

  try {
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