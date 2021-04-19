package com.example.questionnaire.model;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.questionnaire.utils.MyLinkedMap;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.model.Models.AnswerSessions.ANSWER_DB;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionSession.QUESTION_SESSION_DB;


public class Models {

    public static String HY = "-";
    public static String keyValueSep = "|";
    public static String sep1 = "{";
    public static String sep2 = "}";
    public static String primary_secondary_sep = "^";
    public static String fullSecondDateTime = Calendar.getInstance().getTime().toString();
    public static String fullDateTime = truncate(Calendar.getInstance().getTime().toString(), 16);
    public static String time = fullDateTime.substring(11);
    public static String date = truncate(fullDateTime, 11);
    public static String MAID_SUR = "MD";
    public static String CUST_SUR = "CT";
    public static final String PRIMARY = "Primary";
    public static final String SECONDARY = "Secondary";

    public static final String WORK_MAID = "Maid";
    public static final String WORK_CUSTOMER = "Customer";

    @Entity(tableName = QUESTION_DB)
    public static class QuestionClass implements Serializable {
        @PrimaryKey
        @NotNull
        private String questionId;
        public static final String QUESTION_ID = "questionId";
        private int workForceType;
        public static final String WORK_FORCE_TYPE = "workForceType";
        private String questionType;
        public static final String QUESTION_TYPE = "questionType";
        private String primaryQuestion;
        public static final String PRIMARY_QUESTION = "primaryQuestion";
        private String secondaryQuestion;
        public static final String SECONDARY_QUESTION = "secondaryQuestion";

        private String createdAt;
        public static final String CREATED_AT = "createdAt";
        private String lastModifiedAt;
        public static final String LAST_MODIFIED = "lastModifiedAt";
        private String closedAnswerYes;
        public static final String CLOSED_ANSWER_YES = "closedAnswerYes";
        private String closedAnswerNo;
        public static final String CLOSED_ANSWER_NO = "closedAnswerNo";
        private String primaryAnswer;
        public static final String PRIMARY_ANSWER = "primaryAnswer";
        private String secondaryAnswer;
        public static final String SECONDARY_ANSWER = "secondaryAnswer";

        public static final String OPEN_QUESTION = "Open Question";
        public static final String CLOSED_QUESTION = "Closed Question";
        public static final String RATING_QUESTION = "Rating Question";
        public static final String CONDITION_QUESTION_OPEN = "Condition Question Open";
        public static final String CONDITION_QUESTION_CLOSED = "Condition Question Closed";

        public static final String QUESTION_DB = "Questions";

        public QuestionClass(@NotNull String questionId) {
            this.questionId = questionId;
        }

        public @NotNull String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(@NotNull String questionId) {
            this.questionId = questionId;
        }

        public int getWorkForceType() {
            return workForceType;
        }

        public void setWorkForceType(int workForceType) {
            this.workForceType = workForceType;
        }

        public String getQuestionType() {
            return questionType;
        }

        public void setQuestionType(String questionType) {
            this.questionType = questionType;
        }

        public String getPrimaryQuestion() {
            return primaryQuestion;
        }

        public void setPrimaryQuestion(String primaryQuestion) {
            this.primaryQuestion = primaryQuestion;
        }

        public String getSecondaryQuestion() {
            return secondaryQuestion;
        }

        public void setSecondaryQuestion(String secondaryQuestion) {
            this.secondaryQuestion = secondaryQuestion;
        }


        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getLastModifiedAt() {
            return lastModifiedAt;
        }

        public void setLastModifiedAt(String lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
        }

        public String getClosedAnswerYes() {
            return closedAnswerYes;
        }

        public void setClosedAnswerYes(String closedAnswerYes) {
            this.closedAnswerYes = closedAnswerYes;
        }

        public String getClosedAnswerNo() {
            return closedAnswerNo;
        }

        public void setClosedAnswerNo(String closedAnswerNo) {
            this.closedAnswerNo = closedAnswerNo;
        }

        public String getPrimaryAnswer() {
            return primaryAnswer;
        }

        public void setPrimaryAnswer(String primaryAnswer) {
            this.primaryAnswer = primaryAnswer;
        }

        public String getSecondaryAnswer() {
            return secondaryAnswer;
        }

        public void setSecondaryAnswer(String secondaryAnswer) {
            this.secondaryAnswer = secondaryAnswer;
        }
    }

    @Entity(tableName = ANSWER_DB)
    public static class AnswerSessions {
        @PrimaryKey
        @NotNull
        private String questionId;
        private String primaryAnswer;
        public static final String PRIMARY_ANSWER = "primaryAnswer";
        private String secondaryAnswer;
        public static final String SECONDARY_ANSWER = "secondaryAnswer";

        public static final String ANSWER_DB = "Answers";

        public AnswerSessions(@NotNull String questionId) {
            this.questionId = questionId;
        }

        public @NotNull String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(@NotNull String questionId) {
            this.questionId = questionId;
        }

        public String getPrimaryAnswer() {
            return primaryAnswer;
        }

        public void setPrimaryAnswer(String primaryAnswer) {
            this.primaryAnswer = primaryAnswer;
        }

        public String getSecondaryAnswer() {
            return secondaryAnswer;
        }

        public void setSecondaryAnswer(String secondaryAnswer) {
            this.secondaryAnswer = secondaryAnswer;
        }

    }

    @Entity(tableName = QUESTION_SESSION_DB)
    public static class QuestionSession implements Serializable {
        @NotNull
        @PrimaryKey
        private String sessionId;
        public static final String SESSION_ID = "sessionId";
        private String workForceType;
        private String createdAt;
        private String lastModified;
        public static final String LAST_MODIFIED_SESSION = "lastModified";
        private String workForceAgentName;
        public static final String WORK_FORCE_AGENT_NAME = "workForceAgentName";
        private String workForceAgentAge;
        public static final String WORK_FORCE_AGENT_Age = "workForceAgentAge";
        private String answerListMap;
        public static final String ANSWER_LIST_MAP = "answerListMap";


        public static final String QUESTION_SESSION_DB = "Question_Sessions";

        public QuestionSession(@NotNull String sessionId) {
            this.sessionId = sessionId;
        }

        public String getAnswerListMap() {
            return answerListMap;
        }

        public void setAnswerListMap(String answerListMap) {
            this.answerListMap = answerListMap;
        }

        public @NotNull String getSessionId() {
            return sessionId;
        }

        public void setSessionId(@NotNull String sessionId) {
            this.sessionId = sessionId;
        }

        public String getWorkForceType() {
            return workForceType;
        }

        public void setWorkForceType(String workForceType) {
            this.workForceType = workForceType;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getLastModified() {
            return lastModified;
        }

        public void setLastModified(String lastModified) {
            this.lastModified = lastModified;
        }

        public String getWorkForceAgentName() {
            return workForceAgentName;
        }

        public void setWorkForceAgentName(String workForceAgentName) {
            this.workForceAgentName = workForceAgentName;
        }

        public String getWorkForceAgentAge() {
            return workForceAgentAge;
        }

        public void setWorkForceAgentAge(String workForceAgentAge) {
            this.workForceAgentAge = workForceAgentAge;
        }


    }

    public static MyLinkedMap<String, String> getMapFromString(String s) {
        MyLinkedMap<String, String> map = new MyLinkedMap<>();
        if (s == null) {
            return map;
        } else if (s.equals("")) {
            return map;
        } else {
            String key = "";
            String value = "";
            String entry = "";

            if (s.equals("")) {
                return map;
            } else { //if sep1 > start, if : > 1st word is key, if } > 2nd word is value ... end word
                boolean keyFound = false;
                for (int i = 0; i <= s.length() - 1; i++) {
                    if (String.valueOf(s.charAt(i)).equals(sep1)) {
                        System.out.println("Start of map");
                    } else if (String.valueOf(s.charAt(i)).equals(sep2)) {
                        value = entry;
                        keyFound = false;
                        map.put(key, value);
                        entry = "";
                        System.out.println("End : entry is " + key + keyValueSep + value);
                    } else {

                        if (String.valueOf(s.charAt(i)).equals(keyValueSep)) { //(key:value) //reached key

                            if (!keyFound) {
                                key = entry;
                                entry = "";
                                keyFound = true;
                                System.out.println("key is " + key);
                            }


                        } else {
                            entry = entry.concat(String.valueOf(s.charAt(i))); //add word
                        }
                    }
                }
            }
        }
        return map;
    }

    public static MyLinkedMap<String, String> getAnswer1Answer2(String s) {
        MyLinkedMap<String, String> map = new MyLinkedMap<>();
        //  answer 1^answer 2
        //if ^ ans 1 is found, ans 2 looking... if } ans 2 is found


        if (s == null || s.equals("")) {
            return map;
        } else {
            String ans1 = "";
            String ans2 = "";
            String entry = "";

            if (s.equals("")) {
                return map;
            } else {
                boolean ans1found = false;

                for (int i = 0; i <= s.length() - 1; i++) {
                    if (String.valueOf(s.charAt(i)).equals(primary_secondary_sep)) { //answer 1 is found
                        ans1found = true;
                        ans1 = entry;
                        entry = "";
                    } else {
                        entry = entry.concat(String.valueOf(s.charAt(i)));
                        System.out.println("answer characters "+entry);

                        if (ans1found) {
                            if (i == s.length() - 1) { // end of ans 2
                                ans1found = true;
                                ans2 = entry;
                                map.put(ans1, ans2);
                            }
                        }

                    }
                }

            }
        }

        return map;
    }

    public static MyLinkedMap<String, String> mapFromListWithDifferentKeys(String[] keyList, String[] list) {
        MyLinkedMap<String, String> map = new MyLinkedMap<>();
        if (list.length == 0 || keyList.length == 0) {
            System.out.println("map is empty");
            return map;
        } else {
            for (int i = 0; i <= list.length - 1; i++) {
                map.put(keyList[i], list[i]);
                System.out.println("key is " + keyList[i] + "map is " + list[i]);
            }
        }
        return map;
    }

    /*public static ArrayList<AnswerSessions> getAnswerSessionsFromString () {

    }*/

    public static String getStringListFromMap(MyLinkedMap<String, String> map) {
        String mapString = "";
        if (map.isEmpty()) {
            return mapString;

        } else {
            for (Map.Entry<String, String> pair : map.entrySet()) {
                mapString = mapString.concat(sep1.concat(pair.getKey()).concat(keyValueSep).concat(pair.getValue()).concat(sep2));
                System.out.println("entry " + mapString);
            }
        }

        return mapString;
    }

    public static String getQuestionId (int type) {
        if (type == MAID) {
            return Calendar.getInstance().getTime().toString().concat(HY).concat(MAID_SUR);
        } else {
            return Calendar.getInstance().getTime().toString().concat(HY).concat(CUST_SUR);
        }
    }

    public static String getSessionId(int type , String name) {
        if (type == MAID) {
            return Calendar.getInstance().getTime().toString().concat(HY).concat(name).concat(HY).concat(MAID_SUR);
        } else {
            return Calendar.getInstance().getTime().toString().concat(HY).concat(name).concat(HY).concat(CUST_SUR);
        }
    }

    public static String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }

    public static SpannableStringBuilder underlineString(String s) {
        SpannableStringBuilder underlined = new SpannableStringBuilder(s);
        underlined.setSpan(new UnderlineSpan(), 0, s.length(), 0);
        return underlined;
    }

    public static SpannableStringBuilder strikeString(String s) {
        SpannableStringBuilder stricken = new SpannableStringBuilder(s);
        stricken.setSpan(new StrikethroughSpan(), 0, s.length(), 0);
        return stricken;
    }

    public static SpannableStringBuilder boldString(String s) {
        SpannableStringBuilder boldness = new SpannableStringBuilder(s);
        boldness.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        return boldness;
    }

    public static SpannableStringBuilder italicString(String s) {
        SpannableStringBuilder mamamia = new SpannableStringBuilder(s);
        mamamia.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
        return mamamia;
    }

    public static String getRightAnswer(String primaryAnswer , String secondaryAnswer, String ansPicked) {
        if (primaryAnswer.equals(ansPicked)) {
            return PRIMARY;
        } else if (secondaryAnswer.equals(ansPicked)){
            return SECONDARY;
        } else {
            return SECONDARY;
        }
    }

    public static void isLoadingData(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void stopLoadingData(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    public static void isUploadingData (ProgressBar progressBar, Button button) {
        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);
    }

    public static void failedUploadingData(ProgressBar progressBar, Button button) {
        progressBar.setVisibility(View.GONE);
        button.setEnabled(true);
    }
}
