package com.inspireuser.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;


public class MainActivity2 extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {
    InAppUpdateManager inAppUpdateManager;
    Button btNotification;
    private final static int FCR = 1;
    private WebView wv;
    private String mCM;
    private int xxx = 1;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = MainActivity2.class.getSimpleName();
    private static final String Tag = "MyTag";
    private TextView mOutputText;
    private ProgressBar mProgressBar;
    private NotificationManagerCompat notificationManager;
    String url = "https://deltatrek.in/user/login";
    String newUrl="https://deltatrek.in/user/login";
    String tokens="";
    private FirebaseRemoteConfig remoteConfig;
    RelativeLayout relativeLayout;
    Button btnNoInternetConnection;
    Button hideZoom;
    Button showZoom;
    Button allowScreenshot;
    Button notallowScreenshot;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final String CHANNEL_1_ID = "Task";
    public static final String CHANNEL_2_ID = "Result";
    public static final String CHANNEL_3_ID = "Paper Publish";
    public static final String CHANNEL_4_ID = "Notification";
    public static final String CHANNEL_5_ID = "Updates";



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }
    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        createNotification();
        inAppUpdateManager = InAppUpdateManager.Builder(this,101)
                .resumeUpdates(true)
                .mode(Constants.UpdateMode.IMMEDIATE)
                .snackBarMessage("An update has just been download...")
                .snackBarAction("RESTART")
                .handler(this);
        inAppUpdateManager.checkForAppUpdate();

        if (Build.VERSION.SDK_INT >= 21 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity2.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                tokens = newToken;

            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("com_inspireuser_app");
        if (Build.VERSION.SDK_INT == 23) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity2.this);
            View layoutView = getLayoutInflater().inflate(R.layout.not_support, null);
            Button dialogButton = layoutView.findViewById(R.id.notsupportexit);
            dialogBuilder.setView(layoutView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
        // block screenshot capture....
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SCALED);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        wv = (WebView) findViewById(R.id.webView);

        hideZoom = (Button) findViewById(R.id.hideZoom);
        showZoom = (Button) findViewById(R.id.showZoom);
        allowScreenshot = (Button) findViewById(R.id.allowScreenshot);
        notallowScreenshot = (Button) findViewById(R.id.notallowScreenshot);


        hideZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        showZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wv.getSettings().setSupportZoom(true);
                wv.getSettings().setBuiltInZoomControls(true);
            }
        });
        allowScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SCALED);
            }
        });
        notallowScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SCALED, WindowManager.LayoutParams.FLAG_SCALED);
            }
        });

        assert wv!= null;
        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 0);
        btnNoInternetConnection = (Button) findViewById(R.id.btnNoConnection);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(0xFFFF8800,Color.RED,Color.GREEN);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wv.reload();
            }
        });
        WebSettings webSettings = wv.getSettings();
        if(savedInstanceState !=null){
            wv.restoreState(savedInstanceState);
        }
        else {
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setDomStorageEnabled(true);
            wv.getSettings().setGeolocationEnabled(true);
            wv.getSettings().setAllowFileAccess(true);
            wv.getSettings().setPluginState(WebSettings.PluginState.ON);
            wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
            wv.getSettings().setSupportMultipleWindows(false);
            wv.getSettings().setSupportZoom(false);
            wv.getSettings().setBuiltInZoomControls(false);
            wv.getSettings().setDisplayZoomControls(false);
            wv.setVerticalScrollBarEnabled(false);
            wv.setHorizontalScrollBarEnabled(false);
            wv.getSettings().setMediaPlaybackRequiresUserGesture(false);
            wv.addJavascriptInterface(new WebviewInterface(), "AndroidFunction");
            wv.addJavascriptInterface(new Object(){
                @JavascriptInterface
                public void hideZoom(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wv.getSettings().setUseWideViewPort(true);
                            wv.getSettings().setLoadWithOverviewMode(true);
                            wv.setInitialScale(1);
                            wv.getSettings().setSupportZoom(false);
                            wv.getSettings().setBuiltInZoomControls(false);
                            wv.getSettings().setDisplayZoomControls(false);


                        }
                    });
                }
                @JavascriptInterface
                public void showZoom(){  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wv.getSettings().setSupportZoom(true);
                        wv.getSettings().setBuiltInZoomControls(true);
                        wv.getSettings().setDisplayZoomControls(false);
                        wv.setInitialScale(2);
                    }
                }); }
                @JavascriptInterface
                public void allowScreenshot(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SCALED);
                        }
                    }); }
                @JavascriptInterface
                public void notallowScreenshot(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SCALED, WindowManager.LayoutParams.FLAG_SCALED);
                        }
                    }); }
                @JavascriptInterface
                public void disableScroll(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            swipeRefreshLayout.setEnabled(false);
                        }
                    }); }

                @JavascriptInterface
                public void enableScroll(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            swipeRefreshLayout.setEnabled(true);
                        }
                    }); }

            },"AndroidButton");
            wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

            checkConnection();
        }
        wv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (wv.getScrollY() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimeType);
                //------------------------COOKIE!!------------------------
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });

        wv.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                //Log.i(TAG, "onGeolocationPermissionsShowPrompt()");

                final boolean remember = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Locations");
                builder.setMessage("Would like to use your Current Location ")
                        .setCancelable(true).setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // origin, allow, remember
                        callback.invoke(origin, true, remember);
                        int result = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (result == PackageManager.PERMISSION_GRANTED) {

                        } else {
                            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }
                    }
                }).setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // origin, allow, remember
                        callback.invoke(origin, false, remember);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            private File createImageFile() throws IOException {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File imageFile = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                return imageFile;

            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePath;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                    mCapturedImageURI = Uri.fromFile(photoFile);
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }
                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                // Create AndroidExampleFolder at sdcard
                File imageStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)
                        , "AndroidExampleFolder");
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }
                // Create camera captured image file path and name
                File file = new File(
                        imageStorageDir + File.separator + "IMG_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".jpg");
                mCapturedImageURI = Uri.fromFile(file);
                // Camera capture image intent
                final Intent captureIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                // Create file chooser intent
                Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                        , new Parcelable[] { captureIntent });
                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

        });


        wv.setWebViewClient(new myWebClient());
        wv.loadUrl(newUrl);

    }

    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        if (status.isDownloaded()){
            View rootView =getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar =Snackbar.make(rootView,
                    "An update has just been downloaded...",Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("Restart", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inAppUpdateManager.completeUpdate();
                }
            });
        }
    }

    public class WebviewInterface {
        //private Context context;
      //  public WebviewInterface(Context context){
           //  this.context = context;
        // }

        @JavascriptInterface
        public void landscape() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });

        }
        @JavascriptInterface
        public void hidenavbar() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            });

        }
        @JavascriptInterface
        public void portrait() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
        }
        @JavascriptInterface
        public String token() {
            return tokens;
        }
        @JavascriptInterface
        public String subscribeToTopic(String val){
            String topic = val;
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            return topic;
        }
        @JavascriptInterface
        public String unsubscribeFromTopic(String val){
            String topic = val;
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
            return topic;
        }
        @JavascriptInterface
        public int Version_Code(){
            int versionCode = BuildConfig.VERSION_CODE;
            return versionCode;
        }
        @JavascriptInterface
        public void showUpdate_Box() {
            if(xxx==1) {
                showUpdateDialog();
            }
        }
        @JavascriptInterface
        public void forceUpdate_Box() {
            forceUpdateDialog();
        }

    }

    public void showUpdateDialog(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity2.this);
        View layoutView = getLayoutInflater().inflate(R.layout.update_dialog, null);
        Button dialogButton = layoutView.findViewById(R.id.tvUpdateNow);
        Button closeButton = layoutView.findViewById(R.id.ivClose);
        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        xxx = 0;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.inspireuser.app")));
                }catch (android.content.ActivityNotFoundException anfe){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/details?id=com.inspireuser.app")));
                }
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.hide();
        }
    });
}
    public void forceUpdateDialog(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity2.this);
        View layoutView = getLayoutInflater().inflate(R.layout.force_update_dialog, null);
        Button dialogButton = layoutView.findViewById(R.id.ftvUpdateNow);
        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.inspireuser.app")));
                }catch (android.content.ActivityNotFoundException anfe){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/details?id=com.inspireuser.app")));
                }
            }
        });

    }
    private void createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.notification);
            final long[] VIBRATE_PATTERN    = {0, 500};
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Tasks", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Task Board notifications");
            channel1.enableLights(true);
            channel1.enableVibration(true);
            channel1.setLightColor(Color.GREEN);
            Uri uri = Uri.parse("android.resource://"+this.getPackageName()+"/" + R.raw.notification);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            channel1.setSound(uri,att);
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Results", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Results notifications");
            channel2.enableLights(true);
            channel2.enableVibration(true);
            channel2.setLightColor(Color.GREEN);
            channel2.setSound(uri,att);
            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID, "Paper Publish", NotificationManager.IMPORTANCE_HIGH);
            channel3.setDescription("Paper Publish notifications");
            channel3.enableLights(true);
            channel3.enableVibration(true);
            channel3.setLightColor(Color.GREEN);
            channel3.setSound(uri,att);
            NotificationChannel channel4 = new NotificationChannel(CHANNEL_4_ID, "Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel4.setDescription("Academic notifications");
            channel4.enableLights(true);
            channel4.enableVibration(true);
            channel4.setLightColor(Color.GREEN);
            channel4.setSound(uri,att);
            NotificationChannel channel5 = new NotificationChannel(CHANNEL_5_ID, "Updates", NotificationManager.IMPORTANCE_HIGH);
            channel5.setDescription("Updates related notifications");
            channel5.enableLights(true);
            channel5.enableVibration(true);
            channel5.setLightColor(Color.GREEN);
            channel5.setSound(uri,att);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
            manager.createNotificationChannel(channel4);
            manager.createNotificationChannel(channel5);


        }
    }
    public class myWebClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            try {
                webView.stopLoading();
            } catch (Exception e) {
            }

            if (webView.canGoBack()) {
                webView.goBack();
            }

            newUrl=failingUrl;
            //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity2.this).create();
            // alertDialog.setTitle("Error");
            // alertDialog.setCancelable(false);
            // alertDialog.setMessage("Check your internet connection and try again.");
            //alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
            //  public void onClick(DialogInterface dialog, int which) {
            //    finish();
            //  startActivity(getIntent());
            // }
            // });
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            btnNoInternetConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkConnection();
                }
            });
            //alertDialog.show();
            super.onReceivedError(webView, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            newUrl=url;
            mProgressBar.setVisibility(view.VISIBLE);
            setTitle("Loading...");
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            newUrl=url;
            mProgressBar.setVisibility(view.GONE);
            swipeRefreshLayout.setRefreshing(false);
            setTitle(view.getTitle());
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
//        @Override
//        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
//            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    handler.proceed();
//                }
//            });
//            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    handler.cancel();
//                }
//            });
//            final AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            Uri uri = request.getUrl();
//            if (uri.getHost().equals("assets")) {
//                try {
//                    return new WebResourceResponse(
//                            URLConnection.guessContentTypeFromName(uri.getPath()),
//                            "utf-8",
//                            MainActivity2.this.getAssets().open(uri.toString().substring(15)));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            newUrl=url;
            if((String.valueOf(url)).contains("deltatrek.in")) {
                view.loadUrl(url);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
            }
            return true;

        }
    }
    public class Android {
        private Activity activity = null;
        private WebView webView  = null;
        public Android(Activity activity,WebView webview) {
            this.activity = activity;
            this.webView  = webview;
            Log.d("Test 1","in Interface");

        }

        @JavascriptInterface
        public void Android(final String message) {
            Log.d("from javascript", "" + message);
            final Activity theActivity = this.activity;
            final WebView theWebView = this.webView;
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!theWebView.getUrl().startsWith("file:///android_asset/www/index.html")) {
                        return;
                    }
                    Toast toast = Toast.makeText(
                            theActivity.getApplicationContext(),
                            message,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            Context activity;
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.exit_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            FrameLayout mDialogNo = dialog.findViewById(R.id.yesexit);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            FrameLayout mDialogOk = dialog.findViewById(R.id.noexit);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Okay", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            dialog.show();

        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wv.saveState(outState);
    }

    public void checkConnection(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if(wifi.isConnected()){
            wv.loadUrl("javascript:window.location.reload( true )");
            wv.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);


        }
        else if (mobileNetwork.isConnected()){
            wv.loadUrl("javascript:window.location.reload( true )");
            wv.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        else{

            wv.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

        }


    }

}