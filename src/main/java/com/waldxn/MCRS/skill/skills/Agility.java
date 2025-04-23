package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class Agility extends Skill {
    public Agility() {
        super(SkillType.AGILITY);
    }

    @Override
    public List<String> getHoverInfo() {
        List<String> info = new ArrayList<>(super.getHoverInfo());
        info.add(ChatUtil.color("&bTrained by: Sprinting, jumping and climbing "));
        info.add(ChatUtil.color("&3Increases movement mastery"));
        return info;
    }
}
