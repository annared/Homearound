<?php 
	$uploaddir = './images/';
	$file = basename($_FILES['uploadedfile']['name']);
	$uploadfile = $uploaddir . $file;
	//echo "file=".$file;
	if (move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $uploadfile)) {
	   //echo "http://www.redcouch.it/homearound/server/images/{$file}";
	} 	
?>

