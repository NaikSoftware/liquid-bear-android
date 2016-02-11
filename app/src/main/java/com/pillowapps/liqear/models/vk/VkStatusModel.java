package com.pillowapps.liqear.models.vk;

import com.pillowapps.liqear.callbacks.VkSimpleCallback;
import com.pillowapps.liqear.entities.Track;
import com.pillowapps.liqear.entities.vk.VkResponse;
import com.pillowapps.liqear.helpers.TrackUtils;
import com.pillowapps.liqear.helpers.VkCallbackUtils;
import com.pillowapps.liqear.network.service.VkApiService;

public class VkStatusModel {
    private VkApiService vkService;

    public VkStatusModel(VkApiService api) {
        this.vkService = api;
    }

    public void updateStatus(Track track, final VkSimpleCallback<VkResponse> callback) {
        if (TrackUtils.vkInfoAvailable(track)) {
            String audio = track.getOwnerId() + "_" + track.getAudioId();
            vkService.updateStatus(audio, VkCallbackUtils.getTransitiveCallback(callback));
        } else {
            vkService.setAudioStatusWithSearch(TrackUtils.getNotation(track), VkCallbackUtils.getTransitiveCallback(callback));
        }
    }
}

