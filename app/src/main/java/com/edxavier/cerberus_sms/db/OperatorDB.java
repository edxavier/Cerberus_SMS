package com.edxavier.cerberus_sms.db;

import android.util.Log;

import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.db.entities.PersonalContact_Table;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
@Database(name = OperatorDB.NAME, version = OperatorDB.VERSION)
public class OperatorDB {
    public static final String NAME = "OperatorDatabse"; // we will refresh the .db extension
    public static final int VERSION = 1;

    @Migration(version = 3, database = OperatorDB.class, priority = 1)
    public static class Migration2 extends BaseMigration {
        @Override
        public void migrate(DatabaseWrapper database) {
            // run some code here
            Log.d("EDER","migrate");

            SQLite.update(PersonalContact.class)
                    .set(PersonalContact_Table.contact_id.eq("-11"))
                    .execute(database); // required inside a migration to pass the wrapper
        }
    }

    @Migration(version = 2, database = OperatorDB.class, priority = 2)
    public static class MigrationTable1 extends AlterTableMigration<PersonalContact> {

        public MigrationTable1(Class<PersonalContact> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.TEXT, "contact_id");
            Log.d("EDER","AddCol");
        }



    }
}
