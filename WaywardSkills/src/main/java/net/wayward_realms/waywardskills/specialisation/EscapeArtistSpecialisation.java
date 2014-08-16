package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class EscapeArtistSpecialisation extends SpecialisationBase {

    public EscapeArtistSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Escape Artist";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
