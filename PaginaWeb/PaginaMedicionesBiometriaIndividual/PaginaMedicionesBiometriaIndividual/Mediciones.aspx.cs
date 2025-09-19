using System;
using System.Collections.Generic;
using System.Web.Services;

namespace PaginaMedicionesBiometriaIndividual {
    public partial class Mediciones : System.Web.UI.Page
    {
        private static BaseDatos db;

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                string dbFilePath = Server.MapPath("~/mediciones.db");
                db = new BaseDatos(dbFilePath);

                var lista = db.ObtenerMediciones();
            }
        }

        [WebMethod]
        public static List<Medida> ObtenerMediciones()
        {
            return db.ObtenerMediciones();
        }

        [WebMethod]
        public static string AgregarMedida(Medida m)
        {
            if (m == null) return "JSON inválido";

            db.AgregarMedida(m);
            return "Medida agregada correctamente";
        }
    }
}
