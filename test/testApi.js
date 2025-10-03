/*
 * @author David Bayona Lujan
 * Test de la API REST de mediciones. Realiza un POST para insertar una
 * medición y un GET para comprobar que la última medición devuelta coincide con la enviada.
 */
async function testAPI() {
  try {
    const nuevaMedicion = { Tipo: "co2", Valor: 123, Contador: 7 };

    let res = await fetch("https://dbayluj.upv.edu.es/api/mediciones", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(nuevaMedicion)
    });

    const postData = await res.json();
    console.log("POST status:", res.status);
    console.log("POST respuesta:", postData);

    res = await fetch("https://dbayluj.upv.edu.es/api/mediciones/ultima");
    const ultima = await res.json();

    console.log("GET status:", res.status);
    console.log("GET respuesta:", ultima);

    if (
      ultima.Tipo === nuevaMedicion.Tipo &&
      ultima.Valor === nuevaMedicion.Valor &&
      ultima.Contador === nuevaMedicion.Contador
    ) {
      console.log("✅ Test OK: la API devuelve lo esperado");
    } else {
      console.log("❌ Test FAIL: la API no devuelve los datos esperados");
    }

  } catch (err) {
    console.error("⚠️ Error en el test:", err);
  }
}

testAPI();