<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    loginFaculty();
}

function loginFaculty(){
    
    global $connect;
    $sid = $_POST["fac_id"];
    $spass = $_POST["fac_password"];
    
    $query ="SELECT * FROM faculty_login_details WHERE faculty_id = '$sid';";
    $result = array();
    $result['login']=array();
    $response = $connect->query($query);
    if(!$connect->connect_error){
        
        $row = $response->fetch_assoc();
        
        if($spass == $row['faculty_password']){
            
            $index['name'] = $row['faculty_fname'];
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