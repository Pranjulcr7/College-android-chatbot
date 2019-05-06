<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    getList();
}

function getList(){
    
    global $connect;
    $class_id = $_POST["class_id"];
    
    $query ="SELECT student_id, student_fname, student_lname FROM student WHERE class_id='$class_id';";
    
   if($result = mysqli_query($connect, $query)){
       
        $number_of_rows = mysqli_num_rows($result);
        $temp_array  = array();
    
    if($number_of_rows > 0) {
        
        while ($row = mysqli_fetch_assoc($result)) {
            
            $index["id"]=$row["student_id"];
            $index["fname"]=$row["student_fname"];
            $index["lname"]=$row["student_lname"];
            $temp_array[] = $index;
        }
    }
    header('Content-type: application/json');
    echo json_encode(array("students"=>$temp_array));
   }
    mysqli_close($connect);
}


?>