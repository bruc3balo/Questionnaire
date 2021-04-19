package com.example.questionnaire.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questionnaire.MainActivity;
import com.example.questionnaire.R;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.utils.MyLinkedMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.example.questionnaire.adapter.QuestionAdapter.checkIfNull;
import static com.example.questionnaire.model.Models.HY;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_ANSWER_NO;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_ANSWER_YES;
import static com.example.questionnaire.model.Models.QuestionClass.CREATED_AT;
import static com.example.questionnaire.model.Models.QuestionClass.LAST_MODIFIED;
import static com.example.questionnaire.model.Models.QuestionClass.PRIMARY_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_ID;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_TYPE;
import static com.example.questionnaire.model.Models.QuestionClass.SECONDARY_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.WORK_FORCE_TYPE;
import static com.example.questionnaire.model.Models.QuestionSession.QUESTION_SESSION_DB;
import static com.example.questionnaire.model.Models.WORK_MAID;
import static com.example.questionnaire.model.Models.boldString;
import static com.example.questionnaire.model.Models.getMapFromString;
import static com.example.questionnaire.model.Models.italicString;
import static com.example.questionnaire.model.Models.truncate;
import static com.example.questionnaire.model.Models.underlineString;

public class SessionListRv extends RecyclerView.Adapter<SessionListRv.ViewHolder> {

    private final ArrayList<Models.QuestionSession> sessionList;
    private ItemClickListener mClickListener;
    private Context mContext;

    public SessionListRv(Context mContext, ArrayList<Models.QuestionSession> sessionList) {
        this.sessionList = sessionList;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.session_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Models.QuestionSession q = sessionList.get(position);

        boolean age = !checkIfNull(q.getWorkForceAgentAge());
        boolean name = !checkIfNull(q.getWorkForceAgentName());


        if (!checkIfNull(q.getCreatedAt())) {
            holder.createdAt.setText(q.getCreatedAt());
        }

        if (!checkIfNull(q.getLastModified())) {
            holder.lastModified.setText(q.getLastModified());
        }

        if (!checkIfNull(q.getWorkForceType())) {
            if (q.getWorkForceType().equals(WORK_MAID)) {
                holder.workForceBg.setCardBackgroundColor(Color.RED);
            } else {
                holder.workForceBg.setCardBackgroundColor(Color.GREEN);
            }
        }

        if (!checkIfNull(q.getAnswerListMap())) {
            MyLinkedMap<String, String> questionMap = getMapFromString(q.getAnswerListMap());
            SpannableStringBuilder s1 = new SpannableStringBuilder("This seasons");
            if (age && name) {
                holder.sessionDescription.setText(s1.append(Models.boldString(q.getSessionId()).append(" was answered by ").append(underlineString(q.getWorkForceAgentName())).append(" who is of age")).append(underlineString(q.getWorkForceAgentAge()).append("and has ").append(italicString(String.valueOf(questionMap.size())).append(" questions"))));
            } else if (name && !age) {
                holder.sessionDescription.setText(s1.append(Models.boldString(q.getSessionId()).append(" was answered by ").append(underlineString(HY)).append(" who is of age")).append(underlineString(q.getWorkForceAgentAge()).append("and has ").append(italicString(String.valueOf(questionMap.size())).append(" questions"))));
            } else if (age && !name) {
                holder.sessionDescription.setText(s1.append(Models.boldString(q.getSessionId()).append(" was answered by ").append(underlineString(q.getWorkForceAgentName())).append(" who is of age")).append(underlineString(HY).append("and has ").append(italicString(String.valueOf(questionMap.size())).append(" questions"))));
            } else {
                holder.sessionDescription.setText(s1.append(Models.boldString(q.getSessionId()).append(" was answered by ").append(underlineString(HY)).append(" who is of age")).append(underlineString(HY).append("and has ").append(italicString(String.valueOf(questionMap.size())).append(" questions"))));

            }
        }

        if (name) {
            holder.sessionTitle.setText(underlineString(q.getWorkForceAgentName()).append("'s session"));
        } else {
            holder.sessionTitle.setText(boldString(q.getWorkForceType()).append("'s session"));
        }

        holder.sessionTitle.setLongClickable(true);
        holder.sessionTitle.setOnLongClickListener(v -> {
            deleteDialog(sessionList.get(position));
            return false;
        });
    }

    private void deleteSession(Models.QuestionSession session) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_SESSION_DB).document(session.getSessionId()).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                sessionList.remove(session);
                notifyDataSetChanged();
                Toast.makeText(mContext, "Session deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Failed to delete session", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDialog(Models.QuestionSession session) {
        Dialog d = new Dialog(mContext);
        d.setContentView(R.layout.skip_question_dialog);
        TextView info = d.findViewById(R.id.deleteInfoTv);
        Button yes = d.findViewById(R.id.yesButton);
        Button no = d.findViewById(R.id.noButton);
        d.show();
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        info.setText("Delete this session by ".concat(session.getWorkForceAgentName()).concat(" at ").concat(truncate(Calendar.getInstance().getTime().toString(),16)));
        yes.setOnClickListener(v -> {
            d.dismiss();
            deleteSession(session);
        });
        no.setOnClickListener(v -> d.dismiss());
    }

    synchronized Models.QuestionClass questionResolver(String questionId) {
        Models.QuestionClass[] question = new Models.QuestionClass[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).document(questionId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DocumentSnapshot qs = task.getResult();
                if (questionId.equals(Objects.requireNonNull(qs.get(QUESTION_ID)).toString())) {
                    question[0] = new Models.QuestionClass(Objects.requireNonNull(qs.get(QUESTION_ID)).toString());
                    question[0].setSecondaryQuestion(Objects.requireNonNull(qs.get(SECONDARY_QUESTION)).toString());
                    question[0].setPrimaryQuestion(Objects.requireNonNull(qs.get(PRIMARY_QUESTION)).toString());

                    question[0].setClosedAnswerNo(Objects.requireNonNull(qs.get(CLOSED_ANSWER_NO)).toString());
                    question[0].setClosedAnswerYes(Objects.requireNonNull(qs.get(CLOSED_ANSWER_YES)).toString());
                    question[0].setQuestionType(Objects.requireNonNull(qs.get(QUESTION_TYPE)).toString());

                    question[0].setWorkForceType(Integer.parseInt(String.valueOf(qs.get(WORK_FORCE_TYPE))));
                    question[0].setLastModifiedAt(Objects.requireNonNull(qs.get(LAST_MODIFIED)).toString());
                    question[0].setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());
                    System.out.println("Question resolved is " + question[0].getPrimaryQuestion());
                }
            } else {
                System.out.println("Question not resolved");
            }
        });
        return question[0];
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sessionTitle, createdAt, lastModified, sessionDescription;
        CardView workForceBg;

        ViewHolder(View itemView) {
            super(itemView);
            sessionTitle = itemView.findViewById(R.id.sessionTitle);

            createdAt = itemView.findViewById(R.id.postedAtTimeline);
            lastModified = itemView.findViewById(R.id.lastModified);
            sessionDescription = itemView.findViewById(R.id.sessionDescription);

            workForceBg = itemView.findViewById(R.id.workForceBg);
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