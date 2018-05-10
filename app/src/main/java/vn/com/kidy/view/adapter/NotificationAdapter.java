package vn.com.kidy.view.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.model.comment.Comment;
import vn.com.kidy.data.model.notification.Notification;

/**
 * Created by Family on 5/19/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemsViewHolder> {

    private List<Notification> items;
    private ItemClickListener itemClickListener;

    public NotificationAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<Notification> items) {
        this.items = items;
    }
    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Notification item = items.get(position);

        holder.txt_title.setText(item.getTitle());
        holder.txt_time.setText(item.getDateStr() + "");

        if (item.getRead()) {
            holder.txt_title.setTypeface(Typeface.DEFAULT);
        } else {
            holder.txt_title.setTypeface(Typeface.DEFAULT_BOLD);
        }


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
        void onItemClick(List<Notification> items, Notification item, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txt_title;
        @BindView(R.id.txt_time)
        TextView txt_time;

        View itemView;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
