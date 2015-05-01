/*
 * Copyright 2015 Blanyal D'souza.
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


package com.blanyal.remindme;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;


public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar mCalendar = Calendar.getInstance();

        mCalendar.set(Calendar.MONTH, 3);
        mCalendar.set(Calendar.YEAR, 2015);
        mCalendar.set(Calendar.DAY_OF_MONTH, 27);
        mCalendar.set(Calendar.HOUR_OF_DAY, 11);
        mCalendar.set(Calendar.MINUTE, 58);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.AM_PM, Calendar.PM);

        new AlarmReceiver().setAlarm(context, mCalendar, 1);

        // Delete the notification
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(1);
    }
}