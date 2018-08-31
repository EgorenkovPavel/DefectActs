package ru.a7flowers.pegorenkov.defectacts.data;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDeliveriesCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDeliveryCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadGoodsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.UploadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.UploadPhotosCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectServer;
import ru.a7flowers.pegorenkov.defectacts.data.network.DeliveryApi;

public class NetworkDataSource {

    private static final String TAG = "DATA_TRANSFER";
    private static volatile NetworkDataSource INSTANCE;

    private AppExecutors mAppExecutors;
    private DeliveryApi mDeliveryApi;

    private NetworkDataSource() {
        mAppExecutors = AppExecutors.getInstance();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Basic dXNlcjo=") //""Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://msavelev/UT11_PE/ru_RU/hs/DefectActs/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mDeliveryApi = mRetrofit.create(DeliveryApi.class);
    }

    public static NetworkDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (NetworkDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetworkDataSource();
                }
            }
        }
        return INSTANCE;
    }

    public void loadDeliveries(final LoadDeliveriesCallback callback){

        Log.d(TAG, "Start download deliveries");
        Call<List<Delivery>> deliveries = mDeliveryApi.getDeliveries();
        deliveries.enqueue(new Callback<List<Delivery>>() {
            @Override
            public void onResponse(Call<List<Delivery>> call, retrofit2.Response<List<Delivery>> response) {
                Log.d(TAG, "End download deliveries - success");
                List<Delivery> list = response.body();
                callback.onDeliveriesLoaded(list);
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

    public void loadDefects(String deliveryId, final LoadDefectsCallback callback){

        Log.d(TAG, "Start download defects");
        Call<List<DefectServer>> defects = mDeliveryApi.getDefects(deliveryId);
        defects.enqueue(new Callback<List<DefectServer>>() {
            @Override
            public void onResponse(Call<List<DefectServer>> call, retrofit2.Response<List<DefectServer>> response) {
                Log.d(TAG, "End download defects - success");
                List<DefectServer> list = response.body();
                callback.onDefectsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<DefectServer>> call, Throwable t) {
                Log.d(TAG, "End download defects - failed");
                callback.onDefectsLoadFailed();
            }
        });
    }

    public void loadDefect(String deliveryId, String defectId, final LoadDefectCallback callback){

        Log.d(TAG, "Start download 1 defect");
        Call<DefectServer> defect = mDeliveryApi.getDefect(deliveryId, defectId);
        defect.enqueue(new Callback<DefectServer>() {
            @Override
            public void onResponse(Call<DefectServer> call, retrofit2.Response<DefectServer> response) {
                Log.d(TAG, "End download 1 defect - success");
                DefectServer defectServer = response.body();
                callback.onDefectLoaded(defectServer);
            }

            @Override
            public void onFailure(Call<DefectServer> call, Throwable t) {
                Log.d(TAG, "End download 1 defect - failed");
                callback.onDefectLoadFailed();
            }
        });
    }

    public void loadReasons(final DataSource.LoadReasonsCallback callback){

        Log.d(TAG, "Start download reasons");
        Call<List<Reason>> reasons = mDeliveryApi.getReasons();
        reasons.enqueue(new Callback<List<Reason>>() {
            @Override
            public void onResponse(Call<List<Reason>> call, retrofit2.Response<List<Reason>> response) {
                Log.d(TAG, "End download reasons - success");
                List<Reason> list = response.body();
                callback.onReasonsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Reason>> call, Throwable t) {
                Log.d(TAG, "End download reasons - failed");
                callback.onReasonsLoadFailed();
            }
        });
    }

    public void loadDefectReasons(final String[] deliveryIds, final Defect defect, final LoadDefectReasonsCallback callback) {

        //TODO
//        Log.d(TAG, "Start download defect reasons");
//        Call<List<DefectReason>> reasons = mDeliveryApi.getDefectReasons(delivery.getId(), defect.getId());
//        reasons.enqueue(new Callback<List<DefectReason>>() {
//            @Override
//            public void onResponse(Call<List<DefectReason>> call, retrofit2.Response<List<DefectReason>> response) {
//                Log.d(TAG, "End download defect reasons - success");
//                List<DefectReason> list = response.body();
//                callback.onDefectReasonsLoaded(list);
//            }
//
//            @Override
//            public void onFailure(Call<List<DefectReason>> call, Throwable t) {
//                Log.d(TAG, "End download defect reasons - failed");
//                callback.onDefectReasonsLoadFailed();
//            }
//        });
    }


    public void saveDefect(final Defect defect, List<Reason> reasons, final UploadDefectCallback callback) {

        Log.d(TAG, "Start upload defect");

        DefectServer defectServer = new DefectServer(defect, reasons);

        Call<String> responce = mDeliveryApi.setDefect(defect.getDeliveryId(), defectServer);
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

    public void savePhotos(final Defect defect, final List<String> photoPaths, final UploadPhotosCallback callback) {

        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                boolean success = true;

                for (String path : photoPaths) {

                    byte[] buf = null;

                    try {
                        InputStream in = new FileInputStream(new File(path));
                        buf = new byte[in.available()];
                        while (in.read(buf) != -1) ;
                    } catch (IOException e) {
                        e.printStackTrace();
                        success = false;
                        break;
                    }

                    if (buf == null) continue;

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), buf);
                    Call<Boolean> responce = mDeliveryApi.setPhoto(defect.getDeliveryId(), defect.getId(), requestBody);
                    try {
                        responce.execute();
                    } catch (IOException e) {
                        success = false;
                        e.printStackTrace();
                    }
                }
                if (success)
                    callback.onPhotosUploaded();
                else
                    callback.onPhotosUploadingFailed();
            }
        });

    }
}
