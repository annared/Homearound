<?php  
	include_once("function.php");
	$json = $_REQUEST['json'];

	$risultato = json_decode($json);
	$categoria = $risultato->categoria;
	$email = $risultato->user_email;
	$password = $risultato->user_password;

	$password = md5($password);
	unset($risultato->categoria);
	unset($risultato->user_password);

	if ($account=check_account($email, $password)){
		foreach($risultato as $key=>$valore){
			
			$sql[] = $key."="."'".$valore."'";
		}
		$sqlclause = implode(", ",$sql);
		$query_inserimento = mysql_query ("INSERT INTO ".$categoria." SET ".$sqlclause, $connessione);
		
		if ($query_inserimento) {
			echo stato_json('inserimento_ok');
			//check_research($valore, $categoria);
		}else {
			echo stato_json('inserimento_error');
		}
	} else {
		echo stato_json('data_error');
	}
?>
