<?php

	include_once("function.php");
	
	$indirizzo = $_REQUEST['address'];
	$citta = $_REQUEST['city'];

	$address = urlencode($indirizzo);
	$url = "http://maps.google.com/maps/api/geocode/json?address=".$indirizzo."&sensor=true";
	$url = str_replace(" ", "%20", $url);
	$content = file_get_contents($url);
	$json = json_decode($content);
	if($json->status == 'OK'){
		$lat = $json->results[0]->geometry->location->lat;
		$lng = $json->results[0]->geometry->location->lng;
			
		echo json_encode(array("lat"=>$lat,"lng"=>$lng));
		
	}

?>