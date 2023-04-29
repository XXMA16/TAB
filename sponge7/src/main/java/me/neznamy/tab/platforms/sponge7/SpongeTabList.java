package me.neznamy.tab.platforms.sponge7;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.neznamy.tab.shared.chat.IChatBaseComponent;
import me.neznamy.tab.shared.platform.TabList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.property.ProfileProperty;

import java.util.UUID;

@RequiredArgsConstructor
public class SpongeTabList implements TabList {

    /** Player this TabList belongs to */
    private final SpongeTabPlayer player;

    @Override
    public void removeEntry(@NonNull UUID entry) {
        player.getPlayer().getTabList().removeEntry(entry);
    }

    @Override
    public void updateDisplayName(@NonNull UUID id, @Nullable IChatBaseComponent displayName) {
        player.getPlayer().getTabList().getEntry(id).ifPresent(
                entry -> entry.setDisplayName(displayName == null ? null : Sponge7TAB.getTextCache().get(displayName, player.getVersion())));
    }

    @Override
    public void updateLatency(@NonNull UUID id, int latency) {
        player.getPlayer().getTabList().getEntry(id).ifPresent(entry -> entry.setLatency(latency));
    }

    @Override
    public void updateGameMode(@NonNull UUID id, int gameMode) {
        player.getPlayer().getTabList().getEntry(id).ifPresent(
                entry -> entry.setGameMode(convertGameMode(gameMode)));
    }

    @Override
    public void addEntry(@NonNull TabList.Entry entry) {
        GameProfile profile = GameProfile.of(entry.getUniqueId(), entry.getName());
        if (entry.getSkin() != null) profile.getPropertyMap().put(TEXTURES_PROPERTY, ProfileProperty.of(
                TEXTURES_PROPERTY, entry.getSkin().getValue(), entry.getSkin().getSignature()));
        player.getPlayer().getTabList().addEntry(org.spongepowered.api.entity.living.player.tab.TabListEntry.builder()
                .list(player.getPlayer().getTabList())
                .profile(profile)
                .latency(entry.getLatency())
                .gameMode(convertGameMode(entry.getGameMode()))
                .displayName(entry.getDisplayName() == null ? null : Sponge7TAB.getTextCache().get(entry.getDisplayName(), player.getVersion()))
                .build());
    }

    @Override
    public void setPlayerListHeaderFooter(@NonNull IChatBaseComponent header, @NonNull IChatBaseComponent footer) {
        player.getPlayer().getTabList().setHeaderAndFooter(
                Sponge7TAB.getTextCache().get(header, player.getVersion()),
                Sponge7TAB.getTextCache().get(footer, player.getVersion())
        );
    }

    private GameMode convertGameMode(int mode) {
        switch (mode) {
            case 0: return GameModes.SURVIVAL;
            case 1: return GameModes.CREATIVE;
            case 2: return GameModes.ADVENTURE;
            case 3: return GameModes.SPECTATOR;
            default: return GameModes.NOT_SET;
        }
    }
}
