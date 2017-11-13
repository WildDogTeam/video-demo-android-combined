package com.wilddog.boarddemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddog.boarddemo.util.SharedpereferenceTool;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.result.AuthResult;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class LoginActivity extends AppCompatActivity {
    private Button loginWithAnonymous;
    private boolean islogining = false;
    private EditText roomId;

    private PromptDialog promptDialog;
    private EditText etDimension;
    private EditText nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //动态申请权限
        int sdk=android.os.Build.VERSION.SDK_INT;
        if (sdk>=23){
            List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
            permissionItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "声音", R.drawable.permission_ic_phone));
            permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "相机", R.drawable.permission_ic_phone));
            permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "文件存储", R.drawable.permission_ic_phone));

            HiPermission.create(this).permissions(permissionItems).checkMutiPermission(new PermissionCallback() {
                @Override
                public void onClose() {

                }

                @Override
                public void onFinish() {

                }

                @Override
                public void onDeny(String permission, int position) {

                }

                @Override
                public void onGuarantee(String permission, int position) {

                }
            });
        }
    }



    private void initView() {
        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        roomId = (EditText) findViewById(R.id.et_roomId);
        nickName = (EditText) findViewById(R.id.et_nickname);
        etDimension = (EditText) findViewById(R.id.et_dimension);
        etDimension.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptButton cancle = new PromptButton("取消", null);
                cancle.setTextColor(Color.parseColor("#0076ff"));
                //默认两个按钮为Alert对话框，大于三个按钮的为底部SHeet形式展现
                promptDialog.showAlertSheet("", true, cancle,
                        new PromptButton("360P",promptButtonListener), new PromptButton("480P", promptButtonListener),
                        new PromptButton("720P", promptButtonListener));
            }
        });
        loginWithAnonymous = (Button) findViewById(R.id.btn_login_anonymous);
        loginWithAnonymous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (islogining) {
                    return;
                }
                islogining = true;
                checkRoomID();
            }
        });
    }

    PromptButtonListener promptButtonListener=  new PromptButtonListener() {
        @Override
        public void onClick(PromptButton promptButton) {
            etDimension.setText(promptButton.getText());
        }
    };


    private void checkRoomID() {
        String strRoonId = roomId.getText().toString().trim();
        if (TextUtils.isEmpty(strRoonId)) {
            Toast.makeText(LoginActivity.this, "输入的房间号不能为空", Toast.LENGTH_SHORT).show();
            islogining = false;
            return;
        }
        if(TextUtils.isEmpty(nickName.getText().toString())){
            Toast.makeText(LoginActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            islogining = false;
            return;
        }
        loginWithAnonymous(strRoonId);
    }

    private void loginWithAnonymous(final String strRoomId) {
        WilddogAuth auth = WilddogAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> var1) {
                if (var1.isSuccessful()) {
                    islogining = false;
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    String uid = var1.getResult().getWilddogUser().getUid();
                    SharedpereferenceTool.saveUserId(LoginActivity.this,uid);
                    SharedpereferenceTool.saveRoomId(LoginActivity.this,strRoomId);
                    SharedpereferenceTool.saveDimension(LoginActivity.this, etDimension.getText().toString());
                    SharedpereferenceTool.setUserInfo(LoginActivity.this,nickName.getText().toString());
                    startActivity(intent);
                } else {
                    islogining = false;
                    Toast.makeText(LoginActivity.this, "登录失败,请查看日志寻找失败原因", Toast.LENGTH_SHORT).show();
                    Log.e("error", var1.getException().getMessage());
                }
            }
        });
    }


}
