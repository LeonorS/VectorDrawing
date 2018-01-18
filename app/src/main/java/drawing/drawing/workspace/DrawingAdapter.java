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
 * VectorDrawing for FretX
 * Created by pandor on 17/01/18 20:21.
 */

public class DrawingAdapter extends RecyclerView.Adapter<DrawingAdapter.ViewHolder> {
    private List<Drawing> drawings;
    private Activity activity;
    private View.OnClickListener onClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public DrawingAdapter(Activity activity, List<Drawing> drawings, View.OnClickListener onClickListener) {
        this.drawings = drawings;
        this.onClickListener = onClickListener;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DrawingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawing_cardview, parent, false);
        v.setOnClickListener(onClickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return drawings.size();
    }
}
