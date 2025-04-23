package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class Construction extends Skill {
    public Construction() {
        super(SkillType.CONSTRUCTION);
    }

    @Override
    public List<String> getHoverInfo() {
        List<String> info = new ArrayList<>(super.getHoverInfo());
        info.add(ChatUtil.color("&bTrained by: Placing blocks"));
        info.add(ChatUtil.color("&3Increases building mastery"));
        return info;
    }
}
