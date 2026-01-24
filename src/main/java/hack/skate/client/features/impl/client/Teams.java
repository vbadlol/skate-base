package hack.skate.client.features.impl.client;

import hack.skate.client.features.Feature;
import hack.skate.client.features.settings.impl.BoolSetting;
import hack.skate.client.features.Category;
import hack.skate.client.features.FeatureInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;

@FeatureInfo(name = "Teams", description = "Adds team checks for other features.", category = Category.CLIENT)
public class Teams extends Feature {
    public BoolSetting colorCheck = new BoolSetting("Color Check", true);
    public BoolSetting nameCheck = new BoolSetting("Name Check", false);
    public BoolSetting scoreboardCheck = new BoolSetting("Scoreboard Check", true);

    public boolean isOnSameTeam(Entity entity) {
        if (!(entity instanceof PlayerEntity) || !this.isEnabled()) {
            return false;
        }

        PlayerEntity player = (PlayerEntity) entity;

        if (colorCheck.getValue()) {
            String playerName = player.getDisplayName().getString();
            String selfName = mc.player.getDisplayName().getString();
            if (playerName.startsWith("§") && selfName.startsWith("§")) {
                return playerName.charAt(1) == selfName.charAt(1);
            }
        }

        if (nameCheck.getValue()) {
            String playerName = player.getDisplayName().getString();
            String selfName = mc.player.getDisplayName().getString();
            int bracketIndex = playerName.lastIndexOf('[');
            if (bracketIndex != -1) {
                String playerTeam = playerName.substring(bracketIndex);
                return selfName.endsWith(playerTeam);
            }
        }

        if (scoreboardCheck.getValue()) {
            Team playerTeam = mc.world.getScoreboard().getScoreHolderTeam(player.getName().getString());
            Team selfTeam = mc.world.getScoreboard().getScoreHolderTeam(mc.player.getName().getString());
            return playerTeam != null && playerTeam.equals(selfTeam);
        }

        return false;
    }
}
