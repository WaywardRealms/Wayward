package net.wayward_realms.waywardmonsters.bleed;

import org.bukkit.entity.LivingEntity;

import java.lang.ref.WeakReference;

public class BleedingEntity {

    private WeakReference<LivingEntity> entity;
    private int timeleft;

    public BleedingEntity(LivingEntity entity, int timeleft) {
        this.entity = new WeakReference<>(entity);
        this.timeleft = timeleft;
    }

    public LivingEntity getEntity() {
        return entity.get();
    }

    public int reduceTimeleft(final int interval) {
        timeleft -= interval;
        return timeleft;
    }

}
