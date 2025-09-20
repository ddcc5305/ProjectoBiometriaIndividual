using System.Collections.Generic;
using System.Web;
using System.Web.Services;

namespace PaginaMedicionesBiometriaIndividual
{
    [WebService(Namespace = "https://dbayluj.upv.edu.es/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    public class MedidasService : WebService
    {
        private RestServidor rest;

        private void InicializarRest()
        {
            if (rest == null)
            {
                string dbFilePath = HttpContext.Current.Server.MapPath("~/mediciones.db");
                rest = new RestServidor(dbFilePath);
            }
        }

        [WebMethod]
        public List<Medida> ObtenerMediciones()
        {
            InicializarRest();
            return rest.ObtenerMediciones();
        }

        [WebMethod]
        public string AgregarMedida(Medida m)
        {
            InicializarRest();
            return rest.AgregarMedida(m);
        }
    }
}