<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    getList();
}

function getList(){
    
    global $connect;
    $lid = $_POST["lecture_id"];
    $sid = $_POST["student_id"];
    $a = $_POST["attendance"];
    $cc = $_POST["course_code"];
    $cn = $_POST["course_name"];
    
    $query ="INSERT INTO attendance VALUES('$lid','$sid','$a','$cc','$cn');";
    
   if($result = mysqli_query($connect, $query)){
    
    $r["message"]='1';
    header('Content-type: application/json');
    echo json_encode($r);
   }
   else{
       
       $r["message"]=mysqli_error($connect);
    header('Content-type: application/json');
    echo json_encode($r);
   }
    mysqli_close($connect);
}


?>