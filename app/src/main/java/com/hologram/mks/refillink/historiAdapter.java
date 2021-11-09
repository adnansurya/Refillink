package com.hologram.mks.refillink;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class historiAdapter extends FirebaseRecyclerAdapter<
        histori, historiAdapter.historiViewholder> {

    public historiAdapter(
            @NonNull FirebaseRecyclerOptions<histori> options)
    {
        super(options);
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void
    onBindViewHolder(@NonNull historiViewholder holder,
                     int position, @NonNull histori model)
    {

        holder.literTxt.setText(String.format("%.0f mL", (model.getJumlah() * 1000)));
        holder.hargaTxt.setText(String.format("Rp. %d",model.getHarga()));
        holder.waktuTxt.setText(model.getWaktu());
        holder.mesinTxt.setText(model.getKode_mesin());
    }

    @NonNull
    @Override
    public historiViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_history, parent, false);
        return new historiAdapter.historiViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class historiViewholder
            extends RecyclerView.ViewHolder {
        TextView literTxt, waktuTxt, mesinTxt, hargaTxt;
        public historiViewholder(@NonNull View itemView)
        {
            super(itemView);

            literTxt = itemView.findViewById(R.id.literTxt);
            waktuTxt = itemView.findViewById(R.id.waktuTxt);
            hargaTxt = itemView.findViewById(R.id.hargaTxt);
            mesinTxt = itemView.findViewById(R.id.mesinTxt);
        }
    }
}
