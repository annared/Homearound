<?php
	include_once ("function.php");
	$email = $_REQUEST['user_email'];
	$password = md5($_REQUEST['user_password']);
	
	$latitudine = $_REQUEST['latitudine'];
	$longitudine = $_REQUEST['longitudine'];
	//$cat = $_REQUEST['categoria'];
	
	$latitudine = str_replace('"', "", $latitudine);
	$longitudine = str_replace('"', "", $longitudine); 
	//$cat = str_replace('"', '', $cat);
	$response=array();
	$coordinate = $latitudine .",". $longitudine;
	$cat=array("offer_home", "offer_room");
	if ($account=check_account($email, $password)) {
		foreach($cat as $c){
			$geo_annuncio=array();
			$query_coordinate = mysql_query("SELECT * from ".$c . " where " . $c . "_active = 1", $connessione);
			
			if($query_coordinate){
				
				while ($row = mysql_fetch_object($query_coordinate)){
					$lat_name = $c._lat;
					$lon_name = $c._lng;
		
					$lat = $row->$lat_name;
					$lon = $row->$lon_name;
					$annuncio = $lat.','.$lon;

					$diroom = calcola ($coordinate, $annuncio);
					
					if ($diroom<2.0){
						$geo_annuncio[] = $row;
					}
				}

				$data = array($c => $geo_annuncio);
				$response = array_merge($response, $data);
				//print_r($response)."<br>";
				
			} else {
				echo stato_json('bacheca_error');
			}	
			
		}
		if	(count($response["offer_room"])==0 && count($response["offer_home"])==0){
			echo stato_json ('bacheca_bianca');
		} else {
			echo json_encode($response);
		}
		
	} else {
		echo stato_json('data_error');
	}
	
	
?>
