using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace PaginaMedicionesBiometriaIndividual
{
    public class Medida
    {
        public int Id { get; set; }
        public string Tipo { get; set; }
        public double Valor { get; set; }
        public int Contador { get; set; }
        public string Timestamp { get; set; }
    }
}