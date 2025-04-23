package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class Cooking extends Skill {
    public Cooking() {
        super(SkillType.COOKING);
    }

    @Override
    public List<String> getHoverInfo() {
        List<String> info = new ArrayList<>(super.getHoverInfo());
        info.add(ChatUtil.color("&bTrained by: Cooking and crafting food"));
        info.add(ChatUtil.color("&3Increases cooking mastery"));
        return info;
    }
}
