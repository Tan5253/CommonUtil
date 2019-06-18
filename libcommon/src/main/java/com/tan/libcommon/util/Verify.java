package com.tan.libcommon.util;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证格式工具类
 */
public class Verify {

    /**
     * 验证手机号码
     *
     * @param context
     * @param phoneNumber
     * @return
     */
    public static boolean verifyPhoneNumber(Context context, String phoneNumber) {
        boolean result = false;
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入手机号码");
            return false;
        }
//        String reg = "(^13\\d{9}$)|(^14[5,7]\\d{8}$)|(^15[0,1,2,3,5,6,7,8,9]\\d{8}$)|(^17[6,7,8]\\d{8}$)|(^18\\d{9}$)";
        String reg = "(^1\\d{10}$)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(phoneNumber);
        result = m.matches();
        if (!result) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入正确的手机号码");
        }
        return result;
    }

    /**
     * 验证密码，6位及以上非空白符
     *
     * @param context
     * @param pwd
     * @return
     */
    public static boolean verifyPwd(Context context, String pwd) {
        boolean result = false;
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入密码");
            return false;
        }
        if (pwd.length() < 6) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "密码长度至少6位");
            return false;
        }
        if (pwd.length() > 20) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "密码长度最多20位");
            return false;
        }
        String reg = "^\\S{6,}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(pwd);
        result = m.matches();
        if (!result) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入正确的密码");
        }
        return result;
    }
    
    /**
     * 验证新支付密码，6位数字
     *
     * @param context
     * @param pwd
     * @return
     */
    public static boolean verifyNewPayPwd(Context context, String pwd) {
        boolean result = false;
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入新支付密码");
            return false;
        }
        if (pwd.contains(" ")) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入正确的支付密码");
            return false;
        }
        String reg = "^\\d{6}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(pwd);
        result = m.matches();
        if (!result) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "支付密码必须是六位数字");
        }
        return result;
    }

    /**
     * 验证验证码，6位数字
     *
     * @param context
     * @param code
     * @return
     */
    public static boolean verifyVerificationCode(Context context, String code) {
        if (TextUtils.isEmpty(code)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入验证码");
            return false;
        }
        if (code.contains(" ")) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入正确的验证码");
            return false;
        }
        String reg = "^\\d{6}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(code);
        boolean result = m.matches();
        if (!result) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入正确的验证码");
        }
        return result;
    }

    /**
     * 验证身份证号码。15、18位
     *
     * @param context
     * @param number
     * @return
     */
    public static boolean verifyIdNumber(Context context, String number) {
        boolean result = false;
        if (TextUtils.isEmpty(number)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "身份证号码不能为空");
        } else {
            String reg = "";
            if (number.length() == 15) {
                reg = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
            } else if (number.length() == 18) {
                reg = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
            }
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(number);
            result = m.matches();
            if (!result) {
                ToastUtil.shortToastCenter(context.getApplicationContext(), "身份证号码格式错误");
            }
        }
        return result;
    }

    /**
     * 验证邮箱。
     *
     * @param context
     * @param email
     * @return
     */
    public static boolean verifyEmail(Context context, String email) {
        boolean result = false;
        if (TextUtils.isEmpty(email)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入邮箱地址");
        } else {
            String reg = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(email);
            result = m.matches();
            if (!result) {
                ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入正确的邮箱地址");
            }
        }
        return result;
    }

    /**
     * 验证QQ号码。非0数字开头，5-10位
     *
     * @param context
     * @param qqNumber
     * @return
     */
    public static boolean verifyQQ(Context context, int qqNumber) {
        boolean result = false;
        if (qqNumber == 0) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "QQ号码不能为空");
        } else {
            String reg = "^[1-9][0-9]{4,9}$";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(qqNumber + "");
            result = m.matches();
            if (!result) {
                ToastUtil.shortToastCenter(context.getApplicationContext(), "QQ号码格式错误");
            }
        }
        return result;
    }

    /**
     * 验证电卡账户编号，10位数字
     *
     * @param context
     * @param accountNo
     * @return
     */
    public static boolean verifyAccountNo(Context context, String accountNo) {
        if (TextUtils.isEmpty(accountNo)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入账户编号");
            return false;
        }
        String reg = "^\\d{1,10}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(accountNo);
        boolean result = m.matches();
        if (!result) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入正确的账户编号");
        }
        return result;
    }

    /**
     * 验证昵称，2-20位
     *
     * @param context
     * @param nickName
     * @return
     */
    public static boolean verifyNickName(Context context, String nickName) {
        if (TextUtils.isEmpty(nickName)) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入昵称");
            return false;
        }
        if (nickName.length() < 2 || nickName.length() > 20) {
            ToastUtil.shortToastCenter(context.getApplicationContext(), "请输入2-20字符的昵称");
            return false;
        }
        return true;
    }
}
