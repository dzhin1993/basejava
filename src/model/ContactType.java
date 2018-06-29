package model;

public enum ContactType {
    PHONE("Тел."),
    MAIL("Skype"),
    SKYPE("Почта"),
    LINKED_IN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");


    ContactType(String title) {
        this.title = title;
    }

    String title;

    public String getTitle() {
        return title;
    }

}
