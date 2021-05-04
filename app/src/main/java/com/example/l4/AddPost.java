package com.example.l4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class AddPost extends Fragment {

    private ImageView previewImage;
    private ImageView musicMarker;
    private Bitmap bitmapImage;
    private Uri songUri = null;
    private Uri imageUri = null;
    private MediaPlayer mediaPlayer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);


        final Context context = getActivity();

        final EditText title = view.findViewById(R.id.et_set_title);
        final EditText description = view.findViewById(R.id.et_set_description);

        previewImage = view.findViewById(R.id.iv_post_image_preview);
        musicMarker = view.findViewById(R.id.iv_music_marker_new_post);



        final Button selectMedia = view.findViewById(R.id.b_select_media);

        Button createPost = view.findViewById(R.id.b_create_post);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        selectMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicMarker.setVisibility(View.INVISIBLE);


                selectMedia(context);
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.length() > 0) {
                    Post post = new Post(title.getText().toString(), description.getText().toString(), bitmapImage, songUri, imageUri);
                    ((MainActivity) getActivity()).addPost(post);
                    getActivity().getSupportFragmentManager().beginTransaction().hide(AddPost.this).commit();
                    getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
                    mediaPlayer.reset();
                }
                else {
                    Toast.makeText(getContext(), "Set title", Toast.LENGTH_SHORT).show();
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().beginTransaction().hide(AddPost.this).commit();
                getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
                mediaPlayer.reset();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }

    private void selectMedia(Context context) {
        final CharSequence[] options = {"Chose from Audios", "Chose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select media");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Chose from Audios")) {
                    Intent selectAudio = new Intent(Intent.ACTION_GET_CONTENT);
                    selectAudio.setType("audio/*");
                    startActivityForResult(selectAudio, 1);
                }
                else if (options[which].equals("Chose from Gallery")) {
                    Intent selectPicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(selectPicture, 2);
                }
                else  {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RESULT_CANCELED) {
            previewImage.setImageResource(0);
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        songUri = data.getData();
                        bitmapImage = getSongPicture(getContext(), songUri);
                        previewImage.setImageBitmap(bitmapImage);
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            imageUri = data.getData();
                            Log.d("imageUri", imageUri.toString());
                            bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            previewImage.setImageBitmap(bitmapImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            if (previewImage.getDrawable() != null)
            resizePicture(previewImage);
            if (songUri != null) {
                try {
                    attachSongToImageView(previewImage, songUri);
                    musicMarker.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private Bitmap getSongPicture(Context context, Uri songUri) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(context, songUri);
        Bitmap bitmap;
        byte[] data = metadataRetriever.getEmbeddedPicture();
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }
    private void attachSongToImageView(ImageView iv, Uri songUri) throws IOException {
        mediaPlayer.reset();
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
    private void resizePicture(ImageView iv) {
        if (iv.getDrawable() != null) {
            Point displaySize = getDisplaySize();
            int imageWidth = iv.getDrawable().getIntrinsicWidth();
            float multiplier = (float) displaySize.x / imageWidth;
            ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
            layoutParams.width = displaySize.x;
            layoutParams.height = (int) (iv.getDrawable().getIntrinsicHeight() * multiplier);
            iv.setLayoutParams(layoutParams);
        }
    }
    private Point getDisplaySize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


}
