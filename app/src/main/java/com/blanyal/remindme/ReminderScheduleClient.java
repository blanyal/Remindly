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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Calendar;


public class ReminderScheduleClient {

    // The hook into our service
    private ReminderScheduleService mBoundService;
    // The context to start the service in
    private Context mContext;
    // A flag if we are connected to the service or not
    private boolean mIsBound;

    public ReminderScheduleClient(Context context) {
        mContext = context;
    }

    // Call this to connect your activity to your service
    public void doBindService() {
        // Establish a connection with our service
        mContext.bindService(new Intent(mContext, ReminderScheduleService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    // On an attempt to connect to the service, this connection will be called with the result.
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with service has been established,
            // giving us the service object we can use to interact with service.
            mBoundService = ((ReminderScheduleService.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    public void setAlarmForNotification(Calendar c){
        mBoundService.setAlarm(c);
    }

    public void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }
}