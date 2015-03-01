<?php
	include_once("function.php");
	
	$email = $_REQUEST['user_email'];
	$id = $_REQUEST['research_id'];
	$password = md5($_REQUEST['user_password']);

	if($account=check_account($email, $password)){
		$query_del=mysql_query("delete from user_research where user_email='$email' and research_id = '$id'", $connessione);
		
		if ($query_del) {
			echo stato_json('inserimento_ok');
		}else {
			echo stato_json('inserimento_error');
		}
	} else {
		echo stato_json('data_error');
	}
?>
