package com.project.mycompany.firebaseapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class My_ad_view_Adapter extends RecyclerView.Adapter<MyAdViewHolder> {

    private Context mContext;
    private List<AdCredentials> list;

    public My_ad_view_Adapter(Context mContext, List<AdCredentials> myAdPostCredentials) {
        this.mContext = mContext;
        list = myAdPostCredentials;
    }

    @NonNull
    @Override
    public MyAdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_single_row_item, parent, false);
        return new MyAdViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdViewHolder holder, int position) {

        final AdCredentials credentials = list.get(position);

        Glide.with(mContext).load(credentials.getImageLinks().get(0)).into(holder.myAdImageView);
        holder.myAdsTitleTv.setText(credentials.getTitle());


        if(credentials.getAdType().equals("Bid") && credentials.getMax_offer().equals("dummy")){
            String startingBid="Starting price: Rs"+credentials.getPrice();
            holder.myAdsPriceTv.setText(startingBid);
        }
        else if(credentials.getAdType().equals("Bid") && !credentials.getMax_offer().equals("dummy")){
            String lastOffer="Last Offer: "+credentials.getMax_offer();
            holder.myAdsPriceTv.setText(lastOffer);
        }
        else{
            String price="Rs: "+credentials.getPrice();
            holder.myAdsPriceTv.setText(price);
        }

        holder.myAdsType.setText(credentials.getAdType());

        holder.mCardVieew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(credentials.getAdType().toString().equals("Sell")|| credentials.getAdType().toString().equals("Rent")){
                    Intent intent=new Intent(mContext,CellRentDetail.class);
                    intent.putExtra("adInfo",credentials);
                    intent.putExtra("call from","myAds");
                    mContext.startActivity(intent);
                    ((My_ads)mContext).finish();
                }
                    else{
                        Intent intent1=new Intent(mContext,BidAd_detail_admin.class);
                        intent1.putExtra("adInfo",credentials);
                        intent1.putExtra("call from","myAds");
                        mContext.startActivity(intent1);

                    ((My_ads)mContext).finish();
                    }
                }
        });
    }

    @Override

    public int getItemCount() {
        Log.d("item count", " " + list.size());
        return list.size();
    }
}

class MyAdViewHolder extends RecyclerView.ViewHolder {

    ImageView myAdImageView;
    TextView myAdsPriceTv, myAdsTitleTv, myAdsType;

    CardView mCardVieew;
    public MyAdViewHolder(View itemView) {
        super(itemView);
        mCardVieew=itemView.findViewById(R.id.myAdCardView);
        myAdsType = itemView.findViewById(R.id.id_myAdType);
        myAdImageView = itemView.findViewById(R.id.id_myAdsImageView);
        myAdsPriceTv = itemView.findViewById(R.id.id_myAdPrice);
        myAdsTitleTv = itemView.findViewById(R.id.id_myAdsTitle);
    }
}