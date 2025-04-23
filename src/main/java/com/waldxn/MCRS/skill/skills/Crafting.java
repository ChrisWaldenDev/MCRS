package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class Crafting extends Skill {
    public Crafting() {
        super(SkillType.CRAFTING);
    }

    @Override
    public List<String> getHoverInfo() {
        List<String> info = new ArrayList<>(super.getHoverInfo());
        info.add(ChatUtil.color("&bTrained by: Crafting items and blocks"));
        info.add(ChatUtil.color("&3Increases crafting mastery"));
        return info;
    }
}
