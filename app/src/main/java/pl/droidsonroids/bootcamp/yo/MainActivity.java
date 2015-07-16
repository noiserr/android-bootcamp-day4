/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.droidsonroids.bootcamp.yo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.bootcamp.yo.api.ApiService;
import pl.droidsonroids.bootcamp.yo.model.User;
import pl.droidsonroids.bootcamp.yo.service.RegistrationIntentService;
import pl.droidsonroids.bootcamp.yo.ui.UserListAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.users_recycler)
    RecyclerView usersRecycler;
    @Bind(R.id.name_edit_text)
    EditText nameEditText;

    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        String userName = getIntent().getAction();
        if (userName != null){
            userListAdapter = new UserListAdapter(userName);
            Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();

        }else{
            userListAdapter = new UserListAdapter("x");
        }
        usersRecycler.setAdapter(userListAdapter);

        onRefreshButtonClick();
        GoogleApiAvailability.getInstance().showErrorDialogFragment(this, checkGooglePlay(), 0);
    }

    private int checkGooglePlay() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
    }

    @OnClick(R.id.refresh_button)
    public void onRefreshButtonClick() {
        ApiService.API_SERVICE.getUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> users) {
                        userListAdapter.refreshUserList(users);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//        userListAdapter.sortUserList();

    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        if(nameEditText.getText().toString().length() != 0){
            intent.setAction(nameEditText.getText().toString());
            startService(intent);
            onRefreshButtonClick();
        }else {
            Toast.makeText(getApplicationContext(), "Username must be filled!", Toast.LENGTH_SHORT).show();
        }

    }

}
