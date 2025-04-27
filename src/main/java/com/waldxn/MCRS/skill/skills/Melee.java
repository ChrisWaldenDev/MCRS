package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class Melee extends Skill {
    public Melee() {
        super(SkillType.MELEE);
    }

    @Override
    public List<Component> getHoverInfo() {
        List<Component> info = new ArrayList<>(super.getHoverInfo());
        info.add(Component.text("Trained by: Attacking aggressive mobs with a melee weapon", NamedTextColor.AQUA));
        info.add(Component.text("Increases your melee mastery", NamedTextColor.DARK_AQUA));
        return info;
    }
}
