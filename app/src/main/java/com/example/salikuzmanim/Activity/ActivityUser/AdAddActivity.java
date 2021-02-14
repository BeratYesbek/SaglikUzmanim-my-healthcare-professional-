package com.example.salikuzmanim.Activity.ActivityUser;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.salikuzmanim.DataBaseManager.FireBaseJobAdvertisementDal;
import com.example.salikuzmanim.Fragment.SingleChoiceExpertFragment;
import com.example.salikuzmanim.Fragment.SingleChoiceLocationFragment;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetJobAdvertisementDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.example.salikuzmanim.Interfaces.SingleChoiceLister;
import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.R;

import java.util.UUID;

public class AdAddActivity extends AppCompatActivity implements SingleChoiceLister {


    private EditText editText_title;
    private EditText editText_explanation;

    private ProgressBar progressBar_ad;

    private TextView display_chosen_department_jobAdvertisement;
    private TextView display_chosen_location_jobAdvertisement;

    private String departmant = null;
    private String location = null;

    private String jobAdvertisementID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_add);


        editText_title = findViewById(R.id.editText_ad_add_title_fragment);
        editText_explanation = findViewById(R.id.editText_ad_add_explaination_fragment);


        display_chosen_department_jobAdvertisement = findViewById(R.id.textView_dipslay_choses1);
        display_chosen_location_jobAdvertisement = findViewById(R.id.textView_dipslay_choses);

        progressBar_ad = findViewById(R.id.progressBar_add_ad);
        progressBar_ad.setVisibility(View.INVISIBLE);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btn_insert_ad(View view) {
        String title = editText_title.getText().toString();
        String explaination = editText_explanation.getText().toString();


        if (title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "İlan başlığı boş bırakılamaz!", Toast.LENGTH_LONG).show();
        } else if (explaination.isEmpty()) {
            Toast.makeText(getApplicationContext(), "İlan açıklaması boş bırakılamaz!", Toast.LENGTH_LONG).show();
        } else if (departmant == null) {
            Toast.makeText(getApplicationContext(), "Uzman seçimi boş bırakılamaz!", Toast.LENGTH_LONG).show();
        } else if (location == null) {
            Toast.makeText(getApplicationContext(), "Konum seçimi boş bırakılamaz!", Toast.LENGTH_LONG).show();

        } else {
            progressBar_ad.setVisibility(View.VISIBLE);
            UUID uuid = UUID.randomUUID();
            jobAdvertisementID = uuid.toString();
            if (jobAdvertisementID != null) {

                FireBaseJobAdvertisementDal fireBaseJobAdvertisementDal = new FireBaseJobAdvertisementDal();
                fireBaseJobAdvertisementDal.addJobAdvertisement(new JobAdvertisement(title, explaination, departmant, location, jobAdvertisementID, null, null, null), new IGetJobAdvertisementDataListener() {
                    @Override
                    public void onSuccess(IEntity entity) {
                        progressBar_ad.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "İlan ekleme işlemi başarı ile tamamlandı.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "Bir hata oluştu", Toast.LENGTH_LONG).show();
                progressBar_ad.setVisibility(View.INVISIBLE);
            }

        }
    }

    public void btnChoiceLocation(View view) {
        DialogFragment singleDialogFragment = new SingleChoiceLocationFragment();
        singleDialogFragment.setCancelable(false);
        singleDialogFragment.show(getSupportFragmentManager(), "single choice Locaiton");
    }

    public void btnChoiceExpert(View view) {
        DialogFragment singleDialogFragment = new SingleChoiceExpertFragment();
        singleDialogFragment.setCancelable(false);
        singleDialogFragment.show(getSupportFragmentManager(), "single choice expert");
    }


    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        if (whichList.equals("Departmant")) {
            departmant = list[position];
            display_chosen_location_jobAdvertisement.setText("Uzman seçiminiz: " + departmant.toUpperCase());
        } else if (whichList.equals("Location")) {
            location = list[position];
            display_chosen_department_jobAdvertisement.setText("Konum seçiminiz: " + location.toUpperCase());

        }

    }

    @Override
    public void onNegativeButtonClicked() {

    }


}