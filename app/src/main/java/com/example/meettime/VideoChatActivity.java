package com.example.meettime;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.Manifest;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class VideoChatActivity extends AppCompatActivity
        implements Session.SessionListener,PublisherKit.PublisherListener
{

    private static String API_key="47803631";
    private static String SESSION_ID="1_MX40NzgwMzYzMX5-MTY5OTI3NDkyNjMxNX54Y0dxREJNUTJTcU5tSCt3RHhXZ21Wc05-fn4";
    private static String TOKEN="T1==cGFydG5lcl9pZD00NzgwMzYzMSZzaWc9YzE4YjgxOGY2YWI1MWNiZWU1MGZjZjNjM2RkOTA2ZDkyZDU5MDYzOTpzZXNzaW9uX2lkPTFfTVg0ME56Z3dNell6TVg1LU1UWTVPVEkzTkRreU5qTXhOWDU0WTBkeFJFSk5VVEpUY1U1dFNDdDNSSGhYWjIxV2MwNS1mbjQmY3JlYXRlX3RpbWU9MTY5OTI3NTAyMCZub25jZT0wLjMwOTU2MTAzMjM5ODU4Mzgmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTY5OTI5NjYxMiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static final String LOG_TAG=VideoChatActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM=124;
    private FrameLayout mPublisherViewController;
    private FrameLayout mSubscriberViewController;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 102;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ImageView closeVideoChatBtn;
    private DatabaseReference userRef;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");

        closeVideoChatBtn=findViewById(R.id.close_video_chat_btn);
        closeVideoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(userID).hasChild("Ringing")){
                            userRef.child(userID).child("Ringing").removeValue();

                            if (mPublisher!=null)
                            {
                                mPublisher.destroy();
                            }
                            if (mSubscriber!=null)
                            {
                                mSubscriber.destroy();
                            }
                            startActivity(new Intent(VideoChatActivity.this,RegisterationActivity.class));
                            finish();
                        }

                        if (dataSnapshot.child(userID).hasChild("Calling")){
                                userRef.child(userID).child("Calling").removeValue();

                            if (mPublisher!=null)
                            {
                                mPublisher.destroy();
                            }
                            if (mSubscriber!=null)
                            {
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(VideoChatActivity.this,RegisterationActivity.class));
                            finish();
                        }
                        else
                        {

                            if (mPublisher!=null)
                            {
                                mPublisher.destroy();
                            }
                            if (mSubscriber!=null)
                            {
                                mSubscriber.destroy();
                            }
                            
                            startActivity(new Intent(VideoChatActivity.this,RegisterationActivity.class));
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,VideoChatActivity.this);

    }
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions(){
        String []perms={Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
                if (EasyPermissions.hasPermissions(this,perms))
                {
                mPublisherViewController=findViewById(R.id.publisher_container);
                mSubscriberViewController=findViewById(R.id.subscriber_container);

                //1.initalize and connect to the session
                    mSession=new Session.Builder(this, API_key,SESSION_ID).build();
                    mSession.setSessionListener(VideoChatActivity.this);
                    mSession.connect(TOKEN);
                }
                else
                {
                    EasyPermissions.requestPermissions(this,"Hey this app needs Mic and Camera ,Please allow ",RC_VIDEO_APP_PERM);
                }
    }


   //2.publishing a stream to the session
    @Override
    public void onConnected(Session session)
    {
        Log.i(LOG_TAG,"Session Connected");
        mPublisher=new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoChatActivity.this);
        mPublisherViewController.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView)
        {
            ((GLSurfaceView)mPublisher.getView()).setZOrderOnTop(true);

        }
        mSession.publish(mPublisher);
    }
    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG,"Stream Disconnected");

    }
    //2.subscribing a stream
    // @Override
    public void onStreamReceived(Session session ,Stream stream){
        Log.i(LOG_TAG,"Stream Received");
        if (mSubscriber==null)
        {

            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewController.addView(mSubscriber.getView());

        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream Dropped");

        if (mSubscriber!=null)
        {
            mSubscriber=null;
            mSubscriberViewController.removeAllViews();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture){

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, com.opentok.android.Stream stream) {
        Log.i(LOG_TAG,"Stream Received");
        if (mSubscriber==null)
        {
            mSubscriber=new Subscriber.Builder(this,stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewController.addView(mSubscriber.getView());

        }

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, com.opentok.android.Stream stream) {
        Log.i(LOG_TAG,"Stream Dropped");

        if (mSubscriber!=null)
        {
            mSubscriber=null;
            mSubscriberViewController.removeAllViews();
        }

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.i(LOG_TAG,"Stream error");


    }
    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_TAG, "Exception: " + opentokError.getErrorCode() + " " + opentokError.getMessage());
    }
}