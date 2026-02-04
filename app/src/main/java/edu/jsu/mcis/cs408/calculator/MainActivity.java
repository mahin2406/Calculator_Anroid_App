package edu.jsu.mcis.cs408.calculator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}