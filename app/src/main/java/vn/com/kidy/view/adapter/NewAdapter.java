package vn.com.kidy.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
import vn.com.kidy.data.model.news.NewsList;

/**
 * Created by Family on 5/19/2017.
 */

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ItemsViewHolder> {

    private List<NewsList> items;
    private ItemClickListener itemClickListener;

    public NewAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<NewsList> items) {
        this.items = items;
    }
    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new NewAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        NewsList item = items.get(position);

        holder.txt_title.setText(item.getNewsTitle());
        holder.txt_time.setText(Html.fromHtml(item.getCreatedDate() + ""));

        Log.e("a", item.getNewsPresentImage());

        Uri uri = Uri.parse(Constants.IMAGE_BASE_URL + item.getNewsPresentImage());
        holder.img_art.setImageURI(uri);

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
        void onItemClick(List<NewsList> items, NewsList item, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_art)
        SimpleDraweeView img_art;
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
