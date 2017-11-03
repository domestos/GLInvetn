<?php
 
$response = array();
 
if (isset($_POST['id']) ) {
 
    $id = $_POST['id'];
    $number = $_POST['number'];
    $owner = $_POST['owner'];
    $location = $_POST['location'];
    $description = $_POST['description'];
 
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("UPDATE wp_inventory SET owner = '$owner', location = '$location', description = '$description' WHERE id = $id");
 
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
 
        echo json_encode($response);
    } else {
 
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>