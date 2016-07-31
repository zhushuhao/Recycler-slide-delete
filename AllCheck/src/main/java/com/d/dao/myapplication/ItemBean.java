package com.d.dao.myapplication;

import java.io.Serializable;

/**
 * Created by dao on 7/31/16.
 */
public class ItemBean implements Serializable{
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
