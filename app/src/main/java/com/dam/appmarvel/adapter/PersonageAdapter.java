package com.dam.appmarvel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dam.appmarvel.DetailsActivity;
import com.dam.appmarvel.R;
import com.dam.appmarvel.model.Personagem;

import java.util.List;


public class PersonageAdapter extends RecyclerView.Adapter<PersonageAdapter.PersonageMVH> {

    private Activity activity;
    private List<Personagem> personagemList;
    private SharedPreferences sharedPreferences;
    private String id;

    public PersonageAdapter(Activity activity, List<Personagem> personagemList) {

        this.activity = activity;
        this.personagemList = personagemList;
    }


    @Override
    public PersonageMVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_personagem, parent, false);

        sharedPreferences = activity.getSharedPreferences("personagem_information", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("personagem_id",null);

        return new PersonageMVH(view);
    }

    @Override
    public void onBindViewHolder(PersonageMVH holder, int position) {

        Personagem personagem = personagemList.get(position);
        holder.vincula(personagem);

    }

    @Override
    public int getItemCount() {
        return personagemList.size();
    }


    public class PersonageMVH extends RecyclerView.ViewHolder{

        TextView tName;
        TextView tId;
        TextView tUrl;
        TextView tDes;
        ImageView img_personagem;

        public PersonageMVH(View itemView) {
            super(itemView);

            tName = itemView.findViewById(R.id.card_tvName);
            tId = itemView.findViewById(R.id.card_tvId);
            tUrl = itemView.findViewById(R.id.card_tvUrl);
            tDes = itemView.findViewById(R.id.card_tvDescription);
            img_personagem = itemView.findViewById(R.id.card_imgPersonage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (personagemList.get(position).getId() == tId.getText().toString()){
                        //Toast.makeText(activity, txt_nome_personagem.getText().toString(), Toast.LENGTH_SHORT).show();

                        Intent goDetails = new Intent(activity, DetailsActivity.class);
                        goDetails.putExtra("id", tId.getText().toString());
                        goDetails.putExtra("url", tUrl.getText().toString());
                        goDetails.putExtra("name", tName.getText().toString());
                        goDetails.putExtra("description", tDes.getText().toString().trim());
                        activity.startActivity(goDetails);

                    }


                }
            });

        }

        public void vincula(Personagem personagem) {
            fillFields(personagem);

        }

        public void fillFields(Personagem personagem) {
            tName.setText(personagem.getNome());
            tId.setText(personagem.getId());
            tUrl.setText(personagem.getUrl());
            tDes.setText(personagem.getDescription());

            Glide.with(activity)
                    .load(personagem.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(img_personagem);
        }
    }

}
