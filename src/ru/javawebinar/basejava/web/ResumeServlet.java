package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            case "view", "edit" -> resume = storage.get(uuid);
            case "save" -> {
                saveResume(new Resume(), request);
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
        Resume resume = storage.get(uuid);
        resume.setFullName(fullName);

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            if (type.name().equals("EXPERIENCE") || type.name().equals("EDUCATION")) {
                break;
            }
            resume.getSections().remove(type);
            String[] section = request.getParameterMap().get(type.name());
            if (section == null) {
                resume.getSections().remove(type);
            } else if (type.name().equals("PERSONAL") || type.name().equals("OBJECTIVE")) {
                TextSection textSection = new TextSection(section[0]);
                resume.addSection(type, textSection);
            } else if (type.name().equals("ACHIEVEMENT") || type.name().equals("QUALIFICATIONS")){
                ListSection listSection = new ListSection(section);
                resume.addSection(type, listSection);
            }
        }

        storage.update(resume);
        response.sendRedirect("resume");
    }

    protected void saveResume(Resume resume, HttpServletRequest request) {
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            }
        }

        for (SectionType type : SectionType.values()) {
            resume.getSections().remove(type);
            String section = Arrays.toString(request.getParameterMap().get(type.name()));
            if (section.trim().length() != 0) {
                resume.addSection(type, JsonParser.read(section, AbstractSection.class));
            }
        }

        storage.save(resume);
    }
}
