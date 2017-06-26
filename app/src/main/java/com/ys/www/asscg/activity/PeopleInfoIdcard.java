package com.ys.www.asscg.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 进行验证
 */
public class PeopleInfoIdcard extends BaseActivity implements OnClickListener {

    private EditText edit_realname, edit_idcard;
    private static final int PHOTO_WITH_DATA = 18; // 从SD卡中得到图片
    private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
    private ImageView iv_temp, iv_temp2;

    private String imgPath = "";
    private String imgName = "";

    private String mRealName;
    private String mRealIdcard;

    // 身份证正面图片保存路径
    private String id_card_first_path;

    // 身份证反面图片保存路径
    private String id_card_second_path;

    private Bitmap fist_bitmap = null;
    private Bitmap second_bitmap = null;
    ImageView iv_back;
    TextView tv_title;
    private boolean first_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_info_jinxingrz);

        iv_temp = (ImageView) findViewById(R.id.btn_zmidcard);// 身份证正面
        iv_temp2 = (ImageView) findViewById(R.id.btn_fmidcard);// 身份证反面
        iv_temp.setOnClickListener(this);
        iv_temp2.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.title_right);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText("实名认证");
        findViewById(R.id.btn_tijiao).setOnClickListener(this);

        TextView text = (TextView) findViewById(R.id.title);
        text.setText(R.string.jinxingrz);

        edit_realname = (EditText) findViewById(R.id.edit_realname);
        edit_idcard = (EditText) findViewById(R.id.edit_idcard);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zmidcard:// 正面
                first_flag = true;
                openPictureSelectDialog();
                break;
            case R.id.btn_fmidcard:// 反面
                first_flag = false;
                openPictureSelectDialog2();
                break;
            case R.id.btn_tijiao:
                mRealName = edit_realname.getText().toString();
                mRealIdcard = edit_idcard.getText().toString();

                if (mRealName.equals("")) {
                    showCustomToast("请输入真实姓名");
                    return;
                }

                if (mRealIdcard.equals("")) {
                    showCustomToast("请输入真实身份证号码");
                    return;
                }
                // if (id_card_first_path == null || id_card_second_path==null) {
                // showCustomToast("请输入身份证图片");
                // return;
                // }
                doHttp2();
                break;
            case R.id.title_right:
                finish();
                break;
        }

    }

    public void doHttp2() {
        String url = Default.ip + Default.peoInfosmrz_sx;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("realname", mRealName);
            jsonObject.put("idcard", mRealIdcard);
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.getInt("status") == 1) {
                    Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showCustomToast(response.getString("message"));
                }
            } else {
                showCustomToast("获取失败");
            }
            Log.e("HttpClient", s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject str2json(String s) {
        JSONObject json = null;
        try {
            json = new JSONObject(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 打开对话框
     **/
    private void openPictureSelectDialog() {
        // 自定义Context,添加主题
        Context dialogContext = new ContextThemeWrapper(PeopleInfoIdcard.this, android.R.style.Theme_Light);
        String[] choiceItems = new String[2];
        choiceItems[0] = "相机拍摄"; // 拍照
        choiceItems[1] = "本地相册"; // 从相册中选择
        ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choiceItems);
        // 对话框建立在刚才定义好的上下文上
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setTitle("请选择照片");
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Default.isgestures = true;
                switch (which) {
                    case 0: // 相机
                        doTakePhoto();
                        break;
                    case 1: // 从图库相册中选取
                        doPickPhotoFromGallery();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 拍照获取相片
     **/
    private void doTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机

        // /String imageNamePath = "image"+(int)(Math.random()*100)+
        String imge_path_Str = "";
        if (first_flag) {

            imge_path_Str = "image.png";
        } else {

            imge_path_Str = "image2.png";
        }
        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imge_path_Str));
        // 指定照片保存路径（SD卡），image.png为一个临时文件，每次拍照后这个图片都会被替换
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        // 直接使用，没有缩小
        startActivityForResult(intent, PHOTO_WITH_CAMERA); // 用户点击了从相机获取
    }

    /**
     * 从相册获取图片
     **/
    private void doPickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_WITH_DATA); // 取得相片后返回到本画面
    }

    /**
     * 获取文件路径
     **/
    public String uri2filePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        return path;
    }

    /**
     * 创建图片不同的文件名
     **/
    private String createPhotoFileName() {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis()); // 系统当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        fileName = dateFormat.format(date) + ".png";
        return fileName;
    }

    private void displayForDlg(ImageView imgView, String imgPath2, Button btnBig) {
        imgView.setVisibility(View.VISIBLE);
        btnBig.setVisibility(View.VISIBLE);
        imgPath = getApplicationContext().getFilesDir() + "/" + imgName;
        System.out.println("图片文件路径----------》" + imgPath);
        if (!imgPath.equals("")) {
            Bitmap tempBitmap = BitmapFactory.decodeFile(imgPath);
            imgView.setImageBitmap(tempBitmap);// 显示图片
            // 释放Bitmap 内存
            tempBitmap.recycle();
        }
    }

    /**
     * You will receive this call immediately before onResume() when your
     * activity is re-starting.
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) { // 返回成功
            switch (requestCode) {
                case PHOTO_WITH_CAMERA: {// 拍照获取图片
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡

                        String image_path_Str = "";
                        if (first_flag) {
                            image_path_Str = "/image.png";
                            id_card_first_path = Environment.getExternalStorageDirectory() + "/temp/" + image_path_Str;

                        } else {

                            image_path_Str = "/image2.png";
                            id_card_second_path = Environment.getExternalStorageDirectory() + "/temp/" + image_path_Str;

                        }
                        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
                        bitmapFactoryOptions.inJustDecodeBounds = true;
                        bitmapFactoryOptions.inSampleSize = 2;
                        bitmapFactoryOptions.inJustDecodeBounds = false;
                        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + image_path_Str,
                                bitmapFactoryOptions);

                        // 压缩图片存储到SDCard下面

                        if (bitmap != null) {

                            // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.comp(bitmap);
                            bitmap.recycle();

                            // imgName = createPhotoFileName();
                            // 写一个方法将此文件保存到本应用下面啦

                            if (first_flag) {
                                // 设置身份证正面照片
                                iv_temp.setImageBitmap(smallBitmap);
                                savePicture("image.png", smallBitmap);
                            } else {
                                // 设置身份证反面照片
                                iv_temp2.setImageBitmap(smallBitmap);
                                savePicture("image2.png", smallBitmap);

                            }

                        }
                        Toast.makeText(PeopleInfoIdcard.this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PeopleInfoIdcard.this, "没有SD卡", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PHOTO_WITH_DATA: {// 从图库中选择图片
                    ContentResolver resolver = getContentResolver();
                    // 照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        // 使用ContentProvider通过URI获取原始图片

                        String image_path_Str = "";
                        if (first_flag) {
                            image_path_Str = "/image.png";
                            id_card_first_path = Environment.getExternalStorageDirectory() + "/temp/" + image_path_Str;

                        } else {

                            image_path_Str = "/image2.png";
                            id_card_second_path = Environment.getExternalStorageDirectory() + "/temp/" + image_path_Str;

                        }
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                        if (photo != null) {

                            // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.comp(photo);
                            photo.recycle();

                            if (first_flag) {
                                // 设置身份证正面照片
                                iv_temp.setImageBitmap(smallBitmap);
                                savePicture("image.png", smallBitmap);
                            } else {
                                // 设置身份证反面照片
                                iv_temp2.setImageBitmap(smallBitmap);
                                savePicture("image2.png", smallBitmap);

                            }

                        }
                        // iv_temp.setImageURI(originalUri); // 在界面上显示图片
                        Toast.makeText(PeopleInfoIdcard.this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存图片在用户手机之中
     **/
    private void savePicture(String fileName, Bitmap bitmap) {

        FileOutputStream b = null;
        String filePathStr = Environment.getExternalStorageDirectory() + "/temp/";
        File file = new File(filePathStr);
        if (!file.exists()) {

            file.mkdirs();

        }

        try {
            b = new FileOutputStream(filePathStr + fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 身份证反，打开对话框
     **/
    private void openPictureSelectDialog2() {
        // 自定义Context,添加主题
        Context dialogContext = new ContextThemeWrapper(PeopleInfoIdcard.this, android.R.style.Theme_Light);
        String[] choiceItems = new String[2];
        choiceItems[0] = "相机拍摄"; // 拍照
        choiceItems[1] = "本地相册"; // 从相册中选择
        ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choiceItems);
        // 对话框建立在刚才定义好的上下文上
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setTitle("添加图片");
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Default.isgestures = true;
                switch (which) {
                    case 0: // 相机
                        doTakePhoto();
                        break;
                    case 1: // 从图库相册中选取
                        doPickPhotoFromGallery();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
