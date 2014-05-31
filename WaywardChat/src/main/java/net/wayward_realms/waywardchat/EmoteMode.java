package net.wayward_realms.waywardchat;

public enum EmoteMode {

    ASTERISK_START("\\*.*"), ASTERISK_END(".*\\*"), TWO_ASTERISKS("\\*.*\\*");

    private String regex;

    private EmoteMode(String regex) {
        this.regex = regex;
    }

    public boolean isEmote(String message) {
        return message.matches(regex);
    }

}
