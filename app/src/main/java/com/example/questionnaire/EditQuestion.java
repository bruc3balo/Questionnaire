package com.example.questionnaire;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.questionnaire.model.Models;
import com.example.questionnaire.questionDb.QuestionRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

import static com.example.questionnaire.adapter.QuestionAdapter.checkIfNull;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;

public class EditQuestion extends AppCompatActivity {

    private Models.QuestionClass thisQuestion;
    EditText noB, yesB;
    EditText questionTv, questionTv2;
    private ProgressBar editPb;
    private boolean isWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        Toolbar editQtb = findViewById(R.id.editQtb);
        setSupportActionBar(editQtb);
        editQtb.setNavigationOnClickListener(v -> finish());

        editPb = findViewById(R.id.editPb);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            thisQuestion = (Models.QuestionClass) extras.get(QUESTION_DB);
            setUpPage();
        }

        isWaiting = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save").setIcon(R.drawable.ic_save).setOnMenuItemClickListener(item -> {
            if (!isWaiting) {
                updateQuestion(thisQuestion);
            } else {
                Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show();
            }
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("Delete").setIcon(R.drawable.ic_delete).setOnMenuItemClickListener(item -> {
            if (!isWaiting) {
                deleteDialog();
            } else {
                Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show();
            }
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void deleteQuestion(Models.QuestionClass thisQuestion) {
        isWaiting = true;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).document(thisQuestion.getQuestionId()).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditQuestion.this, "Deleted question", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditQuestion.this, "Failed to delete question", Toast.LENGTH_SHORT).show();
                isWaiting = false;
            }
        });
    }

    private void updateQuestion(Models.QuestionClass question) {
        Models.isLoadingData(editPb);
        isWaiting = true;
        switch (question.getQuestionType()) {

            default:
                break;

            case CLOSED_QUESTION:

                question.setClosedAnswerYes(yesB.getText().toString());
                question.setClosedAnswerNo(noB.getText().toString());

                break;


            case CONDITION_QUESTION_OPEN:
                question.setSecondaryQuestion(questionTv2.getText().toString());
                break;

            case CONDITION_QUESTION_CLOSED:
                question.setSecondaryQuestion(questionTv2.getText().toString());
                question.setClosedAnswerYes(yesB.getText().toString());
                question.setClosedAnswerNo(noB.getText().toString());


                break;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        question.setLastModifiedAt(Calendar.getInstance().getTime().toString());
        question.setPrimaryQuestion(questionTv.getText().toString());
        db.collection(QUESTION_DB).document(question.getQuestionId()).set(question).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditQuestion.this, "Question Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Models.stopLoadingData(editPb);
                    isWaiting = false;
                    Toast.makeText(EditQuestion.this, "Question Not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void discardDialog() {
        Dialog d = new Dialog(EditQuestion.this);
        d.setContentView(R.layout.skip_question_dialog);
        TextView info = d.findViewById(R.id.deleteInfoTv);
        Button yes = d.findViewById(R.id.yesButton);
        Button no = d.findViewById(R.id.noButton);
        d.show();
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        info.setText("Discard Changes?");
        yes.setOnClickListener(v -> {
            d.dismiss();
            super.onBackPressed();
            finish();
        });
        no.setOnClickListener(v -> d.dismiss());
    }

    private void deleteDialog() {
        Dialog d = new Dialog(EditQuestion.this);
        d.setContentView(R.layout.skip_question_dialog);
        TextView info = d.findViewById(R.id.deleteInfoTv);
        Button yes = d.findViewById(R.id.yesButton);
        Button no = d.findViewById(R.id.noButton);
        d.show();
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        info.setText("Sure you want to delete this question?");
        yes.setOnClickListener(v -> {
            d.dismiss();
            deleteQuestion(thisQuestion);
        });
        no.setOnClickListener(v -> d.dismiss());
    }


    private void getQuestionDetailsOfflineWithIdOffline(String questionId) {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        thisQuestion = questionRepository.getSpecificQuestionWithId(questionId);
    }

    private void deleteOfflineQuestion(String questionId) {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        if (checkIfAvailableOffline(thisQuestion)) {
            questionRepository.deleteSpecificQuestionWithId(questionId);
        } else {
            try {
                questionRepository.delete(thisQuestion);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No question found", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateQuestionOffline(Models.QuestionClass question) {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        if (checkIfAvailableOffline(question)) {
            questionRepository.update(question);
        } else {
            questionRepository.insert(question);
        }
    }

    private boolean checkIfAvailableOffline(Models.QuestionClass question) {
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

    private void setUpPage() {
        Models.stopLoadingData(editPb);
        Models.QuestionClass q = thisQuestion;
        setOpenPage(q.getQuestionType());
        switch (q.getQuestionType()) {

            default:
                break;

            case OPEN_QUESTION:
                questionTv = findViewById(R.id.questionTvOpen);
                break;

            case CLOSED_QUESTION:

                questionTv = findViewById(R.id.questionTvClosed);
                yesB = findViewById(R.id.yesClosedField);
                noB = findViewById(R.id.noClosedField);

                if (!checkIfNull(q.getClosedAnswerYes())) {
                    yesB.setText(q.getClosedAnswerYes());
                } else {
                    yesB.setText("Yes");
                }

                if (!checkIfNull(q.getClosedAnswerNo())) {
                    noB.setText(q.getClosedAnswerNo());
                } else {
                    noB.setText("No");
                }

                break;

            case RATING_QUESTION:
                questionTv = findViewById(R.id.questionTvRating);

                break;

            case CONDITION_QUESTION_OPEN:

                questionTv = findViewById(R.id.questionTvOpenConditional);
                questionTv2 = findViewById(R.id.questionTv2OpenConditional);

                if (!checkIfNull(q.getSecondaryQuestion())) {
                    questionTv2.setText(q.getSecondaryQuestion());
                }

                break;

            case CONDITION_QUESTION_CLOSED:

                questionTv = findViewById(R.id.questionTvClosedConditional);
                questionTv2 = findViewById(R.id.questionTv2ClosedConditional);
                yesB = findViewById(R.id.yesClosedFieldConditional);
                noB = findViewById(R.id.noClosedFieldConditional);

                if (!checkIfNull(q.getSecondaryQuestion())) {
                    questionTv2.setText(q.getSecondaryQuestion());
                }

                questionTv2.setVisibility(View.VISIBLE);

                if (!checkIfNull(q.getClosedAnswerYes())) {
                    yesB.setText(q.getClosedAnswerYes());
                }

                if (!checkIfNull(q.getClosedAnswerNo())) {
                    noB.setText(q.getClosedAnswerNo());
                }


                break;
        }

        if (!checkIfNull(q.getPrimaryQuestion())) {
            questionTv.setText(q.getPrimaryQuestion());
            questionTv.setFreezesText(true);
        }
    }

    private void setOpenPage(String qType) {
        LinearLayout closedConditional = findViewById(R.id.editClosedConditional);
        LinearLayout openConditional = findViewById(R.id.editOpenConditional);
        LinearLayout ratingLayout = findViewById(R.id.editRatingQuestion);
        LinearLayout closedLayout = findViewById(R.id.editClosedQuestion);
        LinearLayout openLayout = findViewById(R.id.editOpenQuestion);

        switch (qType) {

            default:
                break;

            case OPEN_QUESTION:
                openLayout.setVisibility(View.VISIBLE);

                closedConditional.setVisibility(View.GONE);
                openConditional.setVisibility(View.GONE);
                ratingLayout.setVisibility(View.GONE);
                closedLayout.setVisibility(View.GONE);
                break;

            case CLOSED_QUESTION:
                closedLayout.setVisibility(View.VISIBLE);

                closedConditional.setVisibility(View.GONE);
                openConditional.setVisibility(View.GONE);
                ratingLayout.setVisibility(View.GONE);
                openLayout.setVisibility(View.GONE);
                break;
            case RATING_QUESTION:
                ratingLayout.setVisibility(View.VISIBLE);

                closedConditional.setVisibility(View.GONE);
                openConditional.setVisibility(View.GONE);
                closedLayout.setVisibility(View.GONE);
                openLayout.setVisibility(View.GONE);
                break;

            case CONDITION_QUESTION_OPEN:
                openConditional.setVisibility(View.VISIBLE);

                closedConditional.setVisibility(View.GONE);
                ratingLayout.setVisibility(View.GONE);
                closedLayout.setVisibility(View.GONE);
                openLayout.setVisibility(View.GONE);
                break;

            case CONDITION_QUESTION_CLOSED:
                closedConditional.setVisibility(View.VISIBLE);

                openConditional.setVisibility(View.GONE);
                ratingLayout.setVisibility(View.GONE);
                closedLayout.setVisibility(View.GONE);
                openLayout.setVisibility(View.GONE);

                break;
        }

    }

    @Override
    public void onBackPressed() {
        discardDialog();
    }
}