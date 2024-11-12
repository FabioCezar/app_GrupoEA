<?php
//Envia e-mail
date_default_timezone_set('America/Sao_Paulo');


require_once('src/PHPMailer.php');
require_once('src/SMTP.php');
require_once('src/Exception.php');

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

function enviarEmail($placa)
{
    $data = date('d/m/Y H:i:s');
    $assunto = utf8_decode("PLACA LOCALIZADA");
    
    $mail = new PHPMailer();
    $mail->isSMTP();
    $mail->Host = 'mail.grupoea.net.br';
    $mail->SMTPAuth = true;
    $mail->Username = 'placas@grupoea.net.br';
    $mail->Password = '@recife2021';
    $mail->Port = 465;
    
    $mail->setFrom('placas@grupoea.net.br');
    $mail->addAddress('fabio.cezar@grupoea.net.br');
    
    $mail->isHTML(true);
    $mail->Subject = $assunto;
    $mail->Body = "Placa: {$placa} <br>
    Data/hora: {$data} <br>";
    
    if ($mail->send()) {
        return "Email enviado";
    } else {
        return $mail->ErrorInfo;
    }
}



