package com.wilddog.boarddemo.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wilddog.boarddemo.R;
import com.wilddog.boarddemo.ui.chat.Chat;
import com.wilddog.boarddemo.ui.chat.ChatListAdapter;
import com.wilddog.boarddemo.util.SharedPereferenceTool;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;

import java.util.Random;


public class RightLayout extends RelativeLayout {
    private Context context;
    private ListView lvChat;
    private EditText inputText;
    private Button send;
    private SyncReference mWilddogRef;
    private ChatListAdapter mChatListAdapter;
    private String mUserId;
    private String userName;

    public RightLayout(Context context) {
        super(context);
    }

    public RightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(final Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.right, this);
        setupUsername();
        String roomId = SharedPereferenceTool.getRoomId(context);
        lvChat = (ListView) findViewById(R.id.lv_chat);
        inputText = (EditText) findViewById(R.id.et_msg);
        send = (Button) findViewById(R.id.btn_send);
        mWilddogRef = WilddogSync.getInstance().getReference().child(roomId+"/chat");

        mChatListAdapter = new ChatListAdapter(mWilddogRef.limitToLast(50), context, mUserId);
        lvChat.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                lvChat.clearFocus();
                lvChat.post(new Runnable() {
                    @Override
                    public void run() {
                        lvChat.setSelection(mChatListAdapter.getCount() - 1);
                    }
                });
            }
        });

        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }
    public void release() {
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        mUserId = SharedPereferenceTool.getUserId(context);
        userName = SharedPereferenceTool.getUserInfo(context);
        if (mUserId == null|| mUserId.equals("")) {
            Random r = new Random();
            mUserId = "WilddogUser" + r.nextInt(100000);
            SharedPereferenceTool.saveUserId(context, mUserId);
        }
    }
    private void sendMessage() {
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            Chat chat = new Chat(input, userName,mUserId);
            mWilddogRef.push().setValue(chat);
            inputText.setText("");
        }
    }
}
