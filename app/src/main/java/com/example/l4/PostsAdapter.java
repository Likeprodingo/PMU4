package com.example.l4;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder>{

    private List<com.example.l4.Post> posts;
    private OnPostListener onPostListener;

    PostsAdapter(List<com.example.l4.Post> posts, OnPostListener onPostListener) {
        this.posts = posts;
        this.onPostListener = onPostListener;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.post_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        PostsViewHolder viewHolder = new PostsViewHolder(view, onPostListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        //holder.bind(position);
        Post post = posts.get(position);
        holder.listItemPostTitle.setText(post.title);
        holder.listItemPostDescription.setText(post.description);
        holder.imagePreview.setImageBitmap(post.bitmap);
        if (post.songUri != null) {
            holder.musicMarker.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemPostTitle;
        TextView listItemPostDescription;
        ImageView imagePreview;
        ImageView musicMarker;
        Uri imageUri;

        FrameLayout frameLayout;


        OnPostListener onPostListener;

        Point displaySize = MainActivity.size;

        PostsViewHolder(View itemView, OnPostListener onPostListener) {
            super(itemView);

            listItemPostTitle = itemView.findViewById(R.id.tv_post_title);
            listItemPostDescription = itemView.findViewById(R.id.tv_post_text);
            imagePreview = itemView.findViewById(R.id.iv_image_preview);
            musicMarker = itemView.findViewById(R.id.iv_post_list_music_marker);
            frameLayout = itemView.findViewById(R.id.fl);

            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);




            setSize(frameLayout);
            setSize(listItemPostTitle);
            setSize(listItemPostDescription);
            setSize(imagePreview);


        }

        public void bind(int position) {
            //listItemPostTitle.setText(title);
            //listItemPostText.setText(description);
            //listItemPostView.setText(String.valueOf(listIndex));
        }

        void setSize(FrameLayout fl) {
            ViewGroup.LayoutParams lp = fl.getLayoutParams();
            lp.height = displaySize.y / 3;
        }
        void setSize(ImageView iv) {
            ViewGroup.LayoutParams lp = iv.getLayoutParams();
            lp.width = displaySize.y / 3;
            lp.height = lp.width;
            iv.setLayoutParams(lp);
        }
        void setSize(TextView tv) {
            int width = displaySize.x - displaySize.y / 3 - 20;
            ViewGroup.LayoutParams lp = tv.getLayoutParams();
            lp.width = width;
            tv.setLayoutParams(lp);
        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface OnPostListener{
        void onPostClick(int position);
    }

}
