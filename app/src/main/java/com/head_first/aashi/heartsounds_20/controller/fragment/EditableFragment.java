package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Aashish Indorewala on 06-Feb-17.
 */

public abstract class EditableFragment extends Fragment{
    protected boolean editMode;

    protected abstract void editFragment();
    protected abstract void saveChanges();
    public abstract void cancelChanges();
    protected abstract void showEditableViews();
    protected abstract void showNonEditableViews();
    protected abstract void hideNonEditableViews();
    protected abstract void hideEditableViews();
    protected abstract void showActionBarMenuItems();
    public abstract boolean editModeEnabled();
    protected abstract void makeViewsEditable();
    protected abstract void makeViewsUneditable();
}
