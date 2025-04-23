package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class Melee extends Skill {
    public Melee() {
        super(SkillType.MELEE);
    }

    @Override
    public List<String> getHoverInfo() {
        List<String> info = new ArrayList<>(super.getHoverInfo());
        info.add(ChatUtil.color("&bTrained by: Attacking aggressive mobs with a melee weapon"));
        info.add(ChatUtil.color("&3Increases your melee mastery"));
        return info;
    }
}
