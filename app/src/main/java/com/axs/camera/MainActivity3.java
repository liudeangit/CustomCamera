package com.axs.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.axs.camera.crop.view.CropImageView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;

public class MainActivity3 extends AppCompatActivity {

    public static final String KEY_IMAGE_PATH = "imagePath";
    private CropImageView cropImageView;
    private ImageView cancleSaveButton,saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //picture.setImageBitmap(bitmap);
        cropImageView = findViewById(R.id.cropImageView);
        saveButton = findViewById(R.id.save_button);
        cancleSaveButton = findViewById(R.id.cancle_save_button);

        String imagePath = getIntent().getStringExtra(KEY_IMAGE_PATH);
        cropImageView.setImageURI(Uri.fromFile(new File(imagePath)));

        saveButton.setOnClickListener(onClickListener);
        cancleSaveButton.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
//保存
            if(id == R.id.save_button){
                //获取裁剪的图片
                Bitmap cropBitMap = cropImageView.getCroppedImage();
                System.out.println();
                cropImageView.setImageBitmap(cropBitMap);
            }
 //取消保存
            else if (id == R.id.cancle_save_button){
                //返回拍照
                PermissionUtils.applicationPermissions(MainActivity3.this, new PermissionUtils.PermissionListener() {
                    @Override
                    public void onSuccess(Context context) {
                        Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                        MainActivity3.this.startActivity(intent);
                    }

                    @Override
                    public void onFailed(Context context) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.CAMERA)
                                && AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.STORAGE)) {
                            AndPermission.with(context).runtime().setting().start();
                        }
                        Toast.makeText(context, context.getString(R.string.permission_camra_storage), Toast.LENGTH_SHORT);
                    }
                }, Permission.Group.STORAGE, Permission.Group.CAMERA);
            }
        }
    };
}