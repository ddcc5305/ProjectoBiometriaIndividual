<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Mediciones.aspx.cs" Inherits="PaginaMedicionesBiometriaIndividual.Mediciones" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Prueba Mediciones</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>
<body>
    <form id="form1" runat="server">
        <div>
            <button type="button" onclick="agregarMedida()">Agregar Medida de prueba</button>
            <button type="button" onclick="obtenerMediciones()">Ver Mediciones</button>
            <pre id="resultado" style="border:1px solid #ccc; padding:10px; margin-top:10px;"></pre>
        </div>
    </form>

    <script>
        function agregarMedida() {
            var medida = {
                Tipo: "co2",
                Valor: 450,
                Contador: 5,
                Timestamp: new Date().toISOString()
            };

            $.ajax({
                type: "POST",
                url: "Mediciones.aspx/AgregarMedida",
                data: JSON.stringify({ m: medida }),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (response) {
                    $("#resultado").text(response.d);
                },
                error: function (err) {
                    $("#resultado").text("Error: " + JSON.stringify(err));
                }
            });
        }

        function obtenerMediciones() {
            $.ajax({
                type: "POST",
                url: "Mediciones.aspx/ObtenerMediciones",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (response) {
                    $("#resultado").text(JSON.stringify(response.d, null, 2));
                },
                error: function (err) {
                    $("#resultado").text("Error: " + JSON.stringify(err));
                }
            });
        }
    </script>
</body>
</html>
