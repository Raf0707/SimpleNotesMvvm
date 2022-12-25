package com.example.notes_mvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.notes_mvvm.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private NoteViewModel noteViewModel;
    RVAdapter adapter = new RVAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteViewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                        .getInstance(this.getApplication()))
                .get(NoteViewModel.class);

        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        DataInsertActivity.class);
                intent.putExtra("type", "addMode");
                startActivityForResult(intent, 1);
            }
        });

        binding.rV.setLayoutManager(new LinearLayoutManager(this));
        binding.rV.setHasFixedSize(true);
        binding.rV.setAdapter(adapter);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                    Snackbar.make(binding.getRoot(), "note deleted", Snackbar.LENGTH_SHORT);
                } else {
                    Intent intent = new Intent
                            (MainActivity.this, DataInsertActivity.class);

                    intent.putExtra("type", "update");

                    intent.putExtra("title", adapter.getNote
                            (viewHolder.getAdapterPosition()).getTitle());

                    intent.putExtra("disp", adapter.getNote
                            (viewHolder.getAdapterPosition()).getDisp());

                    intent.putExtra("id", adapter.getNote
                            (viewHolder.getAdapterPosition()).getId());

                    startActivityForResult(intent, 2);

                    Snackbar.make(binding.getRoot(), "note updated", Snackbar.LENGTH_SHORT);
                }
            }
        }).attachToRecyclerView(binding.rV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            Note note = new Note(title, disp);
            noteViewModel.insert(note);

            Snackbar.make(binding.getRoot(), "note added", Snackbar.LENGTH_SHORT).show();
        } else if (requestCode == 2) {
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            Note note = new Note(title, disp);
            note.setId(data.getIntExtra("id", 0));
            noteViewModel.update(note);

            Snackbar.make(binding.getRoot(), "note updated", Snackbar.LENGTH_SHORT).show();
        }
    }
}