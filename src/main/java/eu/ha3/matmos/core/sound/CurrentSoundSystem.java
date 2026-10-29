package eu.ha3.matmos.core.sound;

import paulscode.sound.SoundSystem;

/** The sound system SoundManager last created, recorded by MixinSoundSystemStarterThread. */
public final class CurrentSoundSystem {
    private static volatile SoundSystem current;

    private CurrentSoundSystem() {}

    public static void set(SoundSystem system) {
        current = system;
    }

    public static SoundSystem get() {
        return current;
    }
}
