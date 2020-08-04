package com.project.mycompany.firebaseapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecyclerViewBidDetail extends RecyclerView.Adapter<AdViewHolderBidder> {


    private Context context;
    private List<OffersDetail>myOfferList;

    public AdapterRecyclerViewBidDetail(Context context, List<OffersDetail> myOfferList) {
        this.context = context;
        this.myOfferList = myOfferList;
    }

    @NonNull
    @Override
    public AdViewHolderBidder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_item_bid_detail,parent,false);
        return new AdViewHolderBidder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolderBidder holder, int position) {

        final OffersDetail offer = myOfferList.get(position);

        Log.d("offerList", "onBindViewHolder: "+offer.getContact());
        Log.d("offerList", "onBindViewHolder: "+offer.getAmount().toString());

        holder.mContact.setText(offer.getContact());
        holder.mAmount.setText(offer.getAmount().toString());
    }

    @Override
    public int getItemCount() {
        return myOfferList.size();
    }
}

class AdViewHolderBidder extends RecyclerView.ViewHolder{

    TextView mContact, mAmount;
    CardView mCardviewBidder;

    public AdViewHolderBidder( View itemView) {
        super(itemView);
        mContact= itemView.findViewById(R.id.bidderContact);
        mAmount = itemView.findViewById(R.id.bidderAmount);
        mCardviewBidder = itemView.findViewById(R.id.myAdsBidCardView);

    }
}