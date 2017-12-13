<?php
    include '../../conn.php';
    session_start();
    $user = $_SESSION['user'];
    if(isset($_GET["lat"])&&isset($_GET["lng"])){
      $range=$_GET["range"];
      $lat=$_GET["lat"];
      $lng=$_GET["lng"];
    }
    $fechaq = mysqli_query($con,"SELECT id FROM aplicacion_examen WHERE UsrEmail = '$user' ORDER BY id DESC LIMIT 1");
    $fecha = mysqli_fetch_array($fechaq, MYSQLI_NUM);
    $query = "SELECT * FROM exam_recomendacion WHERE UsrEmail = '$user' AND Id_Aplicacion_Examen = '{$fecha[0]}'";
    $result = mysqli_query($con,$query);

    while($registro = mysqli_fetch_array($result, MYSQLI_ASSOC)){
      $univq = mysqli_query($con,"SELECT uni_foto,latitud,longitud FROM universidades WHERE nombre = '{$registro['NombreUni']}'");
      $univ = mysqli_fetch_array($univq, MYSQLI_NUM);
      $uni = mysqli_fetch_array(mysqli_query($con,"SELECT * FROM universidades INNER JOIN institucion ON institucion.id = universidades.idInstitutucion WHERE nombre =  '".$registro['NombreUni']."'"), MYSQLI_ASSOC);
      if(isset($range)){
        if((distancia($lat,$lng,$univ[1],$univ[2])/1000) <= $range){
          echoS($univ,$registro,$uni);
        }
      }else{
        echoS($univ,$registro,$uni);
      }
    }

    function echoS($univ,$registro,$uni){
      echo '
      <div class="card row hoverable">
          <div class="reco_foto col s12 m3" data-content="'.$univ[0].'" style="background:url('.$univ[0].') no-repeat center center;"></div>
          <div class="col s12 m9">
              <div class="card-content">
                  <span class="card-title reco_carrera">'.$registro['Nombre_Carrera'].'</span>
                  <span class="reco_universidad">'.$registro['NombreUni'].'</span><br>
                  <span class="reco_inst">'.$uni['name'].'</span>
              </div>
              <div class="card-action">
                  <a href="carrera.php?carrera='.$registro["Nombre_Carrera"].'&uni='.$registro["NombreUni"].'" class="reco_info">Información</a>
                  <a class="modal-trigger reco_maps" onclick="if(JSI){JSI.loadMap('.$univ[1].','.$univ[2].');}else{modalMap('.$univ[1].','.$univ[2].');}" href="#map-modal">Ubicación</a>
              </div>
          </div>
      </div>';
    }

    function distancia($latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6371000){
      $latFrom = deg2rad(floatval($latitudeFrom));
      $lonFrom = deg2rad(floatval($longitudeFrom));
      $latTo = deg2rad($latitudeTo);
      $lonTo = deg2rad($longitudeTo);

      $latDelta = $latTo - $latFrom;
      $lonDelta = $lonTo - $lonFrom;

      $angle = 2 * asin(sqrt(pow(sin($latDelta / 2), 2) +
        cos($latFrom) * cos($latTo) * pow(sin($lonDelta / 2), 2)));
      return $angle * $earthRadius;
    }
?>
<script type="text/javascript" src="../js/JSI.js"></script>
<script type="text/javascript">
  if(JSI){
    var carrera = document.getElementsByClassName('reco_carrera');
    var uni = document.getElementsByClassName('reco_universidad');
    var inst = document.getElementsByClassName('reco_inst');
    var info = document.getElementsByClassName('reco_info');
    var maps = document.getElementsByClassName('reco_maps');
    var foto = document.getElementsByClassName('reco_foto');
    if(carrera.length){
      for(var i = 0 ; i < carrera.length ; ++i)
        try{JSI.recPage(carrera[i].innerText,uni[i].innerText,info[i].href, i , foto[i].dataset.content, inst[i].innerText);}catch(e){console.log(e);}
    }else{
      //window.HTMLOUT.empty();
    }
  }
</script>
