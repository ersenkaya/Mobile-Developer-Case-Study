package com.tohan.veriparkcasestudy.Api;

import com.tohan.veriparkcasestudy.Api.Models.DetailRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.DetailResponseModel;
import com.tohan.veriparkcasestudy.Api.Models.HandshakeRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.HandshakeResponseModel;
import com.tohan.veriparkcasestudy.Api.Models.ListRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.StockListModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/handshake/start")
    Call<HandshakeResponseModel> getHandshake(@Body HandshakeRequestModel requestModel);

    @Headers("Content-Type: application/json")
    @POST("api/stocks/list")
    Call<StockListModel> getStockList(@Body ListRequestModel requestModel, @Header("X-VP-Authorization") String authorization);


    @Headers("Content-Type: application/json")
    @POST("api/stocks/detail")
    Call<DetailResponseModel> getStockDetail(@Body DetailRequestModel requestModel, @Header("X-VP-Authorization") String authorization);

}
