package fr.piotr.reactions.dialogs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import fr.piotr.reactions.EditRuleActivity;
import fr.piotr.reactions.MainActivity;
import fr.piotr.reactions.R;
import fr.piotr.reactions.managers.ImagesManager;
import fr.piotr.reactions.managers.PhotoItem;

/**
 * Created by piotr_000 on 10/12/2016.
 *
 */

public class WallpaperPicker extends AlertDialog{

    private class WallpaperAdapter extends BaseAdapter {

        private List<PhotoItem> thumbs = ImagesManager.getAlbumThumbnails(getContext());

        @Override
        public int getCount() {
            return thumbs.size();
        }

        @Override
        public PhotoItem getItem(int i) {
            return thumbs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getImageID();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            PhotoItem item = getItem(i);
            if(view==null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.image_picker_item, null, false);
            }
            view.setOnClickListener(view1 -> select(item));
            ((ImageView)view).setImageBitmap(ImagesManager.toThumbnailBitmap(getContext(), item.getImageID()));
            return view;
        }

        private void select(PhotoItem item) {
            Intent intent = new Intent(EditRuleActivity.EVENT_WALLPAPER_PICKED_UP);
            intent.putExtra(EditRuleActivity.EXTRA_WALLPAPER_ID, item.getImageID());
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            dismiss();
        }
    }

    public WallpaperPicker(@NonNull Context context) {
        super(context);

        GridView gridView = (GridView) LayoutInflater.from(context).inflate(R.layout.image_picker, null, false);
        gridView.setAdapter(new WallpaperAdapter());
        setView(gridView);
    }
}
