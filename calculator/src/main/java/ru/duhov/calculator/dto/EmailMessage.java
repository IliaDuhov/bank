package ru.duhov.calculator.dto;

import ru.duhov.calculator.dto.enums.Theme;

//TODO is it necessary here?
public class EmailMessage {

    private String address;

    private Theme theme;

    private Long statementId;

    private String text;
}