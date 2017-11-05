<html>
    <head>
        <title>Perfil | MEI</title>
        <?php include '../resourses/header.html'; ?>
        <script type="text/javascript">
            console.log("perfil|");
        </script>
    </head>
    <body>
        <?php include '../resourses/menu.html'; ?>
        <?php 
            include '../conn.php';
            $ext = array(".jpg",".jpeg",".png",".gif");
            $fileext=NULL;
            $query = "SELECT * FROM usuarios WHERE (email = '".$_SESSION['user']."')";
            $result = mysqli_query($con,$query);
            $usuario = mysqli_fetch_array($result, MYSQLI_ASSOC);
            
            foreach($ext as &$val){
                if(file_exists("../resourses/profile_pics/".$usuario["id"].$val)){
                    $fileext = $val;
                    break;
                }
            }

            if($fileext){
                echo '<img id="profile-img" src="../resourses/profile_pics/'.$usuario["id"].$fileext.'" alt="">';
            }else{
                echo '<img id="profile-img" src="../resourses/profile_pics/0.png" alt="">';
            }

            echo '<br><span id="profile-nombre">'.$usuario["nombre(s)"]." ".$usuario["apellido(s)"].'</span><br>';
            echo '<span id="profile-correo">'.$usuario["email"].'</span><br>';
            echo '<span id="profile-edad">'.$usuario["edad"].'</span><br>';
            echo '<span id="profile-ciudad">'.$usuario["ciudad"].'</span><br>';
            
            echo '<span>Examenes aplicados</span><br>';

            $query = "SELECT * FROM aplicacion_examen INNER JOIN examenes ON aplicacion_examen.IdExamen=examenes.id WHERE (UsrEmail = '".$usuario["email"]."')";
            $result = mysqli_query($con,$query);

            echo "<table id='examen-table' class='striped'>
                    <thead>
                        <tr>
                            <th>Examen</th>
                            <th>Fecha</th>
                            <th>Resultado</th>
                        </tr>
                    </thead>";
            while ($examen = mysqli_fetch_array($result, MYSQLI_NUM)){
                    echo 
                    "<tr>
                        <td>".$examen[6]."</td>
                        <td>".$examen[3]."</td>
                        <td>".$examen[4]."</td>
                    </tr>";
            }

            echo '</table>';
            
        ?>
        <form enctype="multipart/form-data" action="" method="post" style="display:none">
            <input type="file" name="img" id="input-img">
        </form>
        <?php include '../resourses/footer.html'; ?>
        <?php include '../resourses/scripts.html';
            if(isset($_FILES["img"])){
                $imageFileType = pathinfo($_FILES["img"]["name"], PATHINFO_EXTENSION);
                if($imageFileType == "jpg" || $imageFileType == "png" || $imageFileType == "jpeg" || $imageFileType == "gif"){
                    foreach($ext as &$val){
                        if(file_exists("../resourses/profile_pics/".$usuario["id"].$val)){
                            $fileext = $val;
                            break;
                        }
                    }
                    if($fileext){
                        unlink("../resourses/profile_pics/".$usuario["id"].$fileext);
                    }
                    if(move_uploaded_file($_FILES["img"]["tmp_name"], "../resourses/profile_pics/".$usuario["id"].".".$imageFileType)){
                        echo "<script> Materialize.toast('Se ha actualizado la imagen', 3000, 'rounded');
                                $('#profile-img').attr('src','../resourses/profile_pics/".$usuario["id"].".".$imageFileType."');
                            </script>";
                    }else{
                        echo "<script> Materialize.toast('No se ha podido actualizar la imagen...', 3000, 'rounded');</script>";
                    }
                }else{
                    echo "<script> Materialize.toast('El archivo subido no es una imagen...', 3000, 'rounded');</script>";
                }
            }
        ?>
        
        <script>
            $("#profile-img").click(e => {
                $("#input-img").click();
            });
            $("#input-img").change(e => {
                $(e.target).parent().submit();
            });
        </script>
    </body>
</html>