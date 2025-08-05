package edu.gatech.seclass.jobcompare6300;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new MainMenuFragment());
            fragmentTransaction.commit();
        }

        // Still kinda confused why, but passing in context here,
        // and requiring context in the main menu fragment allows the
        // other classes to access the same data
        user = new User(this);
    }

}

