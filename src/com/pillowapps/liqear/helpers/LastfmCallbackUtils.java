package com.pillowapps.liqear.helpers;

import com.pillowapps.liqear.entities.LastfmResponse;
import com.pillowapps.liqear.network.callbacks.LastfmCallback;
import com.pillowapps.liqear.network.callbacks.SimpleCallback;

public class LastfmCallbackUtils {
    public static LastfmCallback<LastfmResponse> createTransitiveCallback(final SimpleCallback<Object> callback) {
        return new LastfmCallback<LastfmResponse>() {
            @Override
            public void success(LastfmResponse data) {
                callback.success(data);
            }

            @Override
            public void failure(String error) {
                callback.failure(error);
            }
        };
    }

}
