package net.waterraid.KitAbilities.Abilities;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.AttackStrategy;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.CloneTargetEffect;
import net.waterraid.KitAbilities.Effects.DurationEffects.NoTargetEffect;
import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class Clone extends RightClickAbilities {
    {
        setUpAbilityAndItem(25, 3, Material.QUARTZ);
    }
    Player target;
    NPC npc;
    public Clone(Main instance, Player player) {
        super(instance, player);
    }

    @Override
    protected void doAbility(LivingEntity entity) {
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, getPlayer().getName());
        npc.spawn(getPlayer().getLocation());
        npc.data().setPersistent(NPC.DEFAULT_PROTECTED_METADATA, false);

        npc.addTrait(Equipment.class);
        Equipment e = npc.getTrait(Equipment.class);
        e.set(Equipment.EquipmentSlot.BOOTS, getPlayer().getInventory().getBoots());
        e.set(Equipment.EquipmentSlot.LEGGINGS, getPlayer().getInventory().getLeggings());
        e.set(Equipment.EquipmentSlot.CHESTPLATE, getPlayer().getInventory().getChestplate());
        e.set(Equipment.EquipmentSlot.HELMET, getPlayer().getInventory().getHelmet());
        e.set(Equipment.EquipmentSlot.HAND, getPlayer().getInventory().getItem(0));
        npc.getNavigator().getLocalParameters().attackRange(5.5);
        npc.getEntity().setMetadata("CLONE", new FixedMetadataValue(getPlugin(), "true"));
        new BukkitRunnable(){
            @Override
            public void run(){
                if(npc.getEntity() != null && !npc.getEntity().isDead()) {
                    Location loc = npc.getEntity().getLocation();
                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SPELL_INSTANT, false, (float) (loc.getX()), (float) (loc.getY())+0.5F, (float) loc.getZ(), 0.15f, 0.7f, 0.15f, 0, 500);

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }
                npc.destroy();
            }
        }.runTaskLater(getPlugin(), secondsToTicks(getCastTime()));
        if(isTargeteableLivingEntity(target)) {
            Effect effect = new CloneTargetEffect(getPlugin(), target, npc, getCastTime());
            effect.setFrom(getPlayer());
            effect.applyEffect();
        }
        Effect effect = new NoTargetEffect(getPlugin(), getPlayer(), getCastTime()/2d);
        effect.setFrom(getPlayer());
        effect.applyEffect();

    }
    @Override
    public void onEvent(EntityDamageByEntityEvent event){
        if(event.getEntity().getUniqueId().equals(getPlayer().getUniqueId())){
            if(event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player){
                target = (Player)((Projectile) event.getDamager()).getShooter();
            }else if(event.getDamager() instanceof Player){
                target = (Player)event.getDamager();
            }
        }
    }
}