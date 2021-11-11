package com.tohan.veriparkcasestudy.Adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.tohan.veriparkcasestudy.Api.Models.StockModel;
import com.tohan.veriparkcasestudy.Helper.CryptoHepler;
import com.tohan.veriparkcasestudy.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableDataAdapter;


public class StockTableDataAdapter extends TableDataAdapter<String[]> {

    private String[][] stockList;
    private int paddingLeft = 20;
    private int paddingTop = 0;
    private int paddingRight = 20;
    private int paddingBottom = 0;
    private int textSize = 10;
    private int typeface = Typeface.NORMAL;
    private int textColor = 0x99000000;


    public StockTableDataAdapter(Context context, String[][] stockList) {
        super(context, stockList);
        this.stockList = stockList;

    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {

        final TextView textView = new TextView(getContext());
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setTypeface(textView.getTypeface(), typeface);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);


        if (columnIndex == 6) {
            final ImageView imageView = new ImageView(getContext());
            imageView.setPadding(0, paddingTop, 0, paddingBottom);

            if (stockList[rowIndex][columnIndex].equals("up")) {
                imageView.setImageResource(R.drawable.ic_arrow_up);
                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorUpper), android.graphics.PorterDuff.Mode.MULTIPLY);


            } else if (stockList[rowIndex][columnIndex].equals("down")) {

                imageView.setImageResource(R.drawable.ic_arrow_down);
                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorDowm), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else {
                imageView.setImageResource(R.drawable.ic_line);
                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLine), android.graphics.PorterDuff.Mode.MULTIPLY);

            }

            return imageView;
        }

        try {
            final String textToShow = getItem(rowIndex)[columnIndex];
            textView.setText(textToShow);
        } catch (final IndexOutOfBoundsException e) {

        }

        return textView;
    }


    public void setTextSize(final int textSize) {
        this.textSize = textSize;
    }


    public void setTextColor(final int textColor) {
        this.textColor = textColor;
    }

}
