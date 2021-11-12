package com.tohan.veriparkcasestudy.Helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.tohan.veriparkcasestudy.Adapters.StockTableDataAdapter;
import com.tohan.veriparkcasestudy.Api.Models.DetailRequestModel;
import com.tohan.veriparkcasestudy.Api.Models.DetailResponseModel;
import com.tohan.veriparkcasestudy.Api.Models.GraphicDataModel;
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
import com.github.mikephil.charting.components.YAxis;
import com.tohan.veriparkcasestudy.ui.detail.MyMarkerView;


import java.util.ArrayList;
import java.util.List;

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

    /**
     * Creates a HandShake State for listing.
     *
     * @param context      getContext() from fragment.
     * @param tableView    from source fragment's Tableview.
     * @param searchView   from source fragment's SearchView.
     * @param root         get View from fragment's binding.getRoot().
     * @param fragmentName for ClickListener navigation.
     * @param period       for list period.
     */
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

    /**
     * Creates a HandShake State for detail page.
     *
     * @param context       getContext() from fragment.
     * @param id            from clicked row for detail.
     * @param detailBinding from detail fragment's binding.
     */
    public void initDetailState(Context context, int id, FragmentDetailBinding detailBinding) {
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

                if (!query.equals("") && stda != null && stockListMain.size() > 0) {
                    SearchHelper searchHelper = new SearchHelper();
                    stda = new StockTableDataAdapter(context, searchHelper.searchList(stockListMain, query));
                    stockClickListener.setStockList(searchHelper.getResultArray());
                    tableView.setDataAdapter(stda);
                }
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

    private void getDataList(HandshakeResponseModel handshakeModel, String period) {
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


                        getDetailData(handshakeModel, id, handshakeModel.getAesKey(), handshakeModel.getAesIV());


                    }
                }
            }

            @Override
            public void onFailure(Call<HandshakeResponseModel> call, Throwable t) {
                Log.i("retrofittest", "getAuthorization");

            }


        });

    }

    private void getDetailData(HandshakeResponseModel handshakeModel, int id, String AesKey, String AesIV) {
        Call<DetailResponseModel> responseModel = repository.getStockDetail(new DetailRequestModel(cryptoHepler.encrypt(String.valueOf(id), handshakeModel.getAesKey(), handshakeModel.getAesIV())),
                handshakeModel.getAuthorization());
        ;

        responseModel.enqueue(new Callback<DetailResponseModel>() {
            @Override
            public void onResponse(Call<DetailResponseModel> call, retrofit2.Response<DetailResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        DetailResponseModel detailResponseModel = response.body();
                        detailBinding.symbolText.setText(context.getResources().getString(R.string.sembol) + cryptoHepler.decrypt(detailResponseModel.getSymbol(), AesKey, AesIV));
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
                        List<GraphicDataModel> graphicDataModelList = detailResponseModel.getGraphicData();
                        GetChartData(graphicDataModelList, detailResponseModel.getMaximum(), detailResponseModel.getMinimum());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailResponseModel> call, Throwable t) {
                Log.i("retrofittest", "Detail fail");

            }


        });
    }

    private void GetChartData(List<GraphicDataModel> dataPoints, double maximum, double minimum) {
        LineChart chart = detailBinding.chart;

        List<Entry> entries = new ArrayList<Entry>();
        for (GraphicDataModel data : dataPoints) {
            entries.add(new Entry(data.getDay(), (float) data.getValue()));
        }
        // disable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(entries, "Aylık Grafik");

        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLUE);
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setLineWidth(1.75f);
        set1.setCircleRadius(3f);
        set1.setCircleHoleColor(Color.BLACK);
        set1.setDrawValues(false);
        set1.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.chart_gradient);
        set1.setFillDrawable(drawable);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        LimitLine ll1 = new LimitLine((float) maximum, "Tavan");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine((float) minimum, "Taban");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(10f);

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum((float) (maximum*1.1f));
            yAxis.setAxisMinimum((float) (minimum-minimum*.1f));
        }
        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(true);

        // add limit lines
        yAxis.addLimitLine(ll1);
        yAxis.addLimitLine(ll2);

        set1.setHighlightEnabled(true); // allow highlighting for DataSet

        // set this to false to disable the drawing of highlight indicator (lines)
        set1.setDrawHighlightIndicators(true);
        set1.setHighLightColor(Color.BLACK);


        IMarker marker = new MyMarkerView(context, R.layout.custom_marker_view);
        chart.setMarker(marker);



        // create a data object with the data sets
        LineData data = new LineData(dataSets);



        // set data
        chart.setData(data);
        chart.invalidate();


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
