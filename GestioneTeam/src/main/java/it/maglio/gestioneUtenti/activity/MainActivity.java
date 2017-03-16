package it.maglio.gestioneUtenti.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.asyncTask.UploadProfileImage;
import it.maglio.gestioneUtenti.fragment.CambioPasswordFragment;
import it.maglio.gestioneUtenti.fragment.ListViewFragment;
import it.maglio.gestioneUtenti.fragment.SignUpFragment;
import it.maglio.gestioneUtenti.bean.SessionBean;
import it.maglio.gestioneUtenti.fragment.UserProfileFragment;
import it.maglio.gestioneUtenti.service.MqttListnerService;
import it.tortuga.beans.User;


/**
 * Created by pc ads on 20/02/2017.
 */

public class MainActivity extends GeneralActivity {


    //Defining Variables
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private User user;

    public void autoLogin() {

    }

    private void insertImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 8);
    }

    private void openMaps() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=Via Beata Savina Petrilli+Roma,+Roma+Italia");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void startServiceNotifiche() {
        Intent service = new Intent(this, MqttListnerService.class);
        startService(service);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        buildMenuDrawerLayout(navigationView.getMenu());
      //  startServiceNotifiche();
        TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        user = SessionBean.getSessionBean().getUser();
        username.setText(user.getNome() + " " + user.getCognome());
        email.setText(user.get_id());
        setSupportActionBar(toolbar);
        // Initializing Toolbar and setting it as the actionbar
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    case R.id.registra_utente:
                        SignUpFragment signUpFragment = new SignUpFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, signUpFragment);
                        fragmentTransaction.commit();
                        return false;
                    case R.id.cambioPassword:
                        CambioPasswordFragment cambioPasswordFragment = new CambioPasswordFragment();
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, cambioPasswordFragment);
                        transaction.commit();
                        return false;
                    case R.id.listUser:
                        ListViewFragment listFragment = new ListViewFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frame, listFragment);
                        fragmentTransaction1.commit();
                        return false;
                    case R.id.userProfile:
                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame, userProfileFragment);
                        fragmentTransaction2.commit();
                        return false;
                    case R.id.insertImage:
                        openMaps();
                        return false;
                    case R.id.insertGalleryImg:
                        insertImage();
                    default:
                        return true;
                }
            }


        });
    }

    @Override
    public void callBackAfterCall(Object bean) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void buildMenuDrawerLayout(Menu menu) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 8) {
            Uri uri = data.getData();
            ParcelFileDescriptor parcelFileDescriptor =
                    null;
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            new UploadProfileImage(getRealPathFromURI(uri)).execute();
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}