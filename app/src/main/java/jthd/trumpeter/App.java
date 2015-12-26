package jthd.trumpeter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;



public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        // Register any ParseObject subclass. Must be done before calling Parse.initialize()

        Parse.initialize(this, "jadibgKgQBjKhvYdPhptYCM0C2mTWIYkXeYKhsuq", "AYj5VuAVO5eaCD2KTHR19BqJZiq0zA6xMMmAsj1d");
    }
}