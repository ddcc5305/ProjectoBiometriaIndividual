using PaginaMedicionesBiometriaIndividual;
using System;
using System.Collections.Generic;

namespace PaginaMedicionesBiometriaIndividual
{
    public partial class Mediciones : System.Web.UI.Page
    {
        private RestServidor rest;

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                InicializarRest();
                RefrescarGrid();
            }
        }

        private void InicializarRest()
        {
            if (rest == null)
            {
                string dbFilePath = Server.MapPath("~/mediciones.db");
                rest = new RestServidor(dbFilePath);
            }
        }

        private void RefrescarGrid()
        {
            GridViewMediciones.DataSource = rest.ObtenerMediciones();
            GridViewMediciones.DataBind();
        }

        protected void BtnRefrescar_Click(object sender, EventArgs e)
        {
            InicializarRest();
            RefrescarGrid();
        }
    }
}