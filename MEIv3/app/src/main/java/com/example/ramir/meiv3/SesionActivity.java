package com.example.ramir.meiv3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Arrays;
import java.util.zip.Inflater;

public class SesionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GlobalVars globalVars = new GlobalVars();
    String PagMadre = globalVars.urlMEIMaster()+"MEI/";
    AppBarLayout appBarLayout;
    WebView mei;
    ImageView publicidad, portada, foto, univ_foto;
    TextView tvUniv,tvCarrer,tvDesrip,tvPlan,tvInterc,tvPerfilE,tvArea, tvResultado,tvResultado_detalles,tvNombreUsuario,tvCorreo,tvBecas, tvInst;
    RelativeLayout rlempty, rloffline, rlResultados;
    LinearLayout lyreco, lytest, lytestvoca, lyPerfil;
    TableLayout lyTablaTest;
    ScrollView lyinicio,sv_reco,sv_test,sv_testvoca,sv_perfil,sv_carrera;
    ProgressBar PageLoad;
    Button redirect,btEnviar, btRecomendacion;
    Integer num_radio=0, Rbindex=0;
    MenuItem rangoMenu;
    SeekBar rangeSeekBar;
    Boolean isRecomendacion = false, isCarrera=false;
    Double lat,lng;
    int seekBarValue;
    LocationManager locationManager;
    Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("pag",PagMadre);
        setContentView(R.layout.activity_sesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = (AppBarLayout) findViewById(R.id.ActionBar);
        rangeSeekBar = (SeekBar) findViewById(R.id.rangeSeekBar);

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(SesionActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                Location location = null;
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(locationManager
                            .getBestProvider(criteria, false));
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }
            }
        }

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        checkLocationPermission();

        rangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //mei.loadUrl("javascript:$('#rangeReco').val("+(i+10)+");");
                seekBarValue = i;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isRecomendacion)setTitle(String.valueOf(seekBarValue+10)+" Km.");
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @SuppressLint("MissingPermission")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Location location = null;
                lyreco.removeAllViews();

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(locationManager
                            .getBestProvider(criteria, false));
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    Log.e("Loc",lat.toString()+" - "+lng.toString()+" - "+String.valueOf((seekBarValue)));
                    mei.loadUrl("javascript:ajaxReco(" + lat + "," + lng + "," + (seekBarValue + 10) + ");");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lyreco.removeAllViews();
                            mei.loadUrl("javascript:var carrera = document.getElementsByClassName('reco_carrera');" +
                                    "var uni = document.getElementsByClassName('reco_universidad');" +
                                    "var info = document.getElementsByClassName('reco_info');" +
                                    "var maps = document.getElementsByClassName('reco_maps');" +
                                    "var foto = document.getElementsByClassName('reco_foto');" +
                                    "if(carrera.length){" +
                                    "for(var i = 0 ; i < carrera.length ; ++i)" +
                                    "   window.HTMLOUT.recomienda(carrera[i].innerText,uni[i].innerText,info[i].href, i , foto[i].dataset.content);" +
                                    "}else{" +
                                    "   window.HTMLOUT.empty();" +
                                    "}");
                        }
                    },200);
                }
            }
        });

        /*Web views*/
        mei = (WebView) findViewById(R.id.mei_sesion);
        PageLoad = (ProgressBar) findViewById(R.id.progressBar3);

        /*Text views*/
        tvUniv= (TextView) findViewById(R.id.id_univ);
        tvCarrer= (TextView) findViewById(R.id.id_carrera);
        tvDesrip= (TextView) findViewById(R.id.id_descrip);
        tvPlan= (TextView) findViewById(R.id.id_plan);
        tvInterc= (TextView) findViewById(R.id.id_intercambios);
        tvPerfilE= (TextView) findViewById(R.id.id_perfil_egreso);
        tvArea= (TextView) findViewById(R.id.id_area);
        tvResultado = (TextView) findViewById(R.id.id_resultado);
        tvResultado_detalles = (TextView) findViewById(R.id.id_resu_detalles);
        tvNombreUsuario = (TextView) findViewById(R.id.perfil_usuario);
        tvCorreo = (TextView) findViewById(R.id.perfil_correo);
        tvBecas = (TextView) findViewById(R.id.id_becas);
        tvInst = (TextView) findViewById(R.id.id_inst);

        /*Scroll views*/
        lyinicio = (ScrollView) findViewById(R.id.InicioLayout);
        sv_reco = (ScrollView) findViewById(R.id.sv_reco);
        sv_testvoca =(ScrollView) findViewById(R.id.sv_testvoca);
        sv_test = (ScrollView) findViewById(R.id.sv_test);
        sv_perfil = (ScrollView) findViewById(R.id.sv_perfil);
        sv_carrera = (ScrollView) findViewById(R.id.sv_carrera);

        /*Relative layouts*/
        rlempty = (RelativeLayout) findViewById(R.id.rl_empty);
        rloffline = (RelativeLayout) findViewById(R.id.rl_offline);
        rlResultados = (RelativeLayout) findViewById(R.id.rl_resultado_test);

        /*Botones*/
        redirect = (Button) findViewById(R.id.btredirect);
        btEnviar = (Button) findViewById(R.id.enviarbt);
        btRecomendacion = (Button) findViewById(R.id.bt_Recomendaciones);

        /*Linear Layouts*/
        lyreco = (LinearLayout) findViewById(R.id.RecomLayout);
        lytest = (LinearLayout) findViewById(R.id.TestLayout);
        lytestvoca = (LinearLayout) findViewById(R.id.ly_testvoca);
        lyPerfil = (LinearLayout) findViewById(R.id.ly_perfil);

        /*Image views*/
        foto = (ImageView) findViewById(R.id.perfil_foto);
        portada = (ImageView) findViewById(R.id.perfil_portada);
        publicidad = (ImageView) findViewById(R.id.publicidad);
        univ_foto = (ImageView) findViewById(R.id.univ_foto);

        /*Web views settings*/
        WebSettings webSettings = mei.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        lyTablaTest = (TableLayout) findViewById(R.id.tablaTest);

        /*---------------------------------Web view Client----------------------------------*/

        mei.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                isRecomendacion=false;
                isCarrera=false;

                lyreco.removeAllViews();
                lytestvoca.removeAllViews();
                lytest.removeAllViews();
                lyTablaTest.removeAllViews();

                rangeSeekBar.setVisibility(View.GONE);
                rangeSeekBar.setMax(0);
                rangeSeekBar.setMax(390);
                rangeSeekBar.setProgress(0);
                lyreco.setPadding(0,0,0,0);
                rangoMenu.setVisible(false);

                sv_testvoca.setVisibility(View.GONE);
                sv_perfil.setVisibility(View.GONE);
                sv_carrera.setVisibility(View.GONE);
                lyinicio.setVisibility(View.GONE);
                sv_reco.setVisibility(View.GONE);
                sv_test.setVisibility(View.GONE);

                rlempty.setVisibility(View.GONE);
                rloffline.setVisibility(View.GONE);
                rlResultados.setVisibility(View.GONE);

                PageLoad.setVisibility(View.VISIBLE);

                appBarLayout.getBackground().setAlpha(255);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setTitle(mei.getTitle());
                PageLoad.setVisibility(View.GONE);
                sv_carrera.scrollTo(0,0);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                rloffline.setVisibility(View.VISIBLE);
                Log.e("MenError", error.toString());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setTitle("Sin conexion");
                    }
                },20);
            }
        });

        /*-----------------------------------Chrome client-----------------------------------*/
        mei.setWebChromeClient(new WebChromeClient(){ //ChromeClient para leer la consola de JS

            public boolean onConsoleMessage(ConsoleMessage cm) { //Listener
                String[] msg = {};

                try {
                    msg = cm.message().split("\\|"); //Guardo el mensaje en un array string
                }catch (Exception e) {
                    Log.e("SesionConsole", e.toString());
                }

                Log.i("MensajeSesion", msg[0]);
                if(Arrays.asList(msg).contains("sesion")) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mei.loadUrl("javascript:" +
                                    "   var el = document.getElementById('dataUser');" +
                                    "   var img = el.dataset.picture;" +
                                    "   var name = el.dataset.name;" +
                                    "   var email = el.dataset.email;" +
                                    "   window.HTMLOUT.navbar(img,name,email);");
                        }
                    }, 200);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lyinicio.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                } else if(Arrays.asList(msg).contains("recomendaciones")) {
                    final Handler handler = new Handler();
                    isRecomendacion = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rangoMenu.setVisible(true);
                            mei.loadUrl("javascript:window.onload = rec;" +
                                    "function rec(){" +
                                    "var carrera = document.getElementsByClassName('reco_carrera');" +
                                    "var uni = document.getElementsByClassName('reco_universidad');" +
                                    "var info = document.getElementsByClassName('reco_info');" +
                                    "var maps = document.getElementsByClassName('reco_maps');" +
                                    "var foto = document.getElementsByClassName('reco_foto');" +
                                    "if(carrera.length){" +
                                    "for(var i = 0 ; i < carrera.length ; ++i)" +
                                    "   window.HTMLOUT.recomienda(carrera[i].innerText,uni[i].innerText,info[i].href, i , foto[i].dataset.content);" +
                                    "}else{" +
                                    "   window.HTMLOUT.empty();" +
                                    "}}");
                        }
                    }, 0);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sv_reco.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                } else if(Arrays.asList(msg).contains("resultados")) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mei.loadUrl("javascript:" +
                                    "window.HTMLOUT.test_result(document.getElementById('resu').innerText);" +
                                    "window.HTMLOUT.test_result_detalles(document.getElementById('resu_info').innerHTML)");
                        }
                    }, 200);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rlResultados.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                } else if(Arrays.asList(msg).contains("carrera")) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isCarrera = true;
                            mei.loadUrl("javascript:" +
                                    "var info = document.getElementsByClassName('carrera');" +
                                    "window.HTMLOUT.carrerafoto(document.getElementsByClassName('carrera_img')[0].src);" +
                                    "for(var i = 0 ; i < info.length ; ++i){" +
                                    "   window.HTMLOUT.carrerainfo(info[i].innerHTML,i);" +
                                    "}");
                            appBarLayout.getBackground().setAlpha(0);
                        }
                    }, 200);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sv_carrera.setVisibility(View.VISIBLE);
                        }
                    }, 400);
                } else if(Arrays.asList(msg).contains("test")) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mei.loadUrl("javascript:" +
                                    "var tests = document.getElementsByClassName('test');" +
                                    "for(var i = 0 ; i < tests.length ; ++i)" +
                                    "   window.HTMLOUT.test(tests[i].innerText,tests[i].href);");
                        }
                    }, 200);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sv_test.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                } else if(Arrays.asList(msg).contains("testvoca")) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mei.loadUrl("javascript:" +
                                    "var pregdiv = document.getElementsByClassName('pregs');" +
                                    "var radioB = document.getElementsByClassName('option');" +
                                    "window.HTMLOUT.numradio(radioB.length);" +
                                    "for(var i = 0 ; i < pregdiv.length ; ++i){" +
                                    "   window.HTMLOUT.pregunta(pregdiv[i].getElementsByClassName('preg')[0].innerText);" +
                                    "   window.HTMLOUT.radioGCreate();" +
                                    "   var radioBs = pregdiv[i].getElementsByClassName('option');" +
                                    "   for(var e = 0 ; e < radioBs.length ; ++e){" +
                                    "       window.HTMLOUT.radioCreate(radioBs[e].innerText);" +
                                    "   }" +
                                    "   window.HTMLOUT.separador();" +
                                    "}");
                        }
                    }, 200);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sv_testvoca.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                } else if(Arrays.asList(msg).contains("mapa")){
                    Intent intent = new Intent(SesionActivity.this, MapsActivity.class);
                    intent.putExtra("lat",Double.parseDouble(msg[1]));
                    intent.putExtra("lon",Double.parseDouble(msg[2]));
                    startActivity(intent);
                    mei.loadUrl(PagMadre+"recomendaciones.php");
                } else if(Arrays.asList(msg).contains("perfil")){
                    sv_perfil.setVisibility(View.VISIBLE);
                    mei.loadUrl("javascript: var imgUrl = document.getElementById('dataUser').dataset.picture;" +
                                            "var name = document.getElementById('profile-nombre').innerText;" +
                                            "var email = document.getElementById('profile-correo').innerText;" +
                                            "var edad = document.getElementById('profile-edad').innerText;" +
                                            "var city = document.getElementById('profile-ciudad').innerText;" +
                                            "window.HTMLOUT.perfil(imgUrl,name,email,edad,city);" +
                                            "var test = document.getElementsByClassName('testName');" +
                                            "var fecha = document.getElementsByClassName('testFecha');" +
                                            "var resu = document.getElementsByClassName('testResu');" +
                                            "window.HTMLOUT.perfilTabla('Nombre','Fecha','Resultado');" +
                                            "for(var i = 0 ; i < test.length ; ++i){" +
                                                "window.HTMLOUT.perfilTabla(test[i].innerText,fecha[i].innerText,resu[i].innerText);" +
                                            "}");
                } else if(Arrays.asList(msg).contains("login")){
                    Intent intent = new Intent(SesionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SesionActivity.this.finish();
                }

                return true;
            }

        });

        mei.addJavascriptInterface(new JavaScriptInterface(getApplicationContext()), "HTMLOUT");

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mei.loadUrl("javascript:document.getElementById('boton').click();");
            }
        });

        btRecomendacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mei.loadUrl(PagMadre+"recomendaciones.php");
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv_perfil.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    portada.setY(i1/2);
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv_carrera.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    univ_foto.setY(i1/2);
                    if(isCarrera) {
                        if (i1 < 200)
                            appBarLayout.getBackground().setAlpha(0);
                        if (i1 <= 455 && i1 >= 200)
                            appBarLayout.getBackground().setAlpha(i1 - 200);
                        if (i1 > 455) {
                            appBarLayout.getBackground().setAlpha(255);
                        }
                    }
                }
            });
        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mei.loadUrl(PagMadre);
    }

    private class JavaScriptInterface {
        Context context;

        JavaScriptInterface(Context c) {
            context = c;
        }

        @JavascriptInterface
        public void navbar(final String img, final String name, final String email){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View hView =  navigationView.getHeaderView(0);
                    ImageView imgView = (ImageView) hView.findViewById(R.id.profile_image);
                    TextView textViewName = (TextView) hView.findViewById(R.id.tvName);
                    TextView textViewEmail = (TextView) hView.findViewById(R.id.tvEmail);
                    Picasso.with(getBaseContext()).load(PagMadre+"../resourses/profile_pics/"+img).transform(new CircleTransform()).into(imgView);

                    textViewName.setText(name);
                    textViewEmail.setText(email);
                }
            });
        }

        @JavascriptInterface
        public void test(final String nombre, final String urlTest) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button btTest = new Button(context);
                    btTest.setGravity(3);

                    btTest.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0,255,255,255)));
                    btTest.setTextColor(Color.BLACK);
                    btTest.getBackground().setColorFilter(0x00FFFFFF, PorterDuff.Mode.SRC);

                    ImageView arrow = new ImageView(context);
                    arrow.setBackgroundResource(R.drawable.ic_arrow);

                    RelativeLayout rlTest = new RelativeLayout(context);

                    float density = context.getResources().getDisplayMetrics().density;
                    int dp_10 = (int)(-10 * density);
                    int dp_8 = (int)(-8 * density);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(dp_10,dp_8,dp_10,0);

                    rlTest.addView(arrow);
                    rlTest.addView(btTest,layoutParams);

                    lytest.addView(rlTest);

                    int dp20 = (int)(25 * density);
                    int dp60 = (int)(60 * density);

                    arrow.getLayoutParams().height = dp60;
                    arrow.getLayoutParams().width = dp60;
                    arrow.requestLayout();

                    rlTest.setPadding(0,0,0,0);

                    btTest.setPadding(dp20,dp20,dp60,dp20);

                    RelativeLayout.LayoutParams paramsarrow = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
                    paramsarrow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    paramsarrow.addRule(RelativeLayout.CENTER_VERTICAL);

                    btTest.setText(nombre);

                    btTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mei.loadUrl(urlTest);
                        }
                    });
                }
            });
        }

        @JavascriptInterface
        public void publicidad(String html) {
            String[] msg = html.split("\"");
            final String url = msg[1];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getBaseContext()).load(url).into(publicidad);
                }
            });
        }

        @JavascriptInterface
        public void empty() {
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    if(rangeSeekBar.getVisibility()==View.GONE){
                        rlempty.setVisibility(View.VISIBLE);

                        redirect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mei.loadUrl(PagMadre+"test.php");
                            }
                        });
                    }
                }
            });
        }

        @JavascriptInterface
        public void recomienda(final String Nombre, final String Uni, final String urlInfo,final int iMaps, final String urlFoto) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    float density = context.getResources().getDisplayMetrics().density;

                    CardView cardView =  new CardView(context);
                    cardView.setElevation((int)(3 * density));
                    cardView.setRadius((int)(3 * density));
                    cardView.setCardBackgroundColor(Color.WHITE);
                    cardView.setPadding(16,16,16,16);

                    LinearLayout cvLinLy = new LinearLayout(context);
                    cvLinLy.setOrientation(LinearLayout.VERTICAL);

                    RelativeLayout rlEscuela = new RelativeLayout(context);
                    rlEscuela.setPadding(0,0,0,0);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(22,22,22,22);

                    TextView tvName = new TextView(context);
                    tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                    tvName.setTextColor(Color.BLACK);
                    tvName.setText(Nombre);

                    final ImageView ivescuela = new ImageView(context);

                    TextView tvUni = new TextView(context);
                    tvUni.setText(Uni);
                    tvUni.setTextColor(Color.rgb(200,200,200));

                    TextView btInfo = new TextView(context);
                    btInfo.setText(R.string.uni_info);
                    btInfo.setTextColor(Color.RED);

                    TextView btMaps = new TextView(context);
                    btMaps.setText(R.string.uni_place);
                    btMaps.setTextColor(Color.RED);

                    RelativeLayout rlUni = new RelativeLayout(context);
                    int dp16 = (int)(16 * density);
                    int dp24 = (int)(24 * density);
                    rlUni.setPadding(dp16,0,dp16,dp16);

                    RelativeLayout rlName = new RelativeLayout(context);
                    rlName.setPadding(dp16,dp24,dp16,0);

                    LinearLayout actions = new LinearLayout(context);
                    actions.setOrientation(LinearLayout.HORIZONTAL);
                    actions.setPadding(dp16, 0, dp16, dp16);

                    rlEscuela.addView(ivescuela);
                    rlName.addView(tvName);
                    rlUni.addView(tvUni);
                    cvLinLy.addView(rlEscuela);
                    cvLinLy.addView(rlName);
                    cvLinLy.addView(rlUni);
                    actions.addView(btInfo);
                    actions.addView(btMaps);
                    cvLinLy.addView(actions);
                    cardView.addView(cvLinLy);
                    lyreco.addView(cardView, layoutParams);

                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(dp24, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
                    btMaps.setLayoutParams(llp);
                    btMaps.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                    btInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

                    RelativeLayout.LayoutParams paramsIVe = (RelativeLayout.LayoutParams)ivescuela.getLayoutParams();
                    paramsIVe.setMargins(0,0,0,0);

                    ivescuela.getLayoutParams().width = DrawerLayout.LayoutParams.MATCH_PARENT;
                    ivescuela.getLayoutParams().height = (int)(175 * density);
                    Picasso.with(context).load(urlFoto).transform(new Transformation() {

                        @Override public Bitmap transform(Bitmap source) {
                            int targetHeight = 800;
                            double aspectRatio =  (double) source.getWidth() / (double) source.getHeight();
                            int targetWidth = (int) (targetHeight * aspectRatio);
                            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                            if (result != source) {
                                source.recycle();
                            }
                            return result;
                        }

                        @Override public String key() {
                            return "cropPosterTransformation720";
                        }
                    }).into(ivescuela);

                    ivescuela.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    btMaps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mei.loadUrl("javascript: $(\".reco_maps\").get("+iMaps+").click()");
                        }
                    });

                    btInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mei.loadUrl(urlInfo);
                        }
                    });
                }
            });
        }

        @JavascriptInterface
        public void separador(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    View view2 = new View(context);
                    float density = context.getResources().getDisplayMetrics().density;
                    int dp16 = (int) (16 * density);

                    lytestvoca.addView(view2, layoutParams);

                    view2.getLayoutParams().height = dp16;

                }
            });
        }

        @JavascriptInterface
        public void numradio(Integer num) {
            num_radio = num;
            Rbindex = 0;
        }

        @JavascriptInterface
        public void pregunta(final String pregunta) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float density = context.getResources().getDisplayMetrics().density;

                    TableRow.LayoutParams param = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT,1f);

                    param.setMargins((int) (10 * density),(int) (16 * density),(int) (10 * density),0);

                    TextView textView = new TextView(context);
                    textView.setText(pregunta);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                    lytestvoca.addView(textView, param);
                }
            });
        }

        RadioGroup respuestas;

        @JavascriptInterface
        public void radioGCreate() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    respuestas = new RadioGroup(context);
                    lytestvoca.addView(respuestas);

                    respuestas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                            mei.loadUrl("javascript:document.getElementsByTagName('input')["+String.valueOf(i)+"].checked = true;");
                        }
                    });
                }
            });
        }

        @JavascriptInterface
        public void radioCreate(final String valor) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RadioButton radioButton = new RadioButton(context);
                    radioButton.setText(valor);
                    radioButton.setTextColor(Color.BLACK);
                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{
                                    new int[]{-android.R.attr.state_enabled}, //disabled
                                    new int[]{android.R.attr.state_enabled} //enabled
                            }, new int[] {
                                    Color.GRAY, //disabled
                                    Color.DKGRAY //enabled
                            }
                    );
                    radioButton.setButtonTintList(colorStateList);
                    radioButton.setId(Rbindex++);

                    respuestas.addView(radioButton);
                }
            });
        }

        @JavascriptInterface
        public void carrerafoto(final String url){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(context).load(url).into(univ_foto);
                }
            });
        }

        @JavascriptInterface
        public void carrerainfo(final String info, final int i){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (i) {
                        case 0:
                            tvCarrer.setText(Html.fromHtml(info));
                        case 1:
                            tvUniv.setText(Html.fromHtml(info));
                        case 2:
                            tvInst.setText(Html.fromHtml(info));
                        case 3:
                            tvArea.setText(Html.fromHtml(info));
                        case 4:
                            tvDesrip.setText(Html.fromHtml(info));
                        case 5:
                            tvPlan.setText(Html.fromHtml(info));
                            tvPlan.setMovementMethod(LinkMovementMethod.getInstance());
                        case 6:
                            tvBecas.setText(Html.fromHtml(info));
                            tvBecas.setMovementMethod(LinkMovementMethod.getInstance());
                        case 7:
                            tvPerfilE.setText(Html.fromHtml(info));
                            tvPerfilE.setMovementMethod(LinkMovementMethod.getInstance());
                        case 8:
                            tvInterc.setText(Html.fromHtml(info));
                            tvInterc.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }
            });
        }

        @JavascriptInterface
        public void test_result(final String resultado){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvResultado.setText(resultado);
                }
            });
        }

        @JavascriptInterface
        public void test_result_detalles(final String html){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {tvResultado_detalles.setText(Html.fromHtml(html));}
            });
        }

        @JavascriptInterface
        public  void perfil(final String url, final String name, final String correo, final String edad, final String ciudad){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getBaseContext()).load(PagMadre+"../resourses/profile_pics/"+url).transform(new CircleTransform()).into(foto);
                    tvNombreUsuario.setText(name);
                    tvCorreo.setText(correo);
                }
            });
        }

        @JavascriptInterface
        public void perfilTabla(final String test, final String fecha, final String resu){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float density = context.getResources().getDisplayMetrics().density;
                    TextView tvTest = new TextView(context);
                    TextView tvFecha = new TextView(context);
                    TextView tvResu = new TextView(context);
                    tvTest.setText(test);
                    tvFecha.setText(fecha);
                    tvResu.setText(resu);
                    tvFecha.setTextColor(Color.BLACK);
                    tvResu.setTextColor(Color.BLACK);
                    tvTest.setTextColor(Color.BLACK);

                    TableRow.LayoutParams param = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT,1f);

                    param.setMargins((int) (10 * density),(int) (16 * density),(int) (10 * density),(int) (16 * density));

                    TableRow tableRow = new TableRow(context);
                    tableRow.addView(tvTest,param);
                    tableRow.addView(tvFecha,param);
                    tableRow.addView(tvResu,param);
                    lyTablaTest.addView(tableRow);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    View view2 = new View(context);
                    view2.setBackgroundResource(R.color.grey);
                    int dp1 = (int) (1 * density);

                    lyTablaTest.addView(view2, layoutParams);

                    view2.getLayoutParams().height = dp1;
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(SesionActivity.this, "La localización no está activada", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("MEI")
                        .setMessage("Se necesitan permisos de ubicación.")
                        .setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SesionActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mei.canGoBack()) {
                        mei.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_button_reco, menu);
        rangoMenu = menu.getItem(0);
        rangoMenu.setVisible(false);
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.rangoReco) {
            if(rangeSeekBar.getVisibility()==View.VISIBLE) {
                rangeSeekBar.setVisibility(View.GONE);
                mei.loadUrl(PagMadre + "recomendaciones.php");
                lyreco.setPadding(0,0,0,0);
            }else{
                float density = getBaseContext().getResources().getDisplayMetrics().density;
                rangeSeekBar.setVisibility(View.VISIBLE);

                lyreco.setPadding(0,(int)(50*density),0,0);

                Location location = null;
                lyreco.removeAllViews();

                if (locationManager != null) {
                    setTitle("10 Km.");
                    location = locationManager.getLastKnownLocation(locationManager
                            .getBestProvider(criteria, false));
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    Log.e("Loc",lat.toString()+" - "+lng.toString()+" - "+String.valueOf((seekBarValue)));
                    mei.loadUrl("javascript:ajaxReco(" + lat + "," + lng + ",10);");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lyreco.removeAllViews();
                            mei.loadUrl("javascript:var carrera = document.getElementsByClassName('reco_carrera');" +
                                    "var uni = document.getElementsByClassName('reco_universidad');" +
                                    "var info = document.getElementsByClassName('reco_info');" +
                                    "var maps = document.getElementsByClassName('reco_maps');" +
                                    "var foto = document.getElementsByClassName('reco_foto');" +
                                    "if(carrera.length){" +
                                    "for(var i = 0 ; i < carrera.length ; ++i)" +
                                    "   window.HTMLOUT.recomienda(carrera[i].innerText,uni[i].innerText,info[i].href, i , foto[i].dataset.content);" +
                                    "}else{" +
                                    "   window.HTMLOUT.empty();" +
                                    "}");
                        }
                    },200);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                mei.loadUrl(PagMadre);
                setTitle("Página principal");
            } else if (id == R.id.nav_test) {
                mei.loadUrl(PagMadre + "test.php");
                setTitle("Test");
            } else if (id == R.id.nav_perfil) {
                mei.loadUrl(PagMadre + "perfil.php");
                setTitle("Perfil");
            } else if (id == R.id.nav_recomendaciones) {
                mei.loadUrl(PagMadre + "recomendaciones.php");
                setTitle("Recomendaciones");
            } else if (id == R.id.nav_contacto) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ramiroestradag@gmail.com"});
                try {
                    startActivity(Intent.createChooser(i, "Enviar email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SesionActivity.this, "Hubo un error al enviar al usar el servicio de email.", Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.nav_cerrarc) {
                mei.loadUrl(PagMadre + "logout.php");
                setTitle("Cerrando sesión...");
            }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
