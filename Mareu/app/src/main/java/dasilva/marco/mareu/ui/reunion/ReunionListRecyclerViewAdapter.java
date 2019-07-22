package dasilva.marco.mareu.ui.reunion;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import dasilva.marco.mareu.R;
import dasilva.marco.mareu.events.DeleteReunionEvent;
import dasilva.marco.mareu.model.Reunion;


import java.util.List;

public class ReunionListRecyclerViewAdapter extends RecyclerView.Adapter<ReunionListRecyclerViewAdapter.ViewHolder> {

    private final List<Reunion> reunionList;

    public ReunionListRecyclerViewAdapter( List<Reunion> reunionList){
        this.reunionList = reunionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_reunion_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Reunion reunion = reunionList.get(i);
        viewHolder.reunionDescription.setText(reunion.getDescription());
        viewHolder.reunionDetails.setText(reunion.getParticipants());
        viewHolder.reunionAvatar.setImageResource(reunion.getColorAvatar());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteReunionEvent(reunion));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reunionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView reunionAvatar;
        public TextView reunionDescription;
        public TextView reunionDetails;
        public ImageButton deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);

           reunionAvatar = (ImageView) itemView.findViewById(R.id.reunion_list_avatar);
           reunionDescription = (TextView) itemView.findViewById(R.id.reunion_info_textView);
           reunionDetails = (TextView) itemView.findViewById(R.id.reunion_details_textView);
           deleteBtn = (ImageButton) itemView.findViewById(R.id.reunion_list_delete_button);
        }
    }
}
