package com.example.zadanie9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_BOOK_DETAILS_TITLE = "com.example.BOOK_DETAILS_TITLE";
    public static final String EXTRA_BOOK_DETAILS_AUTHOR = "com.example.BOOK_DETAILS_AUTHOR";
    public static final String EXTRA_BOOK_DETAILS_COVER_ID = "com.example.BOOK_DETAILS_COVERID";
    public static final String EXTRA_BOOK_DETAILS_SUBTITLE = "com.example.BOOK_DETAILS_SUBTITLE";
    private TextView title;
    private TextView author;
    private TextView subtitle;
    private ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        title = findViewById(R.id.title_t);
        author = findViewById(R.id.author_t);
        cover = findViewById(R.id.cover_t);
        subtitle = findViewById(R.id.subtitle_t);

        Intent start = getIntent();
        title.setText(start.getStringExtra(EXTRA_BOOK_DETAILS_TITLE));
        author.setText(start.getStringExtra(EXTRA_BOOK_DETAILS_AUTHOR));
        subtitle.setText(start.getStringExtra(EXTRA_BOOK_DETAILS_SUBTITLE));
        String coverId = start.getStringExtra(EXTRA_BOOK_DETAILS_COVER_ID);
        if (coverId != null) {
            Picasso.with(this)
                    .load(MainActivity.IMAGE_URL_BASE + coverId + "-L.jpg")
                    .placeholder(R.drawable.book)
                    .into(cover);
        } else {
            cover.setImageResource(R.drawable.book);
        }

    }
}