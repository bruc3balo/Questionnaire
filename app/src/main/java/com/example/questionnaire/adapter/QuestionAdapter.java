package com.example.questionnaire.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import java.util.ArrayList;

import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.SliderViewHolder> {

    private Context context;
    private final ArrayList<Models.QuestionClass> questionList;
    private ItemClickListener mClickListener;
    private int current_position;
    private SliderViewHolder sliderViewHolder;
    private int viewTypeI;
    private ViewPager2 viewPager2;

    public static final int CLOSED_VT = 0;
    public static final int OPEN_VT = 1;
    public static final int RATING_VT = 2;
    public static final int CONDITION_VT = 3;
    public static final int CONDITION_VT_C = 4;

    public QuestionAdapter(Context context, ArrayList<Models.QuestionClass> questionList, ViewPager2 viewPager2) {
        this.context = context;
        this.questionList = questionList;
        this.viewPager2 = viewPager2;
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

                break;

            case CLOSED_QUESTION:
                if (!checkIfNull(q.getClosedAnswerYes())) {
                    holder.yesB.setText(q.getClosedAnswerYes());
                }
                holder.yesB.setOnClickListener(v -> {
                    holder.yesB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    holder.noB.setBackgroundTintList(null);
                });
                if (!checkIfNull(q.getClosedAnswerNo())) {
                    holder.noB.setText(q.getClosedAnswerNo());
                }
                holder.noB.setOnClickListener(v -> {
                    holder.noB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    holder.yesB.setBackgroundTintList(null);
                });
                break;

            case RATING_QUESTION:
                holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show());
                break;

            case CONDITION_QUESTION_OPEN:
                if (!checkIfNull(q.getSecondaryQuestion())) {
                    holder.questionTv2.setText(q.getSecondaryQuestion());
                }
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


                System.out.println("q1.. primary : " + q.getPrimaryQuestion() + " secondary : " + q.getSecondaryQuestion());


                if (!checkIfNull(q.getPrimaryAnswer())) {
                    holder.yesB.setText(q.getPrimaryAnswer());
                    holder.yesB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.yesB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                            holder.noB.setBackgroundTintList(null);

                            holder.questionTv2.setVisibility(View.GONE);
                            holder.answerField2.setVisibility(View.GONE);
                        }
                    });
                }

                if (!checkIfNull(q.getSecondaryAnswer())) {
                    holder.noB.setText(q.getSecondaryAnswer());
                    holder.noB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.noB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                            holder.yesB.setBackgroundTintList(null);
                            holder.questionTv2.setVisibility(View.VISIBLE);
                            holder.answerField2.setVisibility(View.VISIBLE);
                        }
                    });
                }
                break;

        }

        holder.skipButton.setOnClickListener(v -> skipDialog());
        holder.doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNextQuestion();
            }
        });
        if (!checkIfNull(q.getPrimaryQuestion())) {
            holder.questionTv.setText(q.getPrimaryQuestion());
        }
    }


    private void updateQuestion() {
    }

    @SuppressLint("SetTextI18n")
    private void skipDialog() {
        Dialog d = new Dialog(context);
        d.setContentView(R.layout.skip_question_dialog);
        d.show();
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView skipInfo = d.findViewById(R.id.deleteInfoTv);
        skipInfo.setText("Do you want to skip this question?");
        Button yes = d.findViewById(R.id.yesButton), no = d.findViewById(R.id.noButton);
        no.setOnClickListener(v -> d.dismiss());
        yes.setOnClickListener(v -> {
            skipQuestion();
            d.dismiss();
        });
    }

    private void moveToNextQuestion() {
        if (viewPager2.getCurrentItem() < questionList.size() - 1) {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        } else if (viewPager2.getCurrentItem() == questionList.size() - 1) {
            Toast.makeText(context, "Last question", Toast.LENGTH_SHORT).show();
        }
    }

    private void skipQuestion() {
        moveToNextQuestion();
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

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
