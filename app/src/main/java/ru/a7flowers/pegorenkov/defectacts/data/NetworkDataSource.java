package ru.a7flowers.pegorenkov.defectacts.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
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

    public void loadGoods(Delivery delivery, LoadGoodsCallback callback){
        List<Good> list = new ArrayList<>();

        list.add(new Good(2, "1111111111111", "Rose", "OZ", "Vietnam", 1, delivery.getId()));
        list.add(new Good(23, "234234234234", "Rose", "OZ", "Vietnam", 2, delivery.getId()));
        list.add(new Good(3, "3333333333333", "Rose", "OZ", "Vietnam", 3, delivery.getId()));

        callback.onGoodsLoaded(list);
    }

    public void loadDefects(Delivery delivery, LoadDefectsCallback callback){
        List<Defect> list = new ArrayList<>();

        //list.add(new Defect(34, "dfsf334fd34f", "234234234234", 34, 5 , delivery.getId(), "comment1"));

        callback.onDefectsLoaded(list);
    }

    public void loadReasons(LoadReasonsCallback callback){
        List<Reason> list = new ArrayList<>();

        list.add(new Reason("asdfsdasda", "reason 1"));
        list.add(new Reason("asdfsdssa", "reason 2"));
        list.add(new Reason( "asdfsd55da", "reason 3"));

        callback.onReasonsLoaded(list);
    }

    public void createAct(Delivery delivery) {
    }

    public void saveDefect(Defect defect, List<Reason> reasons) {
    }

    public void savePhotos(List<String> photoPaths) {
    }
}
