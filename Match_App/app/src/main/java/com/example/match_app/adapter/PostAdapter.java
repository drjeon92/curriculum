package com.example.match_app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.NotiDataDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.fragment.SearchFragment;
import com.example.match_app.post.PostDetailActivity;
import com.example.match_app.post.PostUpdateActivity;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.keywords;
import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.Common.CommonMethod.notiDataDTO;
import static com.example.match_app.Common.MyService.notiDTO;
import static com.example.match_app.MainActivity.user;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
        implements com.example.match_app.adapter.PostOnClickListener {

    private static final String TAG = "ListItemAdapter ";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser(); // ???????????? ????????? ?????? ????????????
    String uid = user2 != null ? user2.getUid() : null; // ???????????? ????????? ?????? uid ????????????
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // 1. ????????? ??????
    com.example.match_app.adapter.PostOnClickListener listener;

    // ???????????? ?????? ?????????
    ArrayList<PostDTO> dtos;
    Context context;
    LayoutInflater inflater;

    public PostAdapter(ArrayList<PostDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    // 2. ????????? ??????????????? ???????????? ?????????????????? ?????? ????????????
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_list_view,
                parent, false);
        return new ViewHolder(itemView, this);
    }

    // ??????????????? ?????? ????????? ???????????? ???????????????
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PostDTO dto = dtos.get(position);
        // ???????????? ????????? ?????? setDto??? ????????? dto??? ?????????
        holder.setDto(dto);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , PostDetailActivity.class);

                intent.putExtra("post", getItem(position));
                ((Activity)context).startActivityForResult(intent, 100); /* ??? ?????? ??? ???????????? */
            }
        });

        //todo ??????????????? ?????? ?????? ????????? ?????? ??????
        Log.d(TAG, "user.getIdToken(): " + user.getIdToken());
        Log.d(TAG, "dto.getWriterToken(): " + dto.getWriterToken());
        if(user.getIdToken().equals(dto.getWriterToken())){  //Writer??? user??? token??? ??????????????? ???????????? ?????? ??????, ?????? ????????? ????????? ??????
            holder.writerLayout.setVisibility(View.VISIBLE);
        }
        CheckBox like = holder.itemView.findViewById(R.id.like);
        if (user.getIdToken().equals(dto.getWriterToken())) {
            holder.like.setVisibility(View.GONE);
        }else {
            holder.like.setVisibility(View.VISIBLE);
        }

        for (int j = 0 ; j<notiDTO.size(); j++) {
            if(dtos.get(position).getPostKey().equals(notiDTO.get(j).getPostToken())){
                like.setChecked(notiDTO.get(j).isLike());
            }
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiDataDTO.setIdToken(uid);
                notiDataDTO.setPostToken(dto.getPostKey());
                notiDataDTO.setLike(like.isChecked());
                firebaseDatabase.getReference("matchapp").child("NotiData").child(uid).child(dto.getPostKey()).setValue(notiDataDTO);
                firebaseDatabase.getReference("matchapp").child("Post").child(dto.getPostKey()).setValue(dto);
            }
        });


        //?????? ?????? ????????? ??????
        holder.itemView.findViewById(R.id.btnModify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostUpdateActivity.class);
                intent.putExtra("post", dto);
                context.startActivity(intent);
            }
        });

        //?????? ?????? ????????? ??????
        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("?????? ?????????????????????????")        // ?????? ??????
                        .setCancelable(false)        // ?????? ?????? ????????? ?????? ?????? ??????
                        .setPositiveButton("??????", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){

//                                            Log.d(TAG, "onClick: " + firebaseDatabase.getReference().child(dto.getPostKey()));

                                //?????????1     databaseReference = firebaseDatabase.getReference("matchapp/Post");
                                //                                            databaseReference.child(dto.getPostKey()).removeValue().

                                //?????????2     firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().

                                firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // ????????? ?????????????????? ????????? : Adapter??? ??????????????? ???????????? ????????? FLAG_ACTIVITY_NEW_TASK ??????
                                        Intent mainIntent = new Intent(context, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mainIntent.putExtra("requestCode", 100);
                                        context.startActivity(mainIntent);

                                        Toast.makeText(context, "?????? ??????", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "?????? ??????", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("??????", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){

                            }
                        });

                AlertDialog dialog = builder.create();    // ????????? ?????? ??????
                dialog.show();    // ????????? ?????????
            }
        });

    }

    @Override
    public int getItemCount() {
        return dtos==null ? 0: dtos.size();
    }

    // ArrayList<SuperDTO>??? dto??? ???????????? ????????? ???????????? ?????????
    public void addDto(PostDTO dto){
        dtos.add(dto);
        notifyItemInserted(dtos.size()-1);
    }

    // ArrayList<SuperDTO>??? ??????????????? dto??? ???????????? ????????? ???????????? ?????????
    public void removeDto(int position){
        dtos.remove(position);
    }

    // 6. ???????????? ????????? ????????? ?????? dto ????????????
    public PostDTO getItem(int position){
        return dtos.get(position);
    }

    // 4. ???????????? ?????????????????? ??????????????? ???????????? ???????????? ???????????????
    public void setOnItemClickListener(com.example.match_app.adapter.PostOnClickListener listener){
        this.listener = listener;
    }

    // ?????????????????? ????????? ????????? ??????
    // 5. ???????????? ?????????????????? ????????? ????????? ????????? ???????????? ????????????
    @Override
    public void onItemClick(ViewHolder holderm, View view, int position) {
        if(listener != null){
            listener.onItemClick(holderm, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, imageLayout, tvGame, tvTime, tvPlace, tvFee;
        ImageView image; CheckBox like;
        LinearLayout parentLayout, textLayout, writerLayout;


        public ViewHolder(@NonNull View itemView, com.example.match_app.adapter.PostOnClickListener listener) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parentLayout);
            tvTitle = itemView.findViewById(R.id.tvTitle);
//            imageLayout = itemView.findViewById(R.id.imageLayout); /*????????? ????????????*/
            image = itemView.findViewById(R.id.image);
            tvGame = itemView.findViewById(R.id.tvGame);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvFee = itemView.findViewById(R.id.tvFee);
            like = itemView.findViewById(R.id.like);

            writerLayout = itemView.findViewById(R.id.writerLayout);

            // 3. ?????????????????? ????????????
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,
                                view, position);
                    }
                }
            });
        }

        public void setDto(PostDTO dto){
            //??????, ????????? ?????? ????????? ?????????

            tvTitle.setText(dto.getTitle());

            if(dto.getFee().equals("0")) {
                tvFee.setText("????????? ??????");
            } else {
                tvFee.setText(dto.getFee() + "???");
            }
            tvPlace.setText(dto.getPlace());
            tvTime.setText(dto.getTime());





            if(dto.getMatchConfirm().equals("enable")){
                tvGame.setText(dto.getGame());
            }else if(dto.getMatchConfirm().equals("disable")){
                tvGame.setText("?????? ??????");
                parentLayout.setBackgroundColor(R.drawable.button_background2);
           }

            switch (dto.getGame()){
                case "??????" :
                    image.setImageResource(R.drawable.soccer);
                    break;
                case "??????" :
                    image.setImageResource(R.drawable.basketball);
                    break;
                case "?????????" :
                    image.setImageResource(R.drawable.tennis);
                    break;
                case "??????" :
                    image.setImageResource(R.drawable.baseball);
                    break;
                case "??????" :
                    image.setImageResource(R.drawable.volleyball);
                    break;
                case "????????????" :
                    image.setImageResource(R.drawable.badminton);
                    break;
                case "??????" :
                    image.setImageResource(R.drawable.bowling);
                    break;
                case "??????" :
                    image.setImageResource(R.drawable.snooker);
                    break;
                case "????????????" :
                    image.setImageResource(R.drawable.computer);
                    break;
                default:    //?????? ??????
                    image.setImageResource(R.drawable.match);
            }



        }

    }

}
