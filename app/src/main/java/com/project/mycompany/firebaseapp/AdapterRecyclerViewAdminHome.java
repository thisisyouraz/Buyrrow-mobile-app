package com.project.mycompany.firebaseapp;

import android.app.ProgressDialog;
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
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
public class AdapterRecyclerViewAdminHome extends RecyclerView.Adapter<AdViewHolderAdminHome> {

    private Context mContextAdmin;
    private List<AdCredentials> list;

    public AdapterRecyclerViewAdminHome (Context mContextAdmin, List<AdCredentials> myAdPostCredentials) {
        this.mContextAdmin = mContextAdmin;
        list = myAdPostCredentials;
}
    @NonNull
    @Override
    public AdViewHolderAdminHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_home_row_item,parent,false);
        return new AdViewHolderAdminHome(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdViewHolderAdminHome holder, final int position) {

        final AdCredentials credentials = list.get(position);

        Log.d("ad id", ": "+credentials.getDownloadAbleLink());

        setImagesInSlider(credentials,holder);

//        Glide.with(mContextAdmin).load(credentials.getDownloadAbleLink()).into(holder.imageViewAdmin);
        holder.mPriceAdmin.setText(credentials.getPrice());
        holder.mTitleAdmin.setText(credentials.getTitle());
        //for clicking on the ad in home page to show detail
        holder.viewAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(credentials.getAdType().toString().equals("Sell")|| credentials.getAdType().toString().equals("Rent")){
                    Intent intent=new Intent(mContextAdmin,CellRentDetail.class);
                    intent.putExtra("adInfo",credentials);
                    intent.putExtra("call from","adminHome");
                    mContextAdmin.startActivity(intent);

                    ((AdminHome)mContextAdmin).finish();

                }
                else{
                    Intent intent1=new Intent(mContextAdmin,BidAd_detail_admin.class);
                    intent1.putExtra("adInfo",credentials);
                    intent1.putExtra("call from","adminHome");
                    mContextAdmin.startActivity(intent1);
                    ((AdminHome)mContextAdmin).finish();

                }
            }
//            @Override
//            public void onClick(View v) {
//                if (myAdPostCredentials.get(position).getAdCategory().equals("Sell")) {
//                    Intent intent = new Intent(mContextAdmin, AdminSellingDetail.class);
//                    intent.putExtra("Image", myAdPostCredentials.get(holder.getAdapterPosition()).getAdImage());
//                    intent.putExtra("Category", myAdPostCredentials.get(holder.getAdapterPosition()).getAdCategory());
//                    intent.putExtra("Posted By", myAdPostCredentials.get(holder.getAdapterPosition()).getAdPostedBy());
//                    intent.putExtra("Contact", myAdPostCredentials.get(holder.getAdapterPosition()).getAdContact());
//                    intent.putExtra("KeyValue", myAdPostCredentials.get(holder.getAdapterPosition()).getKey());
//                    mContextAdmin.startActivity(intent);
//                }
//                if (myAdPostCredentials.get(position).getAdCategory().equals("Rent")) {
//                    Intent intent = new Intent(mContextAdmin, AdminRentingDetail.class);
//                    intent.putExtra("Image", myAdPostCredentials.get(holder.getAdapterPosition()).getAdImage());
//                    intent.putExtra("Category", myAdPostCredentials.get(holder.getAdapterPosition()).getAdCategory());
//                    intent.putExtra("Posted By", myAdPostCredentials.get(holder.getAdapterPosition()).getAdPostedBy());
//                    intent.putExtra("Contact", myAdPostCredentials.get(holder.getAdapterPosition()).getAdContact());
//                    intent.putExtra("KeyValue", myAdPostCredentials.get(holder.getAdapterPosition()).getKey());
//                    mContextAdmin.startActivity(intent);
//                }
//            }
        });
    }
    @Override
    public int getItemCount() {
        Log.d("item count", " "+list.size());
        return list.size();
    }

    private void setImagesInSlider(AdCredentials credentials,AdViewHolderAdminHome holder ) {
        ProgressDialog progressDialog = new ProgressDialog(mContextAdmin);
        progressDialog.setMessage("Loading ads...");
        progressDialog.show();

        List<SlideModel> slideModels = new ArrayList<>();


        for (int i = 0; i < credentials.getImageLinks().size(); i++) {
            slideModels.add(new SlideModel(credentials.getImageLinks().get(i)));

            Log.d("link", "setImagesInSlider: "+credentials.getImageLinks().get(i));
        }
        Log.d("link", "setImagesInSlider: before images set");



        holder.slider.setImageList(slideModels,true);

        progressDialog.dismiss();
    }


}

class AdViewHolderAdminHome extends RecyclerView.ViewHolder{

//    ImageView imageViewAdmin;
    TextView mPriceAdmin, mTitleAdmin;
    Button viewAd;
    ImageSlider slider;
    CardView adminHOmeCardView;

    public AdViewHolderAdminHome( View itemView) {
        super(itemView);

        slider=itemView.findViewById(R.id.image_slider_admin_home);
        adminHOmeCardView=itemView.findViewById(R.id.adminHome_cardView);
//        imageViewAdmin = itemView.findViewById(R.id.ivImageAdmin);
        mPriceAdmin = itemView.findViewById(R.id.tvPriceAdmin);
        mTitleAdmin = itemView.findViewById(R.id.tvTitleAdminHome);
        viewAd = itemView.findViewById(R.id.btnView_Ad_Admin);
    }
}