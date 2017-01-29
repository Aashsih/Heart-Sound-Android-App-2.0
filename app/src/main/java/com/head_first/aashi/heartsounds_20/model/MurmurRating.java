package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.enums.AddedSounds;
import com.head_first.aashi.heartsounds_20.enums.CardiacPhase;
import com.head_first.aashi.heartsounds_20.enums.ChangeWithBreathing;
import com.head_first.aashi.heartsounds_20.enums.CHARACTER;
import com.head_first.aashi.heartsounds_20.enums.FinalDiagnosis;
import com.head_first.aashi.heartsounds_20.enums.Intensity;
import com.head_first.aashi.heartsounds_20.enums.LeftLateralPosition;
import com.head_first.aashi.heartsounds_20.enums.MostIntenseLocation;
import com.head_first.aashi.heartsounds_20.enums.MurmurDuration;
import com.head_first.aashi.heartsounds_20.enums.Radiation;
import com.head_first.aashi.heartsounds_20.enums.S1;
import com.head_first.aashi.heartsounds_20.enums.S2;
import com.head_first.aashi.heartsounds_20.enums.SittingForward;
import com.head_first.aashi.heartsounds_20.enums.Valsalva;

import java.util.List;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class MurmurRating {

    private long id;
    private long doctorId;
    private long heartSoundId;
    //enums
    private CardiacPhase cardiacPhase;
    private AddedSounds addedSounds;
    private ChangeWithBreathing changeWithBreathing;
    private LeftLateralPosition leftLateralPosition;
    private MostIntenseLocation mostIntenseLocation;
    private MurmurDuration murmurDuration;
    private Radiation radiation;
    private S1 s1;
    private S2 s2;
    private SittingForward sittingForward;
    private Valsalva valsalva;

    //check these ones
    private FinalDiagnosis finalDiagnosis;
    private Intensity intensity;
    private String ratingInfo;

    private List<CHARACTER> character;//this has 3 checkboxes..can the user select multiple checkboxes. If so we will use an array

    /**
     *This constructor should be used when gettin data from the database
     */
    public MurmurRating(long doctorId, long heartSoundId, CardiacPhase cardiacPhase, AddedSounds addedSounds, ChangeWithBreathing changeWithBreathing,
                        LeftLateralPosition leftLateralPosition, MostIntenseLocation mostIntenseLocation, MurmurDuration murmurDuration, Radiation radiation,
                        S1 s1, S2 s2, SittingForward sittingForward, Valsalva valsalva, FinalDiagnosis finalDiagnosis, Intensity intensity, String ratingInfo,
                        List<CHARACTER> character) {
        //The id will be received from the data base/
        // /this.id = (ID.add(BigInteger.ONE)).toString();
        this.doctorId = doctorId;
        this.heartSoundId = heartSoundId;
        this.setCardiacPhase(cardiacPhase);
        this.setAddedSounds(addedSounds);
        this.setChangeWithBreathing(changeWithBreathing);
        this.setLeftLateralPosition(leftLateralPosition);
        this.setMostIntenseLocation(mostIntenseLocation);
        this.setMurmurDuration(murmurDuration);
        this.setRadiation(radiation);
        this.setS1(s1);
        this.setS2(s2);
        this.setSittingForward(sittingForward);
        this.setValsalva(valsalva);
        this.setFinalDiagnosis(finalDiagnosis);
        this.setIntensity(intensity);
        this.setRatingInfo(ratingInfo);
        this.setCharacter(character);
    }


    /**
     * This constructor should only be called when the MurmurRating is created for the first time
     */
    public MurmurRating(User user, HeartSound heartSound, CardiacPhase cardiacPhase, AddedSounds addedSounds, ChangeWithBreathing changeWithBreathing,
                        LeftLateralPosition leftLateralPosition, MostIntenseLocation mostIntenseLocation, MurmurDuration murmurDuration, Radiation radiation,
                        S1 s1, S2 s2, SittingForward sittingForward, Valsalva valsalva, FinalDiagnosis finalDiagnosis, Intensity intensity, String ratingInfo,
                        List<CHARACTER> character) {
        //deviceId can also be checked for but is skipped for now

        //The first argument needs to be obtained from the current logged in user.
        //How to validate if the correct heart sound object (to whom the murmur rating belongs) is passed?
        this(user.getId(), heartSound.getId(), cardiacPhase, addedSounds, changeWithBreathing, leftLateralPosition, mostIntenseLocation, murmurDuration,
                radiation, s1, s2, sittingForward, valsalva, finalDiagnosis, intensity, ratingInfo, character);
    }

    //Getters and Setters
    public final long getHeartSoundId() {
        return heartSoundId;
    }

    public final long getDoctorId() {
        return doctorId;
    }

    public final long getId() {
        return id;
    }

    public final List<CHARACTER> getCharacter() {
        return character;
    }

    public final void setCharacter(List<CHARACTER> character) {
        this.character = character;
    }

    public final String getRatingInfo() {
        return ratingInfo;
    }

    public final void setRatingInfo(String ratingInfo) {
        this.ratingInfo = ratingInfo;
    }

    public final Intensity getIntensity() {
        return intensity;
    }

    public final void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }

    public final FinalDiagnosis getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public final void setFinalDiagnosis(FinalDiagnosis finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
    }

    public final Valsalva getValsalva() {
        return valsalva;
    }

    public final void setValsalva(Valsalva valsalva) {
        this.valsalva = valsalva;
    }

    public final SittingForward getSittingForward() {
        return sittingForward;
    }

    public final void setSittingForward(SittingForward sittingForward) {
        this.sittingForward = sittingForward;
    }

    public final S2 getS2() {
        return s2;
    }

    public final void setS2(S2 s2) {
        this.s2 = s2;
    }

    public final S1 getS1() {
        return s1;
    }

    public final void setS1(S1 s1) {
        this.s1 = s1;
    }

    public final Radiation getRadiation() {
        return radiation;
    }

    public final void setRadiation(Radiation radiation) {
        this.radiation = radiation;
    }

    public final MurmurDuration getMurmurDuration() {
        return murmurDuration;
    }

    public final void setMurmurDuration(MurmurDuration murmurDuration) {
        this.murmurDuration = murmurDuration;
    }

    public final MostIntenseLocation getMostIntenseLocation() {
        return mostIntenseLocation;
    }

    public final void setMostIntenseLocation(MostIntenseLocation mostIntenseLocation) {
        this.mostIntenseLocation = mostIntenseLocation;
    }

    public final LeftLateralPosition getLeftLateralPosition() {
        return leftLateralPosition;
    }

    public final void setLeftLateralPosition(LeftLateralPosition leftLateralPosition) {
        this.leftLateralPosition = leftLateralPosition;
    }

    public final ChangeWithBreathing getChangeWithBreathing() {
        return changeWithBreathing;
    }

    public final void setChangeWithBreathing(ChangeWithBreathing changeWithBreathing) {
        this.changeWithBreathing = changeWithBreathing;
    }

    public final AddedSounds getAddedSounds() {
        return addedSounds;
    }

    public final void setAddedSounds(AddedSounds addedSounds) {
        this.addedSounds = addedSounds;
    }

    public final CardiacPhase getCardiacPhase() {
        return cardiacPhase;
    }

    public final void setCardiacPhase(CardiacPhase cardiacPhase) {
        this.cardiacPhase = cardiacPhase;
    }


}
