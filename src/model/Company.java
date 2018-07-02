package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Company {
    private final Link link;
    private final List<Post> postList = new ArrayList<>();

    public Company(String name, String url) {
        this.link = new Link(name, url);
    }

    public void addPost(String position, LocalDate startWork, LocalDate endWork, String description) {
        this.postList.add(new Post(position, startWork, endWork, description));
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

    private class Post {
        private final String position;
        private final LocalDate startWork;
        private final LocalDate endWork;
        private String description;

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