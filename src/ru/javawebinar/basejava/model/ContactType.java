package ru.javawebinar.basejava.model;

public enum ContactType {

    TELEPHONE ("Тел."),
    SKYPE ("Профиль Skype") {
        @Override
        public String toHtmlNotNull(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    EMAIL ("Почта") {
        @Override
        public String toHtmlNotNull(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    LINKEDIN ("Профиль LinkedIn"),
    GITHUB ("Профиль GitHub"),
    STACKOVERFLOW ("Профиль StackOverflow"),
    HOME_PAGE("Домашняя страница");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toHtmlNotNull(String value) {
        return title + ": " + value;
    }

    public String toHtml(String value) {
        return value == null ? "" : toHtmlNotNull(value);
    }
}
