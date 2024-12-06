package com.example.exoplayer;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo;
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;

    @UnstableApi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PlayerView playerView = findViewById(R.id.player_view);

        // This code can be used to skip media codec with hardware acceleration
//        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this)
//                .setMediaCodecSelector(new SoftwareOnlyMediaCodecSelector());
//        player = new ExoPlayer.Builder(this, renderersFactory).build();

        player = new ExoPlayer.Builder(this).build();

        playerView.setPlayer(player);

        String videoUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8";

        Uri uri = Uri.parse(videoUrl);
        MediaItem mediaItem = MediaItem.fromUri(uri);

        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
        player.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}


@UnstableApi
class SoftwareOnlyMediaCodecSelector implements MediaCodecSelector {

    @Override
    public List<MediaCodecInfo> getDecoderInfos(String mimeType, boolean requiresSecureDecoder, boolean requiresTunnelingDecoder) throws MediaCodecUtil.DecoderQueryException {
        List<MediaCodecInfo> allDecoders = MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder);
        List<MediaCodecInfo> softwareDecoders = new ArrayList<>();

        for (MediaCodecInfo codecInfo : allDecoders) {
            if (!codecInfo.hardwareAccelerated) {
                softwareDecoders.add(codecInfo);
            }
        }
        return softwareDecoders;
    }
}