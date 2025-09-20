using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;
using System.Web;

namespace PaginaMedicionesBiometriaIndividual
{
    public class BaseDatos
    {
        private string connString;

        public BaseDatos(string archivoDb)
        {
            connString = $"Data Source={archivoDb};Version=3;";
            CrearTabla();
        }

        private void CrearTabla()
        {
            using (var con = new SQLiteConnection(connString))
            {
                con.Open();
                string sql = @"CREATE TABLE IF NOT EXISTS Mediciones (
                                Id INTEGER PRIMARY KEY,
                                Tipo TEXT,
                                Valor REAL,
                                Contador INTEGER,
                                Timestamp TEXT
                              )";
                using (var cmd = new SQLiteCommand(sql, con))
                {
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public void AgregarMedida(Medida m)
        {
            using (var con = new SQLiteConnection(connString))
            {
                con.Open();
                string sql = "INSERT INTO Mediciones (Tipo, Valor, Contador, Timestamp) VALUES (@tipo, @valor, @contador, @timestamp)";
                using (var cmd = new SQLiteCommand(sql, con))
                {
                    cmd.Parameters.AddWithValue("@tipo", m.Tipo);
                    cmd.Parameters.AddWithValue("@valor", m.Valor);
                    cmd.Parameters.AddWithValue("@contador", m.Contador);
                    cmd.Parameters.AddWithValue("@timestamp", m.Timestamp);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<Medida> ObtenerMediciones()
        {
            var lista = new List<Medida>();
            using (var con = new SQLiteConnection(connString))
            {
                con.Open();
                string sql = "SELECT Id, Tipo, Valor, Contador, Timestamp FROM Mediciones";
                using (var cmd = new SQLiteCommand(sql, con))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        lista.Add(new Medida
                        {
                            Id = reader.GetInt32(0),
                            Tipo = reader.GetString(1),
                            Valor = reader.GetDouble(2),
                            Contador = reader.GetInt32(3),
                            Timestamp = reader.GetString(4)
                        });
                    }
                }
            }
            return lista;
        }
    }
}