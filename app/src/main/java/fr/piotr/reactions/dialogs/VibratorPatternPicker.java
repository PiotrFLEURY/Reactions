package fr.piotr.reactions.dialogs;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fr.piotr.reactions.R;
import fr.piotr.reactions.utils.PatternConverter;

/**
 * Created by piotr_000 on 30/12/2016.
 *
 */

public class VibratorPatternPicker extends AlertDialog {

    View mRootView;
    ImageView ivReset;
    ImageView ivTest;
    ImageView ivRing;
    ImageView ivAccept;

    TextView tvDebug;

    List<Long> pattern = new ArrayList<>();

    long step = 0;

    boolean started=false;

    public VibratorPatternPicker(@NonNull Context context) {
        super(context);

        mRootView = LayoutInflater.from(context).inflate(R.layout.vibrator_pattern_picker, null, false);

        ivReset = (ImageView) mRootView.findViewById(R.id.vibrator_picker_reset);
        ivReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });

        ivTest = (ImageView) mRootView.findViewById(R.id.vibrator_picker_test);
        ivTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test(view);
            }
        });

        ivRing = (ImageView) mRootView.findViewById(R.id.vibrator_picker_ring);
        ivRing.setOnTouchListener((view, motionEvent) -> onMotion(motionEvent));

        ivAccept = (ImageView) mRootView.findViewById(R.id.vibrator_picker_accept);
        ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvDebug = (TextView) mRootView.findViewById(R.id.vibrator_picker_debug);

        setView(mRootView);

        pattern.add(0l);

        setCancelable(false);

    }

    private void reset(View v) {
        started=false;
        pattern.clear();
    }

    private void test(View v) {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(getPattern(), -1);
    }

    private boolean onMotion(MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                onDown();
                return true;
            case MotionEvent.ACTION_UP:
                onUp();
                return true;
        }
        return false;
    }

    private void onDown(){
        if(started){
            addStep();
        }
        step = System.currentTimeMillis();
        ivRing.animate().scaleX(0.5f).scaleY(0.5f);
        started=true;
    }

    private void addStep() {
        pattern.add(System.currentTimeMillis() - step);
        tvDebug.setText(PatternConverter.asString(getPattern()));
    }

    private void onUp() {
        addStep();
        step = System.currentTimeMillis();
        ivRing.animate().scaleX(1f).scaleY(1f);
    }

    public long[] getPattern(){
        long[] result = new long[pattern.size()];
        for(int i=0;i<pattern.size();i++){
            result[i]=pattern.get(i);
        }
        return result;
    }
}
