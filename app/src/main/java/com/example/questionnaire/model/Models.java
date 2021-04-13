package com.example.questionnaire.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.questionnaire.utils.MyLinkedMap;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.questionnaire.model.Models.AnswerSessions.ANSWER_DB;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionSession.*;


public class Models {

    static String HY = "-";
    static String keyValueSep = ":";
    static String sep1 = "{";
    static String sep2 = "}";
    static String primary_secondary_sep = "^";

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
        private String sessionId;
        public static final String SESSION_ID = "sessionId";
        private String workForceType;
        private String createdAt;
        private String lastModified;
        private String workForceAgentName;
        public static final String WORK_FORCE_AGENT_NAME = "workForceAgentName";
        private String workForceAgentAge;
        public static final String WORK_FORCE_AGENT_Age = "workForceAgentAge";
        private String answerListMap;
        public static final String ANSWER_LIST_MAP = "answerListMap";


        public static final String QUESTION_SESSION_DB = "Question_Sessions";

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
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

    static MyLinkedMap<String, String> mapFromListWithDifferentKeys(ArrayList<String> list, ArrayList<String> keyList) {
        MyLinkedMap<String, String> map = new MyLinkedMap<>();
        for (int i = 0; i <= list.size() - 1; i++) {
            map.put(list.get(i), keyList.get(i));
        }
        return map;
    }

    static MyLinkedMap<String, String> getMapFromString(String s) {
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

}
