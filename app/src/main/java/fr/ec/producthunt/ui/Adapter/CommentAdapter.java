package fr.ec.producthunt.ui.Adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Comment;

/**
 * Created by gterral on 14/03/2017.
 */

public class CommentAdapter extends BaseAdapter {

    private List<Comment> datasource = Collections.EMPTY_LIST;

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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment,parent, false);
        }

        Comment comment = datasource.get(position);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText(comment.getContent());
        TextView createdAt = (TextView) convertView.findViewById(R.id.createdAt);
        createdAt.setText(comment.getCreatedAt());

        return convertView;
    }

    private class CommentAndView {
        public Comment comment;
        public View view;
    }


    public void showListComment(List<Comment> comments) {
        this.datasource = comments;
        notifyDataSetChanged();
    }
}
