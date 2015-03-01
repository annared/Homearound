<?php  
	include_once("function.php");
	$json = $_REQUEST['json'];
	$json=	str_replace("%22","\"", $json);
	
	$risultato = json_decode($json);
	$email = $risultato->user_email;
	$password = $risultato->user_password;
	
	$password = md5($password);
	if($account=check_account($email, $password)){
			$table = $risultato->research_cat;
			$ad=array();
			foreach($risultato as $key=>$valore){
				if ($valore!="None" && $valore!="none"){
					if ($key=='research_city'){
					$sql[] = $table."_city="."'".$valore."'"; 	
					} 
					if ($key=='research_price'){
						if ($table=='search_home' || $table=='search_room'){
							$sql[] = $table."_maxprice<="."'".$valore."'"; 
						} else {
							if ($valore!='0' && $valore!='0.00') $sql[] = $table."_price<="."'".$valore."'"; 
						}
							
					} 
					if ($key=='research_area'){
						$sql[] = $table."_area="."'".$valore."'"; 	
					}
					if ($key=='research_type'){
						$sql[] = $table."_type="."'".$valore."'"; 	
					}  
				}
				
			}
			
			if(count($sql)==0){
				$query = mysql_query("SELECT * from ".$table);
			} else {
				$sqlclause = implode(" AND ", $sql);
				$sqlclause=	str_replace("%20"," ", $sqlclause);
				$query = mysql_query("SELECT * from ".$table." where ".$sqlclause);
			}
		
			if ($query){
				while($row = mysql_fetch_object($query)){
					$ad[] = $row;
				}
				if (count($ad)==0){
					echo stato_json('bacheca_bianca');
				} else {
					echo json_encode(array($table => $ad));
				}
			}
		
	} else {
		echo stato_json('data_error');
	}
?>
