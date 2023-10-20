package com.project.mycompany.firebaseapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AdapterRecyclerViewHome extends RecyclerView.Adapter<MyViewHolder> {
    @NonNull
    private Context myContext;
    private List<AdCredentials> myList;


    public AdapterRecyclerViewHome(@NonNull Context myContext, List<AdCredentials> myList) {
        this.myContext = myContext;
        this.myList = myList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_item_home,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final AdCredentials credentials=myList.get(position);

        setImagesInSlider(credentials,holder);


//        Glide.with(myContext).load(credentials.getDownloadAbleLink()).into(holder.imageView);
        holder.tvTitle.setText(credentials.getTitle());

//        holder.tvCategory.setText(credentials.getCategory());

        if(credentials.getAdType().equals("Bid") && credentials.getMax_offer().equals("dummy")){
            String startingBid="Starting price: Rs"+credentials.getPrice();
            holder.tvprice.setText(startingBid);
        }
        else if(credentials.getAdType().equals("Bid") && !credentials.getMax_offer().equals("dummy")){
            String lastOffer="Last Offer: "+credentials.getMax_offer();
            holder.tvprice.setText(lastOffer);
        }
        else{
            String price="Rs: "+credentials.getPrice();
            holder.tvprice.setText(price);
        }

//        holder.tvDesc.setText(credentials.getAdDesSpec());
        holder.tvType.setText(credentials.getAdType());

        //-----------------------------------------------------

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(credentials.getAdType().toString().equals("Sell")|| credentials.getAdType().toString().equals("Rent")){
                    Intent intent=new Intent(myContext,CellRentDetail.class);
                    intent.putExtra("adInfo",credentials);
                    intent.putExtra("call from","home");
                    myContext.startActivity(intent);
                }
                else{
                    Intent intent=new Intent(myContext,BidDetail.class);
                    intent.putExtra("adInfo",credentials);
                    intent.putExtra("call from","home");

                    myContext.startActivity(intent);

                    ((Home)myContext).finish();

                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return myList.size();
    }


    public void filteredList(ArrayList<AdCredentials> filterList) {
        myList = filterList;
        notifyDataSetChanged();
    }



    private void setImagesInSlider(AdCredentials credentials,MyViewHolder holder ) {
        ProgressDialog progressDialog = new ProgressDialog(myContext);
        progressDialog.setMessage("Loading ads...");
        progressDialog.show();

        List<SlideModel> slideModels = new ArrayList<>();


        for (int i = 0; i < credentials.getImageLinks().size(); i++) {
            slideModels.add(new SlideModel(credentials.getImageLinks().get(i), ScaleTypes.FIT));

            Log.d("link", "setImagesInSlider: "+credentials.getImageLinks().get(i));
        }
        Log.d("link", "setImagesInSlider: before images set");



        holder.slider.setImageList(slideModels,ScaleTypes.FIT);

        progressDialog.dismiss();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder{

//    ImageView imageView;
    TextView tvTitle,tvprice,tvType,tvDesc,tvCategory;

    ImageSlider slider;
    CardView cardView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        slider=itemView.findViewById(R.id.image_slider_home);
        cardView=itemView.findViewById(R.id.home_cardview);
//        imageView=itemView.findViewById(R.id.id_recyclerHome_imageView);
        tvTitle=itemView.findViewById(R.id.id_recyclerHome_title);
        tvType=itemView.findViewById(R.id.id_recyclerHome_adType);
        tvprice=itemView.findViewById(R.id.id_recyclerHome_price);

    }
}
