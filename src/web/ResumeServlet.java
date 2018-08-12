package web;

import model.ContactType;
import model.Resume;
import storage.SqlStorage;
import util.Config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private static SqlStorage sqlStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sqlStorage = Config.getInstance().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        /*String name = request.getParameter("name");
        response.getWriter().write(name == null ? "Hello Resumes!" : "Hello " + name + '!');*/
        PrintWriter writer = response.getWriter();
        writer.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "<link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "    <title>Отображение резюме</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table border=\"1\">\n");
        List<Resume> resumes = sqlStorage.getAllSorted();
        for (Resume resume : resumes) {
            writer.write("<tr>\n" +
                    "<td>" + "<header>" + resume.getUuid() + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getFullName() + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getContact(ContactType.MAIL) + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getContact(ContactType.PHONE) + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getContact(ContactType.SKYPE) + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getContact(ContactType.HOMEPAGE) + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getContact(ContactType.GITHUB) + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getContact(ContactType.STACKOVERFLOW) + "</header>" + "</td>\n" +
                    "<td>" + "<header>" + resume.getContact(ContactType.LINKED_IN) + "</header>" + "</td>\n" +
                    "</tr>\n"
            );
        }
        writer.write("</table>" +
                "</body>" +
                "</html>");
    }
}
