package jthd.trumpeter;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;



public class App extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        // Register any ParseObject subclass. Must be done before calling Parse.initialize()

        Parse.initialize(this, "jadibgKgQBjKhvYdPhptYCM0C2mTWIYkXeYKhsuq", "AYj5VuAVO5eaCD2KTHR19BqJZiq0zA6xMMmAsj1d");
        App.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return App.context;
    }
}