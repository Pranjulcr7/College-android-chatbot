<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    loginstudent();
}

function loginstudent(){
    
    global $connect;
    $sid = $_POST["student_id"];
    $spass = $_POST["student_password"];
    
    $query ="SELECT * FROM student_login_details WHERE student_id = '$sid';";
    $result = array();
    $result['login']=array();
    $response = $connect->query($query);
    if(!$connect->connect_error){
        
        $row = $response->fetch_assoc();
        
        if($spass == $row['student_password']){
            
            $index['name'] = $row['student_fname'];
            array_push($result['login'],$index);
            $result["success"] = "1";
            $result["error"] = "success";
            header('Content-type: application/json');
            echo json_encode($result);
        }
        else{
            
            $result["success"] = "0";
            $result["error"] = "Password or id do not match!";
            header('Content-type: application/json');
            echo json_encode($result);
        }
    }
    $connect->close();
}


?>