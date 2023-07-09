package com.tedu.element.component;

/**
 * 生命值组件
 * @author LSR
 */
public class HealthValue extends ComponentBase{
    protected int health = 1;
    protected int maxHealth = 1;
    protected boolean damageable = true;
    protected int changeNum = 0;
    protected Runnable onHealthChangeEvent = null;

    public int getHealth() {
        return health;
    }

    public HealthValue setMaxHealth(int maxHealth) {
        return this.setMaxHealth(maxHealth, false);
    }

    /**
     * 设置最大生命值，若当前血量超过所设置的最大生命值，则设置为最大生命值
     * @param maxHealth 最大生命值
     * @param fill 是否同时设置当前生命值为最大生命值
     * @return
     */
    public HealthValue setMaxHealth(int maxHealth, boolean fill ) {
        int oldMaxHealth = this.maxHealth;
        this.maxHealth = maxHealth;
        this.health = fill ? this.maxHealth : Math.min(this.maxHealth, this.health);
        this.onMaxHealthChange(this.maxHealth, oldMaxHealth);
        return this;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public HealthValue setHealth(int health) {
        if (health != this.health) {
            int oldHealth = this.health;
            this.health = Math.max(health, 0);
            this.onHealthChange(this.health, oldHealth);
        }
        return this;
    }

    public boolean isDamageable() {
        return damageable;
    }

    public void setDamageable(boolean damageable) {
        this.damageable = damageable;
    }

    public void damageBy(int damage) {
        if (!damageable)
            return;
        this.setHealth(this.getHealth() - damage);
    }

    /**
     * 当最大血量发生变化时触发
     * @param health 当前最大血量
     * @param oldHealth 旧最大血量
     */
    public void onHealthChange(int health, int oldHealth) {
        this.changeNum = oldHealth - health;
        if (onHealthChangeEvent != null) {
            onHealthChangeEvent.run();
        }
        if (health == 0) {
            this.getParent().destroy();
        }
    }

    /**
     * 当血量发生变化时触发
     * @param health 当前血量
     * @param oldMaxHealth 旧血量
     */
    public void onMaxHealthChange(int health, int oldMaxHealth) {

    }

    public void setOnHealthChangeEvent(Runnable onHealthChangeEvent) {
        this.onHealthChangeEvent = onHealthChangeEvent;
    }

    public int getChangeNum() {
        return changeNum;
    }
}
