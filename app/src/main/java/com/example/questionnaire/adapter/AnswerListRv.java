package com.example.questionnaire.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questionnaire.R;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.utils.MyLinkedMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.questionnaire.adapter.QuestionAdapter.CLOSED_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.CONDITION_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.CONDITION_VT_C;
import static com.example.questionnaire.adapter.QuestionAdapter.OPEN_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.RATING_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.checkIfNull;
import static com.example.questionnaire.model.Models.PRIMARY;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;
import static com.example.questionnaire.model.Models.getAnswer1Answer2;
import static com.example.questionnaire.model.Models.getMapFromString;

public class AnswerListRv extends RecyclerView.Adapter<AnswerListRv.ViewHolder> {

    private final ArrayList<Models.QuestionClass> questionList;
    private ItemClickListener mClickListener;
    private Context mContext;
    private int viewTypeI;

    public AnswerListRv(Context mContext, ArrayList<Models.QuestionClass> list) {
        this.questionList = list;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutItem = 0;
        switch (viewType) {
            default:
                break;
            case RATING_VT:
                layoutItem = R.layout.single_question_rating;
                break;
            case OPEN_VT:
                layoutItem = R.layout.single_question_text;
                break;
            case CLOSED_VT:
                layoutItem = R.layout.closed_question;
                break;

            case CONDITION_VT:
                layoutItem = R.layout.conditional_layout_view;
                break;

            case CONDITION_VT_C:
                layoutItem = R.layout.conditional_layout_closed_view;
                break;
        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(layoutItem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Models.QuestionClass q = questionList.get(position);

        setAnswers(position,holder);

        if (!checkIfNull(q.getPrimaryQuestion())) {
            holder.questionTv.setText(q.getPrimaryQuestion());
            holder.questionTv.setFreezesText(true);
        }
    }

    private void setAnswers(int position, ViewHolder holder) {
        switch (questionList.get(position).getQuestionType()) {
            default:
                break;

            case OPEN_QUESTION:
                holder.answerField.setText(questionList.get(position).getPrimaryAnswer());

                break;

            case CLOSED_QUESTION:
                if (!checkIfNull(questionList.get(position).getClosedAnswerYes())) {
                    holder.yesB.setText(questionList.get(position).getClosedAnswerYes());
                    System.out.println("Primary : "+questionList.get(position).getPrimaryAnswer());
                } else {
                    holder.yesB.setText("Yes");
                }


                if (!checkIfNull(questionList.get(position).getClosedAnswerNo())) {
                    holder.noB.setText(questionList.get(position).getClosedAnswerNo());
                    System.out.println("Secondary : "+questionList.get(position).getSecondaryAnswer());

                } else {
                    holder.noB.setText("No");
                }

                if (Models.getRightAnswer(questionList.get(position).getClosedAnswerYes(),questionList.get(position).getClosedAnswerNo(),questionList.get(position).getPrimaryAnswer()).equals(PRIMARY)) {
                    holder.yesB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    holder.yesB.setTextColor(Color.WHITE);
                } else {
                    holder.noB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    holder.noB.setTextColor(Color.BLACK);
                }

                break;

            case RATING_QUESTION:
                holder.ratingBar.setRating(Float.parseFloat(questionList.get(position).getPrimaryAnswer()));
                holder.ratingBar.setEnabled(false);
                break;

            case CONDITION_QUESTION_OPEN:

                if (!checkIfNull(questionList.get(position).getSecondaryQuestion())) {
                    holder.questionTv2.setText(questionList.get(position).getSecondaryQuestion());
                    holder.questionTv2.setFreezesText(true);
                }

                if (!checkIfNull(questionList.get(position).getPrimaryAnswer())) {
                    holder.answerField.setText(questionList.get(position).getPrimaryAnswer());
                }

                if (!checkIfNull(questionList.get(position).getSecondaryAnswer())) {
                    holder.answerField2.setText(questionList.get(position).getSecondaryAnswer());
                }



                break;

            case CONDITION_QUESTION_CLOSED:

                if (!checkIfNull(questionList.get(position).getSecondaryQuestion())) {
                    holder.questionTv2.setText(questionList.get(position).getSecondaryQuestion());
                    holder.questionTv2.setFreezesText(true);
                }



                if (!checkIfNull(questionList.get(position).getSecondaryAnswer())) {
                    holder.answerField2.setText(questionList.get(position).getSecondaryAnswer());
                }

                holder.questionTv2.setVisibility(View.GONE);


                if (!checkIfNull(questionList.get(position).getClosedAnswerYes())) {
                    holder.yesB.setText(questionList.get(position).getClosedAnswerYes());
                }

                if (!checkIfNull(questionList.get(position).getClosedAnswerNo())) {
                    holder.noB.setText(questionList.get(position).getClosedAnswerNo());
                }

                if (Models.getRightAnswer(questionList.get(position).getClosedAnswerYes(),questionList.get(position).getClosedAnswerNo(),questionList.get(position).getPrimaryAnswer()).equals(PRIMARY)) {
                    holder.yesB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    holder.yesB.setTextColor(Color.WHITE);
                } else {
                    holder.noB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    holder.noB.setTextColor(Color.BLACK);
                }

                break;
        }
    }

    public static boolean notEmpty (String s) {
        return !s.equals("");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return questionList.size();
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

    private MyLinkedMap<String,String> addAnswersToQuestions (String answerString) {
        return getAnswer1Answer2(getMapFromString(answerString).getValue(0));
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button noB, yesB;
        TextView questionTv, questionTv2;
        TextView answerField,answerField2;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            questionTv = itemView.findViewById(R.id.questionTv);
            questionTv2 = itemView.findViewById(R.id.questionTv2);

            noB = itemView.findViewById(R.id.noButton);
            yesB = itemView.findViewById(R.id.yesButton);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            answerField = itemView.findViewById(R.id.answerField);
            answerField2 = itemView.findViewById(R.id.answerField2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}