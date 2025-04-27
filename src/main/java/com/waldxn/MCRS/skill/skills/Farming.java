package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class Farming extends Skill {
    public Farming() {
        super(SkillType.FARMING);
    }

    @Override
    public List<Component> getHoverInfo() {
        List<Component> info = new ArrayList<>(super.getHoverInfo());
        info.add(Component.text("Trained by: Harvesting various crops", NamedTextColor.AQUA));
        info.add(Component.text("Increases farming mastery", NamedTextColor.DARK_AQUA));
        return info;
    }
}
