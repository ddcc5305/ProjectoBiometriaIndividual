// -----------------------------------------------------------------------------------
// @author: David Bayona Lujan
// Archivo: logicaFrontEnd.js
// Descripción: Funciones del frontend para cargar y mostrar la última medición
//              obtenida desde la base de datos mediante la API.
// -----------------------------------------------------------------------------------

/*
 * Se llama desde el botón onclick="cargarUltima()" en index.html
 * fetch("/api/mediciones/ultima") --> obtener JSON --> actualizar innerHTML
 */
async function cargarUltima() {
  try {
    const res = await fetch("/api/mediciones/ultima");
    if (!res.ok) throw new Error("Error cargando medición");

    const data = await res.json();

    document.getElementById("salida").innerHTML = `
      <div><span class="label">Tipo:</span> ${data.Tipo || "--"}</div>
      <div><span class="label">Valor:</span> ${data.Valor || "--"}</div>
      <div><span class="label">Contador:</span> ${data.Contador || "--"}</div>
      <div><span class="label">Timestamp:</span> ${data.Timestamp || "--"}</div>
    `;
  } catch (err) {
    alert(err.message);
  }
}