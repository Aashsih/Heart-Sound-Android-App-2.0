package com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces;

import com.head_first.aashi.heartsounds_20.model.MurmurRating;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public interface MurmurRatingAPI {
    public void requestMurmurRatingsForHeartSound(int heartSoundId);
    public void requestMurmurRating(int murmurRatingId);
    public void createMurmurRating(MurmurRating murmurRating);
    public void updateMurmurRating(MurmurRating murmurRating);
    public void deleteMurmurRating(int murmurRatingId);
}
