package com.wigitech.yam.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.wigitech.yam.R;
import com.wigitech.yam.dataacess.CoastDataService;
import com.wigitech.yam.model.Coast;
import com.wigitech.yam.model.DetailedCoast;
import com.wigitech.yam.services.FavoriteCoastsService;
import com.wigitech.yam.services.IntentExtrasService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

@EActivity(R.layout.activity_coast_details)
public class CoastDetailsActivity extends AppCompatActivity {

    //region Fields
    public int mCoastId;
    public Coast mCoastLocationDetails;

    @ViewById(R.id.waterTemperatureValue)
    TextView waterTemperatureValue;
    @ViewById(R.id.windDirectionValue)
    TextView windDirectionValue;
    @ViewById(R.id.windSpeedValue)
    TextView windSpeedValue;
    @ViewById(R.id.waveHeightValue)
    TextView waveHeightValue;
    @ViewById(R.id.jellyFishTypeValue)
    AppCompatImageView jellyFishTypeValue;
    @ViewById(R.id.toggleFavoriteButton)
    ImageView toggleFavoriteButton;
    @ViewById (R.id.accessibilityButton)
    ImageView accessibilityButton;
    @ViewById (R.id.blueFlagButton)
    ImageView blueFlagButton;

    @Bean
    FavoriteCoastsService favoriteCoastsService;
    @RestService
    CoastDataService mCoastDataService;
    //endregion

    //region AppCompatActivity Overridden methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //endregion

    //region Methods
    @AfterViews
    void init() {
        Intent intent = getIntent();
        mCoastId = intent.getIntExtra(IntentExtrasService.COAST_ID, -1);
        if (mCoastId == -1) {
            final AlertDialog alert = new AlertDialog.Builder(this).
                    setTitle("Error").
                    setMessage("Could not find the requested coast").
                    setNeutralButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                        CoastDetailsActivity.this.finish();
                    }).
                    create();
            alert.show();
            return;
        }

        boolean isFavoriteCoast = favoriteCoastsService.isFavoriteCoast(mCoastId);
        toggleFavoriteButton.setImageResource(
                isFavoriteCoast ? R.drawable.favorite_selected :
                                    R.drawable.favorite_unselected);
        toggleFavoriteButton.setOnClickListener(v -> {
            boolean isFavCoast = favoriteCoastsService.isFavoriteCoast(mCoastId);
            if (isFavCoast) {
                favoriteCoastsService.
                        setFavoriteCoast(mCoastId, false);
                toggleFavoriteButton.setImageResource(R.drawable.favorite_unselected);
                return;
            }

            boolean isFavoriteCoastSlotAvailable =
                    favoriteCoastsService.isFavoriteCoastSlotAvailable();
            if (!isFavoriteCoastSlotAvailable) {
                AlertDialog alert = new AlertDialog.Builder(CoastDetailsActivity.this).
                        setTitle("Cannot set favorite").
                        setMessage("You must remove a favorite coast first").
                        setNeutralButton("Ok", (dialog, which) -> {
                            dialog.dismiss();
                        }).
                        create();
                alert.show();
                return;
            }

            favoriteCoastsService.setFavoriteCoast(mCoastId, true);
            toggleFavoriteButton.setImageResource(R.drawable.favorite_selected);
        });
        initCoastDetailsAsync(mCoastId);
    }

    @Background
    void initCoastDetailsAsync(int mCoastId) {
        DetailedCoast detailedCoast = null;
        try {
            detailedCoast = mCoastDataService.getCoast(mCoastId);
        } catch (Exception e) {
            Log.e("Exception", "Failed to get coast from server", e);
        }
        if (detailedCoast != null)
            updateCoastDetailsUi(detailedCoast);

        mCoastLocationDetails = Stream.of(mCoastDataService.getAllCoasts()).
                filter(value -> value.getBeachId() == mCoastId).
                findFirst().get();
    }

    @UiThread
    void updateCoastDetailsUi(DetailedCoast detailedCoast) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(detailedCoast.getBeachName());
            supportActionBar.setBackgroundDrawable(
                    ContextCompat.getDrawable(this, R.drawable.app_action_toolbar));
        }
        waterTemperatureValue.setText(detailedCoast.getWaterTemperatureValue());
        windDirectionValue.setText(detailedCoast.getWindDirectionType());
        windSpeedValue.setText(detailedCoast.getWindSpeedValue());
        waveHeightValue.setText(detailedCoast.getWaveHeightValue());
        jellyFishTypeValue.setImageResource(
                getJellyfishImageByType(detailedCoast.getJellyFishType()));
        if (detailedCoast.isBlueFlag()) {
            blueFlagButton.setVisibility(View.VISIBLE);
        }
        if (detailedCoast.isHandicappedFriendly()) {
            accessibilityButton.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.exitButton)
    void exitButtonClicked() {
        finish();
    }

    @Click(R.id.navigateByWazeButton)
    void navigateByWazeButtonClicked() {
        if (mCoastLocationDetails == null)
            return;

        String uri = String.format("waze://?ll=%s,%s&navigate=yes",
                mCoastLocationDetails.getLatitude(), mCoastLocationDetails.getLongitude());
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(uri)));
        } catch (ActivityNotFoundException e) {
            AlertDialog alert = new AlertDialog.Builder(CoastDetailsActivity.this).
                    setTitle("Cannot navigate by Waze").
                    setMessage("Could not start navigation by Waze, is it installed on your device?").
                    setNeutralButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                    }).
                    create();
            alert.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getJellyfishImageByType(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "none":
                return R.drawable.no_jellyfish;
            case "few":
                return R.drawable.one_jellyfish;
            case "some":
                return R.drawable.one_jellyfish;
            case "lots":
                return R.drawable.three_jellyfish;
        }
        return R.drawable.no_m;
    }
    //endregion
}
