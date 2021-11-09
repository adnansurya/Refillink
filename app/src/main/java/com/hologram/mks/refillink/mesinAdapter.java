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

public class mesinAdapter extends FirebaseRecyclerAdapter<
        mesin, mesinAdapter.mesinViewholder> {

    public mesinAdapter(
            @NonNull FirebaseRecyclerOptions<mesin> options)
    {
        super(options);
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void
    onBindViewHolder(@NonNull mesinAdapter.mesinViewholder holder,
                     int position, @NonNull mesin model)
    {

        holder.mliterTxt.setText(String.format("%.1f L", model.getTersedia()));
        holder.persenTxt.setText(String.format("%.0f %%",(model.getTersedia() * 100 / model.getKapasitas())));
        holder.mesinTxt.setText(model.getIdMesin());

        String relay =model.getRelay();
        if(!relay.equals("OFF")){
            relay = "Mengisi... (" + relay + " mL)";
        }else{
            if((model.getTersedia() * 100 / model.getKapasitas()) <= 5){

                relay = "Maintenance";
            }else{
                relay = "Ready";
            }
        }

        holder.relayTxt.setText(relay);
    }

    @NonNull
    @Override
    public mesinAdapter.mesinViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_mesin, parent, false);
        return new mesinAdapter.mesinViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class mesinViewholder
            extends RecyclerView.ViewHolder {
        TextView mliterTxt, persenTxt, mesinTxt, relayTxt;
        public mesinViewholder(@NonNull View itemView)
        {
            super(itemView);

            mliterTxt = itemView.findViewById(R.id.mlTxt);
            persenTxt = itemView.findViewById(R.id.persen2Txt);
            mesinTxt = itemView.findViewById(R.id.mesinTxt);
            relayTxt = itemView.findViewById(R.id.relayTxt);
        }
    }
}

