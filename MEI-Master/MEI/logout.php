<?php
    session_start();
    $_SESSION['user'] = array();
    session_destroy();
    header("Location: ../index.php");
?>