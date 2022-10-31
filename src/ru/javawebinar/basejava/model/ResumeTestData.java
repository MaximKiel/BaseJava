package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume resume = new Resume("Григорий Кислин");

        resume.addContact(ContactType.TELEPHONE, "+7(921) 855-0482");
        resume.addContact(ContactType.SKYPE, "skype:grigory.kislin");
        resume.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        resume.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.addContact(ContactType.HOME_PAGE, "http://gkislin.ru/");

        AbstractSection objective = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web " +
                "и Enterprise технологиям");
        resume.addSection(SectionType.OBJECTIVE, objective);

        AbstractSection personal = new TextSection("Аналитический склад ума, сильная логика, креативность, " +
                "инициативность. Пурист кода и архитектуры.");
        resume.addSection(SectionType.PERSONAL, personal);

        List<String> achievementItems = new ArrayList<>();
        achievementItems.add("Реализация протоколов по приему платежей всех основных платежных системы России " +
                "(Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        achievementItems.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, " +
                "Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievementItems.add("Реализация двухфакторной аутентификации для онлайн платформы управления " +
                "проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        AbstractSection achievement = new ListSection(achievementItems);
        resume.addSection(SectionType.ACHIEVEMENT, achievement);

        List<String> qualificationsItems = new ArrayList<>();
        qualificationsItems.add("Родной русский, английский \"upper intermediate\"");
        qualificationsItems.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектирования, " +
                "архитектурных шаблонов, UML, функционального программирования");
        qualificationsItems.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, " +
                "SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, " +
                "LDAP, OAuth1, OAuth2, JWT.");
        AbstractSection qualifications = new ListSection(qualificationsItems);
        resume.addSection(SectionType.QUALIFICATIONS, qualifications);

        List<Organization> experienceOrganizations = new ArrayList<>();
        experienceOrganizations.add(new Organization("Alcatel",
                "http://www.alcatel.ru/",
                "Инженер по аппаратному и программному тестированию",
                "Тестирование, отладка, внедрение ПО цифровой телефонной станции " +
                        "Alcatel 1000 S12 (CHILL, ASM).",
                LocalDate.of(1997, 9, 1),
                LocalDate.of(2005, 1, 1)));
        experienceOrganizations.add(new Organization("Siemens AG",
                "https://www.siemens.com/ru/ru/home.html",
                "Разработчик ПО",
                "Разработка информационной модели, проектирование интерфейсов, реализация " +
                        "и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).",
                LocalDate.of(2005, 1, 1),
                LocalDate.of(2007, 2, 1)));
        experienceOrganizations.add(new Organization("Enkata",
                "http://enkata.com/",
                "Разработчик ПО",
                "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) " +
                        "частей кластерного J2EE приложения (OLAP, Data mining).",
                LocalDate.of(2007, 3, 1),
                LocalDate.of(2008, 6, 1)));
        AbstractSection experience = new OrganizationSection(experienceOrganizations);
        resume.addSection(SectionType.EXPERIENCE, experience);

        List<Organization> educationOrganizations = new ArrayList<>();
        educationOrganizations.add(new Organization("Заочная физико-техническая школа при МФТИ",
                "http://www.school.mipt.ru/",
                "Закончил с отличием",
                null,
                LocalDate.of(1984, 9, 1),
                LocalDate.of(1987, 6, 1)));
        educationOrganizations.add(new Organization("Luxoft",
                "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'",
                null,
                LocalDate.of(2011, 3, 1),
                LocalDate.of(2011, 4, 1)));
        educationOrganizations.add(new Organization("Coursera",
                "https://www.coursera.org/course/progfun",
                "'Functional Programming Principles in Scala' by Martin Odersky",
                null,
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1)));
        AbstractSection education = new OrganizationSection(educationOrganizations);
        resume.addSection(SectionType.EDUCATION, education);

        System.out.println(ContactType.TELEPHONE.getTitle() + ": " + resume.getContact(ContactType.TELEPHONE));
        System.out.println(ContactType.SKYPE.getTitle() + ": " + resume.getContact(ContactType.SKYPE));
        System.out.println(ContactType.EMAIL.getTitle() + ": " + resume.getContact(ContactType.EMAIL));
        System.out.println(ContactType.LINKEDIN.getTitle() + ": " + resume.getContact(ContactType.LINKEDIN));
        System.out.println(ContactType.GITHUB.getTitle() + ": " + resume.getContact(ContactType.GITHUB));
        System.out.println(ContactType.STACKOVERFLOW.getTitle() + ": " + resume.getContact(ContactType.STACKOVERFLOW));
        System.out.println(ContactType.HOME_PAGE.getTitle() + ": " + resume.getContact(ContactType.HOME_PAGE) + "\n");

        System.out.println(SectionType.OBJECTIVE.getTitle() + ":\n" + resume.getSection(SectionType.OBJECTIVE) + "\n");
        System.out.println(SectionType.PERSONAL.getTitle() + ":\n" + resume.getSection(SectionType.PERSONAL) + "\n");
        System.out.println(SectionType.ACHIEVEMENT.getTitle() + ":\n" +
                resume.getSection(SectionType.ACHIEVEMENT) + "\n");
        System.out.println(SectionType.QUALIFICATIONS.getTitle() + ":\n" +
                resume.getSection(SectionType.QUALIFICATIONS) + "\n");
        System.out.println(SectionType.EXPERIENCE.getTitle() + "\n" +
                resume.getSection(SectionType.EXPERIENCE) + "\n");
        System.out.println(SectionType.EDUCATION.getTitle() + ":\n" + resume.getSection(SectionType.EDUCATION) + "\n");
    }
}
