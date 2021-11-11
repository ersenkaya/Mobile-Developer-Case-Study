package com.tohan.veriparkcasestudy.Api;

import com.tohan.veriparkcasestudy.Api.Models.DetailRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.DetailResponseModel;
import com.tohan.veriparkcasestudy.Api.Models.HandshakeRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.HandshakeResponseModel;
import com.tohan.veriparkcasestudy.Api.Models.ListRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.StockListModel;

import retrofit2.Call;

public class Repository {

    public Call<HandshakeResponseModel> getHandshake(HandshakeRequestModel requestModel) {
        return  RetrofitInstance.api().getHandshake(requestModel);
    }

    public Call<StockListModel> getStockList(ListRequestModel requestModel, String authorization) {
        return  RetrofitInstance.api().getStockList(requestModel, authorization);
    }

    public Call<DetailResponseModel> getStockDetail(DetailRequestModel requestModel, String authorization) {
        return  RetrofitInstance.api().getStockDetail(requestModel, authorization);
    }
}
