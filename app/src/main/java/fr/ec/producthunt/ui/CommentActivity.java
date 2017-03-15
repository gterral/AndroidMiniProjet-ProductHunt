package fr.ec.producthunt.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProviderComment;
import fr.ec.producthunt.data.database.ProductHuntDbHelper;
import fr.ec.producthunt.data.model.Comment;
import fr.ec.producthunt.ui.Adapter.CommentAdapter;

import static java.lang.Integer.parseInt;

/**
 * Created by gterral on 14/03/2017.
 */

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";
    public static final int NB_ITEM = 400;
    public static final String POST_ID_KEY = "1";
    public static final String POST_TITLE_KEY = "TITRE DU POST";

    private CommentAdapter adapter;
    private ListView listView;
    private ProgressBar progressBar;
    private ViewAnimator viewAnimator;
    private ProductHuntDbHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int postId = parseInt(obtainPostIdFromIntent());
        String postTitle = obtainPostTitleFromIntent();
        setTitle(postTitle);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new CommentAdapter();
        listView.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);

        viewAnimator.setDisplayedChild(1);

        dbHelper = new ProductHuntDbHelper(this);

        swipeRefreshComments = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_comments);
        swipeRefreshComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshComments();
                swipeRefreshComments.setRefreshing(false);
            }
        });

        loadComments();

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
                refreshComments();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class FetchCommentsAsyncTask extends AsyncTask<Void, Void, List<Comment>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
            viewAnimator.setDisplayedChild(0);
        }

        @Override
        protected List<Comment> doInBackground(Void... params) {
            String postId = obtainPostIdFromIntent();
            return DataProviderComment.getCommentsFromDatabase(dbHelper,postId);
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            if (comments != null && !comments.isEmpty()) {
                adapter.showListComment(comments);
            }
            viewAnimator.setDisplayedChild(1);

        }
    }

    private class CommentsAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        //Do on Main Thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
            viewAnimator.setDisplayedChild(0);
        }

        //Do on Background Thread
        @Override
        protected Boolean doInBackground(Void... params) {
            String postId = obtainPostIdFromIntent();
            return DataProviderComment.syncComment(dbHelper,postId);
        }

        //Do on Main Thread
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecuteComment() called with: " + "result = [" + result + "]");
            if (result) {

                loadComments();

            } else {
                viewAnimator.setDisplayedChild(1);

            }

        }
    }

    private void loadComments() {
        FetchCommentsAsyncTask fetchCommentsAsyncTask = new FetchCommentsAsyncTask();
        fetchCommentsAsyncTask.execute();
    }

    private void refreshComments() {
        CommentsAsyncTask commentsAsyncTask = new CommentsAsyncTask();
        commentsAsyncTask.execute();
    }

    private String obtainPostIdFromIntent() {

        Intent intent = getIntent();
        if(intent.getExtras().containsKey(POST_ID_KEY)) {
            return intent.getExtras().getString(POST_ID_KEY);
        }else {
            throw new IllegalStateException("Il faut passer l'id du post");
        }
    }

    private String obtainPostTitleFromIntent() {

        Intent intent = getIntent();
        if(intent.getExtras().containsKey(POST_TITLE_KEY)) {
            return intent.getExtras().getString(POST_TITLE_KEY);
        }else {
            throw new IllegalStateException("Il faut passer le titre du post");
        }
    }
}
