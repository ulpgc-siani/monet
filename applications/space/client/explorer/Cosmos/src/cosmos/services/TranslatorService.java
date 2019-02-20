package cosmos.services;

public interface TranslatorService  {

    String getCurrentLanguage();
    String translate(Object name);
    String translateHTML(String html);
}
