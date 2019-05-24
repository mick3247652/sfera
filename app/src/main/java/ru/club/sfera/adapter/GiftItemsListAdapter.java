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
import ru.club.sfera.model.GiftItem;


public class GiftItemsListAdapter extends RecyclerView.Adapter<GiftItemsListAdapter.MyViewHolder> {

	private Context mContext;
	private List<GiftItem> itemList;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, GiftItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

	public class MyViewHolder extends RecyclerView.ViewHolder {

		public TextView mCost;
		public ImageView mGiftImg;
		public ProgressBar mProgressBar;
		public MaterialRippleLayout mParent;

		public MyViewHolder(View view) {

			super(view);

			mParent = (MaterialRippleLayout) view.findViewById(R.id.parent);

			mGiftImg = (ImageView) view.findViewById(R.id.giftImg);
			mCost = (TextView) view.findViewById(R.id.giftCost);
			mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		}
	}


	public GiftItemsListAdapter(Context mContext, List<GiftItem> itemList) {

		this.mContext = mContext;
		this.itemList = itemList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item_thumbnail, parent, false);


		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {

		final GiftItem item = itemList.get(position);

        holder.mCost.setVisibility(View.GONE);
        holder.mGiftImg.setVisibility(View.GONE);
        holder.mProgressBar.setVisibility(View.VISIBLE);

		holder.mCost.setText(Integer.toString(item.getCost()) + " " + mContext.getString(R.string.label_credits));

        final ImageView img = holder.mGiftImg;
        final TextView cost = holder.mCost;
        final ProgressBar progressView = holder.mProgressBar;

        Picasso.with(mContext)
                .load(item.getImgUrl())
                .into(holder.mGiftImg, new Callback() {

                    @Override
                    public void onSuccess() {

                        progressView.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                        cost.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                        progressView.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.profile_default_photo);
                        cost.setVisibility(View.VISIBLE);
                    }
                });

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