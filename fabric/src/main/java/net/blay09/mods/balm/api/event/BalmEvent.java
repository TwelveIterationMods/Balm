package net.blay09.mods.balm.api.event;

// TODO Cleanup: Not sure how to move this to shared without Fabric needing Forge's Event class.
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
