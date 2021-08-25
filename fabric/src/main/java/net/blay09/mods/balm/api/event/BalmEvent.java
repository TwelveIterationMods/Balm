package net.blay09.mods.balm.api.event;

public class BalmEvent implements BalmEventContract {
    private boolean canceled;

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
