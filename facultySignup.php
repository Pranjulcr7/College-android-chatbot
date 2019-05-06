<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    createFaculty();
}

function createFaculty(){
    
    global $connect;
    $id = $_POST["fac_id"];
    $fname = $_POST["fac_fname"];
    $lname = $_POST["fac_lname"];
    $email = $_POST["fac_email"];
    $phone = $_POST["fac_phone"];
    $pass =  $_POST["fac_password"];
    
    $query = "INSERT INTO faculty_login_details VALUES( (SELECT faculty_id FROM faculty WHERE faculty_email = '$email'), '$pass', '$fname','$lname', '$phone' );";
    
    if ( $connect->query($query) === TRUE ) {
        
        $result["success"] = "1";
        $result["message"] = "success";
        header('Content-type: application/json');
        echo json_encode($result);

    } else {

        $result["success"] = "0";
        $result["message"] = mysqli_error($connect);
        header('Content-type: application/json');
        echo json_encode($result);
    }
    $connect->close();
}


?>

