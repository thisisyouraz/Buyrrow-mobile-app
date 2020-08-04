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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Admin_approveAd_adapter extends RecyclerView.Adapter<ApproveApVh> {

    private Context mContext;
    private List<AdCredentials> dataList;

    public Admin_approveAd_adapter(Context mContext, List<AdCredentials> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ApproveApVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_admin_approve, parent, false);

        return new ApproveApVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveApVh holder, int position) {

        final AdCredentials credentials = dataList.get(position);

        setImagesInSlider(credentials,holder);

//        Glide.with(mContext).load(credentials.getDownloadAbleLink()).into(holder.imageView);
        holder.tvTitle.setText(credentials.getTitle());
        holder.tvAdType.setText(credentials.getAdType());
        holder.tvprice.setText(credentials.getPrice());
        holder.tvCategory.setText(credentials.getCategory());
        holder.tvDesc.setText(credentials.getAdDesSpec());

        holder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Data of posted ads")
                        .child(credentials.getParentID()).setValue(credentials);

                FirebaseDatabase.getInstance().getReference("Admin approve ads")
                        .child(credentials.getParentID()).removeValue();

                Intent intent = new Intent(mContext, Admin_approve_ad.class);
                intent.putExtra("callFrom", "adminApprove");
                mContext.startActivity(intent);

                ((Admin_approve_ad) mContext).finish();


            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Admin approve ads")
                        .child(credentials.getParentID()).removeValue();

                Intent intent = new Intent(mContext, Admin_approve_ad.class);
                intent.putExtra("callFrom", "adminApprove");
                mContext.startActivity(intent);
                ((Admin_approve_ad) mContext).finish();

            }
        });
        Log.d("parent id", "" + credentials.getParentID());
    }

    private void setImagesInSlider(AdCredentials credentials,ApproveApVh holder ) {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class ApproveApVh extends RecyclerView.ViewHolder {


    CardView cardView;
    TextView tvTitle, tvAdType, tvprice, tvDesc, tvCategory;
    //    ImageView imageView;
    Button approveBtn, rejectBtn;

    ImageSlider slider;


    public ApproveApVh(@NonNull View itemView) {
        super(itemView);
        slider = itemView.findViewById(R.id.image_slider_admin_approve);
        cardView = itemView.findViewById(R.id.adminApprove_cardview);
//        imageView=itemView.findViewById(R.id.id_recyclerAdminApprove_imageView);
        tvTitle = itemView.findViewById(R.id.id_recyclerAdminApprove_title);
        tvAdType = itemView.findViewById(R.id.id_recyclerAdminApprove_adType);
        tvprice = itemView.findViewById(R.id.id_recyclerAdminApprove_price);
        tvDesc = itemView.findViewById(R.id.id_recycler_ApproveAd_description);
        tvCategory = itemView.findViewById(R.id.id_recyclerAdminApprove_category);
        approveBtn = itemView.findViewById(R.id.id_admin_ad_approveBtn);
        rejectBtn = itemView.findViewById(R.id.id_admin_ad_rejectBtn);
    }
}

