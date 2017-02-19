package fr.piotr.reactions.reactions;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.managers.ImagesManager;
import fr.piotr.reactions.registry.PossibleReactions;
import fr.piotr.reactions.registry.ReactionsRegistry;
import fr.piotr.reactions.utils.NotificationUtils;

/**
 * Created by piotr_000 on 03/12/2016.
 *
 */

public class SetWallpaperReaction extends Reaction {

    private int targetWallpaperResId;

    public SetWallpaperReaction(Context context, int targetWallpaperResId){
        super(context);
        this.targetWallpaperResId=targetWallpaperResId;
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.SET_WALLPAPER.getReactionsId();
    }

    @Override
    public int getName() {
        return R.string.SetWallpaperReactionName;
    }

    @Override
    public boolean run(Event event) {
        if (super.run(event)) {
            try {
                WallpaperManager.getInstance(context).setBitmap(getBitmap());
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
            return true;
        }
        return false;
    }

    public Bitmap getBitmap() {
        try {
            return ImagesManager.toMediaBitmap(context, targetWallpaperResId);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_wallpaper;
    }

    @Override
    public String getExtra() {
        return ""+targetWallpaperResId;
    }
}
