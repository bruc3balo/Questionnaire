package com.example.questionnaire.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.questionnaire.R;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.utils.MyLinkedMap;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.model.Models.HY;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;
import static com.example.questionnaire.model.Models.QuestionSession.QUESTION_SESSION_DB;
import static com.example.questionnaire.model.Models.WORK_CUSTOMER;
import static com.example.questionnaire.model.Models.WORK_MAID;
import static com.example.questionnaire.model.Models.getSessionId;
import static com.example.questionnaire.model.Models.getStringListFromMap;
import static com.example.questionnaire.model.Models.keyValueSep;
import static com.example.questionnaire.model.Models.primary_secondary_sep;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.SliderViewHolder> {

    private Context context;
    private final ArrayList<Models.QuestionClass> questionList;
    private ItemClickListener mClickListener;
    private int current_position;
    private SliderViewHolder sliderViewHolder;
    private int viewTypeI;
    private final ViewPager2 viewPager2;

    public static final int CLOSED_VT = 0;
    public static final int OPEN_VT = 1;
    public static final int RATING_VT = 2;
    public static final int CONDITION_VT = 3;
    public static final int CONDITION_VT_C = 4;
    public static final String SKIPPED = HY;

    private final MyLinkedMap<String, String> myAnswerSessionMap = new MyLinkedMap<>();
    Models.QuestionSession questionSession = new Models.QuestionSession("");
    private final String name;
    private final String age;
    private final Activity activity;
    int workForceType;

    public QuestionAdapter(Context context, ArrayList<Models.QuestionClass> questionList, ViewPager2 viewPager2, String name, String age, int workForceType, Activity activity) {
        this.context = context;
        this.questionList = questionList;
        this.viewPager2 = viewPager2;
        this.name = name;
        this.workForceType = workForceType;
        this.age = age;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutItem = 0;
        switch (viewType) {
            default:
                break;
            case RATING_VT:
                layoutItem = R.layout.single_question_rating_answer;
                break;
            case OPEN_VT:
                layoutItem = R.layout.single_question_text_answer;
                break;
            case CLOSED_VT:
                layoutItem = R.layout.closed_question_dialog;
                break;

            case CONDITION_VT:
                layoutItem = R.layout.conditional_layout;
                break;

            case CONDITION_VT_C:
                layoutItem = R.layout.conditional_layout_closed;
                break;
        }
        return new SliderViewHolder(LayoutInflater.from(context).inflate(layoutItem, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        sliderViewHolder = holder;
        Models.QuestionClass q = questionList.get(position);

        switch (q.getQuestionType()) {
            default:
                break;

            case OPEN_QUESTION:
                holder.doneB.setOnClickListener(v -> {
                    if (holder.answerField.getText().toString().isEmpty()) {
                        holder.answerField.setError("Error message");
                    } else {
                        updateQuestion(questionList.get(position), holder.answerField.getText().toString(), SKIPPED);
                    }
                });
                break;

            case CLOSED_QUESTION:
                if (!checkIfNull(q.getClosedAnswerYes())) {
                    holder.yesB.setText(q.getClosedAnswerYes());
                } else {
                    holder.yesB.setText("Yes");
                }

                holder.yesB.setOnClickListener(v -> {
                    holder.yesB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    holder.yesB.setTextColor(Color.WHITE);
                    holder.noB.setTextColor(Color.BLACK);
                    holder.noB.setBackgroundTintList(null);
                    holder.doneB.setOnClickListener(v12 -> updateQuestion(questionList.get(position), q.getClosedAnswerYes(), SKIPPED));

                });
                if (!checkIfNull(q.getClosedAnswerNo())) {
                    holder.noB.setText(q.getClosedAnswerNo());
                } else {
                    holder.noB.setText("No");
                }
                holder.noB.setOnClickListener(v -> {
                    holder.noB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    holder.noB.setTextColor(Color.WHITE);
                    holder.yesB.setTextColor(Color.BLACK);
                    holder.yesB.setBackgroundTintList(null);
                    holder.doneB.setOnClickListener(v12 -> updateQuestion(questionList.get(position), q.getClosedAnswerNo(), SKIPPED));
                });
                break;

            case RATING_QUESTION:
                float[] myRating = new float[]{11};
                holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                    myRating[0] = rating;
                    Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
                });
                holder.doneB.setOnClickListener(v -> {
                    if (myRating[0] == 11) {
                        holder.ratingBar.setBackgroundColor(Color.RED);
                        Toast.makeText(context, "No rating selected", Toast.LENGTH_SHORT).show();
                        new Handler(Looper.myLooper()).postDelayed(() -> holder.ratingBar.setBackgroundColor(Color.TRANSPARENT), 300);
                    } else {
                        updateQuestion(questionList.get(position), String.valueOf(myRating[0]), SKIPPED);
                    }
                });
                break;

            case CONDITION_QUESTION_OPEN:

                if (!checkIfNull(q.getSecondaryQuestion())) {
                    holder.questionTv2.setText(q.getSecondaryQuestion());
                }

                holder.doneB.setOnClickListener(v1 -> {
                    if (holder.answerField.getText().toString().isEmpty()) {
                        holder.answerField.setError("Answer is wrong");
                        holder.answerField.requestFocus();
                    } else {
                        if (holder.answerField2.getText().toString().isEmpty()) {
                            updateQuestion(questionList.get(position), holder.answerField.getText().toString(), SKIPPED);
                        } else {
                            updateQuestion(questionList.get(position), holder.answerField.getText().toString(), holder.answerField2.getText().toString());
                        }
                    }
                });


                break;

            case CONDITION_QUESTION_CLOSED:


                if (!checkIfNull(q.getPrimaryQuestion())) {
                    holder.questionTv.setText(q.getPrimaryQuestion());
                }

                if (!checkIfNull(q.getSecondaryQuestion())) {
                    holder.questionTv2.setText(q.getSecondaryQuestion());
                }

                holder.questionTv2.setVisibility(View.GONE);
                holder.answerField2.setVisibility(View.GONE);


                if (!checkIfNull(q.getClosedAnswerYes())) {
                    holder.yesB.setText(q.getClosedAnswerYes());
                    holder.yesB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.yesB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                            holder.yesB.setTextColor(Color.WHITE);
                            holder.noB.setTextColor(Color.BLACK);
                            holder.noB.setBackgroundTintList(null);
                            holder.questionTv2.setVisibility(View.VISIBLE);
                            holder.answerField2.setVisibility(View.VISIBLE);
                            holder.doneB.setOnClickListener(v1 -> updateQuestion(questionList.get(position), q.getClosedAnswerYes(), holder.answerField2.getText().toString()));

                        }
                    });
                }

                if (!checkIfNull(q.getClosedAnswerNo())) {
                    holder.noB.setText(q.getClosedAnswerNo());
                    holder.noB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.noB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                            holder.noB.setTextColor(Color.WHITE);
                            holder.yesB.setTextColor(Color.BLACK);
                            holder.yesB.setBackgroundTintList(null);

                            holder.doneB.setOnClickListener(v1 -> updateQuestion(questionList.get(position), q.getClosedAnswerNo(), SKIPPED));
                            holder.questionTv2.setVisibility(View.GONE);
                            holder.answerField2.setVisibility(View.GONE);
                        }
                    });
                }


                break;
        }

        holder.skipButton.setOnClickListener(v -> skipDialog(position));

        if (!checkIfNull(q.getPrimaryQuestion())) {
            holder.questionTv.setText(q.getPrimaryQuestion());
            holder.questionTv.addTextChangedListener(characterWatcher);
        }

       if (!checkIfNull(holder.questionTv2)) {
           holder.questionTv2.addTextChangedListener(characterWatcher);
       }

    }


    private void updateQuestion(Models.QuestionClass questionClass, String primary, String secondary) {
        String content = primary.concat(primary_secondary_sep).concat(secondary);
        if (primary != null) {
            questionClass.setPrimaryAnswer(primary);
        } else {
            questionClass.setPrimaryAnswer("");
        }

        if (primary != null) {
            questionClass.setSecondaryAnswer(primary);
        } else {
            questionClass.setSecondaryAnswer("");
        }

        myAnswerSessionMap.put(questionClass.getQuestionId(), content);
        moveToNextQuestion();
    }

    private void finalizeSession() {
        String s = getStringListFromMap(myAnswerSessionMap);

        questionSession.setSessionId(getSessionId(workForceType, name));
        questionSession.setAnswerListMap(s);
        questionSession.setCreatedAt(Models.fullSecondDateTime);

        questionSession.setLastModified(Models.fullSecondDateTime);
        questionSession.setWorkForceAgentName(name);
        questionSession.setWorkForceAgentAge(age);


        if (workForceType == MAID) {
            questionSession.setWorkForceType(WORK_MAID);
        } else {
            questionSession.setWorkForceType(WORK_CUSTOMER);
        }

        saveSession(questionSession);

    }

    @SuppressLint("SetTextI18n")
    private void skipDialog(int pos) {
        Dialog d = new Dialog(context);
        d.setContentView(R.layout.skip_question_dialog);
        d.show();
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView skipInfo = d.findViewById(R.id.deleteInfoTv);
        skipInfo.setText("Do you want to skip this question?");
        Button yes = d.findViewById(R.id.yesButton), no = d.findViewById(R.id.noButton);
        no.setOnClickListener(v -> d.dismiss());
        yes.setOnClickListener(v -> {
            updateQuestion(questionList.get(pos), SKIPPED, SKIPPED);
            d.dismiss();
        });
    }


    private void moveToNextQuestion() {
        if (viewPager2.getCurrentItem() < questionList.size() - 1) {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        } else if (viewPager2.getCurrentItem() == questionList.size() - 1) {
            Toast.makeText(context, "Last question", Toast.LENGTH_SHORT).show();
            finalizeSession();
        }
    }

    private void skipQuestion() {
        moveToNextQuestion();
    }

    private void saveSession(Models.QuestionSession session) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_SESSION_DB).document(session.getSessionId()).set(session).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Session saved", Toast.LENGTH_SHORT).show();
                activity.finish();
            } else {
                Toast.makeText(context, "Failed to finalize Session", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static TextWatcher characterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String content = s.toString();
            for (int i = 0; i <= content.length() - 1; i++) {
                if (String.valueOf(s.charAt(i)).equals(keyValueSep)) {
                    content = String.valueOf(s.charAt(i)).replace(keyValueSep, "");
                } else if (String.valueOf(s.charAt(i)).equals(primary_secondary_sep)) {
                    content = String.valueOf(s.charAt(i)).replace(primary_secondary_sep, "");
                }
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public class SliderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button doneB, skipButton, noB, yesB;
        TextView questionTv, questionTv2;
        EditText answerField, answerField2;
        RatingBar ratingBar;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            doneB = itemView.findViewById(R.id.doneButton);
            questionTv = itemView.findViewById(R.id.questionTv);
            questionTv2 = itemView.findViewById(R.id.questionTv2);
            skipButton = itemView.findViewById(R.id.skipButton);
            answerField = itemView.findViewById(R.id.answerField);
            answerField2 = itemView.findViewById(R.id.answerField2);
            noB = itemView.findViewById(R.id.noButton);
            yesB = itemView.findViewById(R.id.yesButton);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    @Override
    public int getItemViewType(int position) {
        System.out.println(questionList.get(position).getQuestionType() + " : type");
        switch (questionList.get(position).getQuestionType()) {
            default:
                break;

            case OPEN_QUESTION:
                viewTypeI = OPEN_VT;
                break;

            case CLOSED_QUESTION:
                viewTypeI = CLOSED_VT;
                break;

            case RATING_QUESTION:
                viewTypeI = RATING_VT;
                break;

            case CONDITION_QUESTION_OPEN:
                viewTypeI = CONDITION_VT;
                break;

            case CONDITION_QUESTION_CLOSED:
                viewTypeI = CONDITION_VT_C;
                break;

        }
        return viewTypeI;
    }

    public static boolean checkIfNull(Object o) {
        return o == null;
    }

    public SliderViewHolder getViewHolder() {
        return sliderViewHolder;
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public int getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(int current_position) {
        this.current_position = current_position;
    }

}
