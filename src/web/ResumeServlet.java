package web;

import model.*;
import storage.SqlStorage;
import util.Config;
import util.DateUtil;
import util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private static SqlStorage sqlStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sqlStorage = Config.getInstance().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        final boolean isCreate = (uuid == null || uuid.length() == 0);
        Resume r;
        if (isCreate) {
            r = new Resume(fullName);
            sqlStorage.save(r);
        } else {
            r = sqlStorage.get(uuid);
            r.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (HtmlUtil.isEmpty(value) && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.setSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.setSection(type, new ListSection(Arrays.asList(value.split("\n"))));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Company> companies = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                String pfx = type.name() + i;
                                List<Company.Post> posts = new ArrayList<>();
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] titles = request.getParameterValues(pfx + "title");
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        posts.add(new Company.Post(titles[j], DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), descriptions[j]));
                                    }
                                }
                                Company company = new Company(name, urls[i]);
                                company.setPostList(posts);
                                companies.add(company);
                            }
                        }
                        r.setSection(type, new CompanySection(companies));
                        break;
                }
            }
        }
        sqlStorage.update(r);
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", sqlStorage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                sqlStorage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "add":
                r = Resume.EMPTY;
                break;
            case "view":
            case "edit":
                r = sqlStorage.get(uuid);
                for (SectionType sectionType : SectionType.values()) {
                    Section section = r.getSection(sectionType);
                    switch (sectionType) {
                        case PERSONAL:
                        case OBJECTIVE:
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            List<Company> emptyFirstCompanies = new ArrayList<>();
                            emptyFirstCompanies.add(Company.EMPTY);
                            if (section != null) {
                                for (Company company : ((CompanySection) section).getCompanies()) {
                                    List<Company.Post> emptyFirstPosts = new ArrayList<>();
                                    emptyFirstPosts.add(Company.Post.EMPTY);
                                    emptyFirstPosts.addAll(company.getPostList());
                                    emptyFirstCompanies.add(new Company(company.getLink(), emptyFirstPosts));
                                }
                            }
                            section = new CompanySection(emptyFirstCompanies);
                            break;
                    }
                    r.setSection(sectionType, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}
