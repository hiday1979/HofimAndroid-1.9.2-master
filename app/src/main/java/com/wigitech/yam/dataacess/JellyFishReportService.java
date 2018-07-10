package com.wigitech.yam.dataacess;

import com.wigitech.yam.model.Jellyfish;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by GolaNir on 19/04/2017.
 */

public interface JellyFishReportService {

    @POST("report/Jellyfish")
    Call<Jellyfish> reportJellyFish(@Body Jellyfish Jellyfish);

}
