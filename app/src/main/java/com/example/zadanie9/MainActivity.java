package com.example.zadanie9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        public ImageView bookImage;
        public TextView bookTitle;
        private TextView bookAuthor;
        private TextView numberOfPages;
        private Book book;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            bookImage = itemView.findViewById(R.id.img_cover);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookTitle = itemView.findViewById(R.id.book_title);
            numberOfPages = itemView.findViewById(R.id.number_of_pages);
        }

        public void bind(Book book) {
            View itemController = itemView.findViewById(R.id.list_book_item);

            itemController.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
                intent.putExtra(BookDetailsActivity.EXTRA_BOOK_DETAILS_TITLE, bookTitle.getText());
                intent.putExtra(BookDetailsActivity.EXTRA_BOOK_DETAILS_AUTHOR, bookAuthor.getText());
                intent.putExtra(BookDetailsActivity.EXTRA_BOOK_DETAILS_COVER_ID, book.getCover());
                intent.putExtra(BookDetailsActivity.EXTRA_BOOK_DETAILS_SUBTITLE, book.getSubtitle());
                
                startActivity(intent);
            });

            if(book != null && book.getAuthors() != null && checkNullOrEmpty(book.getTitle())) {
                this.book = book;
                bookTitle.setText(book.getTitle());
                bookAuthor.setText(TextUtils.join(", ", book.getAuthors()));
                numberOfPages.setText(book.getNumberOfPages());
                if (book.getCover() != null) {
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.book).into(bookImage);
                    Log.d("", IMAGE_URL_BASE + book.getCover() + "-S.jpg");
                } else {
                    bookImage.setImageResource(R.drawable.book);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                fetchBooksData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) { return false; }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);
        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                setupBookListView(response.body().getBookList());
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), "Something went wrong. Please try again later!",
                        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<Book> books) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final BookAdapter adapter = new BookAdapter();
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean checkNullOrEmpty(String text) {
        return text != null && !TextUtils.isEmpty(text);
    }

    public class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Book> bookList;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (bookList != null) {
                Book book = bookList.get(position);
                holder.bind(book);
            } else {
                Log.d("MainActivity", "NoBooks");
            }
        }

        void setBooks(List<Book> books) {
            this.bookList = books;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return bookList != null ? bookList.size() : 0;
        }
    }
}