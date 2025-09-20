<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Mediciones.aspx.cs" Inherits="PaginaMedicionesBiometriaIndividual.Mediciones" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Prueba Mediciones</title>
</head>
<body>
    <form id="form1" runat="server">
        <div>   
            <asp:Button ID="BtnRefrescar" runat="server" Text="Recargar Mediciones" OnClick="BtnRefrescar_Click" />
            <asp:GridView ID="GridViewMediciones" runat="server" AutoGenerateColumns="true"></asp:GridView>
        </div>
    </form>
</body>
</html>
