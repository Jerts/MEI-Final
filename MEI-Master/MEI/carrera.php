<html>
    <head>
        <title><?php echo $_GET["carrera"] ?> | MEI</title>
        <?php include '../resourses/header.html'; ?>
        <script type="text/javascript">
            console.log("carrera|");
        </script>
    </head>
    <body>
        <?php include '../resourses/menu.html'; ?>
            <?php
                $carrera = $_GET['carrera'];
                $uni = $_GET['uni'];
                //echo "".$carrera."<br>".$uni."";
                $queryfoto = mysqli_query($con,"SELECT (uni_foto) FROM universidades WHERE nombre = '{$uni}'");

                $foto = mysqli_fetch_array($queryfoto, MYSQLI_NUM);
                echo '
                <div class="parallax-container" style="height: 100vh">
                    <div class=""><img class="carrera_img" src="'.$foto[0].'" height="100%"></div>
                </div>';
            ?>
            <div id="container" style="display:block;">
                <div class="container">
                    <?php
                        $query = "SELECT * FROM carreras WHERE (nombre = '$carrera' AND NombreUni = '$uni')";
                        $result = mysqli_query($con,$query);

                        while ($registro = mysqli_fetch_array($result, MYSQLI_ASSOC)){
                            $uni = mysqli_fetch_array(mysqli_query($con,"SELECT * FROM universidades INNER JOIN institucion ON institucion.id = universidades.idInstitutucion WHERE nombre =  '".$registro['NombreUni']."'"), MYSQLI_ASSOC);
                            echo '
                            <div class="card white-1">
                                <div class="card-content">
                                    <span class="card-title carrera"><h3>'.$registro['nombre'].'</h3></span>
                                    <span class="carrera">'.$registro['NombreUni'].'</span><br>
                                    <span class="carrera">'.$uni['name'].'</span><br>
                                    <span class="carrera">'.$registro['AreaNombre'].'</span>
                                    <h5>Descripción</h5>
                                    <span class="carrera">'.$registro['descripcion'].'</span>
                                    <h5>Plan de estudios</h5>
                                    <span class="carrera">'.$registro['planestudios'].'</span>
                                    <h5>Becas</h5>
                                    <span class="carrera">'.$uni['becas'].'</span>
                                    <h5>Perfil de egreso</h5>
                                    <span class="carrera">'.$registro['perfilegreso'].'</span>
                                    <h5>Intercambios</h5>
                                    <span class="carrera">'.$registro['intercambio'].'</span>
                                </div>
                            </div>';
                        }
                    ?>
                </div>
            <?php include '../resourses/footer.html'; ?>
        </div>
        <?php include '../resourses/scripts.html'; ?>
        <script>
            var cardOffset=570;

            $("body").css("height",document.body.scrollHeight-document.getElementById("container").scrollHeight);
            $("#container").offset({top: cardOffset+(-window.pageYOffset/2)});
            $(window).scroll(e => {
                $("#container").offset({top: cardOffset+(-window.pageYOffset/2)});
            });

            (function(a){(jQuery.browser=jQuery.browser||{}).mobile=/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))})(navigator.userAgent||navigator.vendor||window.opera);

            if(jQuery.browser.mobile){
                console.log("Es mobil");
                $('#modalMobile').modal('open');
                cardOffset=200;
                $("#container").offset({top: cardOffset+(-window.pageYOffset/2)});
            }
        </script>
    </body>
</html>
