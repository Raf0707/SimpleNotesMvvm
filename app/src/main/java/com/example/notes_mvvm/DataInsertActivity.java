package com.example.notes_mvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.notes_mvvm.databinding.ActivityDataInsertBinding;

public class DataInsertActivity extends AppCompatActivity {
    ActivityDataInsertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type = getIntent().getStringExtra("type");
        if (type.equals("update")) {
            binding.title.setText(getIntent().getStringExtra("title"));
            binding.disp.setText(getIntent().getStringExtra("disp"));
            int id = getIntent().getIntExtra("id", 0);
            binding.add.setText("update note");
            binding.add.setTextSize(17.2f);

            binding.add.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra("title", binding.title.getText().toString());
                intent.putExtra("disp", binding.disp.getText().toString());
                intent.putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            });
        } else {
            setTitle("Add Mode");
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("title", binding.title.getText().toString());
                    intent.putExtra("disp", binding.disp.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DataInsertActivity.this, MainActivity.class));
    }
}