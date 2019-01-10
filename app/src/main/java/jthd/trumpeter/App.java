package jthd.trumpeter;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;


import com.facebook.FacebookSdk;



public class App extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Register any ParseObject subclass. Must be done before calling Parse.initialize(
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myAppId")
                .server("https://trumpeter-parse-server.herokuapp.com/parse/")
                .build());
        App.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return App.context;
    }
}