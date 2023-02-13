package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalStateException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("allResumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume resume;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view" -> resume = storage.get(uuid);
            case "save" -> resume = Resume.DEFAULT;
            case "edit" -> {
                resume = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = resume.getSection(type);
                    switch (type) {
                        case OBJECTIVE, PERSONAL -> {
                            if (section == null) {
                                section = TextSection.DEFAULT;
                            }
                        }
                        case ACHIEVEMENT, QUALIFICATIONS -> {
                            if (section == null) {
                                section = ListSection.DEFAULT;
                            }
                        }
                        case EXPERIENCE, EDUCATION -> {
                            OrganizationSection organizationSection = (OrganizationSection) section;
                            List<Organization> organizations = new ArrayList<>();
                            organizations.add(Organization.DEFAULT);
                            if (organizationSection != null) {
                                for (Organization organization : organizationSection.get()) {
                                    List<Organization.Period> periods = new ArrayList<>();
                                    periods.add(Organization.Period.DEFAULT);
                                    periods.addAll(organization.getPeriods());
                                    organizations.add(new Organization(organization.getName(), organization.getWebsite(), periods));
                                }
                            }
                            section = new OrganizationSection(organizations);
                        }
                    }
                    resume.addSection(type, section);
                }
            }
            default -> throw new IllegalStateException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        if (fullName.equals("")) {
            response.sendRedirect("resume");
            return;
        }

        Resume resume;
        boolean exist;

        if (uuid != null && !uuid.equals("")) {
            exist = true;
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        } else {
            exist = false;
            resume = new Resume(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String section = request.getParameter(type.name());

            if (section == null && section.equals("")) {
                resume.getSections().remove(type);
                break;
            }

            switch (type) {
                case PERSONAL, OBJECTIVE -> {
                    TextSection textSection = new TextSection(section);
                    resume.addSection(type, textSection);
                }
                case ACHIEVEMENT, QUALIFICATIONS -> {
                    List<String> listTmp = new ArrayList<>();
                    String[] strings = section.split("\n");
                    for (String s : strings) {
                        if (!s.equals("\r")) {
                            listTmp.add(s);
                        }
                    }
                    ListSection listSection = new ListSection(listTmp);
                    resume.addSection(type, listSection);
                }
                case EXPERIENCE, EDUCATION -> {
                    List<Organization> organizations = new ArrayList<>();
                    String[] values = request.getParameterValues(type.name());
                    String[] websites = request.getParameterValues(type.name() + "website");
                    for (int i = 0; i < values.length; i++) {
                        String name = values[i];
                        if (name != null && !name.equals("null") && !name.equals("")) {
                            List<Organization.Period> periods = new ArrayList<>();
                            String periodCounter = type.name() + i;
                            String[] startDates = request.getParameterValues(periodCounter + "startDate");
                            String[] endDates = request.getParameterValues(periodCounter + "endDate");
                            String[] titles = request.getParameterValues(periodCounter + "title");
                            String[] descriptions = request.getParameterValues(periodCounter + "description");

                            for (int j = 0; j < titles.length; j++) {
                                if (titles[j] != null && !titles[j].equals("null") && !titles[j].equals("")) {
                                    periods.add(new Organization.Period(titles[j], descriptions[j], DateUtil.stringToLocalDate(startDates[j]), DateUtil.stringToLocalDate(endDates[j])));
                                }
                            }
                            organizations.add(new Organization(name, websites[i], periods));
                        }
                    }
                    resume.addSection(type, new OrganizationSection(organizations));
                }
            }
        }

        if (exist) {
            storage.update(resume);
        } else {
            storage.save(resume);
        }
        response.sendRedirect("resume");
    }
}
