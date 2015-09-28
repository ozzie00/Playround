package com.oneme.toplay.weather;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneme.toplay.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {
	
	Typeface weatherFont;
    Typeface weatherfirstdayFont;
    Typeface weatherseconddayFont;
    Typeface weatherthirddayFont;
    Typeface weatherfourthdayFont;
	
	TextView cityField;
	TextView updatedField;
    TextView detailsField;
    TextView weatherfirstdayField;
    TextView weatherseconddayField;
    TextView weatherthirddayField;
    TextView weatherfourthdayField;
	TextView currentTemperatureField;

    TextView weatherIcon;

    TextView weatherfirstdayIcon;
    TextView weatherseconddayIcon;
    TextView weatherthirddayIcon;
    TextView weatherfourthdayIcon;
	
	Handler handler;
	
	public WeatherFragment(){	
		handler = new Handler();
	}

    private int weatherdetail[] = {
            R.id.details_field,
            R.id.weather_first_day_details_field,
            R.id.weather_second_day_details_field,
            R.id.weather_third_day_details_field,
            R.id.weather_fourth_day_details_field
    };
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ome_activity_weather_fragment, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
       // updatedField = (TextView)rootView.findViewById(R.id.updated_field);

        // weather forecast 5 day
        for (int i = 0; i < 5; i++) {
            switch(i) {
                case 0 : detailsField          = (TextView)rootView.findViewById(R.id.details_field);
                    break;
                case 1 : weatherfirstdayField  = (TextView)rootView.findViewById(R.id.weather_first_day_details_field);
                    break;
                case 2 : weatherseconddayField = (TextView)rootView.findViewById(R.id.weather_second_day_details_field);
                    break;
                case 3 : weatherthirddayField  = (TextView)rootView.findViewById(R.id.weather_third_day_details_field);
                    break;
                case 4 : weatherfourthdayField = (TextView)rootView.findViewById(R.id.weather_fourth_day_details_field);
                    break;
            }
        }


       // detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon             = (TextView)rootView.findViewById(R.id.weather_icon);
        weatherfirstdayIcon     = (TextView)rootView.findViewById(R.id.weather_first_day_icon);
        weatherseconddayIcon    = (TextView)rootView.findViewById(R.id.weather_second_day_icon);
        weatherthirddayIcon     = (TextView)rootView.findViewById(R.id.weather_third_day_icon);
        weatherfourthdayIcon    = (TextView)rootView.findViewById(R.id.weather_fourth_day_icon);
        
        weatherIcon.setTypeface(weatherFont);
        weatherfirstdayIcon.setTypeface(weatherfirstdayFont);
        weatherseconddayIcon.setTypeface(weatherseconddayFont);
        weatherthirddayIcon.setTypeface(weatherthirddayFont);
        weatherfourthdayIcon.setTypeface(weatherfourthdayFont);
        return rootView; 
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);  
    	weatherFont          = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        weatherfirstdayFont  = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        weatherseconddayFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        weatherthirddayFont  = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        weatherfourthdayFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
     	updateWeatherData(new CityPreference(getActivity()).getCity());
    }
    
    private void updateWeatherData(final String city){
    	new Thread(){
    		public void run(){
    			final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
    			if(json == null){
    				handler.post(new Runnable(){
    					public void run(){
    						//Toast.makeText(getActivity(),
                            //        getActivity().getString(R.string.OMWPARSEWEATHERPLACENOTFOUND),
                            //        Toast.LENGTH_LONG).show();
    					}
    				});
    			} else {
    				handler.post(new Runnable(){
    					public void run(){
    						renderWeather(json);
    					}
    				});
    			}    			
    		}
    	}.start();
    }
    
    private void renderWeather(JSONObject json){
    	try {
            // forecast 5 day weather info
            for (int i = 0; i < 5; i++) {
                JSONObject detailsindex = json.getJSONArray("list").getJSONObject(i);
                JSONObject details      = detailsindex.getJSONArray("weather").getJSONObject(0);
                switch(i) {
                    case 0 : detailsField.setText(
                            details.getString("description").toUpperCase(Locale.US) +
                                    "\n" + getResources().getString(R.string.OMEPARSEWEATHERHUMIDITY) + ": " + detailsindex.getString("humidity") + "%" +
                                    "\n" + getResources().getString(R.string.OMEPARSEWEATHERPRESSURE) + ": " + detailsindex.getString("pressure") + " hPa");
                        currentTemperatureField.setText(
                                String.format("%.2f", detailsindex.getJSONObject("temp").getDouble("day")) + " ℃");
                        break;
                    case 1 : weatherfirstdayField.setText(
                            String.format("%.2f", detailsindex.getJSONObject("temp").getDouble("day")) + " ℃");
                        break;
                    case 2 : weatherseconddayField.setText(

                            String.format("%.2f", detailsindex.getJSONObject("temp").getDouble("day")) + " ℃");
                        break;
                    case 3 : weatherthirddayField.setText(

                            String.format("%.2f", detailsindex.getJSONObject("temp").getDouble("day")) + " ℃");
                        break;
                    case 4 : weatherfourthdayField.setText(

                            String.format("%.2f", detailsindex.getJSONObject("temp").getDouble("day")) + " ℃");
                        break;
                }
              //  detailsField.get(i).setText(
              //          details.getString("description").toUpperCase(Locale.US) +
              //                  "\n" + "Humidity: " + detailsindex.getString("humidity") + "%" +
              //                  "\n" + "Pressure: " + detailsindex.getString("pressure") + " hPa");



                newsetWeatherIcon(details.getInt("id"), i);
            }

    	}catch(Exception e){
    		//Log.e("SimpleWeather", "One or more fields not found in the JSON data  " + e.getMessage());
    	}
    }

    private void renderWeatherold(JSONObject json){
        try {
            //cityField.setText(json.getString("name").toUpperCase(Locale.US) +
            //		", " +
            //		json.getJSONObject("sys").getString("country"));

            //JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject detailsindex = json.getJSONArray("list").getJSONObject(0);
            JSONObject details = detailsindex.getJSONArray("weather").getJSONObject(0);

            // cityField.setText(json.getString("city").toUpperCase(Locale.US) +
            // 		", ");// +
            //json.getJSONObject("sys").getString("country"));


            // JSONObject main = json.getJSONObject("main");
            //detailsField.setText(
            //		details.getString("description").toUpperCase(Locale.US) +
            //		"\n" + "Humidity: " + main.getString("humidity") + "%" +
            //		"\n" + "Pressure: " + main.getString("pressure") + " hPa");

            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + detailsindex.getString("humidity") + "%" +
                            "\n" + "Pressure: " + detailsindex.getString("pressure") + " hPa");

            //currentTemperatureField.setText(
            //			String.format("%.2f", main.getDouble("temp"))+ " ℃");

            currentTemperatureField.setText(
                    String.format("%.2f", detailsindex.getJSONObject("temp").getDouble("day"))+ " ℃");



            DateFormat df = DateFormat.getDateTimeInstance();
            //String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            String updatedOn = df.format(new Date(detailsindex.getLong("dt")*1000));

            // updatedField.setText("Last update: " + updatedOn);

            //setWeatherIcon(details.getInt("id"),
            //		json.getJSONObject("sys").getLong("sunrise") * 1000,
            //		json.getJSONObject("sys").getLong("sunset") * 1000);

            newsetWeatherIcon(details.getInt("id"), 0);

        }catch(Exception e){
            //Log.e("SimpleWeather", "One or more fields not found in the JSON data  " + e.getMessage());
        }
    }
    
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
    	int id = actualId / 100;
    	String icon = "";
    	if(actualId == 800){
    		long currentTime = new Date().getTime();
    		if(currentTime>=sunrise && currentTime<sunset) {
    			icon = getActivity().getString(R.string.OMEPARSEWEATHERSUNNY);
    		} else {
    			icon = getActivity().getString(R.string.OMEPARSEWEATHERCLEARNIGHT);
    		}
    	} else {
	    	switch(id) {
	    	case 2 : icon = getActivity().getString(R.string.OMEPARSEWEATHERTHUNDER);
			 		 break;     	
	    	case 3 : icon = getActivity().getString(R.string.OMEPARSEWEATHERDRIZZLE);
			 		 break;    	
	    	case 7 : icon = getActivity().getString(R.string.OMEPARSEWEATHERFOGGY);
	    			 break;
	    	case 8 : icon = getActivity().getString(R.string.OMEPARSEWEATHERCLOUDY);
			 		 break;
	    	case 6 : icon = getActivity().getString(R.string.OMEPARSEWEATHERSNOWY);
			 		 break;
	    	case 5 : icon = getActivity().getString(R.string.OMEPARSEWEATHERRAINY);
	    			 break;
	    	}
    	}
    	weatherIcon.setText(icon);
    }

    private void newsetWeatherIcon(int actualId, int day){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
           // if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.OMEPARSEWEATHERSUNNY);
            //} else {
            //    icon = getActivity().getString(R.string.weather_clear_night);
            //}
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.OMEPARSEWEATHERTHUNDER);
                    break;
                case 3 : icon = getActivity().getString(R.string.OMEPARSEWEATHERDRIZZLE);
                    break;
                case 7 : icon = getActivity().getString(R.string.OMEPARSEWEATHERFOGGY);
                    break;
                case 8 : icon = getActivity().getString(R.string.OMEPARSEWEATHERCLOUDY);
                    break;
                case 6 : icon = getActivity().getString(R.string.OMEPARSEWEATHERSNOWY);
                    break;
                case 5 : icon = getActivity().getString(R.string.OMEPARSEWEATHERRAINY);
                    break;
            }
        }

        switch(day) {
            case 0 : weatherIcon.setText(icon);
                break;
            case 1 : weatherfirstdayIcon.setText(icon);
                break;
            case 2 : weatherseconddayIcon.setText(icon);
                break;
            case 3 : weatherthirddayIcon.setText(icon);
                break;
            case 4 : weatherfourthdayIcon.setText(icon);
                break;
        }
    }
    
    public void changeCity(String city){
    	updateWeatherData(city);
    }
}
