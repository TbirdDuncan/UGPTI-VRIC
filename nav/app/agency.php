<?php



/***************************************************************************************
	Connnect to sever
***************************************************************************************/
	$serverName = "dotsc-data.ugpti.ndsu.nodak.edu"; //Server Name for Database
	$connectionInfo = array("Database"=>"RIC", "UID"=>"AssetManagement", "PWD"=>"gem7Nuwe"); //Specifying the Database and Authentication (populate via android)
	$conn = sqlsrv_connect(dotsc-data.ugpti.ndsu.nodak.edu, $connectionInfo); //Connect to database

	if(! $conn ) {
	     die("Database Connection Failed");
	}


/***************************************************************************************
	insert into database
***************************************************************************************/

	$sql4= pg_query($conn,"select * from Agency ");


     	//print_r($sql4);
     	?>
        	<select name = "did">
                 <option value="0">select doctor</option>
    <?php   while ( $row4 = pg_fetch_array($sql4))  {
		$ldid = $row4[1];
		$did = $row4[0];
		$ldid = $did;
 <option onclick="test('<?php echo $ldid;   ?>');" id="<?php echo $i++."der" ;?>"> <?php echo "$ldid";?></option>


       <?php  }








?>