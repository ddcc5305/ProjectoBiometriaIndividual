<?php
//medicionService.php -- Logica de negocio
class MedicionService {
    private $db;

    public function __construct($dbFile) {
        $this->db = new SQLite3($dbFile);
    }

    public function obtenerTodas() {
        $result = $this->db->query("SELECT * FROM Mediciones");
        $medidas = [];
        while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
            $medidas[] = $row;
        }
        return $medidas;
    }

    public function insertar($tipo, $valor, $contador, $timestamp) {
        // Validaciones
        if (empty($tipo) || !is_numeric($valor) || empty($timestamp)) {
            return false;
        }

        $stmt = $this->db->prepare(
            "INSERT INTO Mediciones (Tipo, Valor, Contador, Timestamp) 
             VALUES (:tipo, :valor, :contador, :timestamp)"
        );
        $stmt->bindValue(':tipo', $tipo, SQLITE3_TEXT);
        $stmt->bindValue(':valor', $valor, SQLITE3_FLOAT);
        $stmt->bindValue(':contador', $contador, SQLITE3_INTEGER);
        $stmt->bindValue(':timestamp', $timestamp, SQLITE3_TEXT);
        return $stmt->execute();
    }
}