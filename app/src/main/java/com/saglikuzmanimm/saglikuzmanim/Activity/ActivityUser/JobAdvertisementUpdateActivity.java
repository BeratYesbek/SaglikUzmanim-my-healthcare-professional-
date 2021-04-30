package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.JobAdvertisementManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.JobAdvertisementDal;
import com.saglikuzmanimm.saglikuzmanim.Fragment.SingleChoiceFragment.SingleChoiceExpertFragment;
import com.saglikuzmanimm.saglikuzmanim.Fragment.SingleChoiceFragment.SingleChoiceLocationFragment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.SingleChoiceLister;
import com.saglikuzmanimm.saglikuzmanim.R;

public class JobAdvertisementUpdateActivity extends AppCompatActivity implements SingleChoiceLister {

   private  ProgressBar progressBar_for_adUpdate;
   private  Context context;

    private EditText _editTextTitle;
    private EditText _editTextExplanation;
    private TextView _textLocation;
    private TextView _textDepartment;

    private String title;
    private String explanation;
    private String location;
    private String department;
    private String documentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_advertisement_update);

        progressBar_for_adUpdate = findViewById(R.id.progressBar_update_ad);
        _editTextTitle = findViewById(R.id.editText_update_ad_title_fragment);
        _editTextExplanation = findViewById(R.id.editText_ad_update_explanation_fragment);
        _textLocation = findViewById(R.id.textView_update_ad_chose_location);
        _textDepartment = findViewById(R.id.textView_update_ad_chose_expert);

        progressBar_for_adUpdate.setVisibility(View.INVISIBLE);
        context = getApplicationContext();

        Intent intentGetAdapterAd = getIntent();
        title = intentGetAdapterAd.getStringExtra("title");
        explanation = intentGetAdapterAd.getStringExtra("explanation");
        location = intentGetAdapterAd.getStringExtra("location");
        department = intentGetAdapterAd.getStringExtra("department");
        documentID = intentGetAdapterAd.getStringExtra("documentID");

        _editTextTitle.setText(title);
        _editTextExplanation.setText(explanation);
        _textDepartment.setText("Şuan ki uzman seçiminiz:" + department);
        _textLocation.setText("Şuan ki bölge seçiminiz:" + location);

    }

    public void btn_update_ad(View view) {
        title = _editTextTitle.getText().toString();
        explanation = _editTextExplanation.getText().toString();
        if(title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "İlan başlığı boş bırakılamaz!", Toast.LENGTH_LONG).show();
        }

        else if(explanation.isEmpty()){
            Toast.makeText(getApplicationContext(), "İlan açıklaması boş bırakılamaz!", Toast.LENGTH_LONG).show();
        }

        else if(department == null){
            Toast.makeText(getApplicationContext(), "Uzman seçimi boş bırakılamaz!", Toast.LENGTH_LONG).show();
        }
        else if (location == null) {
            Toast.makeText(getApplicationContext(), "Konum seçimi boş bırakılamaz!", Toast.LENGTH_LONG).show();

        }
        else {

            progressBar_for_adUpdate.setVisibility(View.VISIBLE);
            updateJobAdvertisement(title,explanation);
        }

    }
    void updateJobAdvertisement(String title,String explanation ){

        JobAdvertisementManager jobAdvertisementManager = new JobAdvertisementManager(new JobAdvertisementDal());

        JobAdvertisement jobAdvertisement = new JobAdvertisement(title, explanation, department, location, null, null, documentID, null);

        jobAdvertisementManager.updateData(jobAdvertisement, new IResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(context,"İlan başarı ile güncellendi",Toast.LENGTH_LONG).show();
                progressBar_for_adUpdate.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailed(Exception exception) {
                Toast.makeText(context,"İlan güncellenirken bir hata meydana geldi.",Toast.LENGTH_LONG).show();
                progressBar_for_adUpdate.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void btn_update_location(View view){
        DialogFragment dialogFragment = new SingleChoiceLocationFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(),"single choice Locaiton");

    }

    public void btn_update_department(View view){
        DialogFragment dialogFragment = new SingleChoiceExpertFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(),"single choice expert");


    }

    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        if(whichList.equals("Location")){
            location = list[position];
            _textLocation.setText("yeni bölge seçiminiz: " +location);
        }else{
            department = list[position];
            _textDepartment.setText("yeni uzman seçimi: " + department);
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}