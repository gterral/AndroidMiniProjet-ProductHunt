package fr.ec.producthunt.data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.ec.producthunt.data.database.CommentDao;
import fr.ec.producthunt.data.database.ProductHuntDbHelper;
import fr.ec.producthunt.data.model.Comment;

/**
 * Created by gterral on 14/03/2017.
 */

public class DataProviderComment {
    private static final String TAG = "DataProviderComment";


    public static String getCommentsFromWeb(String postId, String lastCommentId) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Contiendra la réponse JSON brute sous forme de String .
        String comments = null;
        String post_id = postId;
        String last_comment_id=lastCommentId;

        try {
            // Construire l' URL de l'API ProductHunt
            URL url = new URL(
                    "https://api.producthunt.com/v1/posts/" + post_id +"/comments/?newer="+last_comment_id+"&access_token=cd567777bbbfa3108bc701cbcd8b944bab23841dee7b83c39ea8e330972ac08c");

            // Creer de la requête http vers  l'API ProductHunt , et ouvrir la connexion
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Lire le  input stream et le convertir String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Si le stream est vide, on revoie null;
                return null;
            }
            comments = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return comments;
    }

    public static boolean syncComment(ProductHuntDbHelper dbHelper,String postId) {

        CommentDao commentDao = new CommentDao(dbHelper);
        String lastCommentId = commentDao.getLastCommentId(postId);

        String postJson = getCommentsFromWeb(postId,lastCommentId);
        List<Comment> list = JsonParser.jsonToComments(postJson);

        int nb = 0;
        for (Comment comment : list) {
            commentDao.save(comment);
            nb++;
        }
        return nb>0;
    }

    public static List<Comment> getCommentsFromDatabase(ProductHuntDbHelper dbHelper,String postId) {

        CommentDao commentDao = new CommentDao(dbHelper);
        return commentDao.retrieveComments(postId);
    }


}
