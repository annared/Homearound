<?php
	include_once("function.php");
	$uploaddir = './images/';
	
	$json = $_REQUEST['json'];
	$json=	str_replace("%22","\"", $json);

	$risultato = json_decode($json);
	$email = $risultato->user_email;
	$password = $risultato->user_password;
	$password = md5($password);
	$check = 0;
	if ($account=check_account($email, $password)){
		
		foreach($risultato as $key=>$valore){
			if ($key=='new_password'){
				
				$query = mysql_query("UPDATE user set user_password =\"".md5($valore)."\" where user_email='$email'", $connessione);
				if (!$query) $check+=1;
				else $check-=1;
				
			} 
			if ($key=='user_avatar'){
				$q = mysql_query("SELECT user_avatar from user where user_email='$email'", $connessione);
				
				$row = mysql_fetch_assoc($q);
				$uuid = $row['user_avatar'];
				
				$query = mysql_query("UPDATE user set user_avatar=\"".$valore."\" where user_email='$email'", $connessione);
				if ($query){
					if($uuid!="default.png"){
						 //unlink($uploaddir.$uuid.".png");
					}
					$check+=1;
				} else {
					$check-=1;		
				}
			}
		}
		if (check>=0) echo stato_json("mod_ok");
		else echo stato_json("mod_error");
	} else {
		echo stato_json('data_error');
	}
?>