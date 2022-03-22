package com.ssonsh.refactoringstudy._01_smell_mysterious_name._01_before;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudyDashboard {

    private Set<StudyReview> studyReviews = new HashSet<>();

    /**
     * 스터디 리뷰 이슈에 작성되어 있는 리뷰어 목록과 리뷰를 읽어온다.
     * @throws IOException
     */
    private void loadReviews() throws IOException {
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(30);

        List<GHIssueComment> reviews = issue.getComments();
        for (GHIssueComment review : reviews) {

            // AS-IS
            /*
            reviewers.add(review.getUserName());
            this.reviews.add(review.getBody());
             */

            // TO-BE. use Record
            studyReviews.add(new StudyReview(review.getUserName(), review.getBody()));
        }
    }

    public Set<StudyReview> getStudyReviews(){
        return studyReviews;
    }

    public static void main(String[] args) throws IOException {
        StudyDashboard studyDashboard = new StudyDashboard();
        studyDashboard.loadReviews();
        // AS-IS
        /*
        studyDashboard.getReviewers().forEach(name -> System.out.println(name));
        studyDashboard.getReviews().forEach(review -> System.out.println(review));
         */

        // TO-BE
        studyDashboard.getStudyReviews().forEach(System.out::println);
    }
}
