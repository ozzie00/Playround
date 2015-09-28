package com.oneme.toplay.weather;

import android.content.Context;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class RemoteFetch {

	private static final String OPEN_WEATHER_MAP_API =
			"http://api.openweathermap.org/data/2.5/forecast/daily?lat=35&lon=139&cnt=5&mode=json&units=metric";//"http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    private static final String OPEN_WEATHER_FORECAST_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast/daily?";//"http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API_FORMAT = "&cnt=5&mode=json&units=metric";

    public static JSONObject getJSON(Context context, String city){

        String currentLattitude = Application.getCurrentLatitude();
        String currentLongitude = Application.getCurrentLongitude();
        Locale currentlocale    = Locale.getDefault();

		try {
            StringBuilder mstringBuilder = new StringBuilder(OPEN_WEATHER_FORECAST_MAP_API);
            mstringBuilder.append("lat=" + currentLattitude);
            mstringBuilder.append("&lon=" + currentLongitude);
            mstringBuilder.append("&lang=" + currentlocale);
            mstringBuilder.append(OPEN_WEATHER_MAP_API_FORMAT);

            URL url = new URL(mstringBuilder.toString());

			//URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
           // URL url = new URL(String.format(OPEN_WEATHER_MAP_API));

            HttpURLConnection connection =
					(HttpURLConnection)url.openConnection();
			
			connection.addRequestProperty("x-api-key", 
					context.getString(R.string.open_weather_maps_app_id));
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			
			StringBuffer json = new StringBuffer(1024);
			String tmp="";
			while((tmp=reader.readLine())!=null)
				json.append(tmp).append("\n");
			reader.close();
			
			JSONObject data = new JSONObject(json.toString());
			
			if(data.getInt("cod") != 200){
				return null;
			}
			
			return data;
		}catch(Exception e){
			return null;
		}
	}
	
}
