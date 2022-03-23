package com.ssonsh.refactoringstudy._02_duplicated_code.pull_up_method;


import java.io.IOException;

public class ReviewerDashboard extends Dashboard {

    public void printReviewers() throws IOException{
        printUsernames(30);
    }

}
