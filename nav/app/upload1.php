<?php
	/*---------------------------------------------------------------------------------------
	read in parameters from url
	---------------------------------------------------------------------------------------*/
	$userName     = $_REQUEST["username"];	
	$password     = $_REQUEST["password"];       
	$id           = $_REQUEST["id"];	
	$dateEPOCH    = $id;
	$latitude     = doubleval($_REQUEST["latitude"]);
	$longitude    = doubleval($_REQUEST["longitude"]);  
	$quality      = $_REQUEST["quality"];	
	$agency       = $_REQUEST["agency"];	
	$filepath     = "http://dotsc.ugpti.ndsu.nodak.edu/RIC/images/$id.jpg";	
	$filename     = $_REQUEST['filename'];
	$base         = $_REQUEST['image'];

	if($userName == null )
	{
		die("Invalid Username");
	} 
	if($password == null)
	{
		die("Invalid Password");
	}
	if($id == null)
	{
		die("Invalid Picture Name");
	}
	if($dateEPOCH == null)
	{
		die("Invalid Date");
	}
	if($latitude == null)
	{
		die("Invalid Latitude");
	}
	if($longitude == null)
	{
		die("Invalid longitude");
	}
	if($quality == null)
	{
		die("Invalid quality");
	}
	if( $agency == null)
	{
		die("Invalid Agency");
	}
	if( $filename == null)
	{
		die("Invalid Filename");
	}
	if( $base == null)
	{
		die("Invalid Image");
	}



/***************************************************************************************
	output all varables
***************************************************************************************/
	// echo "userName:    $userName";
	// echo "password:    $password <br>";
	// echo "id:          $id <br>";
	// echo "date:        $date <br>";
	// echo "geolocation: $latitude , $longitude <br>"; 
	// echo "quality:     $quality <br>";
	// echo "agency:      $agency <br>";
	// echo "filepath:    $filepath <br>";

	error_reporting(-1);
	ini_set('display_errors', 'On');

/***************************************************************************************
	convert epoch time to datetime data type
***************************************************************************************/
$seconds = round($dateEPOCH / 1000);
$dt = new DateTime("@$seconds");  // convert UNIX timestamp to PHP DateTime
$datetime = $dt->format('Y-m-d H:i:s'); // output = 2012-08-15 00:00:00 


/***************************************************************************************
	upload picture
***************************************************************************************/
	$binary=base64_decode($base);
	header('Content-Type: bitmap; charset=utf-8');
	// Images will be saved under 'www/imgupload/uplodedimages' folder
	$file = fopen('images/'.$filename, 'wb');
	// Create File
   	fwrite($file, $binary);
	fclose($file);



/***************************************************************************************
	Connnect to sever
***************************************************************************************/
	$serverName = "dotsc-data.ugpti.ndsu.nodak.edu"; //Server Name for Database
	$connectionInfo = array("Database"=>"RIC", "UID"=>"$userName", "PWD"=>"$password"); //Specifying the Database and Authentication (populate via android)
	$conn = sqlsrv_connect( $serverName, $connectionInfo); //Connect to database

	if(! $conn ) {
	     die("Database Connection Failed");
	}


/***************************************************************************************
	insert into database
***************************************************************************************/
	$sql_insert = "INSERT INTO VirtualCapture (CollectionDateTime, GeoLocation, FilePath, Quality, AgencyName)
					VALUES('$datetime', geography::Point($latitude, $longitude, 4326), '$filepath', '$quality' , '$agency')";
					
	$stmt = sqlsrv_query($conn, $sql_insert);
	if($stmt === FALSE)
	{
	    die("Database Insertion Faileed");
	}


	if(file_exists ( "images/" . $filename ))
	{
		echo "TRUE";

	}
	else {
		echo "Image Upload Failed";
	}
?>