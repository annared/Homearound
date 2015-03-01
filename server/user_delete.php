<?php
	
	include_once("function.php");

	$email = $_REQUEST['user_email'];
	$password = md5($_REQUEST['user_password']);

	if($account=check_account($email,$password)){
		$query = mysql_query("DELETE from user where user_email='$email'", $connessione);
		if ($query){
			echo stato_json('delete_user');				
		} else {
			echo stato_json('no_delete_user');
		}
				
	}else {
		echo stato_json('data_error');
	}

?>