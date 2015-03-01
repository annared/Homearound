<?php

	$DBhost='62.149.150.162';
	$DBusername='Sql572633';
	$DBpassword='04078191';
	$DBname='Sql572633_1';
	
	$connessione = mysql_connect($DBhost, $DBusername, $DBpassword) or die('Errore connessione '.mysql_error());
	mysql_select_db($DBname) or die ('Errore datebase' . mysql_error());

function check_account($email, $password) {
	$account=false;
	$zero='0';
	$query_check = mysql_query("SELECT * FROM user WHERE user_email='$email' and user_password='$password' and user_active!='$zero'");
	if($query_check){		
		$row = mysql_num_rows($query_check);
		if($row>=1) $account=true;
	}
	return $account;
}

function check_user($email, $password) {
	$account=false;
	$zero='0';
	$query_check = mysql_query("SELECT * FROM user WHERE user_email='$email' and user_active!='$zero'");
	if($query_check){		
		$row = mysql_num_rows($query_check);
		if($row>=1) $account=true;
	}
	return $account;
}


function check_research($id, $cat){
	$annuncio = mysql_query("select * from ".$cat." where ".$cat."_id = $id");
	$res = mysql_query("select * from user_research where research_cat='$cat'");
	if (mysql_num_rows($res)>0) {
	//aargh
		while($row = mysql_fetch_array($result)){
			if ($row['research_city']!='None') {
				$check_city = $row['research_city'];
			}else if ($row['research_area']!='None') {
				$check_area = $row['research_area'];
			}else if ($row['research_type']!='None') {
				$check_type = $row['research_type'];
			}else if ($row['research_price']!=0) {
				$check_price = $row['research_price'];
			}
		}
	}	
}

function account_activation($email,$active,$type)
{	
					
	$header = "MIME-Version: 1.0\r\n"; 
	$header .= "Content-type: text/html; charset=iso-8859-1\r\n"; 
	$header .= "From: HomeAround <annared.quaranta@gmail.com> \r\n"; 
	$header .= "Reply-to: HomeAround <annared.quaranta@gmail.com> \r\n"; 		

	if($type == "reg"){ 
		$content = "Iscrizione avvenuta con successo! Attiva il tuo account <a href='http://www.redcouch.it/homearound/server/user_activate.php?email=$email&active=$active'>cliccando qui</a>";
		$subject="Iscrizione HomeAround";
		
	}elseif ($type="psw") {
		$content = "Ecco la tua nuova password:".$active.", puoi cambiarla nella impostazioni del profilo.";
		$subject="Recupero password HomeAround";
	}
	
	$message = "
	<html>
		<head>
		<meta http-equiv='Content-Type' content='text/html;charset=UTF-8' />
		</head>
		<body bgcolor='#888'>
		
			<div style='text-align:center;padding-top:60px;padding-bottom:60px; color:#F8943D; font-size:16px'> $content </div>	
		</body>
	</html>";
	
	$send = mail($email, $subject, $message, $header);
																	
	if($send) return stato_json('email_send');
		
	else return stato_json('email_nosend');		
}

function calcola ($coordinate_a, $coordinate_b){
	define('RAGGIO_QUADRATICO_MEDIO', 6372.795477598);  
	$PI = 3.1415927;

	list ($gradi_lat_a, $gradi_lon_a) = explode(",", $coordinate_a);
	list ($gradi_lat_b, $gradi_lon_b) = explode(",", $coordinate_b);
	//converto i gradi in radianti
	$radianti_lat_a = $PI * $gradi_lat_a /180;
	$radianti_lon_a = $PI * $gradi_lon_a /180;

	$radianti_lat_b = $PI * $gradi_lat_b /180;
	$radianti_lon_b = $PI * $gradi_lon_b /180;
	
	
	
	$angle = 2 * asin(sqrt(pow(sin(($radianti_lat_b-$radianti_lat_a) / 2), 2) +
    cos($radianti_lat_a) * cos($radianti_lat_b) * pow(sin(($radianti_lon_b-$radianti_lon_a)  / 2), 2)));
	
	$phi = abs($radianti_lon_a - $radianti_lon_b);
	$P = acos((sin($radianti_lat_a)*sin($radianti_lat_b))+(cos($radianti_lat_a)*cos($radianti_lat_b)*cos($phi)));
		
	return $P * RAGGIO_QUADRATICO_MEDIO ;
}

function traduci($indirizzo){
	$stato = false;
	$address = urlencode($indirizzo);
	$url = "http://maps.google.com/maps/api/geocode/json?address=".$indirizzo."&sensor=true";
	$url = str_replace(" ", "%20", $url);
	$content = file_get_contents($url);
	$json = json_decode($content);
	if($json->status == 'OK'){
		$lat = $json->results[0]->geometry->location->lat;
		$lng = $json->results[0]->geometry->location->lng;
		
		echo json_encode(array("lat"=>$lat,"lng"=>$lng));
		$stato = true;
	}
	
	return $stato;
}


//MESSAGGI JSON

function stato_json ($msg){
	switch($msg){
		case 'logged':
			$messaggio = 'logged';
			$stato = true;
			break;
		case 'not_logged':
			$messaggio = 'not logged';
			$stato = false;
			break;
		case 'reg_ok':
			$messaggio = 'registered';
			$stato = true;
			break;
		case 'reg_error':
			$messaggio = 'invalid registration';
			$stato = false;
			break;
		case 'data_error':
			$messaggio = 'data error';
			$stato = false;
			break;
		case 'email_send':
			$messaggio = 'email sent';
			$stato= true;
			break;
		case 'email_nosend':
			$messaggio = 'email not sent';
			$stato = false;
			break;
		case 'user_exists':
			$messaggio = 'user exists';
			$stato = false;
			break;
		case 'bacheca_bianca':
			$messaggio = 'nessun risultato';
			$stato = true;
			break;
		case 'bacheca_error':
			$messaggio = 'errore download';
			$stato = false;
			break;
		case 'no_user_favs':
			$messaggio = $msg;
			$stato = true;
			break;
		case 'error_favs':
			$messaggio = $msg;
			$stato = false;
			break;
		case 'inserimento_ok':
			$messaggio = $msg;
			$stato = true;
			break;
		case 'inserimento_error':
			$messaggio = $msg;
			$stato = false;
			break;
		case 'notfavs':
			$messaggio = $msg;
			$stato = true;
			break;
		case 'favs':
			$messaggio = $msg;
			$stato = true;
			break;
		case 'favs_error':
			$messaggio = 'favs error';
			$stato=false;
			break;
		case 'delete_user':
			$messaggio = 'user deleted';
			$stato = true;
			break;
		case 'no_delete_user':
			$messaggio = 'user not deleted';
			$stato=false;
			break;
		case 'mod_error':
			$messaggio = 'mod error';
			$stato=false;
			break;
		case 'mod_ok':
			$messaggio = 'mod ok';
			$stato=true;
			break;

	}
	$response = array("stato" => $stato, "messaggio" => $messaggio);
	$json = json_encode($response);
	return $json;
}

?>
