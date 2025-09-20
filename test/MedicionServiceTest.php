<?php
use PHPUnit\Framework\TestCase;

require_once __DIR__ . '/../src/PaginaWeb/Sprint0/MedicionService.php';

class MedicionServiceTest extends TestCase
{
    private $dbFile = __DIR__ . '/test_mediciones.db';

    protected function setUp(): void
    {
        $db = new SQLite3($this->dbFile);
        $db->exec("CREATE TABLE IF NOT EXISTS Mediciones (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Tipo TEXT,
            Valor REAL,
            Contador INTEGER,
            Timestamp TEXT
        )");
        $db->close();
    }

    protected function tearDown(): void
    {
        if (file_exists($this->dbFile)) {
            unlink($this->dbFile);
        }
    }

    public function testInsertarMedicion()
    {
        $service = new MedicionService($this->dbFile);
        $resultado = $service->insertar('CO2', 400.5, 1, '2025-09-20 14:00:00');
        $this->assertNotFalse($resultado, 'La inserción debe devolver un objeto SQLite3Result');

        $medidas = $service->obtenerTodas();
        $this->assertCount(1, $medidas);
        $this->assertEquals('CO2', $medidas[0]['Tipo']);
        $this->assertEquals(400.5, $medidas[0]['Valor']);
        $this->assertEquals(1, $medidas[0]['Contador']);
        $this->assertEquals('2025-09-20 14:00:00', $medidas[0]['Timestamp']);
    }

    public function testInsertarVariosRegistros()
    {
        $service = new MedicionService($this->dbFile);
        $service->insertar('CO2', 400, 1, '2025-09-20 14:00:00');
        $service->insertar('Temperatura', 23.5, 2, '2025-09-20 14:05:00');
        $service->insertar('Ruido', 60, 3, '2025-09-20 14:10:00');

        $medidas = $service->obtenerTodas();
        $this->assertCount(3, $medidas);
        $this->assertEquals('CO2', $medidas[0]['Tipo']);
        $this->assertEquals('Temperatura', $medidas[1]['Tipo']);
        $this->assertEquals('Ruido', $medidas[2]['Tipo']);
    }

    public function testObtenerTodasVacia()
    {
        $service = new MedicionService($this->dbFile);
        $medidas = $service->obtenerTodas();
        $this->assertIsArray($medidas);
        $this->assertCount(0, $medidas);
    }

    public function testInsertarMedicionInvalida()
{
    $service = new MedicionService($this->dbFile);

    // Tipo vacío
    $resultado = $service->insertar('', 400.5, 1, '2025-09-20 14:00:00');
    $this->assertFalse($resultado, "No debería permitir tipo vacío");

    // Valor no numérico
    $resultado = $service->insertar('CO2', 'abc', 1, '2025-09-20 14:00:00');
    $this->assertFalse($resultado, "No debería permitir valor no numérico");

    // Timestamp vacío
    $resultado = $service->insertar('CO2', 400.5, 1, '');
    $this->assertFalse($resultado, "No debería permitir timestamp vacío");
}
public function testApiGetMediciones()
{
    $url = 'https://dbayluj.upv.edu.es/mediciones.php';
    $response = file_get_contents($url);
    $data = json_decode($response, true);

    $this->assertIsArray($data);
}

public function testApiPostMediciones()
{
    $url = 'https://dbayluj.upv.edu.es/mediciones.php';
    $jsonData = json_encode([
        'Tipo' => 'CO2',
        'Valor' => 420,
        'Contador' => 2,
        'Timestamp' => '2025-09-20 15:00:00'
    ]);

    $opts = ['http' => [
        'method' => 'POST',
        'header' => "Content-Type: application/json\r\n",
        'content' => $jsonData
    ]];

    $context = stream_context_create($opts);
    $result = file_get_contents($url, false, $context);
    $response = json_decode($result, true);

    $this->assertEquals('ok', $response['status']);
}

}