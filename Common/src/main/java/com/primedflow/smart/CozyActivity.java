package com.primedflow.smart;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.charm.refined.R;

/**
 * Date：2025/12/1
 * Describe:
 */
public class CozyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cozy);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).hide(WindowInsetsCompat.Type.systemBars());
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) { // from class: com.compnew.deepcleaner.KenBoActivity$onCreate$1
            @Override // androidx.activity.OnBackPressedCallback
            public void handleOnBackPressed() {
            }
        });
    }
}
