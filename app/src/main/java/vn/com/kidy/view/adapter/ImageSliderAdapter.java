package vn.com.kidy.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.view.widget.zoomable.DoubleTapGestureListener;
import vn.com.kidy.view.widget.zoomable.ZoomableDraweeView;

/**
 * Created by admin on 3/3/18.
 */

public class ImageSliderAdapter extends PagerAdapter {

    Context context;
    String[] images;
    LayoutInflater layoutInflater;


    public ImageSliderAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
        Log.e("a", "Sizes image:" + images.length);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    private boolean mAllowSwipingWhileZoomed = true;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_viewimage, container, false);

//        SimpleDraweeView image = itemView.findViewById(R.id.image);
//        image.setImageURI(Uri.parse(images[position]));
//        Log.e("a", "images: " + images[position]);

        ZoomableDraweeView zoomableDraweeView =
                itemView.findViewById(R.id.image);
        zoomableDraweeView.setAllowTouchInterceptionWhileZoomed(mAllowSwipingWhileZoomed);
        // needed for double tap to zoom
        zoomableDraweeView.setIsLongpressEnabled(false);
        zoomableDraweeView.setTapListener(new DoubleTapGestureListener(zoomableDraweeView));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(Constants.IMAGE_BASE_URL + images[position]))
                .setCallerContext("ZoomableApp-MyPagerAdapter")
                .build();
        zoomableDraweeView.setController(controller);

        container.addView(itemView);

        //listening to image click
//        zoomableDraweeView.setOnClickListener(v -> Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show());

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}


