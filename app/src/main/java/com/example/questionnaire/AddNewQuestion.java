package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.questionnaire.model.Models;
import com.example.questionnaire.questionDb.QuestionRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;
import static com.example.questionnaire.model.Models.fullDateTime;
import static com.example.questionnaire.model.Models.getQuestionId;


public class AddNewQuestion extends AppCompatActivity {

    private boolean backPressed;
    private int workForceType;
    private String questionType = "";
    private LinearLayout conditionalQuestionLayoutOpen, closedQuestionLayout, openQuestionLayout, conditionalQuestionLayoutClosed;

    public static final String Q1 = "q1";
    public static final String Q2 = "q2";
    private EditText questionFieldOpen, questionFieldClosed, option1Field, option2Field, questionFieldCondition1, questionFieldCondition2, questionFieldCondition1Closed, questionFieldCondition2Closed, option1FieldClosed, option2FieldClosed;
    private Models.QuestionClass question;
    private static Button saveQ;
    private static ProgressBar progressBar;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);

        Toolbar addQtb = findViewById(R.id.addQtb);
        setSupportActionBar(addQtb);
        addQtb.setNavigationOnClickListener(v -> finish());

        conditionalQuestionLayoutOpen = findViewById(R.id.conditionalQuestionLayoutOpen);
        closedQuestionLayout = findViewById(R.id.closedQuestionLayout);
        openQuestionLayout = findViewById(R.id.openQuestionLayout);
        conditionalQuestionLayoutClosed = findViewById(R.id.conditionalQuestionLayoutClosed);

        RadioGroup typeGroup = findViewById(R.id.typeGroup);
        typeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.maidRadio) {
                setMaidMode(addQtb);
            } else if (checkedId == R.id.customerRadio) {
                setCustomerMode(addQtb);
            }
        });

        RadioGroup questionTypeGroup = findViewById(R.id.questionTypeGroup);
        questionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                default:
                    break;

                case R.id.conditionalQuestionOpen:
                    questionType = CONDITION_QUESTION_OPEN;
                    setClosedConditionLayout();
                    break;

                case R.id.conditionalQuestionClosed:
                    questionType = CONDITION_QUESTION_CLOSED;
                    setOpenConditionLayout();
                    break;

                case R.id.ratingQuestion:
                    questionType = RATING_QUESTION;
                    setSingleAnswerLayout();
                    break;

                case R.id.closedQuestionR:
                    questionType = CLOSED_QUESTION;
                    setClosedQuestionLayout();
                    break;

                case R.id.openQuestionR:
                    questionType = OPEN_QUESTION;
                    setSingleAnswerLayout();

                    break;

            }
        });

        //Open //Rating
        questionFieldOpen = findViewById(R.id.questionFieldOpen);
        //Closed
        questionFieldClosed = findViewById(R.id.questionFieldClosed);
        option1Field = findViewById(R.id.option1Field);
        option2Field = findViewById(R.id.option2Field);

        //Con open
        questionFieldCondition1 = findViewById(R.id.questionFieldCondition1);
        questionFieldCondition2 = findViewById(R.id.questionFieldCondition2);

        //con close
        questionFieldCondition1Closed = findViewById(R.id.questionFieldCondition1Closed);
        questionFieldCondition2Closed = findViewById(R.id.questionFieldCondition2Closed);
        option1FieldClosed = findViewById(R.id.option1FieldClosed);
        option2FieldClosed = findViewById(R.id.option2FieldClosed);

        progressBar = findViewById(R.id.newQuestionPb);
        progressBar.setVisibility(View.GONE);

        saveQ = findViewById(R.id.saveQ);
        saveQ.setOnClickListener(v -> {
            if (validateForm(questionType)) {
                Models.isUploadingData(progressBar, saveQ);
                // saveQuestion(question);
                saveOnlineQuestion(question);
            } else {
                Toast.makeText(AddNewQuestion.this, "check details", Toast.LENGTH_SHORT).show();
            }
        });
        setMaidMode(addQtb);
    }


    private void setOpenConditionLayout() {
        conditionalQuestionLayoutClosed.setVisibility(View.VISIBLE);
        conditionalQuestionLayoutOpen.setVisibility(View.GONE);
        closedQuestionLayout.setVisibility(View.GONE);
        openQuestionLayout.setVisibility(View.GONE);
    }

    private void setClosedConditionLayout() {
        conditionalQuestionLayoutOpen.setVisibility(View.VISIBLE);
        closedQuestionLayout.setVisibility(View.GONE);
        openQuestionLayout.setVisibility(View.GONE);
        conditionalQuestionLayoutClosed.setVisibility(View.GONE);
    }

    private void setSingleAnswerLayout() {
        conditionalQuestionLayoutOpen.setVisibility(View.GONE);
        closedQuestionLayout.setVisibility(View.GONE);
        openQuestionLayout.setVisibility(View.VISIBLE);
        conditionalQuestionLayoutClosed.setVisibility(View.GONE);
    }

    private void setClosedQuestionLayout() {
        conditionalQuestionLayoutOpen.setVisibility(View.GONE);
        closedQuestionLayout.setVisibility(View.VISIBLE);
        openQuestionLayout.setVisibility(View.GONE);
        conditionalQuestionLayoutClosed.setVisibility(View.GONE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setMaidMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.maid);
        workForceType = MAID;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setCustomerMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list_cust));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.customer);
        workForceType = CUSTOMER;
    }

    private void setAnimatedBg(Toolbar toolbar) {
        AnimationDrawable animationDrawable = (AnimationDrawable) toolbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }


    private void addToDrafts() {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.skip_question_dialog);
        d.show();
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView skipInfo = d.findViewById(R.id.deleteInfoTv);
        skipInfo.setText("Do you want to add this question");
        Button yes = d.findViewById(R.id.yesButton), no = d.findViewById(R.id.noButton);
        no.setOnClickListener(v -> {
            d.dismiss();
            finishActivity();
        });
        yes.setOnClickListener(v -> {
            saveQuestion(question);
            d.dismiss();
        });
    }

    private boolean validateForm(String type) {
        boolean valid = false;
        switch (type) {
            default:
                break;

            case OPEN_QUESTION:
                if (questionFieldOpen.getText().toString().isEmpty()) {
                    questionFieldOpen.setError("Empty");
                    questionFieldOpen.requestFocus();
                } else {
                    question = new Models.QuestionClass(getQuestionId(workForceType));

                    question.setPrimaryQuestion(questionFieldOpen.getText().toString());

                    question.setClosedAnswerYes("");
                    question.setClosedAnswerNo("");

                    question.setCreatedAt(fullDateTime);
                    question.setLastModifiedAt(fullDateTime);

                    question.setWorkForceType(workForceType);
                    question.setQuestionType(OPEN_QUESTION);

                    question.setSecondaryQuestion("");

                    valid = true;
                }
                break;
            case RATING_QUESTION:
                if (questionFieldOpen.getText().toString().isEmpty()) {
                    questionFieldOpen.setError("Empty");
                    questionFieldOpen.requestFocus();
                } else {
                    question = new Models.QuestionClass(getQuestionId(workForceType));

                    question.setPrimaryQuestion(questionFieldOpen.getText().toString());

                    question.setClosedAnswerYes("");
                    question.setClosedAnswerNo("");

                    question.setCreatedAt(fullDateTime);
                    question.setLastModifiedAt(fullDateTime);

                    question.setWorkForceType(workForceType);
                    question.setQuestionType(RATING_QUESTION);

                    question.setSecondaryQuestion("");

                    valid = true;
                }
                break;

            case CLOSED_QUESTION:
                if (questionFieldClosed.getText().toString().isEmpty()) {
                    questionFieldClosed.setError("Empty");
                    questionFieldClosed.requestFocus();
                } else if (option1Field.getText().toString().isEmpty()) {
                    option1Field.setError("Empty");
                    option1Field.requestFocus();
                } else if (option2Field.getText().toString().isEmpty()) {
                    option2Field.setError("Empty");
                    option2Field.requestFocus();
                } else {
                    question = new Models.QuestionClass(getQuestionId(workForceType));

                    question.setPrimaryQuestion(questionFieldClosed.getText().toString());

                    question.setClosedAnswerYes(option1Field.getText().toString());
                    question.setClosedAnswerNo(option2Field.getText().toString());

                    question.setCreatedAt(fullDateTime);
                    question.setLastModifiedAt(fullDateTime);

                    question.setWorkForceType(workForceType);
                    question.setQuestionType(CLOSED_QUESTION);

                    question.setSecondaryQuestion("");

                    valid = true;
                }
                break;

            case CONDITION_QUESTION_OPEN:
                if (questionFieldCondition1.getText().toString().isEmpty()) {
                    questionFieldCondition1.setError("Empty");
                    questionFieldCondition1.requestFocus();
                } else if (questionFieldCondition2.getText().toString().isEmpty()) {
                    questionFieldCondition2.setError("Empty");
                    questionFieldCondition2.requestFocus();
                } else {

                    question = new Models.QuestionClass(getQuestionId(workForceType));

                    question.setPrimaryQuestion(questionFieldCondition1.getText().toString());

                    question.setClosedAnswerYes("");
                    question.setClosedAnswerNo("");

                    question.setCreatedAt(fullDateTime);
                    question.setLastModifiedAt(fullDateTime);

                    question.setWorkForceType(workForceType);
                    question.setQuestionType(CONDITION_QUESTION_OPEN);

                    question.setSecondaryQuestion(questionFieldCondition2.getText().toString());

                    valid = true;
                }
                break;

            case CONDITION_QUESTION_CLOSED:
                if (questionFieldCondition1Closed.getText().toString().isEmpty()) {
                    questionFieldCondition1Closed.setError("Empty");
                    questionFieldCondition1Closed.requestFocus();
                } else if (questionFieldCondition2Closed.getText().toString().isEmpty()) {
                    questionFieldCondition2Closed.setError("Empty");
                    questionFieldCondition2Closed.requestFocus();
                } else if (option1FieldClosed.getText().toString().isEmpty()) {
                    option1FieldClosed.setError("Empty");
                    option1FieldClosed.requestFocus();
                } else if (option2FieldClosed.getText().toString().isEmpty()) {
                    option2FieldClosed.setError("Empty");
                    option2FieldClosed.requestFocus();
                } else {

                    question = new Models.QuestionClass(getQuestionId(workForceType));

                    question.setPrimaryQuestion(questionFieldCondition1Closed.getText().toString());

                    question.setClosedAnswerYes(option1FieldClosed.getText().toString());
                    question.setClosedAnswerNo(option2FieldClosed.getText().toString());

                    question.setCreatedAt(fullDateTime);
                    question.setLastModifiedAt(fullDateTime);

                    question.setWorkForceType(workForceType);
                    question.setQuestionType(CONDITION_QUESTION_CLOSED);

                    question.setSecondaryQuestion(questionFieldCondition2Closed.getText().toString());

                    valid = true;
                }
                break;

        }
        return valid;
    }

    private void saveQuestion(Models.QuestionClass question) {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        if (checkIfAvailable(question)) {
            questionRepository.update(question);
            Toast.makeText(this, "Question Updated", Toast.LENGTH_SHORT).show();
        } else {
            questionRepository.insert(question);
            Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show();
        }
        finishActivity();
    }

    private void saveOnlineQuestion(Models.QuestionClass question) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).document(question.getQuestionId()).set(question).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Question saved to firebase");
                finishActivity();
            } else {
                String error = Objects.requireNonNull(task.getException()).toString();
                System.out.println(error);
                Models.failedUploadingData(progressBar,saveQ);
                Toast.makeText(AddNewQuestion.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void finishActivity() {
        finish();
    }

    private boolean checkIfAvailable(Models.QuestionClass question) {
        boolean available = false;
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        List<Models.QuestionClass> questionList = questionRepository.getAllQuestionList();

        if (!questionList.isEmpty()) {
            for (int i = 0; i <= questionList.size() - 1; i++) {
                if (questionList.get(i).getQuestionId().equals(question.getQuestionId())) {
                    available = true;
                    return true;
                }
            }
        } else {
            available = false;
            return false;
        }

        return available;
    }

    @Override
    protected void onResume() {
        super.onResume();
        backPressed = false;
    }

    @Override
    public void onBackPressed() {
        switch (questionType) {
            default:
                break;

            case OPEN_QUESTION:
            case RATING_QUESTION:
                if (questionFieldOpen.getText().toString().isEmpty()) {
                    finishActivity();
                } else {
                    addToDrafts();
                }
                break;


            case CLOSED_QUESTION:
                if (questionFieldClosed.getText().toString().isEmpty()) {
                    finishActivity();
                } else {
                    addToDrafts();
                }
                break;

            case CONDITION_QUESTION_OPEN:
                if (questionFieldCondition1.getText().toString().isEmpty()) {
                    finishActivity();
                } else {
                    addToDrafts();
                }


                break;

            case CONDITION_QUESTION_CLOSED:
                if (questionFieldCondition1Closed.getText().toString().isEmpty()) {
                    finishActivity();
                } else {
                    addToDrafts();
                }
                break;

        }
    }
}