package app.season.expandableactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User: Season(ssseasonnn@gmail.com)
 * Date: 2016-05-10
 * Time: 10:30
 * FIXME
 */
public class MenuFragment extends Fragment {


    @Bind(R.id.share)
    FloatingActionButton mShare;
    @Bind(R.id.root_view)
    FrameLayout mRootView;
    @Bind(R.id.email)
    FloatingActionButton mEmail;
    @Bind(R.id.reply)
    FloatingActionButton mReply;
    @Bind(R.id.send)
    FloatingActionButton mSend;

    private int centerX;
    private int centerY;

    private float startX;
    private float endX;

    private float startRadius = 0f;
    private float endRadius;

    private OnAnimatorExcute mOnAnimatorExcute;

    public void setOnAnimatorExcute(OnAnimatorExcute onAnimatorExcute) {
        mOnAnimatorExcute = onAnimatorExcute;
    }

    @OnClick({R.id.share, R.id.email, R.id.reply, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share:
                setExitAnimator();
                break;
            case R.id.email:
                Toast.makeText(getContext(), "button1 click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reply:
                Toast.makeText(getContext(), "button2 click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.send:
                Toast.makeText(getContext(), "button3 click", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public interface OnAnimatorExcute {
        void onEnter();

        void onExit();
    }

    public boolean onBackPressed() {
        setExitAnimator();
        return true;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        final View view = inflater.inflate(R.layout.menu_fragment, container, false);
        ButterKnife.bind(this, view);
        mRootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRootView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRootView.setVisibility(View.INVISIBLE);
                centerX = mShare.getLeft() + mShare.getWidth() / 2;
                centerY = mShare.getTop() + mShare.getHeight() / 2;
                endRadius = (float) Math.hypot(mRootView.getWidth(), mRootView.getHeight());

                startX = mShare.getX();
                endX = mRootView.getWidth() / 2;

                setEnterAnimator();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setEnterAnimator() {
        Animator reveal = ViewAnimationUtils.createCircularReveal(mRootView, centerX, centerY, mShare.getWidth() / 2,
                endRadius);
        reveal.setInterpolator(new AccelerateDecelerateInterpolator());
        reveal.setDuration(400);
        reveal.setStartDelay(100);
        reveal.start();

        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mOnAnimatorExcute.onEnter();
                enterAnimatorSet();
                mRootView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRootView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setExitAnimator() {
        Animator reveal = ViewAnimationUtils.createCircularReveal(mRootView, centerX, centerY, endRadius, mShare
                .getWidth() / 2);
        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                exitAnimatorSet();
                mRootView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mOnAnimatorExcute.onExit();
                mRootView.setVisibility(View.INVISIBLE);
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        reveal.setDuration(400);
        reveal.setStartDelay(100);
        reveal.setInterpolator(new AccelerateDecelerateInterpolator());
        reveal.start();
    }

    private void enterAnimatorSet() {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mShare, "x", endX);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(mShare, "rotation", -225f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(xAnimator, rotateAnimator);
        set.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mEmail, "translationY", -300);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mReply, "translationY", -300);
        animator2.setStartDelay(50);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mSend, "translationY", -300);
        animator3.setStartDelay(100);

        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(mEmail, "alpha", 0.3f, 1f);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(mReply, "alpha", 0.3f, 1f);
        alpha2.setStartDelay(50);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(mSend, "alpha", 0.3f, 1f);
        alpha3.setStartDelay(100);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.playTogether(animator1, alpha1, animator2, alpha2, animator3, alpha3);
        animatorSet.start();
    }

    private void exitAnimatorSet() {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mShare, "x", startX);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(mShare, "rotation", 0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(xAnimator, rotateAnimator);
        set.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mEmail, "translationY", 300);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mReply, "translationY", 300);
        animator2.setStartDelay(50);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mSend, "translationY", 300);
        animator3.setStartDelay(100);

        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(mEmail, "alpha", 1f, 03f);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(mReply, "alpha", 1f, 03f);
        alpha2.setStartDelay(50);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(mSend, "alpha", 1f, 03f);
        alpha3.setStartDelay(100);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.playTogether(animator1, alpha1, animator2, alpha2, animator3, alpha3);
        animatorSet.start();
    }
}
