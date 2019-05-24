package ru.club.sfera.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.balysv.materialripple.MaterialRippleLayout;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.ankushsachdeva.emojicon.EmojiconTextView;
import ru.club.sfera.PhotoViewActivity;
import ru.club.sfera.ProfileActivity;
import ru.club.sfera.R;
import ru.club.sfera.app.App;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.model.Comment;
import ru.club.sfera.util.CustomRequest;
import ru.club.sfera.view.ResizableImageView;


public class AdvancedCommentListAdapter extends RecyclerView.Adapter<AdvancedCommentListAdapter.ViewHolder> implements Constants {

    private List<Comment> items = new ArrayList<>();

    private Context context;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

    private OnItemMenuButtonClickListener onItemMenuButtonClickListener;

    public interface OnItemMenuButtonClickListener {

        void onItemClick(View view, Comment obj, int actionId, int position);
    }

    public void setOnMoreButtonClickListener(final OnItemMenuButtonClickListener onItemMenuButtonClickListener) {

        this.onItemMenuButtonClickListener = onItemMenuButtonClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView mItemAuthorPhoto, mItemAuthorIcon;
        public TextView mItemAuthor, mItemAuthorUsername;
        public ImageView mItemAuthorOnlineIcon, mItemPlayVideo;
        public ImageView mItemMenuButton;
        public ResizableImageView mItemImg;
        public ImageView mItemLikeImg, mItemCommentImg, mItemShareImg;
        public TextView mItemLikesCount, mItemCommentsCount;
        public EmojiconTextView mItemDescription;
        public TextView mItemTimeAgo;
        public ProgressBar mProgressBar;
        public MaterialRippleLayout mItemLikeButton, mItemCommentButton, mItemShareButton;

        public LinearLayout mLinkContainer;
        public ImageView mLinkImage;
        public TextView mLinkTitle;
        public TextView mLinkDescription;

        //public CardView mAdCard;
        //public NativeExpressAdView mAdView;

        public ViewHolder(View v, int itemType) {

            super(v);

            if (itemType == 0) {

                mItemAuthorPhoto = (CircularImageView) v.findViewById(R.id.itemAuthorPhoto);
                mItemAuthorIcon = (CircularImageView) v.findViewById(R.id.itemAuthorIcon);

                mItemAuthor = (TextView) v.findViewById(R.id.itemAuthor);
                mItemAuthorOnlineIcon = (ImageView) v.findViewById(R.id.itemAuthorOnlineIcon);
                mItemAuthorUsername = (TextView) v.findViewById(R.id.itemAuthorUsername);

                mItemImg = (ResizableImageView) v.findViewById(R.id.itemImg);
                mItemDescription = (EmojiconTextView) v.findViewById(R.id.itemDescription);

                mItemPlayVideo = (ImageView) v.findViewById(R.id.itemPlayVideo);

                mItemMenuButton = (ImageView) v.findViewById(R.id.itemMenuButton);
                mItemLikeImg = (ImageView) v.findViewById(R.id.itemLikeImg);
                mItemCommentImg = (ImageView) v.findViewById(R.id.itemCommentImg);
                mItemShareImg = (ImageView) v.findViewById(R.id.itemShareImg);
                mItemTimeAgo = (TextView) v.findViewById(R.id.itemTimeAgo);

                mItemLikesCount = (TextView) v.findViewById(R.id.itemLikesCount);
                mItemCommentsCount = (TextView) v.findViewById(R.id.itemCommentsCount);

                mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);

                mItemLikeButton = (MaterialRippleLayout) v.findViewById(R.id.itemLikeButton);
                mItemCommentButton = (MaterialRippleLayout) v.findViewById(R.id.itemCommentButton);
                mItemShareButton = (MaterialRippleLayout) v.findViewById(R.id.itemRepostButton);

                mLinkContainer = (LinearLayout) v.findViewById(R.id.linkContainer);
                mLinkTitle = (TextView) v.findViewById(R.id.linkTitle);
                mLinkDescription = (TextView) v.findViewById(R.id.linkDescription);
                mLinkImage = (ImageView) v.findViewById(R.id.linkImage);

            } else if (itemType == 1) {

                //mAdCard = (CardView) v.findViewById(R.id.adCard);

                //mAdView = (NativeExpressAdView) v.findViewById(R.id.adView);
            }
        }

    }

    public AdvancedCommentListAdapter(Context ctx, List<Comment> items) {

        this.context = ctx;
        this.items = items;

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_row, parent, false);
            ViewHolder holder = new ViewHolder(v, viewType);

            return holder;

        } else if (viewType == 1) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false);
            ViewHolder holder = new ViewHolder(v, viewType);

            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Comment p = items.get(position);

        if (p.getAd() == 0) {

            onBindItem(holder, position);

        } else {


            //AdRequest request = new AdRequest.Builder()
//                    .addTestDevice("YOUR_DEVICE_ID")
            //        .build();
            //holder.mAdView.loadAd(request);

            //holder.mAdCard.setVisibility(View.VISIBLE);
        }
    }

    public void onBindItem(ViewHolder holder, final int position) {

        final Comment p = items.get(position);

        holder.mItemCommentImg.setVisibility(View.GONE);
        holder.mItemCommentsCount.setVisibility(View.GONE);
        holder.mItemShareImg.setVisibility(View.GONE);

        holder.mLinkContainer.setVisibility(View.GONE);

        holder.mItemPlayVideo.setVisibility(View.GONE);
        holder.mProgressBar.setVisibility(View.GONE);

        holder.mItemAuthorPhoto.setVisibility(View.VISIBLE);

        holder.mItemAuthorPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profileId", p.getFromUserId());
                context.startActivity(intent);
            }
        });

        if (p.getFromUserPhotoUrl().length() != 0) {

            imageLoader.get(p.getFromUserPhotoUrl(), ImageLoader.getImageListener(holder.mItemAuthorPhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            holder.mItemAuthorPhoto.setVisibility(View.VISIBLE);
            holder.mItemAuthorPhoto.setImageResource(R.drawable.profile_default_photo);
        }

        if (p.getFromUserVerified() == 1) {

            holder.mItemAuthorIcon.setVisibility(View.VISIBLE);

        } else {

            holder.mItemAuthorIcon.setVisibility(View.GONE);
        }

        holder.mItemAuthor.setVisibility(View.VISIBLE);
        holder.mItemAuthor.setText(p.getFromUserFullname());

        holder.mItemAuthor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profileId", p.getFromUserId());
                context.startActivity(intent);
            }
        });

        holder.mItemAuthorUsername.setVisibility(View.VISIBLE);
        holder.mItemAuthorUsername.setText("@" + p.getFromUserUsername());

        if (p.getFromUserOnline() && p.getFromUserAllowShowOnline() == ENABLED) {

            holder.mItemAuthorOnlineIcon.setVisibility(View.VISIBLE);

        } else {

            holder.mItemAuthorOnlineIcon.setVisibility(View.GONE);
        }

        if (p.getImgUrl().length() != 0){

            holder.mItemImg.setVisibility(View.VISIBLE);
            holder.mProgressBar.setVisibility(View.VISIBLE);

            final ProgressBar progressView = holder.mProgressBar;
            final ImageView imageView = holder.mItemImg;

//            String s = "http://maps.googleapis.com/maps/api/staticmap?center=" + App.getInstance().getLat() + "," + App.getInstance().getLng() + "&zoom=13&size=300x300&sensor=false";

            Picasso.with(context)
                    .load(p.getImgUrl())
                    .into(holder.mItemImg, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                            progressView.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.img_loading_error);
                        }
                    });

        } else {

            holder.mItemImg.setVisibility(View.GONE);
            holder.mProgressBar.setVisibility(View.GONE);
            holder.mItemImg.setVisibility(View.GONE);
        }

        holder.mItemImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (p.getImgUrl() != null && p.getImgUrl().length() > 0) {

                    Intent i = new Intent(context, PhotoViewActivity.class);
                    i.putExtra("imgUrl", p.getImgUrl());
                    context.startActivity(i);
                }
            }
        });

        if (p.getText().length() != 0) {

            holder.mItemDescription.setVisibility(View.VISIBLE);
            holder.mItemDescription.setText(p.getText().replaceAll("<br>", "\n"));

        } else {

            holder.mItemDescription.setVisibility(View.GONE);
        }

        holder.mItemTimeAgo.setVisibility(View.VISIBLE);

        String timeAgo;

        timeAgo = p.getTimeAgo();

        if (p.getReplyToUserId() != 0) {

            if (p.getReplyToUserFullname().length() != 0) {

                timeAgo = timeAgo + " " + context.getString(R.string.label_to) + " " + p.getReplyToUserFullname();

            } else {

                timeAgo = timeAgo + " " + context.getString(R.string.label_to) + " @" + p.getReplyToUserUsername();
            }
        }

        holder.mItemTimeAgo.setText(timeAgo);

        holder.mItemMenuButton.setVisibility(View.VISIBLE);
        holder.mItemMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                onItemMenuButtonClick(view, p, position);
            }
        });

        if (p.getLikesCount() > 0) {

            holder.mItemLikesCount.setVisibility(View.VISIBLE);
            holder.mItemLikesCount.setText(Integer.toString(p.getLikesCount()));

        } else {

            holder.mItemLikesCount.setVisibility(View.GONE);
        }

        if (p.isMyLike()) {

            holder.mItemLikeImg.setImageResource(R.drawable.perk_active);

        } else {

            holder.mItemLikeImg.setImageResource(R.drawable.perk);
        }

        holder.mItemLikeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (p.isMyLike()) {

                    p.setMyLike(false);
                    p.setLikesCount(p.getLikesCount() - 1);

                } else {

                    p.setMyLike(true);
                    p.setLikesCount(p.getLikesCount() + 1);
                }

                CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_LIKE_ADD, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    if (!response.getBoolean("error")) {

                                        p.setLikesCount(response.getInt("likesCount"));
                                        p.setMyLike(response.getBoolean("myLike"));
                                    }

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                } finally {

                                    notifyDataSetChanged();

                                    Log.d("Comment.Like", response.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Comment.Like", error.toString());
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("accountId", Long.toString(App.getInstance().getId()));
                        params.put("accessToken", App.getInstance().getAccessToken());
                        params.put("itemId", Long.toString(p.getId()));
                        params.put("itemFromUserId", Long.toString(p.getFromUserId()));
                        params.put("itemType", Integer.toString(ITEM_TYPE_COMMENT));

                        return params;
                    }
                };

                App.getInstance().addToRequestQueue(jsonReq);

                notifyDataSetChanged();
            }
        });
    }

    private void onItemMenuButtonClick(final View view, final Comment comment, final int position){

        PopupMenu popupMenu = new PopupMenu(context, view);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                onItemMenuButtonClickListener.onItemClick(view, comment, item.getItemId(), position);

                return true;
            }
        });

        if (comment.getFromUserId() == App.getInstance().getId() && comment.getItemFromUserId() == App.getInstance().getId()) {

            // only delete option
            popupMenu.inflate(R.menu.menu_comment_popup_1);

        } else if (comment.getFromUserId() == App.getInstance().getId() && comment.getItemFromUserId() != App.getInstance().getId()) {

            // only delete option
            popupMenu.inflate(R.menu.menu_comment_popup_1);

        } else if (comment.getFromUserId() != App.getInstance().getId() && comment.getItemFromUserId() == App.getInstance().getId()) {

            // delete option
            // reply option
            // report option
            popupMenu.inflate(R.menu.menu_comment_popup_2);

        } else {

            // report option
            // reply option
            popupMenu.inflate(R.menu.menu_comment_popup_3);
        }

        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        final Comment p = items.get(position);

        if (p.getAd() == 0) {

            return 0;

        } else {

            return 1;
        }
    }
}