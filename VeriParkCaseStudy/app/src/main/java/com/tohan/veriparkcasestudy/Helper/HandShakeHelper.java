package com.tohan.veriparkcasestudy.Helper;

import android.content.Context;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.tohan.veriparkcasestudy.Adapters.StockTableDataAdapter;
import com.tohan.veriparkcasestudy.Api.Models.DetailRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.DetailResponseModel;
import com.tohan.veriparkcasestudy.Api.Models.HandshakeRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.HandshakeResponseModel;
import com.tohan.veriparkcasestudy.Api.Models.ListRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.StockListModel;
import com.tohan.veriparkcasestudy.Api.Models.StockModel;
import com.tohan.veriparkcasestudy.Api.Repository;
import com.tohan.veriparkcasestudy.Listeners.StockClickListener;
import com.tohan.veriparkcasestudy.MainActivity;
import com.tohan.veriparkcasestudy.R;
import com.tohan.veriparkcasestudy.databinding.FragmentDetailBinding;
import com.tohan.veriparkcasestudy.databinding.FragmentStockBinding;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import retrofit2.Call;
import retrofit2.Callback;

public class HandShakeHelper {

    private String authKey;
    private String AesKey;
    private String AesIV;

    private static final String[] TABLE_HEADERS = {"Sembol", "Fiyat", "Fark", "Hacim", "Alış", "Satış", "Değişim"};
    private TableView<String[]> tableView;
    private StockClickListener stockClickListener;
    private StockTableDataAdapter stda;
    private ArrayList<StockModel> stockListMain = new ArrayList<>();
    private CryptoHepler cryptoHepler;

    private Repository repository;

    private Context context;
    private SearchView searchView;

    private FragmentDetailBinding detailBinding;

    public HandShakeHelper() {

    }

    public void initListState(Context context, TableView tableView, SearchView searchView, View root, String fragmentName, String period) {
        this.context = context;
        this.tableView = tableView;
        this.searchView = searchView;
        cryptoHepler = new CryptoHepler();
        repository = new Repository();

        initTableView(root, fragmentName);
        getHandshakeData(period);
        initSearchView();
    }

    public void initDetailState(Context context, View root, int id, FragmentDetailBinding detailBinding) {
        this.context = context;
        this.detailBinding = detailBinding;
        cryptoHepler = new CryptoHepler();
        repository = new Repository();

        getDetailHandshake(id);

    }

    private void initTableView(View root, String fragmentName) {
        //Table view options
        stockClickListener = new StockClickListener(context, root, fragmentName);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(context.getResources().getColor(R.color.white), context.getResources().getColor(R.color.gray)));
        SimpleTableHeaderAdapter stha = new SimpleTableHeaderAdapter(context, TABLE_HEADERS);
        stha.setTextSize(10);
        stha.setPaddings(20, 10, 20, 10);
        tableView.setHeaderAdapter(stha);
        tableView.addDataClickListener(stockClickListener);
        //#Table view options
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!newText.equals("") && stda != null && stockListMain.size() > 0) {
                    SearchHelper searchHelper = new SearchHelper();
                    stda = new StockTableDataAdapter(context, searchHelper.searchList(stockListMain, newText));
                    stockClickListener.setStockList(searchHelper.getResultArray());
                    tableView.setDataAdapter(stda);
                }
                return true;
            }
        });
    }


    private void getHandshakeData(String period) {

        HandshakeRequestModel body = new HandshakeRequestModel(
                Build.ID,
                String.valueOf(android.os.Build.VERSION.SDK_INT),
                "Android",
                Build.MODEL,
                Build.MANUFACTURER);

        Call<HandshakeResponseModel> responseModel = repository.getHandshake(body);

        responseModel.enqueue(new Callback<HandshakeResponseModel>() {
            @Override
            public void onResponse(Call<HandshakeResponseModel> call, retrofit2.Response<HandshakeResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        HandshakeResponseModel handshakeModel = response.body();
                        setAuthKey(cryptoHepler.decrypt(handshakeModel.getAuthorization(), handshakeModel.getAesKey(), handshakeModel.getAesIV()));


                        getDataList(handshakeModel, period);


                    }
                }
            }

            @Override
            public void onFailure(Call<HandshakeResponseModel> call, Throwable t) {
                Log.i("retrofittest", "getAuthorization");

            }


        });

    }



    private void getDetailHandshake(int id) {

        HandshakeRequestModel body = new HandshakeRequestModel(
                Build.ID,
                String.valueOf(android.os.Build.VERSION.SDK_INT),
                "Android",
                Build.MODEL,
                Build.MANUFACTURER);

        Call<HandshakeResponseModel> responseModel = repository.getHandshake(body);

        responseModel.enqueue(new Callback<HandshakeResponseModel>() {
            @Override
            public void onResponse(Call<HandshakeResponseModel> call, retrofit2.Response<HandshakeResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        HandshakeResponseModel handshakeModel = response.body();
                        setAuthKey(cryptoHepler.decrypt(handshakeModel.getAuthorization(), handshakeModel.getAesKey(), handshakeModel.getAesIV()));


                        getDetailData(handshakeModel,id,handshakeModel.getAesKey(),handshakeModel.getAesIV());


                    }
                }
            }

            @Override
            public void onFailure(Call<HandshakeResponseModel> call, Throwable t) {
                Log.i("retrofittest", "getAuthorization");

            }


        });

    }

    private void getDataList(HandshakeResponseModel handshakeModel, String period){
        Call<StockListModel> responseModel = repository.getStockList(new ListRequestModel(cryptoHepler.encrypt(period, handshakeModel.getAesKey(), handshakeModel.getAesIV())),
                handshakeModel.getAuthorization());
        ;

        responseModel.enqueue(new Callback<StockListModel>() {
            @Override
            public void onResponse(Call<StockListModel> call, retrofit2.Response<StockListModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<StockModel> stockList = response.body().getStocks();
                        if (stockList.size() > 0) {
                            stda = new StockTableDataAdapter(context, cryptoHepler.getStockList(stockList, handshakeModel.getAesKey(), handshakeModel.getAesIV()));
                            stockClickListener.setStockList(stockList);
                            tableView.setDataAdapter(stda);
                            stockListMain = stockList;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StockListModel> call, Throwable t) {
                Log.i("retrofittest", "stock list fail");

            }


        });
    }


    private void getDetailData(HandshakeResponseModel handshakeModel, int id, String AesKey, String AesIV){
        Call<DetailResponseModel> responseModel = repository.getStockDetail(new DetailRequestModel(cryptoHepler.encrypt(String.valueOf(id), handshakeModel.getAesKey(), handshakeModel.getAesIV())),
                handshakeModel.getAuthorization());
        ;

        responseModel.enqueue(new Callback<DetailResponseModel>() {
            @Override
            public void onResponse(Call<DetailResponseModel> call, retrofit2.Response<DetailResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        DetailResponseModel detailResponseModel = response.body();
                        detailBinding.symbolText.setText(context.getResources().getString(R.string.sembol) +cryptoHepler.decrypt(detailResponseModel.getSymbol(),AesKey,AesIV));
                        detailBinding.priceText.setText(context.getResources().getString(R.string.fiyat) + detailResponseModel.getPrice());
                        detailBinding.differenceText.setText(context.getResources().getString(R.string.fark) + detailResponseModel.getDifference());
                        detailBinding.volumeText.setText(context.getResources().getString(R.string.hacim) + detailResponseModel.getVolume());
                        detailBinding.bidText.setText(context.getResources().getString(R.string.alis) + detailResponseModel.getBid());
                        detailBinding.offerText.setText(context.getResources().getString(R.string.satis) + detailResponseModel.getOffer());

                        detailBinding.lowestText.setText(context.getResources().getString(R.string.gunluk_dusus) + detailResponseModel.getLowest());
                        detailBinding.highestText.setText(context.getResources().getString(R.string.gunluk_yuksek) + detailResponseModel.getHighest());
                        detailBinding.countText.setText(context.getResources().getString(R.string.adet) + detailResponseModel.getCount());
                        detailBinding.maximumText.setText(context.getResources().getString(R.string.tavan) + detailResponseModel.getMaximum());
                        detailBinding.minimumText.setText(context.getResources().getString(R.string.taban) + detailResponseModel.getMinimum());

                        if (detailResponseModel.isUp()) {
                            detailBinding.changeImage.setImageResource(R.drawable.ic_arrow_up);
                            detailBinding.changeImage.setColorFilter(ContextCompat.getColor(context, R.color.colorUpper), android.graphics.PorterDuff.Mode.MULTIPLY);
                        } else if (detailResponseModel.isDown()) {

                            detailBinding.changeImage.setImageResource(R.drawable.ic_arrow_down);
                            detailBinding.changeImage.setColorFilter(ContextCompat.getColor(context, R.color.colorDowm), android.graphics.PorterDuff.Mode.MULTIPLY);

                        } else {
                            detailBinding.changeImage.setImageResource(R.drawable.ic_line);
                            detailBinding.changeImage.setColorFilter(ContextCompat.getColor(context, R.color.colorLine), android.graphics.PorterDuff.Mode.MULTIPLY);

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<DetailResponseModel> call, Throwable t) {
                Log.i("retrofittest", "Detail fail");

            }


        });
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getAesKey() {
        return AesKey;
    }

    public void setAesKey(String aesKey) {
        AesKey = aesKey;
    }

    public String getAesIV() {
        return AesIV;
    }

    public void setAesIV(String aesIV) {
        AesIV = aesIV;
    }
}
