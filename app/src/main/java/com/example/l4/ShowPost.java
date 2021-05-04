package com.example.l4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class ShowPost extends Fragment {
    MediaPlayer mediaPlayer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_post, container, false);
        final TextView title = view.findViewById(R.id.tv_title);
        final TextView description = view.findViewById(R.id.tv_description);
        final ImageView bitmap = view.findViewById(R.id.iv_show_post_image);
        ImageView musicMarker = view.findViewById(R.id.iv_music_marker_show_post);

        title.setText(this.getArguments().getString("title"));
        description.setText(this.getArguments().getString("description"));
            if (this.getArguments().containsKey("imageUri")) {
                bitmap.setImageURI(Uri.parse(this.getArguments().getString("imageUri")));
                Log.d("picUri", this.getArguments().getString("imageUri"));
        }
        if (this.getArguments().containsKey("songUri")) {
            Uri songUri = Uri.parse(this.getArguments().getString("songUri"));
            Log.d("songUri", songUri.toString());
            musicMarker.setVisibility(View.VISIBLE);
            Bitmap bitmap1 = getSongPicture(getContext(), songUri);
            bitmap.setImageBitmap(bitmap1);
            try {

                attachSongToImageView(bitmap, songUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().beginTransaction().hide(ShowPost.this).commit();
                getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer = null;
                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }
    private void attachSongToImageView(ImageView iv, Uri songUri) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getActivity(), songUri);
        mediaPlayer.prepare();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                else
                    mediaPlayer.pause();
            }
        });
    }
    private Bitmap getSongPicture(Context context, Uri songUri) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(context, songUri);
        Bitmap bitmap;
        byte[] data = metadataRetriever.getEmbeddedPicture();
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }
}
