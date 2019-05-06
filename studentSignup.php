<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    createstudent();
}

function createstudent(){
    
    global $connect;
    $sid = $_POST["student_id"];
    $sfname = $_POST["student_fname"];
    $slname = $_POST["student_lname"];
    $semail = $_POST["student_email"];
    $sphone = $_POST["student_phone"];
    $spass =  $_POST["student_password"];
    
    $query = "INSERT INTO student_login_details VALUES( (SELECT student_id FROM student WHERE student_email = '$semail'), '$sphone', '$spass','$sfname', '$slname' );";
    
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

