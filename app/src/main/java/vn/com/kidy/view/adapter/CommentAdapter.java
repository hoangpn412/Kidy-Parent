package vn.com.kidy.view.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

/**
 * Created by Family on 5/19/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemsViewHolder> {

    private List<Comment> items;
    private ItemClickListener itemClickListener;

    public CommentAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<Comment> items) {
        this.items = items;
    }
    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Comment item = items.get(position);

        holder.txt_title.setText("Nhận xét tuần " + item.getWeek());
        holder.txt_time.setText(Html.fromHtml("Từ ngày " + item.getFromDate() + " đến " + item.getToDate()));
//        if (item.getRead()) {
//            holder.txt_title.setTypeface(Typeface.DEFAULT);
//        } else {
//            holder.txt_title.setTypeface(Typeface.DEFAULT_BOLD);
//        }

        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
//                item.setRead(true);
                this.notifyDataSetChanged();
                itemClickListener.onItemClick(items, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(List<Comment> items, Comment item, int position);
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
