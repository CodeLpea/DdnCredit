package com.example.lp.ddncredit.mainview.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.view.adapter.AutoCompleteAdapter;
import com.example.lp.ddncredit.mainview.view.bgToast;
import com.example.lp.ddncredit.utils.SPUtil;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.ADRESS_RECODE_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.APISP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.mainview.view.dialog.AttendDialog.mDialogHeight;
import static com.example.lp.ddncredit.mainview.view.dialog.AttendDialog.mDialogWith;
import static com.example.lp.ddncredit.utils.NavigationBarUtil.hideNavigationBar;

/**
 * 服务器地址设置Dialog
 * lp
 * 2019/06/25
 */
public class AdressDialog implements View.OnClickListener {

    private static final String TAG = "AdressDialog";
    private static final int MAX_HISTORY_COUNT = 50;                    // 最大的历史记录数
    private static final String SP_SEPARATOR = ":-P";                   // 分隔每条历史记录"/-_0_-\\\\"
    private static final String SP_EMPTY_TAG = "<empty>";               // 空白记录标识
    private static final int MAX_ONCE_MATCHED_ITEM = 5;                 // 提示框最多要显示的记录行数
    private static int simpleItemHeight;                                // 单行item的高度值
    private static int prevCount = -1;                                  // 上一次记录行数
    private static int curCount = -1;                                   // 当前记录行数
    private AutoCompleteAdapter mCustomAdapter;
    private AlertDialog mAlertDialog;
    private Context mContext;
    private AutoCompleteTextView autoCompleteTextView;
    private Button mSureBtn;
    private Button mCancelBtn;
    private Window window;

    private AdressResultListenr mAdressResultListenr;

    public interface AdressResultListenr {
        void AdressResult(int i);
    }

    public AdressDialog(Context mContext, AdressResultListenr mAdressResultListenr) {
        this.mContext = mContext;
        this.mAdressResultListenr = mAdressResultListenr;
    }


    public AlertDialog showAlertDialog() {

        initDialog();

        initWindow();

        initView();

        initData();


        return mAlertDialog;
    }

    private void initData() {//初始化数据
        ArrayList<String> mOriginalValues = new ArrayList<>();
        String[] mCustomHistoryArray = getHistoryArray();
        mOriginalValues.addAll(Arrays.asList(mCustomHistoryArray));
        mCustomAdapter = new AutoCompleteAdapter(window.getContext(), mOriginalValues);
        mCustomAdapter.setDefaultMode(AutoCompleteAdapter.MODE_STARTSWITH | AutoCompleteAdapter.MODE_SPLIT);    // 设置匹配模式
        mCustomAdapter.setSupportPreview(true);     // 支持使用特殊符号进行预览提示内容，默认为"@"

        simpleItemHeight = mCustomAdapter.getSimpleItemHeight();

        mCustomAdapter.setOnFilterResultsListener(new AutoCompleteAdapter.OnFilterResultsListener() {
            @Override
            public void onFilterResultsListener(int count) {
                curCount = count;
                if (count > MAX_ONCE_MATCHED_ITEM) {        // 限制提示框最多要显示的记录行数
                    curCount = MAX_ONCE_MATCHED_ITEM;
                }
                if (curCount != prevCount) {                // 仅当目前的数目和之前的不同才重新设置下拉框高度，避免重复设置
                    prevCount = curCount;
                    autoCompleteTextView.setDropDownHeight(simpleItemHeight * curCount);
                }
            }
        });

        mCustomAdapter.setOnSimpleItemDeletedListener(new AutoCompleteAdapter.OnSimpleItemDeletedListener() {
            @Override
            public void onSimpleItemDeletedListener(String value) {//
                String old_history = getHistoryString();    // 获取之前的记录
                String new_history = old_history.replace(value + SP_SEPARATOR, "");     // 用空字符串替换掉要删除的记录
                setHistoryString(new_history);             // 保存修改过的记录
            }
        });

        autoCompleteTextView.setAdapter(mCustomAdapter);       //设置适配器
        autoCompleteTextView.setThreshold(1);                  //最低索引数字

    }


    //初始化控件
    private void initView() {
        autoCompleteTextView = window.findViewById(R.id.actv_adress);
        mSureBtn =  window.findViewById(R.id.btn_sure);
        mCancelBtn = window.findViewById(R.id.btn_cancel);
        mSureBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    private void initWindow() {
        //加载布局
        window = mAlertDialog.getWindow();
        window.setContentView(R.layout.adress_dialog);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = mDialogWith;
        params.height = mDialogHeight;
        window.setAttributes(params);

    }

    private void initDialog() {
        //创建dialog 这种方式
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater factory = LayoutInflater.from(mContext);
        View textEntryView = factory.inflate(R.layout.adress_dialog, null);
        mAlertDialog.setView(textEntryView);
        mAlertDialog.show();//必须在setConentView之前调用
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_adress_set://开启dialog
                showAlertDialog();
                hideNavigationBar(window);
                break;
            case R.id.btn_sure:
                saveHistoryByAutoCompleteTextView(autoCompleteTextView);
                dissMissDialog(1);
                break;
            case R.id.btn_cancel:
                dissMissDialog(0);
                break;
        }

    }

    private void dissMissDialog(int i){
        if(mAlertDialog!=null){
            mAlertDialog.dismiss();
            mAdressResultListenr.AdressResult(i);
        }
    }


    /**
     * 读取索引
     * 并转换为数组
     * */
    private String[] getHistoryArray() {
        String[] array =getHistoryString().split(SP_SEPARATOR);//按照:-P的间隔读取历史记录
        if (array.length > MAX_HISTORY_COUNT) {         // 最多只提示最近的50条历史记录
            String[] newArray = new String[MAX_HISTORY_COUNT];
            System.arraycopy(array, 0, newArray, 0, MAX_HISTORY_COUNT); // 实现数组间的内容复制
        }
        return array;
    }


    /**
     * 读取索引
     * */
    private String getHistoryString() {
        String history=SPUtil.readString(SP_NAME, ADRESS_RECODE_NAME);
        return history;
    }
    /**
     * 保存索引
     * */
    private void setHistoryString(String details) {
        SPUtil.writeString(SP_NAME,ADRESS_RECODE_NAME,details);
    }
    /**
     * 保存Api
     * */
    private void saveApi(String s){
        SPUtil.writeString(SP_NAME, APISP_NAME,s);
    }

    private void saveHistoryByAutoCompleteTextView(AutoCompleteTextView view) {
        String text = view.getText().toString().trim();     // 去掉前后的空白符
        if (TextUtils.isEmpty(text)) {      // null or ""
            bgToast.myToast((Activity) mContext,"输入为空，不能保存",0,200);
            return;
        }
        saveApi(text);//保存到Sp中

        String old_text = getHistoryString();    // 获取SP中保存的历史记录
        StringBuilder sb;
        if (SP_EMPTY_TAG.equals(old_text)) {
            sb = new StringBuilder();
        } else {
            sb = new StringBuilder(old_text);
        }
        sb.append(text + SP_SEPARATOR);      // 使用逗号来分隔每条历史记录
        // 判断搜索内容是否已存在于历史文件中，已存在则不再添加
        if (!old_text.contains(text + SP_SEPARATOR)) {
            setHistoryString(sb.toString());  // 实时保存历史记录
            mCustomAdapter.add(text);        // 实时更新下拉提示框中的历史记录
        }
    }


}
