package com.ccs.app.frirebasecloudstorage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Choose authentication providers
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build()//,
////                new AuthUI.IdpConfig.FacebookBuilder().build(),
////                new AuthUI.IdpConfig.TwitterBuilder().build()
//        );

        // Create and launch sign-in intent
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN);


        fcs();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == RESULT_OK) {
//                // Successfully signed in
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                fcs();
//
//                // ...
//            } else {
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                // ...
//            }
//        }
    }

    private void fcs() {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://chimchesau-studio.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference testRef = storageRef.child("test");

        StorageReference pushRef = testRef.child("push.txt");

        Log.d(Debug.TAG + TAG, pushRef.getPath());
        Log.d(Debug.TAG + TAG, pushRef.getName());
        Log.d(Debug.TAG + TAG, pushRef.getBucket());

        final long ONE_MEGABYTE = 1024 * 1024;

        pushRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(Debug.TAG + TAG, "onSuccess getBytes");
                        // Data for "images/island.jpg" is returns, use this as needed
                        String text = new String(bytes);

                        ((TextView)findViewById(R.id.text_view)).setText(text);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(Debug.TAG + TAG, "onFailure getBytes");
                        // Handle any errors
                    }
                });

        pushRef.putBytes("qwerty".getBytes(Charset.forName("UTF-8"))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d(Debug.TAG + TAG, "onSuccess putBytes");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(Debug.TAG + TAG, "onFailure putBytes");
            }
        });

        pushRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(Debug.TAG + TAG, "onSuccess delete");
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(Debug.TAG + TAG, "onFailure delete");
                // Uh-oh, an error occurred!
            }
        });
    }
}
