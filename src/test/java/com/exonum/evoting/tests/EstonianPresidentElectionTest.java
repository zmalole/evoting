package com.exonum.evoting.tests;

import com.exonum.evoting.base.BaseTest;
import com.exonum.evoting.components.Pin2Component;
import com.exonum.evoting.components.SignBallotComponent;
import com.exonum.evoting.components.VoteConfirmationComponent;
import com.exonum.evoting.enums.UnsignedBallotButton;
import com.exonum.evoting.pages.*;
import com.exonum.evoting.utils.HttpRequest;
import io.qameta.allure.*;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.testng.Assert.*;

@Log
public class EstonianPresidentElectionTest extends BaseTest {

    private static final String ESTONIAN_PRESIDENT_ELECTION = "Estonian Presidential Election";
    private static final String EIKI_NESTOR = "Eiki Nestor";

    @Test(description = "Vote for " + EIKI_NESTOR + " and email verification")
    @Severity(SeverityLevel.BLOCKER)
    public void eikiNestor() {
        MainPage mainPage = openEvoting();
        ElectionListPage electionListPage = mainPage.voteInElectionClick(ElectionListPage.class);
        electionListPage.selectFromTable(ESTONIAN_PRESIDENT_ELECTION);
        CandidatesOfElectionPage candidatesPage = electionListPage.voteInElectionClick(CandidatesOfElectionPage.class);
        assertEquals(candidatesPage.getNumberOfRows(), 4, "Incorrect number of candidates in the list");
        candidatesPage.selectFromTable(EIKI_NESTOR);
        assertEquals(candidatesPage.getCandidateInfo(), getInfoFromWikiPage(candidatesPage.getCandidateWebPageLink()),
                "Candidate info is out of date");
        VoteConfirmationComponent confirmationComponent = candidatesPage.voteInElectionClick(VoteConfirmationComponent.class, EIKI_NESTOR);
        UnsignedBallotPage unsignedBallotPage = confirmationComponent.confirm(UnsignedBallotPage.class);
        final String wordMemo = unsignedBallotPage.getBallotReciept3WordMemoFieldText();
        final String sha256 = unsignedBallotPage.getBallotSha256HashFieldText();
        verifyButtons(unsignedBallotPage);
        Pin2Component pin2Component = unsignedBallotPage.clickButton(UnsignedBallotButton.SIGN, Pin2Component.class);
        SignBallotComponent signBallotComponent = pin2Component.signBallot(getPin());
        Map<String, String> tempEmail = getTempEmail();
        signBallotComponent.setEmail(tempEmail.get("mail"));
        signBallotComponent.confirm(SubmittedPage.class);
        verifyEmail(tempEmail, wordMemo, sha256);
    }

    @Step("Navigate to wiki page and get 2 first sentences")
    private static String getInfoFromWikiPage(String wikiUrl) {
        log.info("get info from wiki page: " + wikiUrl);
        String wikiText = Jsoup.parse(HttpRequest.executePost(wikiUrl)).text();
        String targetLabel = "2015 ";
        String[] splittedText = wikiText.substring(wikiText.indexOf(targetLabel) + targetLabel.length()).split("\\. ");
        return splittedText[0] + ". " + splittedText[1] + ".";
    }

    @Step("Verify button labels")
    private void verifyButtons(UnsignedBallotPage page) {
        List<String> actualButtonLabels = page.getButtonLabels();
        List<String> expectedButtonLabels = UnsignedBallotButton.getLabels();
        log.info("verify button labels are: " + expectedButtonLabels.toString());
        assertTrue(actualButtonLabels.containsAll(expectedButtonLabels),
                actualButtonLabels + " doesn't contains all " + expectedButtonLabels);
    }

    @Step("Get pin which consists of 4 digits")
    private int[] getPin() {
        int[] pin = new int[4];
        for (int i = 0; i < pin.length; i++) {
            pin[i] = ThreadLocalRandom.current().nextInt(0, 10);
        }
        log.info("generated pin: " + Arrays.toString(pin));
        return pin;
    }

    @Step("Verify email body with mnemonic code and unique hash")
    private void verifyEmail(Map<String, String> email, String mnemonicCode, String hash) {
        String mailBody = getEmail(email);

        log.info("verify mnemonic code is: " + mnemonicCode);
        String mnemonicCodePrefix = "<p><span>MNEMONIC CODE PLAINTEXT: ";
        assertEquals(mailBody.substring(mailBody.indexOf(mnemonicCodePrefix) + mnemonicCodePrefix.length(),
                mailBody.lastIndexOf("</span></p>")), mnemonicCode, "Incorrect mnemonic code");

        log.info("verify hash code is: " + hash);
        String hashPrefix = "<p><span>HASH HEXADECIMAL: ";
        assertEquals(mailBody.substring(mailBody.indexOf(hashPrefix) + hashPrefix.length(),
                mailBody.indexOf("</span></p>")), hash, "Incorrect hash code");
    }

    @Step("Get email letter")
    private String getEmail(Map<String, String> email) {
        String token = email.get("token");
        int emailId = getNeededEmailId(token, 0);
        return getMailBody(token, emailId);
    }

}
