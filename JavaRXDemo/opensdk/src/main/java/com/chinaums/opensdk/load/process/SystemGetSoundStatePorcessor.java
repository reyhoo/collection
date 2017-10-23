package com.chinaums.opensdk.load.process;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.util.JsonUtils;

/**
 * 获取系统静音状态的API
 */
public class SystemGetSoundStatePorcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_GET_SOUND_STATE;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            public void run() {
                AudioManager mAudioManager = (AudioManager) model.getActivity()
                        .getSystemService(Context.AUDIO_SERVICE);
                String state = mAudioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC) > 0 ? "voiced"
                        : "muted";
                Sound sound = new Sound();
                sound.setSoundState(state);
                setRespAndCallWeb(model,
                        createSuccessResponse(JsonUtils.convert2Json(sound)));
            }
        });
    }

    private class Sound {
        @SuppressWarnings("unused")
        private String soundState;

        public void setSoundState(String state) {
            soundState = state;
        }
    }
}
