package com.hz.maiku.maikumodule.modules.junkcleaner.optimized;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 最佳状态Fragment
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class OptimizedFragment extends Fragment {


    private static final String TAG = OptimizedFragment.class.getName();
    @BindView(R2.id.optimized_success_rotate_iv)
    ImageView optimizedSuccessRotateIv;
    Unbinder unbinder;

    public OptimizedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OptimizedFragment newInstance() {
        OptimizedFragment fragment = new OptimizedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.optimized_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_chean_anim);
        optimizedSuccessRotateIv.startAnimation(rotate);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               if(optimizedSuccessRotateIv!=null){
                   optimizedSuccessRotateIv.clearAnimation();
                   optimizedSuccessRotateIv.setVisibility(View.GONE);
               }
           }
       },1000);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
