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
import vn.com.kidy.data.model.login.Kid;

/**
 * Created by Family on 5/19/2017.
 */

public class KidAdapter extends RecyclerView.Adapter<KidAdapter.ItemsViewHolder> {

    private List<Kid> items;
    private ItemClickListener itemClickListener;

    public KidAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<Kid> items) {
        this.items = items;
    }
    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kid, parent, false);
        return new KidAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Kid item = items.get(position);

        holder.kid_name.setText(item.getFullName());

        Uri uri = Uri.parse(item.getAvatar().replaceAll("\\\\", "/"));
        holder.kid_avatar.setImageURI(uri);

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
        void onItemClick(List<Kid> items, Kid item, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.kid_avatar)
        SimpleDraweeView kid_avatar;
        @BindView(R.id.kid_name)
        TextView kid_name;

        View itemView;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
