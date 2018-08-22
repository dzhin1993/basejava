package web;

import model.*;
import storage.SqlStorage;
import util.Config;
import util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        Resume r = sqlStorage.get(uuid);
        r.setFullName(fullName);
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
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    r.setSection(type, new TextSection(value));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    r.setSection(type, new ListSection(Arrays.asList(value.split("\n"))));
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    String[] companyNames = request.getParameterValues(type + "companyName");
                    String[] companyURL = request.getParameterValues(type + "url");
                    List<Company> companies = new ArrayList<>();
                    for (int i = 0; i < companyNames.length; i++) {
                        if (companyNames[i].isEmpty()) {
                            continue;
                        }
                        String companyID = type + companyNames[i];
                        Company company = new Company(companyNames[i], companyURL[i]);
                        String[] positions = request.getParameterValues(companyID + "position");
                        String[] descriptions = request.getParameterValues(companyID + "description");
                        String[] datesStart = request.getParameterValues(companyID + "startWork");
                        String[] datesEnd = request.getParameterValues(companyID + "endWork");
                        List<Company.Post> posts = new ArrayList<>();
                        if (positions != null) {
                            for (int k = 0; k < positions.length; k++) {
                                Company.Post post = new Company.Post(positions[k],
                                        DateUtil.of(datesStart[k]),
                                        DateUtil.of(datesEnd[k]),
                                        descriptions[k]);
                                posts.add(post);
                            }
                            company.setPostList(posts);
                        }
                        companies.add(company);
                    }
                    if(!companies.isEmpty()){
                        CompanySection companySection = new CompanySection();
                        companySection.setCompanies(companies);
                        r.setSection(type, companySection);
                    }
                    break;
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
            case "view":
            case "edit":
                r = sqlStorage.get(uuid);
                for (SectionType sectionType : SectionType.values()) {
                    Section section = r.getSection(sectionType);
                    if (section == null) {
                        if (sectionType == SectionType.PERSONAL || sectionType == SectionType.OBJECTIVE) {
                            r.setSection(sectionType, TextSection.TEXT_EMPTY);
                        } else if (sectionType == SectionType.ACHIEVEMENT || sectionType == SectionType.QUALIFICATIONS) {
                            r.setSection(sectionType, ListSection.LIST_EMPTY);
                        } else {
                            r.setSection(sectionType, CompanySection.COMPANY_EMPTY);
                        }
                    }
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
