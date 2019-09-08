package com.groot.basemodule;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * author: 肖雷
 * created on: 2019/9/8
 * description:
 */

@Database(entities = {User.class}, version = 6)
public class Db extends RoomDatabase {
}
