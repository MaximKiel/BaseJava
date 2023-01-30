package ru.javawebinar.basejava.model;

import java.time.Month;

public class ResumeTestData {

    public Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

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

        AbstractSection achievement = new ListSection(
                "Реализация протоколов по приему платежей всех основных платежных системы России " +
                        "(Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, " +
                        "Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления " +
                        "проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk."
        );
        resume.addSection(SectionType.ACHIEVEMENT, achievement);

        AbstractSection qualifications = new ListSection(
                "Родной русский, английский 'upper intermediate'",
                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектирования, " +
                        "архитектурных шаблонов, UML, функционального программирования",
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, " +
                        "SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, " +
                        "BPMN2, LDAP, OAuth1, OAuth2, JWT."
        );
        resume.addSection(SectionType.QUALIFICATIONS, qualifications);

        AbstractSection experience = new OrganizationSection(
                new Organization("Alcatel", "http://www.alcatel.ru/",
                        new Organization.Period("Инженер по аппаратному и программному тестированию", "Тестирование, отладка, внедрение ПО цифровой телефонной станции ",
                                1997, Month.SEPTEMBER, 2005, Month.JANUARY)),
                new Organization("Siemens AG", "https://www.siemens.com/ru/ru/home.html",
                        new Organization.Period("Разработчик ПО", "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).",
                                2005, Month.JANUARY, 2007, Month.FEBRUARY)),
                new Organization("Enkata", "http://enkata.com/",
                        new Organization.Period("Разработчик ПО", "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining).",
                                2007, Month.MARCH, 2008, Month.JUNE)),
                new Organization("Java Online Projects", "http://javaops.ru/",
                        new Organization.Period("Автор проекта", "Создание, организация и проведение Java онлайн проектов и стажировок",
                                2013, Month.OCTOBER))
        );
        resume.addSection(SectionType.EXPERIENCE, experience);

        AbstractSection education = new OrganizationSection(
                new Organization("Заочная физико-техническая школа при МФТИ", "http://www.school.mipt.ru/",
                        new Organization.Period("Закончил с отличием", null,
                                1984, Month.SEPTEMBER, 1987, Month.JUNE)),
                new Organization("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "http://www.ifmo.ru/",
                        new Organization.Period("Инженер (программист Fortran, C)", null,
                                1987, Month.SEPTEMBER, 1993, Month.JULY),
                        new Organization.Period("Аспирантура (программист С, С++)", null,
                                1993, Month.SEPTEMBER, 1996, Month.JULY)),
                new Organization("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                        new Organization.Period("Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", null,
                                2011, Month.MARCH, 2011, Month.APRIL)),
                new Organization("Coursera", "https://www.coursera.org/course/progfun",
                        new Organization.Period("'Functional Programming Principles in Scala' by Martin Odersky", null,
                                2013, Month.MARCH, 2013, Month.MAY))
        );
        resume.addSection(SectionType.EDUCATION, education);

        return resume;
    }
}
