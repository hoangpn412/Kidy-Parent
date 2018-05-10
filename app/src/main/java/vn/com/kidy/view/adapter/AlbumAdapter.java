package vn.com.kidy.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.media.Photo;

/**
 * Created by Family on 5/19/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ItemsViewHolder> {

    private List<Photo> items;
    private ItemClickListener itemClickListener;

    public AlbumAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<Photo> items) {
        this.items = items;
    }
    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new AlbumAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Photo item = items.get(position);

        Uri uri = Uri.parse(Constants.IMAGE_BASE_URL + item.getPath());
        holder.image.setImageURI(uri);

        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(items, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(List<Photo> items, Photo item, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {

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
