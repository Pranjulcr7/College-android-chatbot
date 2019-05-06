<?php


    include 'connection.php';
    validate();


function validate(){
    
    global $connect;
    $id = '16ETCS002153';

    $query = " Select student_fname from student where student_id='$id';";

    $result = $connect->query($query) ;
    
    $number_of_rows = $result->num_rows;
    $temp_array  = array();
    
    if($number_of_rows > 0) {
        while ($row = mysqli_fetch_assoc($result)) {
            $temp_array[] = $row;
        }
    }
    
    header('Content-type: application/json');
    $temp_array["success"]="1";
    echo json_encode($temp_array);
    $connect->close();
}


?>
