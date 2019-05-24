package ru.club.sfera.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.club.sfera.R;
import ru.club.sfera.model.Guest;


public class GuestsListAdapter extends RecyclerView.Adapter<GuestsListAdapter.MyViewHolder> {

	private Context mContext;
	private List<Guest> itemList;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, Guest obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

	public class MyViewHolder extends RecyclerView.ViewHolder {

		public TextView mProfileFullname, mProfileUsername;
		public ImageView mProfilePhoto, mProfileOnlineIcon, mProfileIcon;
		public MaterialRippleLayout mParent;
		public ProgressBar mProgressBar;

		public MyViewHolder(View view) {

			super(view);

			mParent = (MaterialRippleLayout) view.findViewById(R.id.parent);

			mProfilePhoto = (ImageView) view.findViewById(R.id.profileImg);
			mProfileFullname = (TextView) view.findViewById(R.id.profileFullname);
			mProfileUsername = (TextView) view.findViewById(R.id.profileUsername);
            mProfileOnlineIcon = (ImageView) view.findViewById(R.id.profileOnlineIcon);
            mProfileIcon = (ImageView) view.findViewById(R.id.profileIcon);
			mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		}
	}


	public GuestsListAdapter(Context mContext, List<Guest> itemList) {

		this.mContext = mContext;
		this.itemList = itemList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_thumbnail, parent, false);


		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {

		final Guest item = itemList.get(position);

        holder.mProgressBar.setVisibility(View.VISIBLE);
        holder.mProfilePhoto.setVisibility(View.GONE);

        if (item.getGuestPhotoUrl() != null && item.getGuestPhotoUrl().length() > 0) {

            final ImageView img = holder.mProfilePhoto;
            final ProgressBar progressView = holder.mProgressBar;

            Picasso.with(mContext)
                    .load(item.getGuestPhotoUrl())
                    .into(holder.mProfilePhoto, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressView.setVisibility(View.GONE);
                            img.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                            progressView.setVisibility(View.GONE);
                            img.setVisibility(View.VISIBLE);
                            img.setImageResource(R.drawable.profile_default_photo);
                        }
                    });

        } else {

            holder.mProgressBar.setVisibility(View.GONE);
            holder.mProfilePhoto.setVisibility(View.VISIBLE);

			holder.mProfilePhoto.setImageResource(R.drawable.profile_default_photo);
		}

        holder.mProfileFullname.setText(item.getGuestFullname());
        holder.mProfileUsername.setText(item.getTimeAgo() + " @" + item.getGuestUsername());

		if (item.isOnline() && item.getGuestAllowShowOnline() == 1) {

			holder.mProfileOnlineIcon.setVisibility(View.VISIBLE);

		} else {

            holder.mProfileOnlineIcon.setVisibility(View.GONE);
		}

        if (item.isVerified()) {

            holder.mProfileIcon.setVisibility(View.VISIBLE);

        } else {

            holder.mProfileIcon.setVisibility(View.GONE);
        }

        holder.mParent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mOnItemClickListener != null) {

                    mOnItemClickListener.onItemClick(view, item, position);
                }
            }
        });
	}

	@Override
	public int getItemCount() {

		return itemList.size();
	}
}