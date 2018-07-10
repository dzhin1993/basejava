package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static util.DateUtil.NOW;
import static util.DateUtil.of;

public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Link link;
    private List<Post> postList = new ArrayList<>();

    public Company(String name, String url, Post... posts) {
        this(new Link(name, url), Arrays.asList(posts));
    }

    private Company(Link link, List<Post> postList) {
        this.link = link;
        this.postList = postList;
    }

    public Link getLink() {
        return link;
    }

    public List<Post> getPostList() {
        return postList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(link, company.link) &&
                Objects.equals(postList, company.postList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, postList);
    }

    @Override
    public String toString() {
        return "Company{" +
                "link=" + link +
                ", postList=" + postList +
                '}';
    }

    public static class Post implements Serializable{
        private final String position;
        private final LocalDate startWork;
        private final LocalDate endWork;
        private String description;

        public Post(String position, int startYear, Month startMonth, String description){
            this(position, of(startYear, startMonth), NOW, description);
        }

        public Post(String position, int startYear, Month startMonth, int endYear, Month endMonth, String description) {
            this(position, of(startYear, startMonth), of(endYear, endMonth), description);
        }

        private Post(String position, LocalDate startWork, LocalDate endWork, String description) {
            Objects.requireNonNull(position, "position must not be null");
            Objects.requireNonNull(startWork, "startWork must not be null");
            Objects.requireNonNull(endWork, "endWork must not be null");
            this.position = position;
            this.startWork = startWork;
            this.endWork = endWork;
            this.description = description;
        }

        public String getPosition() {
            return position;
        }

        public LocalDate getStartWork() {
            return startWork;
        }

        public LocalDate getEndWork() {
            return endWork;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Post post = (Post) o;
            return Objects.equals(position, post.position) &&
                    Objects.equals(startWork, post.startWork) &&
                    Objects.equals(endWork, post.endWork) &&
                    Objects.equals(description, post.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, startWork, endWork, description);
        }

        @Override
        public String toString() {
            return "Post{" +
                    "position='" + position + '\'' +
                    ", startWork=" + startWork +
                    ", endWork=" + endWork +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}