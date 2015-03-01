<?php
	include_once("function.php");
	$email = $_REQUEST['user_email'];
	$password = md5($_REQUEST['user_password']);
	$id = $_REQUEST['ad_id'];
	
	if ($account=check_account($email, $password)) {
		$query = mysql_query("select * from favourite where user_email='$email' and ad_id='$id'", $connessione);
		if($query){
			$row=mysql_num_rows($query);
			if($row==1){
				echo stato_json('favs');
			} else{
				echo stato_json('notfavs');
			}
		} else {
			echo stato_json('favs_error');
		}
	} else {
		echo stato_json('data_error');
	}

?>
