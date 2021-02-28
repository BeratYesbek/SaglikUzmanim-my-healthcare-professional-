package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser;

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

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.JobAdvertisementManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseJobAdvertisementDal;
import com.saglikuzmanimm.saglikuzmanim.Fragment.SingleChoiceFragment.SingleChoiceExpertFragment;
import com.saglikuzmanimm.saglikuzmanim.Fragment.SingleChoiceFragment.SingleChoiceLocationFragment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.SingleChoiceLister;
import com.saglikuzmanimm.saglikuzmanim.R;

import java.util.UUID;

public class JobAdvertisementAddActivity extends AppCompatActivity implements SingleChoiceLister {


    private EditText editText_title;
    private EditText editText_explanation;

    private ProgressBar progressBar_ad;

    private TextView display_chosen_department_jobAdvertisement;
    private TextView display_chosen_location_jobAdvertisement;

    private String department = null;
    private String location = null;

    private String jobAdvertisementID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_advertisement_add);


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
        } else if (department == null) {
            Toast.makeText(getApplicationContext(), "Uzman seçimi boş bırakılamaz!", Toast.LENGTH_LONG).show();
        } else if (location == null) {
            Toast.makeText(getApplicationContext(), "Konum seçimi boş bırakılamaz!", Toast.LENGTH_LONG).show();

        } else {
            progressBar_ad.setVisibility(View.VISIBLE);
            UUID uuid = UUID.randomUUID();
            jobAdvertisementID = uuid.toString();
            if (jobAdvertisementID != null) {
                JobAdvertisementManager jobAdvertisementManager = new JobAdvertisementManager(new FireBaseJobAdvertisementDal());
                jobAdvertisementManager.addData(new JobAdvertisement(title, explaination, department, location, jobAdvertisementID), new IResult() {
                    @Override
                    public void onSuccess() {
                        progressBar_ad.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "İlan ekleme işlemi başarı ile tamamlandı.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(Exception exception) {

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
        singleDialogFragment.show(getSupportFragmentManager(), "single choice Location");
    }

    public void btnChoiceExpert(View view) {
        DialogFragment singleDialogFragment = new SingleChoiceExpertFragment();
        singleDialogFragment.setCancelable(false);
        singleDialogFragment.show(getSupportFragmentManager(), "single choice expert");
    }


    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        if (whichList.equals("Departmant")) {
            department = list[position];
            display_chosen_location_jobAdvertisement.setText("Uzman seçiminiz: " + department.toUpperCase());
        } else if (whichList.equals("Location")) {
            location = list[position];
            display_chosen_department_jobAdvertisement.setText("Konum seçiminiz: " + location.toUpperCase());

        }

    }

    @Override
    public void onNegativeButtonClicked() {

    }


}