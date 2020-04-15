get_spinner.php
<?php
include_once './DbConnect.php';

function getCategories(){
    $db = new DbConnect();
    // array for json response
    $response = array();
    $response["agencies"] = array();

    // Mysql select query
    $result = mysql_query("SELECT * FROM agencies");

    while($row = mysql_fetch_array($result)){
        // temporary array to create single category
        $tmp = array();
        $tmp["state"] = $row["State"];
        $tmp["Name"] = $row["Name"];

        // push category to final json array
        array_push($response["Agencies"], $tmp);
    }

    // keeping response header to json
    header('Content-Type: application/json');

    // echoing json result
    echo json_encode($response);
}

getCategories();
?>