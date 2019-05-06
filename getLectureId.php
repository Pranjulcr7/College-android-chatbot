<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
    
    require 'connection.php';
    getAttendanceList();
}

function getAttendanceList(){
    
    global $connect;
    $sec = $_POST["sec"];
    $dept = $_POST["dept"];
    $prog= $_POST["prog"];
    $batch = $_POST["batch"];
    $code = $_POST["code"];
    $name = $_POST["name"];
    $id = $_POST["id"];
    $date = date("Y-m-d");
    
    $query = "insert into lecture( class_id, course_code, course_name, lecture_date, faculty_id) 
        values((select class_id from class where section='$sec' and program='$prog' and department='$dept' and batch='$batch')
            , '$code', '$name', '$date', '$id' );";
    
    $query .="SELECT lecture_id, class_id, course_code, course_name FROM lecture ORDER BY lecture_id DESC LIMIT 1;";
    
    if( mysqli_multi_query($connect, $query)){
        
        $result['lecture_id'] = "01";
        
        if($response = mysqli_store_result($connect)){
           
           while($row = mysqli_fetch_row($response)){
            
            
        }
        mysqli_free_result($response);
       if(mysqli_next_result($connect)){
       }
       }
       if($response = mysqli_store_result($connect)){
           
           while($row = mysqli_fetch_row($response)){
            
            
        }
        mysqli_free_result($response);
       }
        if(mysqli_next_result($connect)){
       if($response = mysqli_store_result($connect)){
           
           while($row = mysqli_fetch_row($response)){
            
            $result["lecture_id"] = $row[0];
            $result["class_id"] = $row[1];
            $result["course_code"]= $row[2];
            $result["course_name"]= $row[3];
        }
        mysqli_free_result($response);
       }
        }
       $result["success"] = "1";
       $result["error"] = "Query executed!";
       header('Content-type: application/json');
            echo json_encode($result);
    }
    else{
            
            $result["success"] = "0";
            $result["error"] = mysqli_error($connect);
            header('Content-type: application/json');
            echo json_encode($result);
        }
    $connect->close();
}


?>