package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class Agility extends Skill {
    public Agility() {
        super(SkillType.AGILITY);
    }

    @Override
    public List<Component> getHoverInfo() {
        List<Component> info = new ArrayList<>(super.getHoverInfo());
        info.add(Component.text("Trained by: Sprinting, jumping and climbing", NamedTextColor.AQUA));
        info.add(Component.text("Increases movement mastery", NamedTextColor.DARK_AQUA));
        return info;
    }
}
