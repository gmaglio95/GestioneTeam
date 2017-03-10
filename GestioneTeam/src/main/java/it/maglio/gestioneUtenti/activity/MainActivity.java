package it.maglio.gestioneUtenti.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.fragment.CambioPasswordFragment;
import it.maglio.gestioneUtenti.fragment.ListViewFragment;
import it.maglio.gestioneUtenti.fragment.SignUpFragment;
import it.maglio.gestioneUtenti.bean.SessionBean;
import it.maglio.gestioneUtenti.fragment.UserProfileFragment;
import it.maglio.gestioneUtenti.mqtt.MqttClientUtils;
import it.maglio.gestioneUtenti.service.MqttListnerService;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.tortuga.beans.User;

import static android.R.attr.bitmap;


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
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);
//        intent.putExtra("outputX", 256);
//        intent.putExtra("outputY", 256);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, 1);
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
        startServiceNotifiche();
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
                        insertImage();
                        return false;
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
        if (requestCode == 1) {
            InputStream stream = null;
            try {
                stream = getContentResolver().openInputStream(
                        data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
            } catch (FileNotFoundException e) {
                Log.e("ERRROR", Log.getStackTraceString(e));
            } catch (IOException e) {
                Log.e("ERRROR", Log.getStackTraceString(e));
            }

        }


    }
}