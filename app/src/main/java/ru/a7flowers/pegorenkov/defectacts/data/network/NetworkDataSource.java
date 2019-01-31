package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import android.util.Base64;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.a7flowers.pegorenkov.defectacts.BuildConfig;
import ru.a7flowers.pegorenkov.defectacts.NetworkSettings;
import ru.a7flowers.pegorenkov.defectacts.data.AppExecutors;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDeliveriesCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDeliveryCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadGoodsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.UploadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.UploadPhotosCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;

public class NetworkDataSource {

    private static final String TAG = "DATA_TRANSFER";
    private static volatile NetworkDataSource INSTANCE;

    private AppExecutors mAppExecutors;
    private DeliveryApi mDeliveryApi;

    private static NetworkSettings mSettings;

    private NetworkDataSource() {
        mAppExecutors = AppExecutors.getInstance();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", mSettings.getAuth())
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(mSettings.getServerPath())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mDeliveryApi = mRetrofit.create(DeliveryApi.class);
    }

    public static NetworkDataSource getInstance(NetworkSettings settings) {
        if (INSTANCE == null) {
            synchronized (NetworkDataSource.class) {
                if (INSTANCE == null) {
                    mSettings = settings;
                    INSTANCE = new NetworkDataSource();
                    mSettings.setServerPathChangeListener(new NetworkSettings.ServerPathChangeListener() {
                        @Override
                        public void onServerPathChanged() {
                            INSTANCE = new NetworkDataSource();
                        }
                    });
                }
            }
        }
        return INSTANCE;
    }

    //USERS
    public void loadUsers(final DataSource.LoadUsersCallback loadUsersCallback) {
        Log.d(TAG, "Start download users");
        Call<List<User>> users = mDeliveryApi.getUsers();
        users.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {

                List<User> list = response.body();
                if (list == null){
                    Log.d(TAG, "End download users - failed");
                    loadUsersCallback.onUsersLoadFailed();
                }else{
                    Log.d(TAG, "End download users - success");
                    loadUsersCallback.onUsersLoaded(list);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "End download users - failed");
                loadUsersCallback.onUsersLoadFailed();
            }
        });
    }

    //DELIVERIES
    public void loadDeliveries(final LoadDeliveriesCallback callback){

        Log.d(TAG, "Start download deliveries");
        Call<List<Delivery>> deliveries = mDeliveryApi.getDeliveries();
        deliveries.enqueue(new Callback<List<Delivery>>() {
            @Override
            public void onResponse(Call<List<Delivery>> call, retrofit2.Response<List<Delivery>> response) {

                List<Delivery> list = response.body();
                if (list == null){
                    Log.d(TAG, "End download deliveries - failed");
                    callback.onDeliveriesLoadFailed();
                }else{
                    Log.d(TAG, "End download deliveries - success");
                    callback.onDeliveriesLoaded(list);
                }
            }

            @Override
            public void onFailure(Call<List<Delivery>> call, Throwable t) {
                Log.d(TAG, "End download deliveries - failed");
                callback.onDeliveriesLoadFailed();
            }
        });
    }

    public void loadDelivery(String deliveryId, final LoadDeliveryCallback callback){

        Log.d(TAG, "Start download 1 delivery");
        Call<Delivery> delivery = mDeliveryApi.getDelivery(deliveryId);
        delivery.enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, retrofit2.Response<Delivery> response) {
                Log.d(TAG, "End download 1 delivery - success");
                Delivery list = response.body();
                callback.onDeliveryLoaded(list);
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {
                Log.d(TAG, "End download 1 delivery - failed");
                callback.onDeliveryLoadFailed();
            }
        });
    }

    public void saveDeliveryPhoto(final User user, final String deliveryId, final String path, final UploadPhotosCallback callback) {

        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                String fileData = readfileToString(path);

                int photoAmount = 0;

                Call<Integer> call = mDeliveryApi.setDeliveryPhoto(user.getId(), deliveryId, fileData);
                try {
                    photoAmount = call.execute().body();

                    File file = new File(path);
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onPhotosUploadingFailed();
                    return;
                }

                callback.onPhotosUploaded(photoAmount);
            }
        });

    }

    //REASONS
    public void loadReasons(final DataSource.LoadReasonsCallback callback){

        Log.d(TAG, "Start download reasons");
        Call<List<Reason>> reasons = mDeliveryApi.getReasons();
        reasons.enqueue(new Callback<List<Reason>>() {
            @Override
            public void onResponse(Call<List<Reason>> call, retrofit2.Response<List<Reason>> response) {

                List<Reason> list = response.body();
                if (list == null){
                    Log.d(TAG, "End download reasons - failed");
                    callback.onReasonsLoadFailed();
                }else{
                    Log.d(TAG, "End download reasons - success");
                    callback.onReasonsLoaded(list);
                }
            }

            @Override
            public void onFailure(Call<List<Reason>> call, Throwable t) {
                Log.d(TAG, "End download reasons - failed");
                callback.onReasonsLoadFailed();
            }
        });
    }

    //GOODS
    public void loadGoods(String deliveryId, final LoadGoodsCallback callback){

        Log.d(TAG, "Start download goods");
        Call<List<Good>> goods = mDeliveryApi.getGoods(deliveryId);
        goods.enqueue(new Callback<List<Good>>() {
            @Override
            public void onResponse(Call<List<Good>> call, retrofit2.Response<List<Good>> response) {
                Log.d(TAG, "End download goods - success");
                List<Good> list = response.body();
                callback.onGoodsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Good>> call, Throwable t) {
                Log.d(TAG, "End download goods - failed");
                callback.onGoodsLoadFailed();
            }
        });
    }

    //DEFECTS
    public void loadDefects(String deliveryId, final LoadDefectsCallback callback){

        Log.d(TAG, "Start download defects");
        Call<List<Defect>> defects = mDeliveryApi.getDefects(deliveryId);
        defects.enqueue(new Callback<List<Defect>>() {
            @Override
            public void onResponse(Call<List<Defect>> call, retrofit2.Response<List<Defect>> response) {
                Log.d(TAG, "End download defects - success");
                List<Defect> list = response.body();
                callback.onDefectsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Defect>> call, Throwable t) {
                Log.d(TAG, "End download defects - failed");
                callback.onDefectsLoadFailed();
            }
        });
    }

    public void loadDefect(String deliveryId, String defectId, final LoadDefectCallback callback){

        Log.d(TAG, "Start download 1 defect");
        Call<Defect> defect = mDeliveryApi.getDefect(deliveryId, defectId);
        defect.enqueue(new Callback<Defect>() {
            @Override
            public void onResponse(Call<Defect> call, retrofit2.Response<Defect> response) {
                Log.d(TAG, "End download 1 defect - success");
                Defect defectServer = response.body();
                callback.onDefectLoaded(defectServer);
            }

            @Override
            public void onFailure(Call<Defect> call, Throwable t) {
                Log.d(TAG, "End download 1 defect - failed");
                callback.onDefectLoadFailed();
            }
        });
    }

    public void saveDefect(final User user, final Defect defect, final UploadDefectCallback callback) {

        Log.d(TAG, "Start upload defect");

        Call<String> responce = mDeliveryApi.setDefect(user.getId(), defect.getDeliveryId(), defect);
        responce.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.d(TAG, "End upload defect - success");
                defect.setId(response.body());
                callback.onDefectUploaded(defect);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "End upload defect - failed");
                callback.onDefectUploadingFailed();
            }
        });
    }

    public void saveDefectPhotos(final User user, final String deliveryId, final String defectId, final List<String> photoPaths, final UploadPhotosCallback callback) {

        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                boolean success = true;
                int photoAmount = 0;

                for (String path : photoPaths) {

                    String fileData = readfileToString(path);

                    Call<Integer> call = mDeliveryApi.setDefectPhoto(user.getId(), deliveryId, defectId, fileData);
                    try {
                        photoAmount = call.execute().body();

                        File file = new File(path);
                        file.delete();
                    } catch (IOException e) {
                        success = false;
                        e.printStackTrace();
                    }
                }

                if (success)
                    callback.onPhotosUploaded(photoAmount);
                else
                    callback.onPhotosUploadingFailed();
            }
        });

    }

    //DIFFS
    public void loadDiff(String deliveryId, final DataSource.LoadDiffsCallback callback){

        Log.d(TAG, "Start download diffs");
        Call<List<Diff>> diffs = mDeliveryApi.getDiffs(deliveryId);
        diffs.enqueue(new Callback<List<Diff>>() {
            @Override
            public void onResponse(Call<List<Diff>> call, retrofit2.Response<List<Diff>> response) {
                Log.d(TAG, "End download diffs - success");
                List<Diff> list = response.body();
                callback.onDiffsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Diff>> call, Throwable t) {
                Log.d(TAG, "End download diffs - failed");
                callback.onDiffsLoadFailed();
            }
        });
    }

    public void saveDiff(final User user, final Diff diff, final DataSource.UploadDiffCallback callback) {

        Log.d(TAG, "Start upload diff");

        Call<String> responce = mDeliveryApi.setDiff(user.getId(), diff.getDeliveryId(), diff);
        responce.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.d(TAG, "End upload defect - success");
                diff.setId(response.body());
                callback.onDiffUploaded(diff);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "End upload defect - failed");
                callback.onDiffUploadingFailed();
            }
        });
    }

    public void loadDiff(String deliveryId, String diffId, final DataSource.LoadDiffCallback callback){

        Log.d(TAG, "Start download 1 diff");
        Call<Diff> defect = mDeliveryApi.getDiff(deliveryId, diffId);
        defect.enqueue(new Callback<Diff>() {
            @Override
            public void onResponse(Call<Diff> call, retrofit2.Response<Diff> response) {
                Log.d(TAG, "End download 1 diff - success");
                Diff diff = response.body();
                callback.onDiffLoaded(diff);
            }

            @Override
            public void onFailure(Call<Diff> call, Throwable t) {
                Log.d(TAG, "End download 1 diff - failed");
                callback.onDiffLoadFailed();
            }
        });
    }

    public void saveDiffPhotos(final User user, final String deliveryId, final String diffId, final List<String> photoPaths, final UploadPhotosCallback callback) {

        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                int photoAmount = 0;
                boolean success = true;
                for (String path : photoPaths) {

                    String fileData = readfileToString(path);

                    Call<Integer> call = mDeliveryApi.setDiffPhoto(user.getId(), deliveryId, diffId, fileData);
                    try {
                        photoAmount = call.execute().body();

                        File file = new File(path);
                        file.delete();
                    } catch (IOException e) {
                        success = false;
                        e.printStackTrace();
                    }
                }
                if (success)
                    callback.onPhotosUploaded(photoAmount);
                else
                    callback.onPhotosUploadingFailed();
            }
        });
    }

    private String readfileToString(String path){
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        return Base64.encodeToString(ba, Base64.NO_WRAP);
    }

    //SERVICE
    private byte[] readFile(String path){
        byte[] buf = null;

        try {
            File file = new File(path);
            InputStream in = new FileInputStream(file);
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;
            in.close();
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buf;
    }

    //TODO add own classes to network entities

    //TODO uploading big photos!!!
//    public String sendFileToServer(String filename, String targetUrl) {
//        String response = "error";
//        Log.e("Image filename", filename);
//        Log.e("url", targetUrl);
//        HttpURLConnection connection = null;
//        DataOutputStream outputStream = null;
//        // DataInputStream inputStream = null;
//
//        String pathToOurFile = filename;
//        String urlServer = targetUrl;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//        DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
//
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024;
//        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(
//                    pathToOurFile));
//
//            URL url = new URL(urlServer);
//            connection = (HttpURLConnection) url.openConnection();
//
//            // Allow Inputs & Outputs
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setUseCaches(false);
//            connection.setChunkedStreamingMode(1024);
//            // Enable POST method
//            connection.setRequestMethod("POST");
//
//            connection.setRequestProperty("Connection", "Keep-Alive");
//            connection.setRequestProperty("Content-Type",
//                    "multipart/form-data;boundary=" + boundary);
//
//            outputStream = new DataOutputStream(connection.getOutputStream());
//            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//            String connstr = null;
//            connstr = "Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
//                    + pathToOurFile + "\"" + lineEnd;
//            Log.i("Connstr", connstr);
//
//            outputStream.writeBytes(connstr);
//            outputStream.writeBytes(lineEnd);
//
//            bytesAvailable = fileInputStream.available();
//            bufferSize = Math.min(bytesAvailable, maxBufferSize);
//            buffer = new byte[bufferSize];
//
//            // Read file
//            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            Log.e("Image length", bytesAvailable + "");
//            try {
//                while (bytesRead > 0) {
//                    try {
//                        outputStream.write(buffer, 0, bufferSize);
//                    } catch (OutOfMemoryError e) {
//                        e.printStackTrace();
//                        response = "outofmemoryerror";
//                        return response;
//                    }
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                response = "error";
//                return response;
//            }
//            outputStream.writeBytes(lineEnd);
//            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
//                    + lineEnd);
//
//            // Responses from the server (code and message)
//            int serverResponseCode = connection.getResponseCode();
//            String serverResponseMessage = connection.getResponseMessage();
//            Log.i("Server Response Code ", "" + serverResponseCode);
//            Log.i("Server Response Message", serverResponseMessage);
//
//            if (serverResponseCode == 200) {
//                response = "true";
//            }
//
//            String CDate = null;
//            Date serverTime = new Date(connection.getDate());
//            try {
//                CDate = df.format(serverTime);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("Date Exception", e.getMessage() + " Parse Exception");
//            }
//            Log.i("Server Response Time", CDate + "");
//
//            filename = CDate
//                    + filename.substring(filename.lastIndexOf("."),
//                    filename.length());
//            Log.i("File Name in Server : ", filename);
//
//            fileInputStream.close();
//            outputStream.flush();
//            outputStream.close();
//            outputStream = null;
//        } catch (Exception ex) {
//            // Exception handling
//            response = "error";
//            Log.e("Send file Exception", ex.getMessage() + "");
//            ex.printStackTrace();
//        }
//        return response;
//    }
}
