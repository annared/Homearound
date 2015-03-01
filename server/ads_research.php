<?php
	include_once("function.php");
	
	$json = $_REQUEST['json'];
	
	$risultato = json_decode($json);
	$categoria = $risultato->Cat;
	$email = $risultato->user_email;
	$password = md5($risultato->user_password);
	unset($risultato->user_password);
	unset($risultato->user_email);
	unset($risultato->Cat);
	
	if ($account=check_account($email, $password)) {
		foreach($risultato as $key=>$valore){
			if (($key == $categoria."_price") or ($key == $categoria."_maxprice")){
				$sql[] = $key."<="."'".$valore."'"; 
			} else if (($key == $categoria."_minprice") or ($key == $categoria."_mq") ){
				$sql[] = $key.">="."'".$valore."'";
			}else if($key==$categoria."_type"){
			
				if ($valore=='Monolocale'||$valore=="One Room") $sql[] = $key."='Monolocale' OR ".$key."='One Room'";
				if ($valore=='Bilocale'||$valore=="Two Rooms") $sql[] = $key."='Bilocale' OR ".$key."='Two Rooms'";
				if ($valore=='Trilocale'||$valore=="Three Rooms") $sql[] = $key."='Trilocale' OR ".$key."='Three Rooms'";
				if ($valore=='Quadrilocale'||$valore=="Four Rooms") $sql[] = $key."='Quadrilocale' OR ".$key."='Four Rooms'";
				if ($valore=='Singola'||$valore=="Single Room") $sql[] = $key."='Singola' OR ".$key."='Single Room'";
				if ($valore=='Doppia'||$valore=="Double Room") $sql[] = $key."='Bilocale' OR ".$key."='Double Room'";
				if ($valore=='Tripla'||$valore=="Triple Room") $sql[] = $key."='Trilocale' OR ".$key."='Triple Room'";
				if ($valore=='Quadrupla'||$valore=="Quadruple Room") $sql[] = $key."='Quadrupla' OR ".$key."='Quadruple Room'";
				
			} else {
				$sql[] = $key."="."'".$valore."'";
			}
		}
		
		if (count($sql)==0){
			$query_ricerca = mysql_query("select * from ".$categoria);
		} else {
			$sqlclause = implode(" AND ", $sql);
			$query_ricerca = mysql_query("SELECT * FROM ".$categoria." WHERE ".$sqlclause ." AND ".$categoria . "_active = 1 order by " . $categoria ."_date");
		}
		
		$annuncio=array();
		if ($query_ricerca){
			while($row = mysql_fetch_object($query_ricerca)){
				$annuncio[] = $row;
			}
			if (count($annuncio)==0){
				echo stato_json('bacheca_bianca');
			} else {
				echo json_encode(array($categoria => $annuncio));
			}
		}
		
	} else {
		echo stato_json('data_error');
	}

?>	