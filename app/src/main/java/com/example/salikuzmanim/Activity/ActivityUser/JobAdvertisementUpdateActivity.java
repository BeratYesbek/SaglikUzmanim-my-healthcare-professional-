package com.example.salikuzmanim.Activity.ActivityUser;

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

import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.DataBaseManager.FireBaseJobAdvertisementDal;
import com.example.salikuzmanim.Fragment.SingleChoiceExpertFragment;
import com.example.salikuzmanim.Fragment.SingleChoiceLocationFragment;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetJobAdvertisementDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.example.salikuzmanim.Interfaces.SingleChoiceLister;
import com.example.salikuzmanim.R;

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
    private String departmant;
    private String ad_url;
    private String documentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_update);

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
        departmant = intentGetAdapterAd.getStringExtra("department");
        documentID = intentGetAdapterAd.getStringExtra("documentID");

        _editTextTitle.setText(title);
        _editTextExplanation.setText(explanation);
        _textDepartment.setText("Şuan ki uzman seçiminiz:" + departmant);
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

        else if(departmant == null){
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
        FireBaseJobAdvertisementDal fireBaseJobAdvertisementDal = new FireBaseJobAdvertisementDal();
        fireBaseJobAdvertisementDal.updateJobAdvertisement(new JobAdvertisement(title, explanation, departmant, location, null, null, documentID, null), new IGetJobAdvertisementDataListener() {
            @Override
            public void onSuccess(IEntity entity) {
                Toast.makeText(context,"İlan başarı ile güncellendi",Toast.LENGTH_LONG).show();
                progressBar_for_adUpdate.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailed(Exception e) {
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
            departmant = list[position];
            _textDepartment.setText("yeni uzman seçimi: " + departmant);
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}