package ru.club.sfera.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pkmmte.view.CircularImageView;

import java.util.List;

import ru.club.sfera.ProfileActivity;
import ru.club.sfera.R;
import ru.club.sfera.app.App;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.model.Notify;

public class NotifyListAdapter extends BaseAdapter implements Constants {

	private Activity activity;
	private LayoutInflater inflater;
	private List<Notify> notifyList;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

	public NotifyListAdapter(Activity activity, List<Notify> notifyList) {

		this.activity = activity;
		this.notifyList = notifyList;
	}

	@Override
	public int getCount() {

		return notifyList.size();
	}

	@Override
	public Object getItem(int location) {

		return notifyList.get(location);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	
	static class ViewHolder {

        public TextView notifyTitle;
        public TextView notifyTimeAgo;
		public CircularImageView notifyAuthor;
        public CircularImageView notifyType;
	        
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.notify_list_row, null);
			
			viewHolder = new ViewHolder();

            viewHolder.notifyAuthor = (CircularImageView) convertView.findViewById(R.id.notifyAuthor);
            viewHolder.notifyType = (CircularImageView) convertView.findViewById(R.id.notifyType);
            viewHolder.notifyTitle = (TextView) convertView.findViewById(R.id.notifyTitle);
			viewHolder.notifyTimeAgo = (TextView) convertView.findViewById(R.id.notifyTimeAgo);

            convertView.setTag(viewHolder);

            viewHolder.notifyAuthor.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int getPosition = (Integer) v.getTag();

                    Notify notify = notifyList.get(getPosition);

                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("profileId", notify.getFromUserId());
                    activity.startActivity(intent);
                }
            });

		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
		}

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }

        viewHolder.notifyTitle.setTag(position);
        viewHolder.notifyTimeAgo.setTag(position);
        viewHolder.notifyAuthor.setTag(position);
        viewHolder.notifyType.setTag(position);
        viewHolder.notifyAuthor.setTag(R.id.notifyAuthor, viewHolder);
		
		final Notify notify = notifyList.get(position);

        if (notify.getType() == NOTIFY_TYPE_COMMENT) {

            viewHolder.notifyTitle.setText(notify.getFromUserFullname() + " " + activity.getText(R.string.label_comment_added));
            viewHolder.notifyType.setImageResource(R.drawable.notify_comment);

        } else if (notify.getType() == NOTIFY_TYPE_FRIEND_REQUEST) {

            viewHolder.notifyTitle.setText(notify.getFromUserFullname() + " " + activity.getText(R.string.label_friend_request_added));
            viewHolder.notifyType.setImageResource(R.drawable.ic_notify_friend);

        } else if (notify.getType() == NOTIFY_TYPE_LIKE) {

            viewHolder.notifyTitle.setText(notify.getFromUserFullname() + " " + activity.getText(R.string.label_likes_item));
            viewHolder.notifyType.setImageResource(R.drawable.ic_notify_like);

        } else if (notify.getType() == NOTIFY_TYPE_GIFT) {

            viewHolder.notifyTitle.setText(notify.getFromUserFullname() + " " + activity.getText(R.string.label_gift_added));
            viewHolder.notifyType.setImageResource(R.drawable.ic_notify_gift);

        } else if (notify.getType() == NOTIFY_TYPE_POST) {

            viewHolder.notifyTitle.setText(notify.getFromUserFullname() + " " + activity.getText(R.string.label_new_item_on_wall));
            viewHolder.notifyType.setImageResource(R.drawable.ic_notify_new_item);

        } else if (notify.getType() == NOTIFY_TYPE_REPOST) {

            viewHolder.notifyTitle.setText(notify.getFromUserFullname() + " " + activity.getText(R.string.label_new_repost));
            viewHolder.notifyType.setImageResource(R.drawable.ic_notify_repost);

        } else {

            viewHolder.notifyTitle.setText(notify.getFromUserFullname() + " " + activity.getText(R.string.label_comment_reply_added));
            viewHolder.notifyType.setImageResource(R.drawable.notify_reply);
        }

        if (notify.getFromUserPhotoUrl().length() > 0) {

            imageLoader.get(notify.getFromUserPhotoUrl(), ImageLoader.getImageListener(viewHolder.notifyAuthor, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            viewHolder.notifyAuthor.setImageResource(R.drawable.profile_default_photo);
        }

        viewHolder.notifyTimeAgo.setText(notify.getTimeAgo());

		return convertView;
	}
}