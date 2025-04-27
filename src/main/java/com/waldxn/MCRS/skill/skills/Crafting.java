package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class Crafting extends Skill {
    public Crafting() {
        super(SkillType.CRAFTING);
    }

    @Override
    public List<Component> getHoverInfo() {
        List<Component> info = new ArrayList<>(super.getHoverInfo());
        info.add(Component.text("Trained by: Crafting items and blocks", NamedTextColor.AQUA));
        info.add(Component.text("Increases crafting mastery", NamedTextColor.DARK_AQUA));
        return info;
    }
}
