package com.wigitech.yam.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.wigitech.yam.listeners.FavoriteCoastListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 30/07/2016.
 */
@EBean(scope = EBean.Scope.Singleton)
public class FavoriteCoastsService {

    //region Consts
    private static final String FAVORITE_PREFS_NAME = "com.wigitech.yam.MY_FAVORITE_COASTS";
    public static final int NUM_FAVORITE_COASTS = 3;
    private static final String FAVORITE_KEY = "favorite";
    //endregion

    //region Fields
    private FavoriteCoastListener mFavoriteCoastListener;
    private Map<String, Integer> mFavoriteCoastsCache;

    @RootContext
    Context mContext;
    //endregion

    //region Methods
    @AfterInject
    protected void initFavoriteCoasts() {
        mFavoriteCoastsCache = new HashMap<>(NUM_FAVORITE_COASTS);
        SharedPreferences settings = getSettings();
        for (int i = 0; i < NUM_FAVORITE_COASTS; i++) {
            String coastKey = getCoastKey(i);
            mFavoriteCoastsCache.put(coastKey, settings.getInt(coastKey, -1));
        }
    }

    /**
     * Gets all the favorite coasts by order.
     * @return all the favorite coasts.
     */
    public List<Integer> getFavoriteCoasts() {
        return Stream.of(mFavoriteCoastsCache.keySet()).
                sorted().
                map(value -> mFavoriteCoastsCache.get(value)).
                collect(Collectors.toList());
    }

    public void setFavoriteCoast(int coastId, boolean isSetFavorite) {
        SharedPreferences settings = getSettings();
        SharedPreferences.Editor editor = settings.edit();
        int coastSlot = getFavoriteCoastSlot(coastId);
        String coastKey = getCoastKey(coastSlot);

        if (isSetFavorite) {
            if (coastSlot == -1) {
                coastSlot = getAvailableFavoriteCoastSlot();
                if (coastSlot == -1)
                    return;

                coastKey = getCoastKey(coastSlot);
                editor.putInt(coastKey, coastId);
                mFavoriteCoastsCache.put(coastKey, coastId);
            }
        } else {
            if (coastSlot == -1)
                return;

            editor.remove(coastKey);
            mFavoriteCoastsCache.put(coastKey, -1);
        }
        editor.apply();
        if (mFavoriteCoastListener != null)
            mFavoriteCoastListener.onFavoriteCoastStateChanged(coastSlot, isSetFavorite, coastId);
    }

    public boolean isFavoriteCoastSlotAvailable() {
        return getAvailableFavoriteCoastSlot() != -1;
    }

    public boolean isFavoriteCoast(int coastId) {
        return getFavoriteCoastSlot(coastId) != -1;
    }

    public void setOnFavoriteCoastStateChangedListener(FavoriteCoastListener listener) {
        mFavoriteCoastListener = listener;
    }

    @NonNull
    private String getCoastKey(int coastSlot) {
        return FAVORITE_KEY + coastSlot;
    }

    private int getFavoriteCoastSlot(int coastId) {
        for (int i = 0; i < NUM_FAVORITE_COASTS; i++) {
            String coastKey = getCoastKey(i);
            Integer coastIdAtSlot = mFavoriteCoastsCache.get(coastKey);
            if (coastId == coastIdAtSlot)
                return i;
        }
        return -1;
    }

    private int getAvailableFavoriteCoastSlot() {
        for (int i = 0; i < NUM_FAVORITE_COASTS; i++) {
            String coastKey = getCoastKey(i);
            Integer coastIdAtSlot = mFavoriteCoastsCache.get(coastKey);
            if (coastIdAtSlot == -1)
                return i;
        }
        return -1;
    }

    private SharedPreferences getSettings() {
        return mContext.getSharedPreferences(FAVORITE_PREFS_NAME, Context.MODE_PRIVATE);
    }
    //endregion
}
