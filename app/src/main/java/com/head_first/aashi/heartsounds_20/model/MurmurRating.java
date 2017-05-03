package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.AddedSounds;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.CardiacPhase;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.ChangeWithBreathing;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.CHARACTER;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.FinalDiagnosis;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Intensity;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.LeftLateralPosition;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.MostIntenseLocation;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.MurmurDuration;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Radiation;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.S1;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.S2;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.SittingForward;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Valsalva;

import java.util.Date;
import java.util.List;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class MurmurRating {

    private long MurmurRatingID;
    private String DoctorID;
    private long HeartSoundID;
    //enums
    private CardiacPhase CardiacPhase;
    private AddedSounds AddedSounds;
    private ChangeWithBreathing ChangeWithBreathing;
    private LeftLateralPosition LeftLateralPosition;
    private MostIntenseLocation LocationMostIntense;
    private MurmurDuration DurationOfMurmur;
    private Radiation Radiation;
    private S1 S1;
    private S2 S2;
    private SittingForward SittingForward;
    private Valsalva Valsalva;

    //check these ones
    private FinalDiagnosis finalDiagnosis;
    private Intensity Intensity;
    private String RatingInfo;

    private String Character;//this has 3 checkboxes..can the user select multiple checkboxes. If so we will use an array
    private Date CreatedOn;
    private boolean IsActive;

    public static Long getIdFromString(String murmurRatingString){
        if(murmurRatingString == null){
            return null;
        }
        else if(murmurRatingString.matches("Murmur Rating [0-9]+")){
            return Long.parseLong(murmurRatingString.split(" ")[2]) - 1;
        }
        return null;
    }

    public MurmurRating(String doctorID, long heartSoundID){
        this.setDoctorID(doctorID);
        this.setHeartSoundID(heartSoundID);
    }

    //Getters
    public long getMurmurRatingID() {
        return MurmurRatingID;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public long getHeartSoundID() {
        return HeartSoundID;
    }

    public CardiacPhase getCardiacPhase() {
        return CardiacPhase;
    }

    public AddedSounds getAddedSounds() {
        return AddedSounds;
    }

    public ChangeWithBreathing getChangeWithBreathing() {
        return ChangeWithBreathing;
    }

    public LeftLateralPosition getLeftLateralPosition() {
        return LeftLateralPosition;
    }

    public MostIntenseLocation getLocationMostIntense() {
        return LocationMostIntense;
    }

    public MurmurDuration getDurationOfMurmur() {
        return DurationOfMurmur;
    }

    public Radiation getRadiation() {
        return Radiation;
    }

    public S1 getS1() {
        return S1;
    }

    public S2 getS2() {
        return S2;
    }

    public SittingForward getSittingForward() {
        return SittingForward;
    }

    public Valsalva getValsalva() {
        return Valsalva;
    }

    public FinalDiagnosis getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public Intensity getIntensity() {
        return Intensity;
    }

    public String getRatingInfo() {
        return RatingInfo;
    }

    public String getCharacter() {
        return Character;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public boolean isActive() {
        return IsActive;
    }

    //Setters
    public void setDoctorID(String doctorID) {
        boolean validDoctorID = User.isValidUserId(doctorID);
        if(validDoctorID){
            this.DoctorID = doctorID;
        }
    }

    public void setHeartSoundID(long heartSoundID) {
        HeartSoundID = heartSoundID;
    }

    public void setCardiacPhase(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.CardiacPhase cardiacPhase) {
        CardiacPhase = cardiacPhase;
    }

    public void setAddedSounds(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.AddedSounds addedSounds) {
        AddedSounds = addedSounds;
    }

    public void setChangeWithBreathing(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.ChangeWithBreathing changeWithBreathing) {
        ChangeWithBreathing = changeWithBreathing;
    }

    public void setLeftLateralPosition(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.LeftLateralPosition leftLateralPosition) {
        LeftLateralPosition = leftLateralPosition;
    }

    public void setLocationMostIntense(MostIntenseLocation locationMostIntense) {
        LocationMostIntense = locationMostIntense;
    }

    public void setDurationOfMurmur(MurmurDuration durationOfMurmur) {
        DurationOfMurmur = durationOfMurmur;
    }

    public void setRadiation(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Radiation radiation) {
        Radiation = radiation;
    }

    public void setS1(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.S1 s1) {
        S1 = s1;
    }

    public void setS2(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.S2 s2) {
        S2 = s2;
    }

    public void setSittingForward(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.SittingForward sittingForward) {
        SittingForward = sittingForward;
    }

    public void setValsalva(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Valsalva valsalva) {
        Valsalva = valsalva;
    }

    public void setFinalDiagnosis(FinalDiagnosis finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
    }

    public void setIntensity(com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Intensity intensity) {
        Intensity = intensity;
    }

    public void setRatingInfo(String ratingInfo) {
        RatingInfo = ratingInfo;
    }

    public void setCharacter(String character) {
        Character = character;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    @Override
    public String toString(){
        return "Murmur Rating " + (getMurmurRatingID() + 1);
    }

    /**
     *This constructor should be used when gettin data from the database
     */
//    public MurmurRating(long DoctorID, long HeartSoundID, CardiacPhase CardiacPhase, AddedSounds AddedSounds, ChangeWithBreathing ChangeWithBreathing,
//                        LeftLateralPosition LeftLateralPosition, MostIntenseLocation LocationMostIntense, MurmurDuration DurationOfMurmur, Radiation Radiation,
//                        S1 S1, S2 S2, SittingForward SittingForward, Valsalva Valsalva, FinalDiagnosis finalDiagnosis, Intensity Intensity, String RatingInfo,
//                        List<CHARACTER> Character) {
//        //The MurmurRatingID will be received from the data base/
//        // /this.MurmurRatingID = (ID.add(BigInteger.ONE)).toString();
//        this.DoctorID = DoctorID;
//        this.HeartSoundID = HeartSoundID;
//        this.setCardiacPhase(CardiacPhase);
//        this.setAddedSounds(AddedSounds);
//        this.setChangeWithBreathing(ChangeWithBreathing);
//        this.setLeftLateralPosition(LeftLateralPosition);
//        this.setMostIntenseLocation(LocationMostIntense);
//        this.setMurmurDuration(DurationOfMurmur);
//        this.setRadiation(Radiation);
//        this.setS1(S1);
//        this.setS2(S2);
//        this.setSittingForward(SittingForward);
//        this.setValsalva(Valsalva);
//        this.setFinalDiagnosis(finalDiagnosis);
//        this.setIntensity(Intensity);
//        this.setRatingInfo(RatingInfo);
//        this.setCharacter(Character);
//    }


    /**
     * This constructor should only be called when the MurmurRating is created for the first time
     */
//    public MurmurRating(User user, HeartSound heartSound, CardiacPhase CardiacPhase, AddedSounds AddedSounds, ChangeWithBreathing ChangeWithBreathing,
//                        LeftLateralPosition LeftLateralPosition, MostIntenseLocation LocationMostIntense, MurmurDuration DurationOfMurmur, Radiation Radiation,
//                        S1 S1, S2 S2, SittingForward SittingForward, Valsalva Valsalva, FinalDiagnosis finalDiagnosis, Intensity Intensity, String RatingInfo,
//                        List<CHARACTER> Character) {
//        //deviceId can also be checked for but is skipped for now
//
//        //The first argument needs to be obtained from the current logged in user.
//        //How to validate if the correct heart sound object (to whom the murmur rating belongs) is passed?
////        this(user.getPatientID(), heartSound.getPatientID(), CardiacPhase, AddedSounds, ChangeWithBreathing, LeftLateralPosition, LocationMostIntense, DurationOfMurmur,
////                Radiation, S1, S2, SittingForward, Valsalva, finalDiagnosis, Intensity, RatingInfo, Character);
//    }

//    //Setters
//    public final void setRatingInfo(String RatingInfo) {
//        this.RatingInfo = RatingInfo;
//    }
//
//    public final void setIntensity(Intensity Intensity) {
//        this.Intensity = Intensity;
//    }
//
//    public final void setFinalDiagnosis(FinalDiagnosis finalDiagnosis) {
//        this.finalDiagnosis = finalDiagnosis;
//    }
//
//    public final void setValsalva(Valsalva Valsalva) {
//        this.Valsalva = Valsalva;
//    }
//
//    public final void setSittingForward(SittingForward SittingForward) {
//        this.SittingForward = SittingForward;
//    }
//    public final void setS2(S2 S2) {
//        this.S2 = S2;
//    }
//
//    public final void setS1(S1 S1) {
//        this.S1 = S1;
//    }
//
//    public final void setRadiation(Radiation Radiation) {
//        this.Radiation = Radiation;
//    }
//
//    public final void setMurmurDuration(MurmurDuration DurationOfMurmur) {
//        this.DurationOfMurmur = DurationOfMurmur;
//    }
//    public final void setMostIntenseLocation(MostIntenseLocation LocationMostIntense) {
//        this.LocationMostIntense = LocationMostIntense;
//    }
//    public final void setLeftLateralPosition(LeftLateralPosition LeftLateralPosition) {
//        this.LeftLateralPosition = LeftLateralPosition;
//    }
//    public final void setChangeWithBreathing(ChangeWithBreathing ChangeWithBreathing) {
//        this.ChangeWithBreathing = ChangeWithBreathing;
//    }
//    public final void setAddedSounds(AddedSounds AddedSounds) {
//        this.AddedSounds = AddedSounds;
//    }
//
//    public final void setCardiacPhase(CardiacPhase CardiacPhase) {
//        this.CardiacPhase = CardiacPhase;
//    }


}
