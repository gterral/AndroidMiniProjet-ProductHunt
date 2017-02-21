package fr.ec.producthunt.ui.Adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Post;
import java.util.Collections;
import java.util.List;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class PostAdapter extends BaseAdapter {

  private List<Post> datasource = Collections.EMPTY_LIST;

  public PostAdapter() {
  }

  @Override public int getCount() {
    return datasource.size();
  }

  @Override public Object getItem(int position) {
    return datasource.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    if(convertView == null) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent, false);
    }

    Post post = datasource.get(position);
    TextView title = (TextView) convertView.findViewById(R.id.title);
    title.setText(post.getTitle());
    TextView subTitle = (TextView) convertView.findViewById(R.id.sub_title);
    subTitle.setText(post.getSubTitle());

    ImageView imageView = (ImageView) convertView.findViewById(R.id.img_product);
    Picasso.with(parent.getContext()).load(post.getUrlImage()).into(imageView);
    return convertView;
  }

  private class PostAndView {
    public Post post;
    public View view;
    public Bitmap bitmap;
  }


  public void showListPost(List<Post> posts) {
    this.datasource = posts;
    notifyDataSetChanged();
  }
}
