package com.mottled.quell.kill;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Date：2026/1/4
 * Describe:
 */
public class BaseAppCom extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dzs_goa);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).hide(WindowInsetsCompat.Type.systemBars());
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) { // from class: com.compnew.deepcleaner.KenBoActivity$onCreate$1
            @Override // androidx.activity.OnBackPressedCallback
            public void handleOnBackPressed() {
            }
        });
    }
}
