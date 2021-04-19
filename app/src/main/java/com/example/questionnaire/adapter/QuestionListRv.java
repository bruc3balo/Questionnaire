package com.example.questionnaire.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
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

import com.example.questionnaire.R;
import com.example.questionnaire.model.Models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.questionnaire.adapter.QuestionAdapter.CLOSED_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.CONDITION_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.CONDITION_VT_C;
import static com.example.questionnaire.adapter.QuestionAdapter.OPEN_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.RATING_VT;
import static com.example.questionnaire.adapter.QuestionAdapter.checkIfNull;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;

public class QuestionListRv extends RecyclerView.Adapter<QuestionListRv.ViewHolder> {

    private final ArrayList<Models.QuestionClass> questionList;
    private ItemClickListener mClickListener;
    private Context mContext;
    private int viewTypeI;

    public QuestionListRv(Context mContext, ArrayList<Models.QuestionClass> list) {
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

        switch (q.getQuestionType()) {
            default:
                break;

            case OPEN_QUESTION:

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
                });
                break;

            case RATING_QUESTION:
                holder.ratingBar.setRating(5);
                holder.ratingBar.setEnabled(false);
                break;

            case CONDITION_QUESTION_OPEN:

                if (!checkIfNull(q.getSecondaryQuestion())) {
                    holder.questionTv2.setText(q.getSecondaryQuestion());
                    holder.questionTv2.setFreezesText(true);
                }

                break;

            case CONDITION_QUESTION_CLOSED:

                if (!checkIfNull(q.getSecondaryQuestion())) {
                    holder.questionTv2.setText(q.getSecondaryQuestion());
                    holder.questionTv2.setFreezesText(true);
                }



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

                            holder.questionTv2.setVisibility(View.GONE);
                        }
                    });
                }


                break;
        }

        if (!checkIfNull(q.getPrimaryQuestion())) {
            holder.questionTv.setText(q.getPrimaryQuestion());
            holder.questionTv.setFreezesText(true);
        }
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

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button noB, yesB;
        TextView questionTv, questionTv2;

        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            questionTv = itemView.findViewById(R.id.questionTv);
            questionTv2 = itemView.findViewById(R.id.questionTv2);

            noB = itemView.findViewById(R.id.noButton);
            yesB = itemView.findViewById(R.id.yesButton);
            ratingBar = itemView.findViewById(R.id.ratingBar);
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