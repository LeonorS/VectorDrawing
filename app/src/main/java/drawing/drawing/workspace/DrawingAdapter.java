package drawing.drawing.workspace;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import drawing.drawing.R;
import drawing.drawing.database.Drawing;
import drawing.drawing.storage.Storage;

/**
 * Created by leo on 17/01/18.
 */

public class DrawingAdapter extends RecyclerView.Adapter<DrawingAdapter.ViewHolder> {
    private List<Drawing> drawings;
    private Activity activity;
    private View.OnClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView date;
        public ImageView preview;
        public CardView cardView;
        public ViewHolder(View v) {
            super(v);
            name = itemView.findViewById(R.id.drawing_name);
            date = itemView.findViewById(R.id.drawing_date);
            preview = itemView.findViewById(R.id.drawing_preview);
            cardView = itemView.findViewById(R.id.cv);
        }
    }

    public DrawingAdapter(Activity activity, List<Drawing> drawings, View.OnClickListener onClickListener) {
        this.drawings = drawings;
        this.onClickListener = onClickListener;
        this.activity = activity;
    }

    @Override
    public DrawingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawing_cardview, parent, false);
        v.setOnClickListener(onClickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Drawing drawing = drawings.get(position);
        holder.name.setText(drawing.name);
        holder.date.setText(drawing.lastUpdate);
        Storage.getInstance().getPreviewDownloadUrl(drawing.name, new Storage.OnStorageCompleteListener() {
            @Override
            public void onSuccess(Object obj) {
                Picasso.with(activity).load(obj.toString()).into(holder.preview);
            }
            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return drawings.size();
    }
}
