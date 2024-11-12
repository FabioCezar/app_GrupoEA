<?php
header('Content-Type: text/json charset=utf-8');
include_once('email/localizacao.php');
$response = array();
$response['erro'] = true;

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    include 'conectar.php';


    $con = mysqli_connect($serverName, $username, $password, $database);

    if (!$con) {
        die("Falha na Conexão: " . mysqli_connect_error());
    }

    $placa = isset($_POST['placa']) ? $_POST['placa'] : "";
    $nome = isset($_POST['nome']) ? $_POST['nome'] : "";
    $coordenadas = isset($_POST['coordenadas']) ? $_POST['coordenadas'] : "";
    $perfil = isset($_POST['perfil']) ? $_POST['perfil'] : "";


    $sql = "SELECT * FROM tb_carro WHERE placa = '$placa'";

    $result = mysqli_query($con, $sql);

    if (mysqli_num_rows($result) > 0) {
        
        $registro = mysqli_fetch_assoc($result);
        $response['linhas'] = mysqli_num_rows($result);
        $response['erro'] = false;
        $response['cpfReu'] = $registro['cpfReu'];
        $response['numProcessoCliente'] = $registro['numProcessoCliente'];
        $response['placa'] = $registro['placa'];
        $response['ano'] = $registro['ano'];
        $response['cor'] = $registro['cor'];
        $response['chassi'] = $registro['chassi'];
        $response['renavam'] = $registro['renavam'];
        $response['marca'] = $registro['marca'];
        $response['modelo'] = $registro['modelo'];
        if ($perfil == "localizador") {
            //$response['email'] =  enviarEmail($placa);

            $sql = "INSERT INTO tb_localizacao (coordenadas, placa, nome_localizador)
                VALUES ('$coordenadas', '$placa', '$nome')";

            $result = mysqli_query($con, $sql);
        }
    } else {
        $response['mensagem'] = "Placa não encontrada";
        $response['placa'] = $placa;
    }

    mysqli_close($con);
}

echo json_encode($response);
