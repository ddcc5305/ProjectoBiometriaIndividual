<?php
// mediciones.phpç
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');

// Ruta de tu SQLite
$dbFile = __DIR__ . '/mediciones.db';

// Abrir la base de datos
$db = new SQLite3($dbFile);

// Leer método HTTP
$method = $_SERVER['REQUEST_METHOD'];

if ($method === 'GET') {
    // Devolver todas las medidas
    $result = $db->query("SELECT * FROM Mediciones");
    $medidas = [];
    while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
        $medidas[] = $row;
    }
    echo json_encode($medidas);

} elseif ($method === 'POST') {
    // Leer JSON enviado desde Android
    $json = file_get_contents('php://input');
    $data = json_decode($json, true);

    if ($data && isset($data['Tipo'], $data['Valor'], $data['Contador'], $data['Timestamp'])) {
        $stmt = $db->prepare("INSERT INTO Mediciones (Tipo, Valor, Contador, Timestamp) VALUES (:tipo, :valor, :contador, :timestamp)");
        $stmt->bindValue(':tipo', $data['Tipo'], SQLITE3_TEXT);
        $stmt->bindValue(':valor', $data['Valor'], SQLITE3_FLOAT);
        $stmt->bindValue(':contador', $data['Contador'], SQLITE3_INTEGER);
        $stmt->bindValue(':timestamp', $data['Timestamp'], SQLITE3_TEXT);
        $stmt->execute();

        echo json_encode(['status' => 'ok', 'message' => 'Medida agregada']);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'JSON inválido']);
    }
} else {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Método no permitido']);
}
?>