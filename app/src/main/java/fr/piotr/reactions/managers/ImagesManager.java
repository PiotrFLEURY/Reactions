package fr.piotr.reactions.managers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotr_000 on 03/12/2016.
 *
 */

public class ImagesManager {

    public static Bitmap toMediaBitmap(Context context, int id) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id));
    }

    public static Bitmap toThumbnailBitmap(Context context, int id){
        return MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
                id, MediaStore.Images.Thumbnails.MINI_KIND, null);
    }

    /**
     * Fetch both full sized images and thumbnails via a single query.
     * Returns all images not in the Camera Roll.
     * @param context
     * @return
     */
    public static List<PhotoItem> getAlbumThumbnails(Context context){
        List<PhotoItem> items = new ArrayList<>();
        getThumbnails(context, items, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI);
        getThumbnails(context, items, MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI);
        return items;
    }

    private static void getThumbnails(Context context, List<PhotoItem> items, Uri uri) {
        final String[] projection = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
        String sortOrder = MediaStore.Images.Thumbnails.IMAGE_ID + " DESC";
        Cursor thumbnailsCursor = context.getContentResolver().query(uri,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                sortOrder);

        if(thumbnailsCursor!=null) {
            try{
                thumbnailsCursor.moveToFirst();
                while (thumbnailsCursor.isAfterLast() == false) {
                    String thumbnailPath = thumbnailsCursor.getString(thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                    int thumbnailImageId = thumbnailsCursor.getInt(thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
                    PhotoItem item = new PhotoItem(thumbnailPath, thumbnailImageId);
                    items.add(item);
                    thumbnailsCursor.moveToNext();
                }
            } finally {
                thumbnailsCursor.close();
            }
        }
    }
}
