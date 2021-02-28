package com.saglikuzmanimm.saglikuzmanim.ui.UserMainUI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser.ProfileEditActivityForUser;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragmentForUser extends Fragment {

    private ProgressBar progressBar;
    private ImageView imageVievUserProfile;
    private TextView userFirstName;
    private TextView userLastName;
    private TextView userEmail;
    private TextView message_profile;
    private Context context;

    private Button btn_edit_profile;

    private String firstName;
    private String lastName;
    private String email;
    private Uri uri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);


        progressBar = viewGroup.findViewById(R.id.progressBar__sign_give_service);
        imageVievUserProfile = viewGroup.findViewById(R.id.image_expert_profile_for_add);
        userFirstName = viewGroup.findViewById(R.id.text_firstName_user);
        userLastName = viewGroup.findViewById(R.id.text_lastName_user);
        userEmail = viewGroup.findViewById(R.id.text_Email_user);
        message_profile = viewGroup.findViewById(R.id.message_profile);
        btn_edit_profile = viewGroup.findViewById(R.id.btnEditProfile);

        getUserProfileData();

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToEditProfile = new Intent(getContext(), ProfileEditActivityForUser.class);
                try {
                    intentToEditProfile.putExtra("firstName", firstName);
                    intentToEditProfile.putExtra("lastName", lastName);
                    intentToEditProfile.putExtra("email", email);
                    if (uri != null) {
                        intentToEditProfile.putExtra("uri", uri.toString());
                    }

                    startActivity(intentToEditProfile);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }
        });

        return viewGroup;
    }

    public void getUserProfileData() {

        UserManager userManager = new UserManager(new FireBaseUserDal());
        userManager.getData(null, new IGetUserListener() {
            @Override
            public void onSuccess(ArrayList<User> userArrayList) {
                User user = (User) userArrayList.get(0);
                userFirstName.setText(user.get_firstName());
                userLastName.setText(user.get_lastName());
                userEmail.setText(user.get_email());
                if (user.get_profileImage() != null) {
                    Picasso.get().load(user.get_profileImage()).into(imageVievUserProfile);
                    uri = user.get_profileImage();

                }

                firstName = user.get_firstName();
                lastName = user.get_lastName();
                email = user.get_email();


                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailed(Exception exception) {

            }
        });

    }

}
