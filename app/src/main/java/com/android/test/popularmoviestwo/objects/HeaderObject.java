package com.android.test.popularmoviestwo.objects;

import com.android.test.popularmoviestwo.adapters.AdapterDetails;

public class HeaderObject extends DetailDisplay{

    private String header;

    public HeaderObject() {
    }

    public HeaderObject(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int getDisplayType() {
        return AdapterDetails.DISPLAY_TYPE_HEADER;
    }
}