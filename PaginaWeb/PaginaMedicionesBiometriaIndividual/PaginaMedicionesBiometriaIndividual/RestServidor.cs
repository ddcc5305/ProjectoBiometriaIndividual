using System;
using System.Collections.Generic;

namespace PaginaMedicionesBiometriaIndividual
{
    public class RestServidor
    {
        private readonly BaseDatos db;

        public RestServidor(string archivoDb)
        {
            db = new BaseDatos(archivoDb);
        }

        public List<Medida> ObtenerMediciones()
        {
            return db.ObtenerMediciones();
        }

        public string AgregarMedida(Medida m)
        {
            if (m == null) return "JSON inválido";
            db.AgregarMedida(m);
            return "Medida agregada correctamente";
        }
    }
}