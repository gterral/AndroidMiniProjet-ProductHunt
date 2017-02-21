package fr.ec.producthunt.data;

import android.util.Log;
import fr.ec.producthunt.data.database.PostDao;
import fr.ec.producthunt.data.database.ProductHuntDbHelper;
import fr.ec.producthunt.data.model.Post;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class DataProvider {

  private static final String TAG = "DataProvider";

  public static List<Post> getPosts(int number) {
    List<Post> list = new ArrayList<>(number);

    for (int i = 0; i < number; i++) {
      Post post = new Post();
      post.setTitle("Gear 360 " + i);
      post.setSubTitle("Capture stunning 360 video for virtual reality, by Samsung");

      list.add(post);
    }

    return list;
  }

  public static String getPostsFromWeb() {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Contiendra la réponse JSON brute sous forme de String .
    String posts = null;

    try {
      // Construire l' URL de l'API ProductHunt
      URL url = new URL(
          "https://api.producthunt.com/v1/posts?access_token=cd567777bbbfa3108bc701cbcd8b944bab23841dee7b83c39ea8e330972ac08c");

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
      posts = buffer.toString();
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

    return posts;
  }

  public static InputStream getImageStream(String urlImage)  {

    URL url= null;
    InputStream in = null ;
    try {

      url = new URL(urlImage);
      in = (InputStream) url.getContent();

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return in;
  }

  public static boolean syncPost(ProductHuntDbHelper dbHelper) {
    String postJson = getPostsFromWeb();
    List<Post> list = JsonPostParser.jsonToPosts(postJson);

    int nb = 0;
    PostDao postDao = new PostDao(dbHelper);
    for (Post post : list) {
      postDao.save(post);
      nb++;
    }
    return nb>0;
  }

  public static List<Post> getPostsFromDatabase(ProductHuntDbHelper dbHelper) {

    PostDao postDao = new PostDao(dbHelper);
    return postDao.retrievePosts();
  }
}

