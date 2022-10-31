package ru.javawebinar.basejava.model;

public enum ContactType {

    TELEPHONE ("Тел."),
    SKYPE ("Профиль Skype"),
    EMAIL ("Почта"),
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
}
