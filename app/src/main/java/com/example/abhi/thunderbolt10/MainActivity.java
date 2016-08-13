package com.example.abhi.thunderbolt10;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    private CurrentWeather mCurrentWeather;
    public static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView  mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);
        final double latitude=37.8267;
        final double longitude=-122.423;
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);
            }
        });

        getForecast(latitude,longitude);
        Log.v(TAG,"MAIN UI THREAD");//would run before than above request as in main ui

    }

    private void getForecast( double latitude,double longitude) {
        String apiKey = "a3604bb0637dba1177ecbd8a9215cffc";

        String ForecastUrl = "https://api.forecast.io/forecast/"+apiKey+"/"+latitude+","+longitude;

        if(isNetworkAvailable()){
            toggleRefresh();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                                     .url(ForecastUrl)
                                      .build();//building the request with url
        Call call =  client.newCall(request);//putting thus request inside call object
     //async call as in que in background down below one
        //enqueue-----call us back when u are done
          call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });
                alertUserAboutUserError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG,jsonData);
//            Response response =call.execute();//execute is the synchronus method
                    if(response.isSuccessful())
                    {
                        mCurrentWeather = getCurrentDetails(jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();

                            }
                        });

                    }
                    else
                    {
                        alertUserAboutUserError();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"EXCEPTION CAUGHT :",e);
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"EXCEPTION CAUGHT :",e);
                }
            }
        });}
        else
        {
            alertUserAboutNetworkError();

            Toast.makeText(this, R.string.network_unavailable_message,Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility()==View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getTemperature()+"");//this task can only be done on main ui thread not background
        //so as called in background cant be done so use RUN ON UI THREAD ******
        mTimeLabel.setText("At "+ mCurrentWeather.getDateFormat()+ " it will be");
        mHumidityValue.setText(mCurrentWeather.getHumidity()+"");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance()+"%");
        mSummaryLabel.setText(mCurrentWeather.getSummmary());
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);

    }

    //jason obj class**
    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException{

        JSONObject forcast  = new JSONObject(jsonData);
        String timeZone = forcast.getString("timezone");//"timezone" was the key of the json object we wanted whose value would be returned
        Log.v(TAG,timeZone);

        JSONObject currently = forcast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummmary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timeZone);

        return currentWeather;
    }

    private void alertUserAboutNetworkError() {
        AlertDialogFragment diallog = new AlertDialogFragment();
        diallog.show(getFragmentManager(),"network_error");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable=false;
        Log.v(TAG,"NETWORKKKK");
        if(networkInfo != null && networkInfo.isConnected())//we have a network and connected
        {
            isAvailable=true;

        }
        return isAvailable;
    }

    private void alertUserAboutUserError() {
        AlertDialogFragment dialog  =new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

}
