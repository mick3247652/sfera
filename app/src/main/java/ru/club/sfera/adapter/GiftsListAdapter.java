package ru.club.sfera.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.club.sfera.ProfileActivity;
import ru.club.sfera.R;
import ru.club.sfera.app.App;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.model.Gift;


public class GiftsListAdapter extends RecyclerView.Adapter<GiftsListAdapter.ViewHolder> implements Constants {

    private List<Gift> items = new ArrayList<>();

    private Context context;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

    private OnItemMenuButtonClickListener onItemMenuButtonClickListener;

    public interface OnItemMenuButtonClickListener {

        void onItemClick(View view, Gift obj, int actionId, int position);
    }
    public void setOnMoreButtonClickListener(final OnItemMenuButtonClickListener onItemMenuButtonClickListener) {

        this.onItemMenuButtonClickListener = onItemMenuButtonClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public CircularImageView mGiftAuthorPhoto, mGiftAuthorIcon;
        public TextView mGiftAuthor, mGiftAuthorUsername;
        public ImageView mGiftAuthorOnlineIcon;
        public ImageView mGiftMenuButton;
        public ImageView mGiftImg;
        public TextView mGiftMessage;
        public TextView mGiftTimeAgo;
        public ProgressBar mProgressBar;
        public LinearLayout mAuthorContainer;

        public ViewHolder(View v) {

            super(v);

            mGiftAuthorPhoto = (CircularImageView) v.findViewById(R.id.giftAuthorPhoto);
            mGiftAuthorIcon = (CircularImageView) v.findViewById(R.id.giftAuthorIcon);

            mGiftAuthor = (TextView) v.findViewById(R.id.giftAuthor);
            mGiftAuthorOnlineIcon = (ImageView) v.findViewById(R.id.giftAuthorOnlineIcon);
            mGiftAuthorUsername = (TextView) v.findViewById(R.id.giftAuthorUsername);

            mGiftImg = (ImageView) v.findViewById(R.id.giftImg);
            mGiftMessage = (TextView) v.findViewById(R.id.giftMessage);

            mGiftMenuButton = (ImageView) v.findViewById(R.id.giftMenuButton);
            mGiftTimeAgo = (TextView) v.findViewById(R.id.giftTimeAgo);

            mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);

            mAuthorContainer = (LinearLayout) v.findViewById(R.id.giftAuthorContainer);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GiftsListAdapter(Context ctx, List<Gift> items) {

        this.context = ctx;
        this.items = items;

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }
    }

    @Override
    public GiftsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_list_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Gift p = items.get(position);

        holder.mProgressBar.setVisibility(View.GONE);

        holder.mGiftAuthorPhoto.setVisibility(View.VISIBLE);

        if ((p.getFromUserPhotoUrl().length() != 0 && p.getAnonymous() == DISABLED) || App.getInstance().getId() == p. getToUserId()) {

            imageLoader.get(p.getFromUserPhotoUrl(), ImageLoader.getImageListener(holder.mGiftAuthorPhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            holder.mGiftAuthorPhoto.setVisibility(View.VISIBLE);
            holder.mGiftAuthorPhoto.setImageResource(R.drawable.profile_default_photo);
        }

        if ((p.getFromUserVerified() == 1 && p.getAnonymous() == DISABLED) || App.getInstance().getId() == p. getToUserId()) {

            holder.mGiftAuthorIcon.setVisibility(View.VISIBLE);

        } else {

            holder.mGiftAuthorIcon.setVisibility(View.GONE);
        }

        if (p.getAnonymous() == DISABLED || App.getInstance().getId() == p.getToUserId()) {

            holder.mGiftAuthorUsername.setVisibility(View.VISIBLE);
            holder.mGiftAuthorUsername.setText("@" + p.getFromUserUsername());

            holder.mGiftAuthor.setVisibility(View.VISIBLE);
            holder.mGiftAuthor.setText(p.getFromUserFullname());

            holder.mGiftAuthor.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("profileId", p.getFromUserId());
                    context.startActivity(intent);
                }
            });

            holder.mGiftAuthorPhoto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("profileId", p.getFromUserId());
                    context.startActivity(intent);
                }
            });

        } else {

            holder.mGiftAuthorUsername.setVisibility(View.VISIBLE);
            holder.mGiftAuthorUsername.setText(context.getString(R.string.label_anonymous_gift));

            holder.mGiftAuthor.setVisibility(View.VISIBLE);
            holder.mGiftAuthor.setText(context.getString(R.string.label_anonymous_gift));

            holder.mGiftAuthor.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //
                }
            });

            holder.mGiftAuthorPhoto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //
                }
            });
        }

        if ((p.getFromUserOnline() && p.getFromUserAllowShowOnline() == ENABLED && p.getAnonymous() == DISABLED) || App.getInstance().getId() == p. getToUserId()) {

            holder.mGiftAuthorOnlineIcon.setVisibility(View.VISIBLE);

        } else {

            holder.mGiftAuthorOnlineIcon.setVisibility(View.GONE);
        }

        if (p.getImgUrl().length() != 0){

            holder.mGiftImg.setVisibility(View.VISIBLE);
            holder.mProgressBar.setVisibility(View.VISIBLE);

            final ProgressBar progressView = holder.mProgressBar;

            Picasso.with(context)
                    .load(p.getImgUrl())
                    .into(holder.mGiftImg, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            // TODO Auto-generated method stub

                        }
                    });
        }

        if (p.getMessage().length() != 0) {

            holder.mGiftMessage.setVisibility(View.VISIBLE);
            holder.mGiftMessage.setText(p.getMessage().replaceAll("<br>", "\n"));

        } else {

            holder.mGiftMessage.setVisibility(View.GONE);
        }

        holder.mGiftTimeAgo.setVisibility(View.VISIBLE);
        holder.mGiftTimeAgo.setText(p.getTimeAgo());

        if (App.getInstance().getId() == p.getToUserId()) {

            holder.mGiftMenuButton.setVisibility(View.VISIBLE);

            holder.mGiftMenuButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {

                    onItemMenuButtonClick(view, p, position);
                }
            });

        } else {

            holder.mGiftMenuButton.setVisibility(View.GONE);
        }
    }

    private void onItemMenuButtonClick(final View view, final Gift post, final int position){

        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                onItemMenuButtonClickListener.onItemClick(view, post, item.getItemId(), position);

                return true;
            }
        });

        popupMenu.inflate(R.menu.menu_gift_item_popup);

        popupMenu.show();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}