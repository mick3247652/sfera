package ru.club.sfera.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.club.sfera.R;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.model.GalleryItem;


public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.MyViewHolder> {

    private List<GalleryItem> images;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, GalleryItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public ImageView playImg;
        public ProgressBar mProgressBar;
        public MaterialRippleLayout mParent;

        public MyViewHolder(View view) {

            super(view);

            mParent = (MaterialRippleLayout) view.findViewById(R.id.parent);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            playImg = (ImageView) view.findViewById(R.id.playImg);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }


    public GalleryListAdapter(Context context, List<GalleryItem> images) {

        mContext = context;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final GalleryItem item = images.get(position);

        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.mProgressBar.setVisibility(View.VISIBLE);
        holder.playImg.setVisibility(View.GONE);

        final ImageView playImg = holder.playImg;
        final ProgressBar progressBar = holder.mProgressBar;
        final ImageView imageView = holder.thumbnail;

        if (item.getItemType() == Constants.GALLERY_ITEM_TYPE_VIDEO) {

            Picasso.with(mContext)
                    .load(item.getPreviewVideoImgUrl())
                    .into(holder.thumbnail, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressBar.setVisibility(View.GONE);
                            playImg.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                            progressBar.setVisibility(View.GONE);
                            playImg.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.img_loading_error);
                        }
                    });

        } else {

            Picasso.with(mContext)
                    .load(item.getImgUrl())
                    .into(holder.thumbnail, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                            progressBar.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.img_loading_error);
                        }
                    });
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

        return images.size();
    }
}