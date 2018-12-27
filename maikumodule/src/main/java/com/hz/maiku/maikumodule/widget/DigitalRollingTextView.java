package com.hz.maiku.maikumodule.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.util.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 数字滚动
 * @date 2018/10/26
 * @email 252774645@qq.com
 */
public class DigitalRollingTextView extends AppCompatTextView {

    private static final int MONEY_TYPE = 0;
    private static final int NUM_TYPE = 1;
    private static final int COOLER_TYPE = 2;
    private int textType;//内容的类型，默认是金钱类型
    private boolean useCommaFormat;//是否使用每三位数字一个逗号的格式，让数字显得比较好看，默认使用
    private boolean runWhenChange;//是否当内容有改变才使用动画,默认是
    private int duration;//动画的周期，默认为800ms
    private int minNum;//显示数字最少要达到这个数字才滚动 默认为1
    private float minMoney;//显示金额最少要达到这个数字才滚动 默认为0.3

    public enum ModleType{
        MONEY_TYPE,NUM_TYPE,COOLER_TYPE
    }

    private DecimalFormat formatter = new DecimalFormat("0.00");// 格式化金额，保留两位小数
    private String preStr;


    public DigitalRollingTextView(Context context) {
        this(context, null);
    }

    public DigitalRollingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public DigitalRollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.DigitalRollingTextView);
        duration = ta.getInt(R.styleable.DigitalRollingTextView_duration, 2000);
        textType = ta.getInt(R.styleable.DigitalRollingTextView_textType, MONEY_TYPE);
        useCommaFormat = ta.getBoolean(R.styleable.DigitalRollingTextView_useCommaFormat, true);
        runWhenChange = ta.getBoolean(R.styleable.DigitalRollingTextView_runWhenChange,true);
        minNum = ta.getInt(R.styleable.DigitalRollingTextView_minNum, 3);
        minMoney = ta.getFloat(R.styleable.DigitalRollingTextView_minMoney,0.1f);

        ta.recycle();
    }


    /**
     * 设置需要滚动的金钱(必须为正数)或整数(必须为正数)的字符串
     *
     * @param str
     */
    public void setContent(String str) {
        //如果是当内容改变的时候才执行滚动动画,判断内容是否有变化
        if (runWhenChange){
            if (TextUtils.isEmpty(preStr)){
                //如果上一次的str为空
                preStr = str;
                useAnimByType(str);
                return;
            }
            //如果上一次的str不为空,判断两次内容是否一致
            if (preStr.equals(str)){
                //如果两次内容一致，则不做处理
                return;
            }
            preStr = str;//如果两次内容不一致，记录最新的str
        }

        useAnimByType(str);
    }

    private void useAnimByType(String str) {
        if (textType == MONEY_TYPE) {
            playMoneyAnim(str);
        } else if (textType == NUM_TYPE){
            playNumAnim(str);
        }else if(textType==COOLER_TYPE){
            playCoolerAnim(str);
        }
    }


    /**
     * 播放金钱数字动画的方法
     *
     * @param moneyStr
     */
    public void playMoneyAnim(String moneyStr) {
        String money = moneyStr.replace(",", "").replace("-", "");//如果传入的数字已经是使用逗号格式化过的，或者含有符号,去除逗号和负号
        try {
            BigDecimal bigDecimal = new BigDecimal(money);
            float finalFloat = bigDecimal.floatValue();
            if (finalFloat < minMoney) {
                //如果传入的为0，则直接使用setText()
                setText(moneyStr);
                return;
            }
            ValueAnimator floatAnimator =  ValueAnimator.ofObject(new BigDecimalEvaluator(),new BigDecimal(0), bigDecimal);
            floatAnimator.setDuration(duration);
            floatAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    BigDecimal currentNum = (BigDecimal) animation.getAnimatedValue();
                    String str = formatter.format(Double.parseDouble(currentNum.toString()));//格式化成两位小数
                    // 更新显示的内容
                    if (useCommaFormat) {
                        //使用每三位数字一个逗号的格式
                        String formatStr = StringUtil.addComma(str);//三位一个逗号格式的字符串
                        setText(formatStr);
                    } else {
                        setText(str);
                    }
                }
            });
            floatAnimator.start();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.setText(moneyStr);//如果转换Double失败则直接用setText
        }
    }

    /**
     * 温度
     * @param num
     */
    public void playCoolerAnim(String num) {

        try {
            BigDecimal bigDecimal = new BigDecimal(num);
            float finalFloat = bigDecimal.floatValue();
            if (finalFloat < minMoney) {
                //如果传入的为0，则直接使用setText()
                setText(num);
                return;
            }
            ValueAnimator floatAnimator =  ValueAnimator.ofObject(new BigDecimalEvaluator(),new BigDecimal(0), bigDecimal);
            floatAnimator.setDuration(duration);
            floatAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    BigDecimal currentNum = (BigDecimal) animation.getAnimatedValue();
                    formatter = new DecimalFormat("0.0");
                    String str = formatter.format(Double.parseDouble(currentNum.toString()));//格式化成两位小数
                    setText(str);
                }
            });
            floatAnimator.start();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.setText(num);
        }
    }



    /**
     * 播放数字动画的方法
     *
     * @param numStr
     */
    public void playNumAnim(String numStr) {
        String num = numStr.replace(",", "").replace("-", "");//如果传入的数字已经是使用逗号格式化过的，或者含有符号,去除逗号和负号
        try {
            int finalNum = Integer.parseInt(num);
            if (finalNum < minNum) {
                //由于是整数，每次是递增1，所以如果传入的数字比帧数小，则直接使用setText()
                this.setText(numStr);
                return;
            }
            ValueAnimator intAnimator = new ValueAnimator().ofInt(0, finalNum);
            intAnimator.setDuration(duration);
            intAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentNum = (int) animation.getAnimatedValue();
                    setText(String.valueOf(currentNum));
                }
            });
            intAnimator.start();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            setText(numStr);//如果转换Double失败则直接用setText
        }
    }

    /**
     * 设置停留时间
     * @param time
     */
    public void setDuration(int time){
        if(time==0){
            duration=1500;
        }else{
            duration=time;
        }
    }



    class BigDecimalEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            BigDecimal start = (BigDecimal) startValue;
            BigDecimal end = (BigDecimal) endValue;
            BigDecimal result = end.subtract(start);
            return result.multiply(new BigDecimal("" + fraction)).add(start);
        }
    }

    /**
     * 保留位数
     * @param num
     */
    public void setFormatter(int num){
        if(num==1){
            formatter = new DecimalFormat("0.0");// 格式化金额，保留一位小数
        }else if(num==2){
            formatter = new DecimalFormat("0.00");// 格式化金额，保留两位小数
        }else if(num==3){
            formatter = new DecimalFormat("0.000");// 格式化金额，保留三位小数
        }
    }


    public void setModleType(ModleType modleType){
        if (modleType == ModleType.MONEY_TYPE) {
            textType=0;
        } else if (modleType == ModleType.NUM_TYPE){
            textType=1;
        }else if(modleType== ModleType.COOLER_TYPE){
            textType=2;
        }
    }

}
