package ru.a7flowers.pegorenkov.defectacts.data.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public interface DeliveryApi {

    @GET("reasons")
    Call<List<Reason>> getReasons();

    @GET("deliveries")
    Call<List<Delivery>> getDeliveries();

    @GET("deliveries/{deliveryId}/goods")
    Call<List<Good>> getGoods(@Path("deliveryId") String deliveryId);

    @GET("deliveries/{deliveryId}/defects")
    Call<List<Defect>> getDefects(@Path("deliveryId") String deliveryId);

    @GET("deliveries/{deliveryId}/defects/{defectId}/reasons")
    Call<List<DefectReason>> getDefectReasons(@Path("deliveryId") String deliveryId, @Path("defectId") String defectId);


}
