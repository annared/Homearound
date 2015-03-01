<?php
	include_once("function.php");
	
	$email = $_REQUEST['user_email'];
	$password = md5($_REQUEST['user_password']);
	$nome = $_REQUEST['user_firstname'];
	$cognome = $_REQUEST['user_lastname'];
	$avatar = $_REQUEST['user_avatar'];

	if (!check_user($email, $password)){
		$query = mysql_query("INSERT INTO user (user_firstname, user_lastname, user_email, user_password, user_avatar) VALUES ('$nome', '$cognome','$email', '$password', '$avatar')", $connessione);
		
		if ($query){
			$active = md5($email);
			echo account_activation($email,$active,"reg");
		} else{ 
			echo stato_json('reg_error');
		}
		
	} else {
		echo stato_json('user_exists');
	}
	
?>