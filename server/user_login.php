
<?php
	
	include_once("function.php");

	$email = $_REQUEST['user_email'];
	$password = md5($_REQUEST['user_password']);
	
	$offer_h=array();
	$offer_r=array();
	$search_h=array();
	$search_r=array();
	$response=array();
	$check=0;
	if ($account=check_account($email, $password)){
		
		$table = array('search_home', 'search_room', 'offer_home', 'offer_room', 'user', 'user_research', 'favourite');
		$response=array();
		foreach ($table as $t){
			$data = array();
			if ($t=='user') $download_all = mysql_query("Select distinct user_firstname, user_lastname, user_avatar from ".$t. " where user_email = '$email'", $connessione);
			else $download_all = mysql_query("Select distinct * from ".$t. " where user_email = '$email'", $connessione);
			
		
			if($download_all){

				while($row = mysql_fetch_object($download_all)){
					if ($t == 'favourite'){
							$id = $row->ad_id;
							if (substr($id, 0,2) == "oc"){
								$cat = "offer_home";
							} else if (substr($id, 0,2) == "os"){
								$cat = "offer_room";
							} else if (substr($id, 0,2) == "cc"){
								$cat = "search_home";
							} else if (substr($id, 0,2) == "cs"){
								$cat = "search_room";
							}
		
							$second_query = mysql_query("select * from ".$cat." where ".$cat."_id='$id'", $connessione);
		
							if ($second_query){
								while($row2 = mysql_fetch_object($second_query)){
									if ($cat == "offer_home"){
										$offer_h []= $row2;
									} else if ($cat == "offer_room"){
										$offer_r []= $row2;
									} else if ($cat == "search_home"){
										$search_h []= $row2;
									} else if ($cat == "search_room"){
										$search_r []= $row2;
			
									}
								}
							}
	
							$data = array("offer_room" => $offer_r, "offer_home"=>$offer_h, "search_home"=>$search_h, "search_room"=>$search_r);
					} else {
						$data[] = $row;
					} 
				}
			} else {
				echo stato_json('bacheca_error');
			}
			
			if (($t=='search_home') || ($t=='search_room') || ($t=='offer_home') || ($t=='offer_room')){
				$arr = array($t=>$data);
				$response=array_merge($response, $arr);
				if ($t=='offer_room'){
					$response=array("Ads"=>$response);
				} 
			} else {
				$arr = array($t => $data);
				$response = array_merge($response, $arr);
			}
			
			
			
		}
		
		echo json_encode($response);
	} else {
		echo stato_json('not_logged');
	}
	
?>


