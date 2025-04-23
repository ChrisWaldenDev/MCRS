package com.waldxn.MCRS.skill.skills;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class Hitpoints extends Skill {
    public Hitpoints() {
        super(SkillType.HITPOINTS);
    }

    @Override
    public List<String> getHoverInfo() {
        List<String> info = new ArrayList<>(super.getHoverInfo());
        info.add(ChatUtil.color("&bTrained by: Doing damage to mobs"));
        //TODO: Currently does not increase health
        info.add(ChatUtil.color("&3Increases your max health"));
        return info;
    }
}
