package io.github.craftizz.mambiance.ambiance;

public class SoundData {

    private int soundId;
    private int repeatIn;

    public SoundData(final int soundId,
                     final int repeatIn) {
        this.soundId = soundId;
        this.repeatIn = repeatIn;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public boolean shouldRepeat() {
        return repeatIn-- > 0;
    }

    public int getRepeatIn() {
        return repeatIn;
    }

    public void setRepeatIn(int repeatIn) {
        this.repeatIn = repeatIn;
    }

    public void addRepeatIn(int repeatIn) {
        this.repeatIn += repeatIn;
    }
}
