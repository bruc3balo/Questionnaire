package com.example.questionnaire.questionDb;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.questionnaire.model.Models;

import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;


@Database(entities = {Models.QuestionClass.class}, version = 2, exportSchema = false)
public abstract class QuestionDB extends RoomDatabase {

    private static QuestionDB instance;

    public abstract QuestionDao questionDao();

    //only 1 instance of db and thread
    static synchronized QuestionDB getInstance(Context context) {
        if (instance == null) {
            //use builder due to abstract
            instance = Room.databaseBuilder(context.getApplicationContext(), QuestionDB.class, QUESTION_DB)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .allowMainThreadQueries()
                    .build();
            System.out.println("Question Room instance");
        }
        return instance;
    }

    private static final Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@androidx.annotation.NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Handler(Looper.getMainLooper()).post(() -> populateDb(instance));
        }
    };

    private static void populateDb(QuestionDB db) {
        QuestionDao questionDao = db.questionDao();
        System.out.println("Question Database populated");
    }

}
