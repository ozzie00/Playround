/*
 *  Copyright (c) 2013, Facebook, Inc.
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree. An additional grant
 *  of patent rights can be found in the PATENTS file in the same directory.
 *
 */

package com.oneme.toplay.spring.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.oneme.toplay.spring.OrigamiValueConverter;
import com.oneme.toplay.spring.Spring;
import com.oneme.toplay.spring.SpringConfig;
import com.oneme.toplay.spring.SpringConfigRegistry;
import com.oneme.toplay.spring.SpringListener;
import com.oneme.toplay.spring.SpringSystem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.oneme.toplay.spring.ui.Util.createLayoutParams;
import static com.oneme.toplay.spring.ui.Util.createMatchParams;
import static com.oneme.toplay.spring.ui.Util.createMatchWrapParams;
import static com.oneme.toplay.spring.ui.Util.dpToPx;

/**
 * The SpringConfiguratorView provides a reusable view for live-editing all registered springs
 * within an Application. Each registered Spring can be accessed by its id and its tension and
 * friction properties can be edited while the user tests the effected UI live.
 */
public class SpringButtonView extends FrameLayout {

  private static final int MAX_SEEKBAR_VAL = 100000;
  private static final float MIN_TENSION = 0;
  private static final float MAX_TENSION = 200;
  private static final float MIN_FRICTION = 0;
  private static final float MAX_FRICTION = 50;
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  //private final SpinnerAdapter spinnerAdapter;
  private final List<SpringConfig> mSpringConfigs = new ArrayList<SpringConfig>();
  private final Spring mRevealerSpring;
  private final float mStashPx;
  private final float mRevealPx;
  private final SpringConfigRegistry springConfigRegistry;
  private Spinner mSpringSelectorSpinner;
  private SpringConfig mSelectedSpringConfig;


  public SpringButtonView(Context context) {
    this(context, null);
  }

  public SpringButtonView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public SpringButtonView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    SpringSystem springSystem = SpringSystem.create();
    springConfigRegistry = SpringConfigRegistry.getInstance();

    Resources resources = getResources();
    mRevealPx = dpToPx(40, resources);
    mStashPx = dpToPx(280, resources);


    mRevealerSpring = springSystem.createSpring();
    SpringListener revealerSpringListener = new RevealerSpringListener();
    mRevealerSpring
        .setCurrentValue(1)
        .setEndValue(1)
        .addListener(revealerSpringListener);

    addView(generateHierarchy(context));
    refreshSpringConfigurations();

    this.setTranslationY(mStashPx);
  }

  /**
   * Programmatically build up the view hierarchy to avoid the need for resources.
   * @return View hierarchy
   */
  private View generateHierarchy(Context context) {
    Resources resources = getResources();

    LayoutParams params;
    int fivePx = dpToPx(5, resources);
    int tenPx = dpToPx(10, resources);
    int twentyPx = dpToPx(20, resources);
    TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        1f);
    tableLayoutParams.setMargins(0, 0, fivePx, 0);

    FrameLayout root = new FrameLayout(context);
    params = createLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(300, resources));
    root.setLayoutParams(params);

      FrameLayout container = new FrameLayout(context);
      params = createMatchParams();
      params.setMargins(0, twentyPx, 0, 0);
      container.setLayoutParams(params);
      container.setBackgroundColor(Color.argb(16, 0, 0, 0));
      root.addView(container);



        mSpringSelectorSpinner = new Spinner(context, Spinner.MODE_DIALOG);
        params = createMatchWrapParams();
        params.gravity = Gravity.TOP;
        params.setMargins(tenPx, tenPx, tenPx, 0);
        mSpringSelectorSpinner.setLayoutParams(params);
        //container.addView(mSpringSelectorSpinner);

        LinearLayout linearLayout = new LinearLayout(context);
        params = createMatchWrapParams();
        params.setMargins(0, 0, 0, dpToPx(80, resources));
        params.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(linearLayout);



    //Button button = new Button(context);

    //button.setBackgroundColor(Color.argb(255, 255, 100, 100));
    //button.setMinimumHeight(150);
    //button.setMinimumWidth(150);
    //button.setPadding(10, 8, 10, 10);


    ImageButton b = new ImageButton(context);
    //b.setText("Button added dynamically!");
    b.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    b.setBackgroundColor(Color.argb(255, 255, 100, 100));
    b.setId(b.getId());
    //b.setOnClickListener(context);
   // button.setHeight(30);
   // button.setWidth(20);
    linearLayout.addView(b);

      View nub = new View(context);
      params = createLayoutParams(dpToPx(60, resources), dpToPx(40, resources));
      params.gravity = Gravity.TOP | Gravity.CENTER;
      nub.setLayoutParams(params);
      nub.setOnTouchListener(new OnNubTouchListener());
      nub.setBackgroundColor(Color.argb(255, 0, 164, 209));
      //nub.setBackground(R.drawable.);
      root.addView(nub);



    return root;
  }

  /**
   * remove the configurator from its parent and clean up springs and listeners
   */
  public void destroy() {
    ViewGroup parent = (ViewGroup) getParent();
    if (parent != null) {
      parent.removeView(this);
    }
    mRevealerSpring.destroy();
  }

  /**
   * reload the springs from the registry and update the UI
   */
  public void refreshSpringConfigurations() {
    Map<SpringConfig, String> springConfigMap = springConfigRegistry.getAllSpringConfig();

    mSpringConfigs.clear();

    for (Map.Entry<SpringConfig, String> entry : springConfigMap.entrySet()) {
      if (entry.getKey() == SpringConfig.defaultConfig) {
        continue;
      }
      mSpringConfigs.add(entry.getKey());
    }
    // Add the default config in last.
    mSpringConfigs.add(SpringConfig.defaultConfig);

    if (mSpringConfigs.size() > 0) {
      mSpringSelectorSpinner.setSelection(0);
    }
  }

  private class SpringSelectedListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      mSelectedSpringConfig = mSpringConfigs.get(i);
      updateSeekBarsForSpringConfig(mSelectedSpringConfig);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
  }


  /**
   * update the position of the seekbars based on the spring value;
   * @param springConfig current editing spring
   */
  private void updateSeekBarsForSpringConfig(SpringConfig springConfig) {
    float tension = (float) OrigamiValueConverter.origamiValueFromTension(springConfig.tension);
    float tensionRange = MAX_TENSION - MIN_TENSION;
    int scaledTension = Math.round(((tension - MIN_TENSION) * MAX_SEEKBAR_VAL) / tensionRange);

    float friction = (float) OrigamiValueConverter.origamiValueFromFriction(springConfig.friction);
    float frictionRange = MAX_FRICTION - MIN_FRICTION;
    int scaledFriction = Math.round(((friction - MIN_FRICTION) * MAX_SEEKBAR_VAL) / frictionRange);


  }

  /**
   * toggle visibility when the nub is tapped.
   */
  private class OnNubTouchListener implements OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        togglePosition();

        //destroy();
      }
      return true;
    }
  }

  private void togglePosition() {
    double currentValue = mRevealerSpring.getEndValue();
    mRevealerSpring
        .setEndValue(currentValue == 1 ? 0 : 1);
  }

  private class RevealerSpringListener implements SpringListener {

    @Override
    public void onSpringUpdate(Spring spring) {
      float val = (float) spring.getCurrentValue();
      float minTranslate = mRevealPx;
      float maxTranslate = mStashPx;
      float range = maxTranslate - minTranslate;
      float yTranslate = (val * range) + minTranslate;
      SpringButtonView.this.setTranslationY(yTranslate);
    }

    @Override
    public void onSpringAtRest(Spring spring) {
    }

    @Override
    public void onSpringActivate(Spring spring) {
    }

    @Override
    public void onSpringEndStateChange(Spring spring) {
    }
  }

}

