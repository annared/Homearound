<!DOCTYPE html>
<html lang="en">
<head>
  <title>HOME AROUND</title>
  <meta charset="utf-8">
  <link rel="stylesheet" href="style.css" type="text/css" media="all">
 </head>

<body>
  <header>

	</header>

  <section id="description">

  </section>

  <div class="main-box">

  <div class="due">
    

            <article>
				<?php
				
					include_once("function.php");
				
					$email = $_REQUEST['email'];
					$active = $_REQUEST['active'];
					
					$query_check=mysql_query("SELECT user_active FROM user WHERE user_email='$email'",$connessione);
					
					$row = mysql_num_rows($query_check);
					
					if($query_check && $row==1){
													
						$user = mysql_fetch_assoc($query_check);							
						
						if($user['user_active']==0) {
							
							$result = mysql_query("UPDATE user SET user_active='$active' WHERE user_email='$email'");
								
							if($result)	echo "Il tuo account &egrave stato attivato :) <br>Your account has been activated :) ";
							else echo "Account non attivato :( <br> Account not activated. Sorry :(";
								
						}else echo "Questo account risulta gi&agrave attivato :/ <br>This account has already been activated :/";
							
					} else { 
						echo "Account inesistente";	
					}
					
				?>
				
            </article> 
        </div>
  </div>

  <footer>
  </footer>
</body>
</html>

