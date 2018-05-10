package vn.com.kidy.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.media.Album;

/**
 * Created by Family on 5/19/2017.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ItemsViewHolder> {

    private List<Album> items;
    private ItemClickListener itemClickListener;

    public MediaAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<Album> items) {
        this.items = items;
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false);
        return new MediaAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Album item = items.get(position);

        holder.txt_title.setText(item.getTitle());
        holder.txt_time.setText(item.getCreatedDate());
        holder.image.setImageURI(Uri.parse(Constants.IMAGE_BASE_URL + item.getThumbnail()));

        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(items, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(List<Album> items, Album item, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txt_title;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.image)
        SimpleDraweeView image;

        View itemView;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
