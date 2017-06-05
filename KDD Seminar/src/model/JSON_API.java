package model;

/**
 * Created by Jan-Peter on 04.06.2017.
 */
public class JSON_API {

    String name;
    String homepage;
    String blog;
    String endpoint;

    String pwDescr;
    String blogDescr;
    String ldDescr;


    public static int counter = 0;

    public JSON_API() {
    }

    public JSON_API(String name, String homepage, String blog, String endpoint) {
        this.name = name;
        this.homepage = homepage;
        this.blog = blog;
        this.endpoint = endpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getPwDescr() {
        return pwDescr;
    }

    public void setPwDescr(String pwDescr) {
        this.pwDescr = pwDescr;
    }

    public String getBlogDescr() {
        return blogDescr;
    }

    public void setBlogDescr(String blogDescr) {
        this.blogDescr = blogDescr;
    }

    public String getLdDescr() {
        return ldDescr;
    }

    public void setLdDescr(String ldDescr) {
        this.ldDescr = ldDescr;
    }

    @Override
    public String toString() {
        if(!this.homepage.equals("") || !this.getBlog().equals("")) {

            counter++;
            return this.name + ": HP: " + this.homepage + ", Blog: " + this.blog;

        } else {
            return "---";
        }
    }
}
