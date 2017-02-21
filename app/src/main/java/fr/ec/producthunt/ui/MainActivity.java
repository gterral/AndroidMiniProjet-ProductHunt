package fr.ec.producthunt.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ViewAnimator;
import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.database.ProductHuntDbHelper;
import fr.ec.producthunt.data.model.Post;
import fr.ec.producthunt.ui.Adapter.PostAdapter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  public static final int NB_ITEM = 400;

  private PostAdapter adapter;
  private ListView listView;
  private ProgressBar progressBar;
  private ViewAnimator viewAnimator;
  private ProductHuntDbHelper dbHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Utilisé une toolbar au lieu d'un actionbar
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(R.string.app_name);

    listView = (ListView) findViewById(R.id.listview);
    adapter = new PostAdapter();
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Post post = (Post) adapter.getItem(position);
        //Toast.makeText(MainActivity.this,post.getTitle(),Toast.LENGTH_SHORT).show();

        navigateToDetailActivity(post);
      }
    });

    progressBar = (ProgressBar) findViewById(R.id.progress);

    viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);

    viewAnimator.setDisplayedChild(1);

    dbHelper = new ProductHuntDbHelper(this);

    loadPosts();

  }

  private void navigateToDetailActivity(Post post) {
    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
    intent.putExtra(DetailActivity.POST_URL_KEY,post.getPostUrl());
    startActivity(intent);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {

    //Attacher le main menu au menu de l'activity
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.main, menu);

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {

      case R.id.refresh:
        refreshPosts();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void refreshPosts() {
    PostsAsyncTask postsAsyncTask = new PostsAsyncTask();
    postsAsyncTask.execute();
  }

  private class FetchPostsAsyncTask extends AsyncTask<Void,Void,List<Post>> {

    @Override protected void onPreExecute() {
      super.onPreExecute();
      Log.d(TAG, "onPreExecute: ");
      viewAnimator.setDisplayedChild(0);
    }


    @Override protected List<Post> doInBackground(Void... params) {

      return DataProvider.getPostsFromDatabase(dbHelper);
    }

    @Override protected void onPostExecute(List<Post> posts) {
      if (posts != null && !posts.isEmpty() ) {
        adapter.showListPost(posts);
      }
      viewAnimator.setDisplayedChild(1);


    }
  }

  private class PostsAsyncTask extends AsyncTask<Void, Integer, Boolean> {

    //Do on Main Thread
    @Override protected void onPreExecute() {
      super.onPreExecute();
      Log.d(TAG, "onPreExecute: ");
      viewAnimator.setDisplayedChild(0);
    }

    //Do on Background Thread
    @Override protected Boolean doInBackground(Void... params) {
      return DataProvider.syncPost(dbHelper);
    }

    //Do on Main Thread
    @Override protected void onPostExecute(Boolean result) {
      super.onPostExecute(result);
      Log.d(TAG, "onPostExecute() called with: " + "result = [" + result + "]");
      if (result ) {

        loadPosts();

      }else {
        viewAnimator.setDisplayedChild(1);

      }

    }
  }

  private void loadPosts() {
    FetchPostsAsyncTask fetchPstsAsyncTask = new FetchPostsAsyncTask();
    fetchPstsAsyncTask.execute();
  }

  public boolean isOnline() {

    ConnectivityManager connectivityManager =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
      return true;
    }

    return false;
  }
}
