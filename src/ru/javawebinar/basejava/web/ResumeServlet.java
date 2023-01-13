package ru.javawebinar.basejava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {

//    private static final Storage STORAGE = Config.getInstance().getStorage();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String name = request.getParameter("name");
        response.getWriter().write(name == null ? "Hello Resumes!" : "Hello " + name + "!");

//        List<Resume> allResumes = STORAGE.getAllSorted();
//        for (Resume resume : allResumes) {
//            response.getWriter().write("<table>\n" +
//                    "  <tr>\n" +
//                    "    <td>" + resume.getUuid() + "</td>\n" +
//                    "  </tr>\n" +
//                    "  <tr>\n" +
//                    "    <td>" + resume.getFullName() + "</td>\n" +
//                    "  </tr>\n" +
//                    "</table>");
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
