package ru.a7flowers.pegorenkov.defectacts.data.network;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public interface DeliveryApi {

    @GET("reasons")
    Call<List<Reason>> getReasons();

    @GET("deliveries")
    Call<List<Delivery>> getDeliveries();

    @GET("deliveries/{deliveryId}")
    Call<Delivery> getDelivery(@Path("deliveryId") String deliveryId);

    @GET("deliveries/{deliveryId}/goods")
    Call<List<Good>> getGoods(@Path("deliveryId") String deliveryId);

    @GET("deliveries/{deliveryId}/defects")
    Call<List<Defect>> getDefects(@Path("deliveryId") String deliveryId);

    @GET("deliveries/{deliveryId}/differencies")
    Call<List<Diff>> getDiffs(@Path("deliveryId") String deliveryId);

    @GET("deliveries/{deliveryId}/defects/{defectId}")
    Call<Defect> getDefect(@Path("deliveryId") String deliveryId, @Path("defectId") String defectId);

    @GET("deliveries/{deliveryId}/differencies/{diffId}")
    Call<Diff> getDiff(@Path("deliveryId") String deliveryId, @Path("diffId") String diffId);

//    @GET("deliveries/{deliveryId}/defects/{defectId}/reasons")
//    Call<List<DefectReason>> getDefectReasons(@Path("deliveryId") String deliveryId, @Path("defectId") String defectId);

    @POST("deliveries/{deliveryId}/defects")
    Call<String> setDefect(@Path("deliveryId") String deliveryId, @Body Defect defect);

    @POST("deliveries/{deliveryId}/differencies")
    Call<String> setDiff(@Path("deliveryId") String deliveryId, @Body Diff diff);

    @POST("deliveries/{deliveryId}/defects/{defectId}/photo")
    Call<Boolean> setDefectPhoto(@Path("deliveryId") String deliveryId, @Path("defectId") String defectId, @Body RequestBody photo);

    @POST("deliveries/{deliveryId}/differencies/{diffId}/photo")
    Call<Boolean> setDiffPhoto(@Path("deliveryId") String deliveryId, @Path("diffId") String diffId, @Body RequestBody photo);

}
