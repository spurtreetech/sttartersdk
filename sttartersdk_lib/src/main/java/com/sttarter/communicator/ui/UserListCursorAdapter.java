package com.sttarter.communicator.ui;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sttarter.R;
import com.sttarter.common.models.User;
import com.sttarter.helper.interfaces.AddRemoveUserInterface;
import com.sttarter.helper.uitools.CircularNetworkImageView;
import com.sttarter.init.STTarterManager;
import com.sttarter.provider.topics.TopicsColumns;
import com.sttarter.provider.users.UsersColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahbaz on 28/11/16.
 */
public class UserListCursorAdapter extends CursorRecyclerAdapter<UserListCursorAdapter.UserViewHolder> {

    Activity activity;
    List<User> users;
    private final int NORMAL = 1;
    AddRemoveUserInterface addRemoveUserInterface;

    public UserListCursorAdapter(Activity activity, AddRemoveUserInterface addRemoveUserInterface, Cursor cursor) {
        super(cursor);
        this.activity = activity;
        this.addRemoveUserInterface = addRemoveUserInterface;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_adduser_list, viewGroup, false);
        return new UserListCursorAdapter.UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder viewHolder, final Cursor cursor) {

        STTarterManager.getInstance().setContext(activity);

        if (URLUtil.isValidUrl(cursor.getString(cursor.getColumnIndex(UsersColumns.USERS_AVATAR)))) {
            try {
                viewHolder.userIconImageView.setImageUrl(cursor.getString(cursor.getColumnIndex(UsersColumns.USERS_AVATAR)), STTarterManager.getInstance().getImageLoader());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        viewHolder.userNameTextView.setText(cursor.getString(cursor.getColumnIndex(UsersColumns.USERS_USERNAME)));

        viewHolder.rootviewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(viewHolder.getLayoutPosition());
                    /*User user = new User();
                    user.setAvatar(cursor.getString(cursor.getColumnIndex(UsersColumns.USERS_AVATAR)));
                    user.setUsername(cursor.getString(cursor.getColumnIndex(UsersColumns.USERS_USERNAME)));*/
                addRemoveUserInterface.addOrRemove(cursor.getString(cursor.getColumnIndex(UsersColumns.USERS_USERNAME)),true);
            }
        });

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public CircularNetworkImageView userIconImageView;
        public TextView userNameTextView;
        LinearLayout rootviewLL;

        public UserViewHolder(View itemView) {
            super(itemView);
            userIconImageView = (CircularNetworkImageView) itemView.findViewById(R.id.userImage);
            userNameTextView = (TextView) itemView.findViewById(R.id.itemNameTextView);
            rootviewLL = (LinearLayout) itemView.findViewById(R.id.rootviewLL);
        }
    }

}