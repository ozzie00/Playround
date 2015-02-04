/*
* Copyright 2014 OneME
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.oneme.toplay.base;

import android.view.Display;
import android.graphics.Point;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.content.Context;


public final class Screen {


    public static final int width() {

      // DisplayMetrics metrics      = new DisplayMetrics();
      // WindowManager windowManager =(WindowManager) Context.getSystemService(Context.WINDOW_SERVICE);
      // windowManager.getDefaultDisplay().getMetrics(metrics);

       // return metrics.widthPixels;
        return 400;

   }

    public static final int height() {

     //   DisplayMetrics metrics      = new DisplayMetrics();
     //   WindowManager windowManager = Context.getSystemService(Context.WINDOW_SERVICE);
     //   windowManager.getDefaultDisplay().getMetrics(metrics);

     //   return metrics.heightPixels;
        return 400;

    }

}
