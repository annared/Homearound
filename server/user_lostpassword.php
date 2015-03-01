<?php  
	include_once("function.php");
	$email = $_REQUEST['user_email'];
	str_replace('"',"", $email);
	
	$query = mysql_query("select user_password, user_active from user where user_email='$email'", $connessione);
	
	if ($query) {
		$active = uniqid();
		echo account_activation($email, $active, "psw");
		$newpsw = md5($active);
		$second_query=mysql_query("update user set user_password='$newpsw' where user_email='$email'", $connessione);
		
	} else {
		echo stato_json('data_error');
	}
	
?>