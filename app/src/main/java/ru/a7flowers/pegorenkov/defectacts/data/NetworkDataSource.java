package ru.a7flowers.pegorenkov.defectacts.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectServer;
import ru.a7flowers.pegorenkov.defectacts.data.network.DeliveryApi;

public class NetworkDataSource implements DataSource {

    private static volatile NetworkDataSource INSTANCE;

    private AppExecutors mAppExecutors;
    private Retrofit mRetrofit;
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

        mRetrofit = new Retrofit.Builder()
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

        Call<List<Delivery>> deliveries = mDeliveryApi.getDeliveries();
        deliveries.enqueue(new Callback<List<Delivery>>() {
            @Override
            public void onResponse(Call<List<Delivery>> call, retrofit2.Response<List<Delivery>> response) {
                List<Delivery> list = response.body();
                callback.onDeliveriesLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Delivery>> call, Throwable t) {
                callback.onDeliveryLoadFailed();
            }
        });
    }

    public void loadGoods(Delivery delivery, final LoadGoodsCallback callback){

        Call<List<Good>> goods = mDeliveryApi.getGoods(delivery.getId());
        goods.enqueue(new Callback<List<Good>>() {
            @Override
            public void onResponse(Call<List<Good>> call, retrofit2.Response<List<Good>> response) {
                List<Good> list = response.body();
                callback.onGoodsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Good>> call, Throwable t) {
                callback.onGoodsLoadFailed();
            }
        });
    }

    public void loadDefects(Delivery delivery, final LoadDefectsCallback callback){

        Call<List<Defect>> defects = mDeliveryApi.getDefects(delivery.getId());
        defects.enqueue(new Callback<List<Defect>>() {
            @Override
            public void onResponse(Call<List<Defect>> call, retrofit2.Response<List<Defect>> response) {
                List<Defect> list = response.body();
                callback.onDefectsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Defect>> call, Throwable t) {
                callback.onDefectsLoadFailed();
            }
        });
    }

    public void loadReasons(final LoadReasonsCallback callback){

        Call<List<Reason>> reasons = mDeliveryApi.getReasons();
        reasons.enqueue(new Callback<List<Reason>>() {
            @Override
            public void onResponse(Call<List<Reason>> call, retrofit2.Response<List<Reason>> response) {
                List<Reason> list = response.body();
                callback.onReasonsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<Reason>> call, Throwable t) {
                callback.onReasonsLoadFailed();
            }
        });
    }

    public void loadDefectReasons(final Delivery delivery, final Defect defect, final LoadDefectReasonsCallback callback) {
        Call<List<DefectReason>> reasons = mDeliveryApi.getDefectReasons(delivery.getId(), defect.getId());
        reasons.enqueue(new Callback<List<DefectReason>>() {
            @Override
            public void onResponse(Call<List<DefectReason>> call, retrofit2.Response<List<DefectReason>> response) {
                List<DefectReason> list = response.body();
                callback.onDefectReasonsLoaded(list);
            }

            @Override
            public void onFailure(Call<List<DefectReason>> call, Throwable t) {
                callback.onDefectReasonsLoadFailed();
            }
        });
    }


    public void saveDefect(final Defect defect, List<Reason> reasons, final UploadDefectCallback callback) {

        DefectServer defectServer = new DefectServer(defect, reasons);

        Call<String> responce = mDeliveryApi.setDefect(defect.getDeliveryId(), defectServer);
        responce.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                defect.setId(response.body());
                callback.onDefectUploaded(defect);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onDefectUploadingFailed();
            }
        });
    }

    public void savePhotos(Defect defect, List<String> photoPaths) {

        for (String path:photoPaths) {

            byte[] buf = null;

            try {
                InputStream in = new FileInputStream(new File(path));
                buf = new byte[in.available()];
                while (in.read(buf) != -1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (buf == null) continue;

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), buf);
            Call responce = mDeliveryApi.setPhoto(defect.getDeliveryId(), defect.getId(), requestBody);
            try {
                responce.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
