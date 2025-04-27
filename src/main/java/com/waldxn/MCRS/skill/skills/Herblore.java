package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class Herblore extends Skill {
    public Herblore() {
        super(SkillType.HERBLORE);
    }

    @Override
    public List<Component> getHoverInfo() {
        List<Component> info = new ArrayList<>(super.getHoverInfo());
        info.add(Component.text("Trained by: Brewing potions in a brewing stand", NamedTextColor.AQUA));
        info.add(Component.text("Increases herblore mastery", NamedTextColor.DARK_AQUA));
        return info;
    }
}