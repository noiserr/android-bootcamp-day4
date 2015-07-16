package pl.droidsonroids.bootcamp.yo.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.bootcamp.yo.Const;
import pl.droidsonroids.bootcamp.yo.api.ApiService;
import pl.droidsonroids.bootcamp.yo.model.User;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UserListAdapter extends RecyclerView.Adapter<UserItemViewVolder> {

    private List<User> userList = Collections.emptyList();
    Context context;
    private String mUserName;

    public void sortUserList() {
        Collections.sort(userList);
    }

    public UserListAdapter(String mUserName) {
        this.mUserName = mUserName;
    }

    @Override
    public UserItemViewVolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        return new UserItemViewVolder(view);
    }

    @Override
    public void onBindViewHolder(UserItemViewVolder userItemViewVolder, final int i) {

        if (mUserName.equals(userList.get(i).getName())){
            userItemViewVolder.bindDataAndColor(userList.get(i));
        }else {
            userItemViewVolder.bindData(userList.get(i));
        }

        userItemViewVolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Log.d(Const.TAG, sharedPreferences.getInt(Const.USERID, 0) + "");
                final int selfId = sharedPreferences.getInt(Const.USERID, 0);

                if (selfId == 0) {
                    Toast.makeText(context, "registration not complete", Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiService.API_SERVICE.postYo(userList.get(i).getId(), selfId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Toast.makeText(context, "message sent", Toast.LENGTH_SHORT).show();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void refreshUserList(List<User> userList) {
        this.userList = userList;
        sortUserList();
        notifyDataSetChanged();
    }

}
