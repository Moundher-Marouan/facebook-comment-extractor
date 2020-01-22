package DragonAnalyzer;

public class PersonComment {
    private String profile = null;
    private String plink = null;
    private String comment = null;
    private String percentage = null;

    public PersonComment() {
    }

    public PersonComment(String profile, String pLink, String comment, String percentage) {
        this.profile = profile;
        this.plink = pLink;
        this.comment = comment;
        this.percentage = percentage;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPlink() {
        return plink;
    }

    public void setPlink(String pLink) {
        this.plink = pLink;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
