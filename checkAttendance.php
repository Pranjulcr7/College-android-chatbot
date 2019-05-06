<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    checkAttend();
}

function checkAttend(){
    
    global $connect;
    $std_id = $_POST["student_id"];
    
    $query ="SELECT attendance_status, course_code, course_name FROM attendance WHERE student_id='$std_id' ORDER BY course_code;";
    
   if($result = mysqli_query($connect, $query)){
       
        $number_of_rows = mysqli_num_rows($result);
        $temp_array  = array();
    
    if($number_of_rows > 0) {
        
        while ($row = mysqli_fetch_assoc($result)) {
            
            $index["attendance"]=$row["attendance_status"];
            $index["course"]=$row["course_code"];
            $index["name"]=$row["course_name"];
            $temp_array[] = $index;
        }
    }
    header('Content-type: application/json');
    echo json_encode(array("students"=>$temp_array));
   }
    mysqli_close($connect);
}


?>