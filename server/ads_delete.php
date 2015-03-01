<?php
	include_once("function.php");
	
	$email = $_REQUEST['user_email'];
	$password = md5($_REQUEST['user_password']);
	$id = $_REQUEST['ad_id'];
	$categoria = $_REQUEST['categoria'];
	
	if($account=check_account($email, $password)){
		$query_del=mysql_query("delete from ".$categoria." where user_email='$email' and ".$categoria."_id = '$id'", $connessione);
		
		if ($query_del) {
			$second_query = mysql_query("delete from favourite where favourite_id='$id'", $connessione);
			echo stato_json('inserimento_ok');
		}else {
			echo stato_json('inserimento_error');
		}
	} else {
		echo stato_json('data_error');
	}

?>
