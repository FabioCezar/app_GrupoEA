<?php
header('Content-Type: text/json charset=utf-8');

$response = array();
$response['erro'] = true;

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    include 'conectar.php';


    $con = mysqli_connect($serverName, $username, $password, $database);

    if (!$con) {
        die("Falha na Conexão: " . mysqli_connect_error());
    }
    $login = "";
    $senha = "";
    $login = $_POST['login'];
    $senha = $_POST['senha'];

    $sql = "SELECT * FROM tb_usuario WHERE BINARY login = '$login' AND BINARY senha = '$senha' AND status = 'ativo'";

    $result = mysqli_query($con, $sql);

    if (mysqli_num_rows($result) > 0) {
        $registro = mysqli_fetch_assoc($result);
        $response['linhas'] = mysqli_num_rows($result);
        $response['erro'] = false;
        $response['id'] = $registro['id'];
        $response['login'] = $registro['login'];
        $response['senha'] = $registro['senha'];
        $response['perfil'] = $registro['perfil'];
        $response['nome'] = $registro['nome'];
    } else {
        $response['mensagem'] = "Usuário e / ou senha inválidos";
        $response['login'] = $login;
        $response['senha'] = $senha;
    }
   
    mysqli_close($con);
}

echo json_encode($response);

