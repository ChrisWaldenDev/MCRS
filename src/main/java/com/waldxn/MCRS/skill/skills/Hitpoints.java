package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class Hitpoints extends Skill {
    public Hitpoints() {
        super(SkillType.HITPOINTS);
    }

    @Override
    public List<Component> getHoverInfo() {
        List<Component> info = new ArrayList<>(super.getHoverInfo());
        info.add(Component.text("Trained by: Doing damage to mobs", NamedTextColor.AQUA));
        info.add(Component.text("Increases your max health", NamedTextColor.DARK_AQUA));
        return info;
    }
}
