package com.example.l4;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostsAdapter.OnPostListener{

    private RecyclerView postsList;
    private PostsAdapter postsAdapter;
    private Fragment fragment;
    ScrollView scrollView;
    FloatingActionButton fab;

    public static Point size;

    List<com.example.l4.Post> posts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();


        size = getDisplaySize();

        postsList = findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        postsList.setLayoutManager(layoutManager);
        //postsList.setHasFixedSize(true);

        String anek = "Мужик:\n" +
                "- Уважаемый, добрый день. А сколько ваши овцы дают шерсти за год?\n" +
                "- Какие: черные или белые?\n" +
                "- Черные.\n" +
                "- 2 килограмма.\n" +
                "- А белые?\n" +
                "- Тоже 2.\n" +
                "Удивился мужик, думает: дай, еще чего спрошу...\n" +
                "- Уважаемый, а сколько ваши овцы съедают корма в день?\n" +
                "- Какие: черные или белые?\n" +
                "- Белые.\n" +
                "- Килограмм.\n" +
                "- А черные?\n" +
                "- Тоже килограмм.\n" +
                "Мужик растерялся:\n" +
                "- Уважаемый, а почему вы все время спрашиваете, какие овцы, хотя результаты одинаковые?\n" +
                "- Дык, черные овцы-то мои!\n" +
                "- А белые?\n" +
                "- Белые? Тоже мои.";
        Post post1 = new Post("Анкдот", anek);
        posts.add(post1);
        postsAdapter = new PostsAdapter(posts, this);
        postsList.setAdapter(postsAdapter);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new AddPost();
                fragmentTransaction.add(R.id.host_activity, fragment);
                fragmentTransaction.commit();
                fab.setVisibility(View.INVISIBLE);
                //findViewById(R.id.host_activity).setVisibility(View.INVISIBLE);
            }
        });


    }


    public Point getDisplaySize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    public void addPost(Post post){
        posts.add(0, post);
        postsAdapter.notifyItemInserted(0);
        postsList.smoothScrollToPosition(0);
    }

    @Override
    public void onPostClick(int position) {

        Log.d("Clicked", position + "");
        Post post = posts.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("title", post.title);
        bundle.putString("description", post.description);
        //bundle.putParcelable("bitmap", post.bitmap);
        if (post.imageUri != null)
            bundle.putString("imageUri", post.imageUri.toString());
        if (post.songUri != null)
            bundle.putString("songUri", post.songUri.toString());
        ShowPost postFragment = new ShowPost();
        postFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.host_activity, postFragment);
        fragmentTransaction.commit();
        fab.setVisibility(View.INVISIBLE);
    }

}
