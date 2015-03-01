<?php  
	include_once("function.php");
	$json = $_REQUEST['json'];
	$json=	str_replace("%22","\"", $json);

	$risultato = json_decode($json);
	
	$categoria = $risultato->categoria;
	$email = $risultato->user_email;
	$password = $risultato->user_password;
	$password = md5($password);
	
	unset($risultato->user_password);
	unset($risultato->user_email);
	unset($risultato->categoria);

	if ($account=check_account($email, $password)){
		foreach($risultato as $key=>$valore){

			if($key==$categoria."_id"){
				$id = $valore;	
			} else {
				$sql[] = $key."="."'".$valore."'";
			}
			
		}

		$sqlclause = implode(", ",$sql);
		$query_mod = mysql_query("UPDATE ".$categoria." SET ".$sqlclause." WHERE ".$categoria."_id='$id'", $connessione);
		
		if ($query_mod) {
			echo stato_json('inserimento_ok');
			
		}else {
			echo stato_json('inserimento_error');
		}
	} else {
		echo stato_json('data_error');
	}
?>
