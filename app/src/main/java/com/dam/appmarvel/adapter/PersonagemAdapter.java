package com.dam.appmarvel.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dam.appmarvel.DetailsActivity;
import com.dam.appmarvel.R;
import com.dam.appmarvel.model.Personagem;
import com.dam.appmarvel.model.Result;

import java.util.ArrayList;
import java.util.List;


public class PersonagemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<Result> resultList;
    private SharedPreferences sharedPreferences;
    private String id;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;


    public PersonagemAdapter(Activity activity) {

        this.activity = activity;
        resultList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList){
        this.resultList = resultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View view2 = inflater.inflate(R.layout.load_progress, parent, false);
                viewHolder = new LoadingVH(view2);
                break;
        }

        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater){
        RecyclerView.ViewHolder viewHolder;
        View view = inflater.inflate(R.layout.card_personagem, parent, false);
        viewHolder = new PublicationVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Result result = resultList.get(position);

        String resultId = resultList.get(position).getId();
        String resultName = resultList.get(position).getName();
        String resultThumbnail = resultList.get(position).getThumbnail().getPath()
                + "." + resultList.get(position).getThumbnail().getExtension();

        String resultDescription = resultList.get(position).getDescription();

        Personagem personagem = new Personagem(resultId, resultName, resultThumbnail,resultDescription);

        switch (getItemViewType(position)){
            case ITEM:

                if (result.getId() != null && result.getName() != null && result.getThumbnail() != null ){
                    final PublicationVH publicationVH = (PublicationVH) holder;

                     publicationVH.tId.setText(personagem.getId());
                     publicationVH.tName.setText(personagem.getNome());
                     publicationVH.tUrl.setText(personagem.getUrl());
                     publicationVH.tDescrip.setText(personagem.getDescription());

                    Glide.with(activity)
                    .load(personagem.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(publicationVH.imghero);

                }

                break;
            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return resultList == null ? 0 : resultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == resultList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Result result) {
        resultList.add(result);
        notifyItemInserted(resultList.size() - 1);
    }

    public void addAll(ArrayList<Result> mcList) {
        for (Result mc : mcList) {
            add(mc);
        }
    }

    public void remove(Result result) {
        int position = resultList.indexOf(result);
        if (position > -1) {
            resultList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;

            int position = resultList.size() - 1;
            Result item = getItem(position);

            if (item.getId() == null) {
                resultList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public Result getItem(int position) {
        return resultList.get(position);
    }

    protected class PublicationVH extends RecyclerView.ViewHolder {
        TextView tName;
        TextView tId;
        TextView tUrl;
        TextView tDescrip;
        ImageView imghero;

        public PublicationVH(View view) {
            super(view);

            tName = itemView.findViewById(R.id.card_tvName);
            tId = itemView.findViewById(R.id.card_tvId);
            tUrl = itemView.findViewById(R.id.card_tvUrl);
            tDescrip = itemView.findViewById(R.id.card_tvDescription);
            imghero = itemView.findViewById(R.id.card_imgPersonage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (resultList.get(position).getId() == tId.getText().toString()){
                        Toast.makeText(activity, tName.getText().toString(), Toast.LENGTH_SHORT).show();

                        Intent goDetails = new Intent(activity, DetailsActivity.class);
                        goDetails.putExtra("id", tId.getText().toString());
                        goDetails.putExtra("url", tUrl.getText().toString());
                        goDetails.putExtra("name", tName.getText().toString());
                        goDetails.putExtra("description", tDescrip.getText().toString());
                        activity.startActivity(goDetails);

                    }

                }
            });

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    }
