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


package com.example.mego.adas.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mego.adas.R;

import java.util.ArrayList;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.BluetoothAdapterViewHolder> {

    private ArrayList<String> mDevices = new ArrayList<>();

    private final BluetoothDevicesAdapterOnClickHandler mClickHandling;

    @Override
    public BluetoothAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_device;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BluetoothAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BluetoothAdapterViewHolder holder, int position) {
        holder.deviceName.setText(mDevices.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDevices == null) return 0;
        return mDevices.size();
    }

    public interface BluetoothDevicesAdapterOnClickHandler {
        void onClick(String address);
    }

    public BluetoothDevicesAdapter(BluetoothDevicesAdapterOnClickHandler bluetoothDevicesAdapterOnClickHandler) {
        this.mClickHandling = bluetoothDevicesAdapterOnClickHandler;
    }

    public class BluetoothAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView deviceName;

        private BluetoothAdapterViewHolder(View view) {
            super(view);
            deviceName = (TextView) view.findViewById(R.id.device_name_textView);
        }

        @Override
        public void onClick(View v) {
            if (mDevices != null) {
                mClickHandling.onClick(mDevices.get(getAdapterPosition()));
            }
        }
    }

    public void setDevice(String device) {
        this.mDevices.add(device);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDevices != null) {
            this.mDevices.clear();
            notifyDataSetChanged();
        }
    }
}
