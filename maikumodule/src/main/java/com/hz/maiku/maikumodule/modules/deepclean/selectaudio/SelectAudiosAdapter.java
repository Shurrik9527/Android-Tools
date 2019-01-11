package com.hz.maiku.maikumodule.modules.deepclean.selectaudio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.ScreenUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/8
 * @email 252774645@qq.com
 */
public class SelectAudiosAdapter extends ArrayAdapter<AudioBean>{


    private Context mContext;
    public List<AudioBean> allPhotoList;
    private int destWidth, destHeight;
    int screenWidth;
    private OnClick monClick;
    public SelectAudiosAdapter(Context mContext, List<AudioBean> list,OnClick onClick) {
        super(mContext,R.layout.select_audio_item_layout);
        this.mContext = mContext;
        this.allPhotoList = list;
        screenWidth = ScreenUtils.getScreenWidth();
        this.destWidth = (screenWidth - 20) / 3;
        this.destHeight = (screenWidth - 20) / 3;
        this.monClick =onClick;
    }


    public void  setData(List<AudioBean> list){
        if(allPhotoList!=null&&allPhotoList.size()>0){
            allPhotoList.clear();
        }
        allPhotoList.addAll(list);
    }

    public interface  OnClick{
        void callBack(int pos);
    }


    @Override
    public int getCount() {
        return allPhotoList==null?0:allPhotoList.size();//加一是为了那个相机图标
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_audio_item_layout, parent, false);
            viewHolder.rlPhoto = (RelativeLayout) convertView.findViewById(R.id.select_audio_rl);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.select_audio_iv);
            viewHolder.iv_select = (CheckBox) convertView.findViewById(R.id.select_audio_cb);
            viewHolder.iv_size =(TextView)convertView.findViewById(R.id.select_audio_size_tv);
            viewHolder.iv_name =(TextView)convertView.findViewById(R.id.select_audio_name_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.iv_photo.getLayoutParams() != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.iv_photo.getLayoutParams();
            lp.width = destWidth;
            lp.height = destHeight;
            viewHolder.iv_photo.setLayoutParams(lp);
        }
        AudioBean mAudioBean = allPhotoList.get(position);
        if(mAudioBean.isSelect()){
            viewHolder.iv_select.setChecked(true);
        }else {
            viewHolder.iv_select.setChecked(false);
        }

        viewHolder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monClick!=null){
                    monClick.callBack(position);
                }
            }
        });


        try {
            viewHolder.iv_name.setText(mAudioBean.getmName()+"");
            FormatUtil.FileSize mFileSize=FormatUtil.formatFileSize(mAudioBean.getSize());
            String msizes =mFileSize.mSize;
            if (msizes.contains(",")) {
                msizes = msizes.replace(",", ".");
            }
            BigDecimal bigDecimal = new BigDecimal(msizes);
            float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            viewHolder.iv_size.setText(dropped+""+mFileSize.mUnit);
        }catch (Exception e){

        }
        return convertView;
    }

    class ViewHolder {
        public RelativeLayout rlPhoto;
        public ImageView iv_photo;
        public TextView iv_size;
        public TextView iv_name;
        public CheckBox iv_select;
    }
}
