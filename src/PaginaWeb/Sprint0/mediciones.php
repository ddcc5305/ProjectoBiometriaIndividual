<?php
//mediciones.php -- Api REST
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');

require_once __DIR__ . "/MedicionService.php";

$dbFile = __DIR__ . '/mediciones.db';
$service = new MedicionService($dbFile);

$method = $_SERVER['REQUEST_METHOD'];

if ($method === 'GET') {
    echo json_encode($service->obtenerTodas());

} elseif ($method === 'POST') {
    $json = file_get_contents('php://input');
    $data = json_decode($json, true);

    if ($data && isset($data['Tipo'], $data['Valor'], $data['Contador'], $data['Timestamp'])) {
        $service->insertar($data['Tipo'], $data['Valor'], $data['Contador'], $data['Timestamp']);
        echo json_encode(['status' => 'ok', 'message' => 'Medida agregada']);
    } else {
        http_response_code(400);
        echo json_encode(['status' => 'error', 'message' => 'JSON inválido']);
    }

} else {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Método no permitido']);
}