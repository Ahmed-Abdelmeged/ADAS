/*
 * Copyright (c) 2017 Ahmed-Abdelmeged
 *
 * github: https://github.com/Ahmed-Abdelmeged
 * email: ahmed.abdelmeged.vm@gamil.com
 * Facebook: https://www.facebook.com/ven.rto
 * Twitter: https://twitter.com/A_K_Abd_Elmeged
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.mego.adas.videos.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Thumbnails {
    @SerializedName("default")
    @Expose
    private VideoPhoto _default;
    @SerializedName("medium")
    @Expose
    private VideoPhoto medium;
    @SerializedName("high")
    @Expose
    private VideoPhoto high;
    @SerializedName("standard")
    @Expose
    private VideoPhoto standard;
    @SerializedName("maxres")
    @Expose
    private VideoPhoto maxres;

    public VideoPhoto getDefault() {
        return _default;
    }

    public void setDefault(VideoPhoto _default) {
        this._default = _default;
    }

    public VideoPhoto getMedium() {
        return medium;
    }

    public void setMedium(VideoPhoto medium) {
        this.medium = medium;
    }

    public VideoPhoto getHigh() {
        return high;
    }

    public void setHigh(VideoPhoto high) {
        this.high = high;
    }

    public VideoPhoto getStandard() {
        return standard;
    }

    public void setStandard(VideoPhoto standard) {
        this.standard = standard;
    }

    public VideoPhoto getMaxres() {
        return maxres;
    }

    public void setMaxres(VideoPhoto maxres) {
        this.maxres = maxres;
    }
}
