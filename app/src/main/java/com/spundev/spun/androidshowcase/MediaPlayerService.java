package com.spundev.spun.androidshowcase;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    //Available Actions
    public static final String ACTION_PAUSE_TRACK = "action_pause_track";
    public static final String ACTION_RESUME_TRACK = "action_resume_track";
    public static final String ACTION_PLAY_PREVIOUS_TRACK = "action_previous_track";
    public static final String ACTION_SET_TRACKS = "action_add_tracks";
    public static final int NOTIFICATION_ID = 3000;

    //Constants
    private static final String TRACK_URL = "track_url";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Set tracks
        if (intent.getAction().equals(ACTION_SET_TRACKS)) {
            playUrl(intent);
        }

        //Pause track
        if (intent.getAction().equals(ACTION_PAUSE_TRACK)) {
            pauseTrack();
        }

        //Resume track
        if (intent.getAction().equals(ACTION_RESUME_TRACK)) {
            resumeTrack();
        }

        return START_NOT_STICKY;
    }

    //Variables
    int mCurrentTrackIndex;
    MediaPlayer mMediaPlayer;
    ArrayList<Parcelable> mTrack;
    NotificationCompat.Builder notificationBuilder;

    public static void playUrl(Context context, String track) {
        Intent serviceIntent = new Intent(context, MediaPlayerService.class);
        serviceIntent.setAction(ACTION_SET_TRACKS);
        serviceIntent.putExtra(TRACK_URL, track);
        context.startService(serviceIntent);
    }

    private void playUrl(Intent data) {

        //Stop playback
        stopPlayback();

        //Start Media Player
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        try {
            mMediaPlayer.setDataSource(data.getStringExtra(TRACK_URL));
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pauseTrack(Context context, String track) {
        Intent serviceIntent = new Intent(context, MediaPlayerService.class);
        serviceIntent.setAction(ACTION_SET_TRACKS);
        serviceIntent.putExtra(TRACK_URL, track);
        context.startService(serviceIntent);
    }

    private void stopPlayback() {
        if (mMediaPlayer == null)
            return;

        if (mMediaPlayer.isPlaying())
            mMediaPlayer.stop();

        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

    }

    public static void pauseTrack(Context context) {
        context.startService(getPauseTrackIntent(context));
    }

    public static Intent getPauseTrackIntent(Context context) {
        Intent serviceIntent = new Intent(context, MediaPlayerService.class);
        serviceIntent.setAction(ACTION_PAUSE_TRACK);
        return serviceIntent;
    }

    private void pauseTrack() {

        Log.d("TAG", "PAUSE");

        if (mMediaPlayer == null)
            return;

        mMediaPlayer.pause();

        //stopForeground(false);

        Notification notification = createNotificationUsingCompat();
        if (notification != null) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    public static void resumeTrack(Context context) {
        context.startService(getResumeTrackIntent(context));
    }

    public static Intent getResumeTrackIntent(Context context) {
        Intent serviceIntent = new Intent(context, MediaPlayerService.class);
        serviceIntent.setAction(ACTION_RESUME_TRACK);
        return serviceIntent;
    }

    private void resumeTrack() {
        if (mMediaPlayer == null)
            return;

        mMediaPlayer.start();
        startForeground(NOTIFICATION_ID, createNotificationUsingCompat());
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Notification notification = createNotificationUsingCompat();
        startForeground(NOTIFICATION_ID, notification);
    }


    public static Intent getPlayPreviousTrackIntent(Context context) {
        Intent serviceIntent = new Intent(context, MediaPlayerService.class);
        serviceIntent.setAction(ACTION_PLAY_PREVIOUS_TRACK);
        return serviceIntent;
    }


    private Notification createNotificationUsingCompat() {

        // PendingIntents
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, getPauseTrackIntent(this), 0);
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, getResumeTrackIntent(this), 0);

        // METADATA
        MediaMetadataCompat.Builder mediaMetaDataBuilder = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Swimming With Dolphins")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Grégoire Lourme")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Cinematic Volume 7 Animation Movies");

        // CREATE A NEW MEDIASASSION
        ComponentName receiverComponent = new ComponentName(this, MainActivity.class);
        MediaSessionCompat mediaSession = new MediaSessionCompat(this, "debug tag", receiverComponent, null);
        mediaSession.setMetadata(mediaMetaDataBuilder.build());// Update the current metadata
        mediaSession.setActive(true);   // Indicate you're ready to receive media commands
        // Attach a new Callback to receive MediaSession updates
        // mediaSession.setCallback(new MediaSessionCompat.Callback() { });
        // Indicate you want to receive transport controls via your Callback
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // CREATE A NEW MEDIASTYLE
        NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
        style.setMediaSession(mediaSession.getSessionToken());      // Attach our MediaSession token
        style.setShowActionsInCompactView(0);                       // Show our playback controls in the compat view

        // CREATE A NEW NOTIFICATION
        notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setShowWhen(false);                     // Hide the timestamp
        notificationBuilder.setStyle(style);                        // Set the Notification style
        // notification.setColor(Color.BLACK);                      // Set the Notification color
        // notification.setLargeIcon(R.mipmap.ic_launcher);         // Set the large and small icons
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_social_mood);
        // Set Notification content information
        notificationBuilder.setContentText("Swimming With Dolphins");
        notificationBuilder.setContentInfo("Grégoire Lourme");
        notificationBuilder.setContentTitle("Cinematic Volume 7 Animation Movies");
        // Add some playback controls
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            notificationBuilder.addAction(android.R.drawable.ic_media_pause, "pause", pausePendingIntent);

        } else {
            notificationBuilder.addAction(android.R.drawable.ic_media_play, "play", playPendingIntent);
            stopForeground(false);
        }

        // Make sure that the notification can be dismissed by the user when we are not playing:
        notificationBuilder.setOngoing(mMediaPlayer != null && mMediaPlayer.isPlaying());

        return notificationBuilder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    @Override
    public void onDestroy() {

        if (mMediaPlayer != null) mMediaPlayer.release();
        stopForeground(true);

        super.onDestroy();
    }


}